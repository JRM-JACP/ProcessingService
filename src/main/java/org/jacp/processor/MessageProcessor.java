package org.jacp.processor;

import org.jacp.dto.QuestionDto;
import org.jacp.service.QuestionTestRequest;
import org.jacp.utils.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * @author saffchen created on 12.09.2023
 */

@Service
public class MessageProcessor {

    @Autowired
    private QuestionTestRequest service;

    @Autowired
    private JavaClassProcessor javaClassProcessor;

    private String solution;
    private Long solutionId;
    private String imports;
    private String testImports;
    private String test;
    private String randomPackageName;

    public static String generatePackageName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public void processMessage(QuestionDto question) {
        solutionId = question.getId();
        solution = question.getSolution();

        responseToQuestion(solutionId);
    }

    public void responseToQuestion(Long id) {
        ResponseEntity<String> response = service.getImportAndTest(id);

        processRequestToQuestion(Objects.requireNonNull(response.getBody()));
    }

    public void processRequestToQuestion(String responseGetBody) {
        JSONObject jsonObject = new JSONObject(responseGetBody.substring(responseGetBody.indexOf("{")));

        imports = jsonObject.getString("imports");
        testImports = jsonObject.getString("testImports");
        test = jsonObject.getString("test");
        randomPackageName = generatePackageName();

        javaClassProcessor.createJavaClass(imports, testImports, StringUtils.CLASS_NAME, StringUtils.TEST_CLASS_NAME, solution, test, randomPackageName);
    }
}