package org.jacp.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jacp.dto.QuestionSolutionDto;
import org.jacp.entity.QuestionEntity;
import org.jacp.mapper.QuestionMapper;
import org.jacp.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author saffchen created on 26.08.2023
 */
@Service
public class CompetitionSolutionsListener {

    @Autowired
    private MessageProcessor messageProcessor;

    @Autowired
    private QuestionMapper questionMapper;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<?, QuestionEntity> consumerRecord) {
        QuestionSolutionDto message = questionMapper.questionDto(consumerRecord.value());
        messageProcessor.processMessage(message);
    }
}