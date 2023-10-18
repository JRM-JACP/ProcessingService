package org.jacp.processor;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.jacp.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

@Service
public class DockerProcessing {

    public void moveExampleToContainer(DockerClient client, CreateContainerResponse container, String randomPackageName) {
        String exampleHostPath = String.format(StringUtils.exampleHostPath, StringUtils.hostPath, randomPackageName, StringUtils.className);
        String exampleContainerPath = StringUtils.exampleContainerPath;
        client.copyArchiveToContainerCmd(container.getId())
                .withHostResource(exampleHostPath)
                .withRemotePath(exampleContainerPath).exec();
    }

    public void moveTestToContainer(DockerClient client, CreateContainerResponse container, String randomPackageName) {
        String testHostPath = String.format(StringUtils.testHostPath, randomPackageName, StringUtils.testClassName);
        String testContainerPath = StringUtils.testContainerHostPath;
        client.copyArchiveToContainerCmd(container.getId())
                .withHostResource(testHostPath)
                .withRemotePath(testContainerPath).exec();
    }

    public void moveSureFireReportToHost(DockerClient client, CreateContainerResponse container, String randomPackageName) throws IOException {
        String reportHostPath = String.format(StringUtils.reportHostPath, randomPackageName);
        String txtReportHostPath = String.format(StringUtils.txtReportHostPath, reportHostPath, StringUtils.testClassName);
        String xmlReportHostPath = String.format(StringUtils.xmlReportHostPath, reportHostPath, StringUtils.className);
        String txtReportContainerPath = String.format(StringUtils.txtReportContainerPath, StringUtils.testClassName);
        String xmlReportContainerPath = String.format(StringUtils.xmlReportContainerPath, StringUtils.testClassName);

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
        return client.createContainerCmd(imageName)
                .withName(randomPackageName)
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
            for (File f : file.listFiles()) {
                deleteSourceFile(f);
            }
        }
        file.delete();
    }
}