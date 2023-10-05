package org.jacp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author saffchen created on 08.09.2023
 */
@Service
public class QuestionTestRequest {

    private final static String URL = "http://localhost:8082/api/v1/imports/{id}";

    @Autowired
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public ResponseEntity<String> getImportAndTest(Long id) {

        return restTemplate().getForEntity(URL, String.class, id);
    }
}