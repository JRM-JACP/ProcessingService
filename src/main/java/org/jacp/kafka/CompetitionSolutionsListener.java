package org.jacp.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jacp.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author saffchen created on 26.08.2023
 */
@Component
public class CompetitionSolutionsListener {

    @Autowired
    private MessageProcessor messageProcessor;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}",
            groupId = "${spring.kafka.consumer.group-id}")
    public String consume(ConsumerRecord<?, ?> consumerRecord) {
        String message = consumerRecord.value().toString();
        return messageProcessor.processMessage(message);
    }
}