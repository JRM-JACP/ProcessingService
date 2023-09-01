package org.jacp.initializer;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author saffchen created on 29.08.2023
 */
@UtilityClass
@ActiveProfiles("test")
public class Kafka {
    public static final KafkaContainer container =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.kafka.consumer.bootstrap-servers=" + container.getBootstrapServers()
            ).applyTo(applicationContext);
        }
    }
}
