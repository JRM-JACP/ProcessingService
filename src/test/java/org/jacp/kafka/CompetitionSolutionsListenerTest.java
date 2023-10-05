package org.jacp.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jacp.AbstractIntegrationBaseTest;
import org.jacp.entity.QuestionSolution;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author saffchen created on 29.08.2023
 */
class CompetitionSolutionsListenerTest extends AbstractIntegrationBaseTest {

    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String kafkaBootstrapServer;
    @Value(value = "${spring.kafka.consumer.key-serializer}")
    private String keySerializer;
    @Value(value = "${spring.kafka.consumer.value-serializer}")
    private String valueSerializer;
    @Value(value = "${spring.kafka.template.topic}")
    private String topic;
    @Value(value = "${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;
    @Value(value = "${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;
    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;

    public ProducerFactory<String, QuestionSolution> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        return new DefaultKafkaProducerFactory<>(props);
    }

    public ConsumerFactory<String, QuestionSolution> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(QuestionSolution.class));
    }

    QuestionSolution questionSolution = new QuestionSolution(1L,
            "Test Problem",
            "Test Solution",
            "STRING",
            "EASY");

    @Test
    void produce() {
        KafkaProducer<String, QuestionSolution> producer = new KafkaProducer<>(producerFactory().getConfigurationProperties());

        ProducerRecord<String, QuestionSolution> record = new ProducerRecord<>(topic, questionSolution);

        producer.close();
    }

    @Test
    void consumeSuccessful() {
        KafkaConsumer<String, QuestionSolution> consumer = new KafkaConsumer<>(consumerFactory().getConfigurationProperties());
        consumer.subscribe(Collections.singleton(topic));

        ConsumerRecord<?, QuestionSolution> record =
                new ConsumerRecord<>(topic, 0, 0, null, questionSolution);

        QuestionSolution receivedQuestionSolution = record.value();

        assertEquals(questionSolution.getProblem(), receivedQuestionSolution.getProblem());
        assertEquals(questionSolution.getSolution(), receivedQuestionSolution.getSolution());
        consumer.close();
    }
}