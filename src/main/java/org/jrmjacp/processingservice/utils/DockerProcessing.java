package org.jrmjacp.processingservice.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

import java.time.Duration;

public class DockerProcessing {
    private String exampleHostPath = "./to_check/examples/HelloClass.java";
    private String testHostPath = "./to_check/tests/HelloTest.java";

    private String exampleContainerPath = "/jrmjacp/src/main/java/jrm/jacp";
    private String testContainerPath = "/jrmjacp/src/test/java";

    public void moveExampleToContainer(DockerClient client, CreateContainerResponse container){
        client.copyArchiveToContainerCmd(container.getId())
                .withHostResource(exampleHostPath)
                .withRemotePath(exampleContainerPath).exec();
    }

    public void moveTestToContainer(DockerClient client, CreateContainerResponse container){
        client.copyArchiveToContainerCmd(container.getId())
                .withHostResource(testHostPath)
                .withRemotePath(testContainerPath).exec();
    }

    public DefaultDockerClientConfig getDefaultDockerConfig(){
        return DefaultDockerClientConfig.createDefaultConfigBuilder().build();
    }

    public DockerHttpClient getDockerHttpClient(DefaultDockerClientConfig config){
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
        return httpClient;
    }

    public DockerClient getDockerClientInstance(DockerClientConfig config, DockerHttpClient httpClient){
        return DockerClientImpl.getInstance(config, httpClient);
    }

    public CreateContainerResponse createContainer(String imageName, DockerClient client){
        return client.createContainerCmd(imageName)
                .withCmd("mvn", "test")
                .exec();
    }

    public void startDockerContainer(DockerClient client, CreateContainerResponse container){
        client.startContainerCmd(container.getId()).exec();
    }


}
