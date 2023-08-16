package org.jrmjacp.processingservice.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;

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

    public DockerClient getDockerClient(DefaultDockerClientConfig config){
        return DockerClientBuilder.getInstance(config).build();
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
