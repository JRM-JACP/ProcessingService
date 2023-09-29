package org.jacp.processor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.jacp.entity.QuestionSolution;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @author saffchen created on 29.09.2023
 */
public class Producer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:29092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");

        KafkaProducer<String, QuestionSolution> producer = new KafkaProducer<>(props);

        ProducerRecord<String, QuestionSolution> record =
                new ProducerRecord<>("jacp-competition-solutions",
                        new QuestionSolution(1L,
                                "MakeUpperCase",
                                "public class Upper{public static String MakeUpperCase(String str){return str.toUpperCase();}}",
                                "STRING",
                                "EASY"));

        Future<RecordMetadata> metadata = producer.send(record);
        producer.flush();
        try {
            RecordMetadata recordMetadata = metadata.get();
            System.out.println("Сообщение " + record.value() + " успешно отправлено в топик " + recordMetadata.topic());
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.close();

    }
}
