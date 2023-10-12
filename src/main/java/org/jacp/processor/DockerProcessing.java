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
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

@Service
public class DockerProcessing {

    public void moveExampleToContainer(DockerClient client, CreateContainerResponse container) {
        String exampleHostPath = String.format("./toResult/%s/source/%s.java", JavaClassProcessor.randomPackageName, MessageProcessor.className);
        String exampleContainerPath = "/ProcessingService/src/main/java/org/jacp";
        System.out.println(exampleHostPath);
        client.copyArchiveToContainerCmd(container.getId())
                .withHostResource(exampleHostPath)
                .withRemotePath(exampleContainerPath).exec();
    }

    public void moveTestToContainer(DockerClient client, CreateContainerResponse container) {
        String testHostPath = String.format("./toResult/%s/test/%s.java", JavaClassProcessor.randomPackageName, MessageProcessor.testClassName);
        String testContainerPath = "/ProcessingService/src/test/java/org/jacp";
        System.out.println(testHostPath);
        client.copyArchiveToContainerCmd(container.getId())
                .withHostResource(testHostPath)
                .withRemotePath(testContainerPath).exec();
    }

    public void moveSureFireReportToHost(DockerClient client, CreateContainerResponse container) throws IOException {
        String txtReportHostPath = String.format("/testsReports/fromContainer/%s.txt", MessageProcessor.testClassName);
        String xmlReportHostPath = String.format("/testReports/fromContainer/TEST-%s.xml", MessageProcessor.testClassName);
        String performanceReportHostPath = "/testReports/fromContainer/performance.json";
        String txtReportContainerPath = String.format("/ProcessingService/target/surefire-reports/org.jacp.%s.txt", MessageProcessor.testClassName);
        String xmlReportContainerPath = String.format("/ProcessingService/target/surefire-reports/TEST-org.jacp.%s.xml", MessageProcessor.testClassName);
        String performanceReportContainerPath = "/ProcessingService/target/surefire-reports/performance.json";

        TarArchiveInputStream txtReport = new TarArchiveInputStream(client
                .copyArchiveFromContainerCmd(container.getId(), txtReportContainerPath)
                .exec());
        TarArchiveInputStream xmlReport = new TarArchiveInputStream(client
                .copyArchiveFromContainerCmd(container.getId(), xmlReportContainerPath)
                .exec());
        TarArchiveInputStream performanceReport = new TarArchiveInputStream(client
                .copyArchiveFromContainerCmd(container.getId(), performanceReportContainerPath)
                .exec());

        unTar(txtReport, new File(txtReportHostPath));
        unTar(xmlReport, new File(xmlReportHostPath));
        unTar(performanceReport, new File(performanceReportHostPath));
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
                .withName("jacp")
                .withCmd("sh", "runtests.sh")
                .exec();
    }

    public void startDockerContainer(DockerClient client, CreateContainerResponse container) {
        System.out.println(container.getId());
        client.startContainerCmd(container.getId()).exec();
    }

    public void stopAndRemoveDockerContainer(DockerClient client, CreateContainerResponse container) {
        client.stopContainerCmd(container.getId()).exec();
        client.removeContainerCmd(container.getId()).withForce(true).exec();
    }
}