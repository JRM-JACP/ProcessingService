package org.jacp.processor;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.transport.DockerHttpClient;
import org.jacp.utils.ReportUtils;
import org.jacp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class StartDockerJava {

    @Autowired
    private ReportUtils reportUtils;

    @Autowired
    private DockerProcessing dockerProcessing;

    public void startContainers(String randomPackageName) {
        DefaultDockerClientConfig config = dockerProcessing.getDefaultDockerConfig();
        DockerHttpClient httpClient = dockerProcessing.getDockerHttpClient(config);
        DockerClient dockerClient = dockerProcessing.getDockerClientInstance(config, httpClient);

        CreateContainerResponse container = dockerProcessing
                .createContainer("jrmjacp:1.0", dockerClient, randomPackageName);
        dockerProcessing.moveExampleToContainer(dockerClient, container, randomPackageName);
        dockerProcessing.moveTestToContainer(dockerClient, container, randomPackageName);

        dockerProcessing.startDockerContainer(dockerClient, container);

        String path = String.format("%s%s", StringUtils.HOST_PATH, randomPackageName);
        File file = new File(path);
        try {
            Thread.sleep(40000);
            dockerProcessing.moveSureFireReportToHost(dockerClient, container, randomPackageName);
            Thread.sleep(20000);
            dockerProcessing.deleteSourceFile(file);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            reportUtils.getTestResults(randomPackageName);
            dockerProcessing.stopAndRemoveDockerContainer(dockerClient, container);
        }
    }
}
