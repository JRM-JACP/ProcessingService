package org.jacp;

import org.jacp.initializer.Kafka;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author saffchen created on 29.08.2023
 */
@ActiveProfiles("test")
@ContextConfiguration(initializers = Kafka.Initializer.class)
@SpringBootTest
public abstract class AbstractIntegrationBaseTest {
    @BeforeAll
    public static void init(){
        Kafka.container.start();
    }
}
