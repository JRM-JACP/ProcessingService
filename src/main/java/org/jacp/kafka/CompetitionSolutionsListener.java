package org.jacp.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jacp.controller.ProcessingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author saffchen created on 26.08.2023
 */
@Component
public class CompetitionSolutionsListener {

    @Autowired
    ProcessingController processingController;

    public CompetitionSolutionsListener(ProcessingController processingController) {
        this.processingController = processingController;
    }

    @KafkaListener(topics = "${spring.kafka.template.default-topic}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<?, ?> consumerRecord) {
        String solution = consumerRecord.value().toString();
        String solutionIdString = solution.substring(solution.indexOf("id=") + 3, solution.indexOf(","));
        Long solutionId = Long.parseLong(solutionIdString);
        ResponseEntity<String> response = processingController.getImportAndTest(solutionId);
    }
}