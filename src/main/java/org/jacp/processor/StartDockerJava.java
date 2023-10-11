package org.jacp.processor;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.transport.DockerHttpClient;
import org.jacp.utils.ReportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class StartDockerJava {

    @Autowired
    DockerProcessing dockerProcessing;

    public void startContainers() {

        DefaultDockerClientConfig config = dockerProcessing.getDefaultDockerConfig();
        DockerHttpClient httpClient = dockerProcessing.getDockerHttpClient(config);
        DockerClient dockerClient = dockerProcessing.getDockerClientInstance(config, httpClient);

        CreateContainerResponse container = dockerProcessing
                .createContainer("jrmjacp:1.0", dockerClient);
        dockerProcessing.moveExampleToContainer(dockerClient, container);
        dockerProcessing.moveTestToContainer(dockerClient, container);

        dockerProcessing.startDockerContainer(dockerClient, container);

        try {
            Thread.sleep(60000);
            dockerProcessing.moveSureFireReportToHost(dockerClient, container);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        dockerProcessing.stopAndRemoveDockerContainer(dockerClient, container);

        ReportUtils reportUtils = new ReportUtils();
        reportUtils.getTestResults();
    }
}
