package org.jacp.service;

import org.jacp.entity.QuestionTestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author saffchen created on 08.09.2023
 */
@Service
public class QuestionTestRequest {

    @Value("${url.questionService}")
    private String url;

    private final RestTemplate restTemplate;

    @Autowired
    public QuestionTestRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<QuestionTestEntity> getImportAndTest(Long id) {

        return restTemplate.getForEntity(url, QuestionTestEntity.class, id);
    }
}