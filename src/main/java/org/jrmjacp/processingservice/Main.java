package org.jrmjacp.processingservice;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import org.jrmjacp.processingservice.utils.DockerProcessing;

public class Main {
    public static void main(String[] args) {
        DockerProcessing dockerProcessing = new DockerProcessing();
        DefaultDockerClientConfig config = dockerProcessing.getDefaultDockerConfig();
        DockerClient client = dockerProcessing.getDockerClient(config);
        CreateContainerResponse container = dockerProcessing.createContainer("jrmjacp:1.0", client);
        dockerProcessing.moveExampleToContainer(client, container);
        dockerProcessing.moveTestToContainer(client, container);
        dockerProcessing.startDockerContainer(client, container);
    }
}