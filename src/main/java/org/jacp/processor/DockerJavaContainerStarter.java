package org.jacp.processor;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.jacp.utils.ReportUtils;
import org.jacp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
public class DockerJavaContainerStarter {

    private final ReportUtils reportUtils;

    @Autowired
    public DockerJavaContainerStarter(ReportUtils reportUtils) {
        this.reportUtils = reportUtils;
    }

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
        File sorceFile = new File(path);
        try {
            dockerProcessing.waitForTestCompletion(dockerClient, container, randomPackageName);
            dockerProcessing.deleteSourceFile(sorceFile);
        } finally {
            reportUtils.printTestResults(randomPackageName);
            dockerProcessing.stopAndRemoveDockerContainer(dockerClient, container);
        }
    }
}
