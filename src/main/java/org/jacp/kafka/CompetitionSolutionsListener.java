package org.jacp.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author saffchen created on 26.08.2023
 */
@Component
public class CompetitionSolutionsListener implements MessageReceived {

    private final MessageProcessor messageProcessor;

    public CompetitionSolutionsListener(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    @KafkaListener(topics = "${spring.kafka.template.default-topic}",
            groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(ConsumerRecord<?, ?> consumerRecord) {
        String message = consumerRecord.value().toString();
        messageProcessor.processMessage(message);
    }
}