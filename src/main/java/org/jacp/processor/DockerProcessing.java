package org.jacp.processor;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.jacp.config.ExecStartResultCallback;
import org.jacp.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

@Slf4j
@Service
public class DockerProcessing {

    public void moveExampleToContainer(DockerClient client, CreateContainerResponse container, String randomPackageName) {
        String exampleHostPath = String.format(StringUtils.EXAMPLE_HOST_PATH, StringUtils.HOST_PATH, randomPackageName, StringUtils.CLASS_NAME);
        String exampleContainerPath = StringUtils.EXAMPLE_CONTAINER_PATH;
        client.copyArchiveToContainerCmd(container.getId())
                .withHostResource(exampleHostPath)
                .withRemotePath(exampleContainerPath).exec();
    }

    public void moveTestToContainer(DockerClient client, CreateContainerResponse container, String randomPackageName) {
        String testHostPath = String.format(StringUtils.TEST_HOST_PATH, randomPackageName, StringUtils.TEST_CLASS_NAME);
        String testContainerPath = StringUtils.TEST_CONTAINER_HOST_PATH;
        client.copyArchiveToContainerCmd(container.getId())
                .withHostResource(testHostPath)
                .withRemotePath(testContainerPath).exec();
    }

    public void moveSureFireReportToHost(DockerClient client, CreateContainerResponse container, String randomPackageName) throws IOException {
        String reportHostPath = String.format(StringUtils.REPORT_HOST_PATH, randomPackageName);
        String txtReportHostPath = String.format(StringUtils.TXT_REPORT_HOST_PATH, reportHostPath, StringUtils.TEST_CLASS_NAME);
        String xmlReportHostPath = String.format(StringUtils.XML_REPORT_HOST_PATH, reportHostPath, StringUtils.CLASS_NAME);
        String txtReportContainerPath = String.format(StringUtils.TXT_REPORT_CONTAINER_PATH, StringUtils.TEST_CLASS_NAME);
        String xmlReportContainerPath = String.format(StringUtils.XML_REPORT_CONTAINER_PATH, StringUtils.TEST_CLASS_NAME);

        TarArchiveInputStream txtReport = new TarArchiveInputStream(client
                .copyArchiveFromContainerCmd(container.getId(), txtReportContainerPath)
                .exec());
        TarArchiveInputStream xmlReport = new TarArchiveInputStream(client
                .copyArchiveFromContainerCmd(container.getId(), xmlReportContainerPath)
                .exec());

        createReportPath(reportHostPath);
        unTar(txtReport, new File(txtReportHostPath));
        unTar(xmlReport, new File(xmlReportHostPath));
    }

    private static void unTar(TarArchiveInputStream tis, File destFile)
            throws IOException {
        try (TarArchiveInputStream tarInput = tis;
             FileOutputStream fos = new FileOutputStream(destFile)) {
            TarArchiveEntry tarEntry = null;
            while ((tarEntry = tarInput.getNextTarEntry()) != null) {
                if (tarEntry.isDirectory()) {
                    if (!destFile.exists()) {
                        destFile.mkdirs();
                    }
                } else {
                    IOUtils.copy(tarInput, fos);
                }
            }
        }
    }

    public DefaultDockerClientConfig getDefaultDockerConfig() {
        return DefaultDockerClientConfig.createDefaultConfigBuilder().build();
    }

    public DockerHttpClient getDockerHttpClient(DefaultDockerClientConfig config) {
        return new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
    }

    public DockerClient getDockerClientInstance(DockerClientConfig config, DockerHttpClient httpClient) {
        return DockerClientImpl.getInstance(config, httpClient);
    }

    public CreateContainerResponse createContainer(String imageName, DockerClient client, String randomPackageName) {
        String userDir = System.getProperty("user.dir");
        String m2Local = "/m2/repository/";
        String m2Remote = "/root/.m2/repository/";
        Volume m2Volume = new Volume(m2Remote);
        HostConfig hostConfig = HostConfig.newHostConfig()
                .withBinds(new Bind(userDir + m2Local, m2Volume, AccessMode.rw));

        return client.createContainerCmd(imageName)
                .withName(randomPackageName)
                .withVolumes(m2Volume)
                .withHostConfig(hostConfig)
                .withCmd("sh", "runtests.sh")
                .exec();
    }

    public void startDockerContainer(DockerClient client, CreateContainerResponse container) {
        client.startContainerCmd(container.getId()).exec();
    }

    public void stopAndRemoveDockerContainer(DockerClient client, CreateContainerResponse container) {
        client.stopContainerCmd(container.getId()).exec();
        client.removeContainerCmd(container.getId()).withForce(true).exec();
    }

    public static void createReportPath(String path) {
        File directory = new File(path);

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void deleteSourceFile(File file) {
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                deleteSourceFile(f);
            }
        }
        file.delete();
    }

    private void generateCompilationErrorReport(String randomPackageName, String fullLog) throws IOException {
        String reportHostPath = String.format(StringUtils.REPORT_HOST_PATH, randomPackageName);
        String txtReportHostPath = String.format(StringUtils.TXT_REPORT_HOST_PATH, reportHostPath, StringUtils.TEST_CLASS_NAME);

        createReportPath(reportHostPath);

        File file = new File(txtReportHostPath);
        FileWriter writer = new FileWriter(file);
        writer.write(fullLog);
        writer.close();
    }

    public void waitForTestCompletion(DockerClient dockerClient, CreateContainerResponse container, String randomPackageName) {
        LogContainerCmd logCmd = dockerClient.logContainerCmd(container.getId())
                .withFollowStream(true)
                .withStdOut(true);

        try {
            logCmd.exec(new ExecStartResultCallback() {
                private final StringBuilder log = new StringBuilder();
                private Boolean isCompileError = false;

                @Override
                public void onNext(Frame frame) {
                    String logLine = new String(frame.getPayload(), StandardCharsets.UTF_8);
                    log.append(logLine);
                    if (logLine.contains("COMPILATION ERROR")) {
                        isCompileError = true;
                    }
                    if (logLine.contains("BUILD SUCCESS") || logLine.contains("BUILD FAILURE")) {
                        complete();
                    }
                }

                private void complete() {
                    try {
                        if (isCompileError) {
                            generateCompilationErrorReport(randomPackageName, log.toString());
                        } else {
                            moveSureFireReportToHost(dockerClient, container, randomPackageName);
                        }
                        close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).awaitCompletion();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}