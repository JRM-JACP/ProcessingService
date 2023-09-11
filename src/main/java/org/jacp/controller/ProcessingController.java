package org.jacp.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author saffchen created on 08.09.2023
 */
@RestController
@RequestMapping(value = ProcessingController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProcessingController {

    static final String URL = "/api/v1/get_imports";

    String url = "http://localhost:8081/api/v1/imports/{id}";

    RestTemplate restTemplate;

    public ProcessingController() {
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getImportAndTest(@PathVariable Long id) {
        return restTemplate.getForEntity(url, String.class, id);
    }
}
