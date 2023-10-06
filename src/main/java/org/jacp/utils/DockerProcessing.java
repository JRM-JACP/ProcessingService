package org.jacp.utils;

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
import org.jacp.processor.JavaClassProcessor;
import org.jacp.processor.MessageProcessor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

@Service
public class DockerProcessing {
    private String exampleHostPath = String.format("./toResult/%s/source/%s.java", JavaClassProcessor.randomPackageName, MessageProcessor.className);
    private String testHostPath = String.format("./toResult/%s/test/%s.java", JavaClassProcessor.randomPackageName, MessageProcessor.testClassName);
    private String exampleContainerPath = "/jacp/src/main/java/jrm/jacp";
    private String testContainerPath = "/jacp/src/test/java";

    private String txtReportHostPath = String.format("./testReports/fromContainer/%s.txt", MessageProcessor.className);
    private String xmlReportHostPath = String.format("./testReports/fromContainer/TEST-%s.xml", MessageProcessor.testClassName);
    private String perfomanceReportHostPath = "./testReports/fromContainer/perfomance.json";
    private String txtReportContainerPath = String.format("/org/jacp/target/surefire-reports/%s.txt", MessageProcessor.className);
    private String xmlReportContainerPath = String.format("/org/jacp/target/surefire-reports/TEST-%s.xml", MessageProcessor.testClassName);
    private String perfomanceReportContainerPath = "/org/jacp/target/surefire-reports/perfomance.json";

    public void moveExampleToContainer(DockerClient client, CreateContainerResponse container) {
        client.copyArchiveToContainerCmd(container.getId())
                .withHostResource(exampleHostPath)
                .withRemotePath(exampleContainerPath).exec();
    }

    public void moveTestToContainer(DockerClient client, CreateContainerResponse container) {
        client.copyArchiveToContainerCmd(container.getId())
                .withHostResource(testHostPath)
                .withRemotePath(testContainerPath).exec();
    }

    public void moveSureFireReportToHost(DockerClient client, CreateContainerResponse container) throws IOException {
        TarArchiveInputStream txtReport = new TarArchiveInputStream(client
                .copyArchiveFromContainerCmd(container.getId(), txtReportContainerPath)
                .exec());
        TarArchiveInputStream xmlReport = new TarArchiveInputStream(client
                .copyArchiveFromContainerCmd(container.getId(), xmlReportContainerPath)
                .exec());
        TarArchiveInputStream perfomanceReport = new TarArchiveInputStream(client
                .copyArchiveFromContainerCmd(container.getId(), perfomanceReportContainerPath)
                .exec());

        unTar(txtReport, new File(txtReportHostPath));
        unTar(xmlReport, new File(xmlReportHostPath));
        unTar(perfomanceReport, new File(perfomanceReportHostPath));
    }

    private static void unTar(TarArchiveInputStream tis, File destFile)
            throws IOException {
        TarArchiveEntry tarEntry = null;
        while ((tarEntry = tis.getNextTarEntry()) != null) {
            if (tarEntry.isDirectory()) {
                if (!destFile.exists()) {
                    destFile.mkdirs();
                }
            } else {
                FileOutputStream fos = new FileOutputStream(destFile);
                IOUtils.copy(tis, fos);
                fos.close();
            }
        }
        tis.close();
    }

    public DefaultDockerClientConfig getDefaultDockerConfig() {
        return DefaultDockerClientConfig.createDefaultConfigBuilder().build();
    }

    public DockerHttpClient getDockerHttpClient(DefaultDockerClientConfig config) {
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
        return httpClient;
    }

    public DockerClient getDockerClientInstance(DockerClientConfig config, DockerHttpClient httpClient) {
        return DockerClientImpl.getInstance(config, httpClient);
    }

    public CreateContainerResponse createContainer(String imageName, DockerClient client) {
        return client.createContainerCmd(imageName)
                .withName("jrm")
                //.withCmd("cd", "/jrmjacp")
                //.withCmd("mvn", "install")
                .withCmd("sh", "runtests.sh")
                .exec();
    }

    public void startDockerContainer(DockerClient client, CreateContainerResponse container) {
        client.startContainerCmd(container.getId()).exec();
    }

    public void stopAndRemoveDockerContainer(DockerClient client, CreateContainerResponse container) {
        client.stopContainerCmd(container.getId()).exec();
        client.removeContainerCmd(container.getId()).exec();
    }
}