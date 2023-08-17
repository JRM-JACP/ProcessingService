import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient;
import org.jrmjacp.processingservice.utils.DockerProcessing;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        DockerProcessing dockerProcessing = new DockerProcessing();
        DefaultDockerClientConfig config = dockerProcessing.getDefaultDockerConfig();
        DockerHttpClient httpClient = dockerProcessing.getDockerHttpClient(config);
        DockerClient dockerClient = dockerProcessing.getDockerClientInstance(config, httpClient);

        CreateContainerResponse container = dockerProcessing.createContainer("jrmjacp:1.0", dockerClient);
        dockerProcessing.moveExampleToContainer(dockerClient, container);
        dockerProcessing.moveTestToContainer(dockerClient, container);

        dockerProcessing.startDockerContainer(dockerClient, container);

        /*try {
            dockerProcessing.moveSureFireReportToHost(dockerClient, container);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }
}
