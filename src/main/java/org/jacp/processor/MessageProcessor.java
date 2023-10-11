package org.jacp.processor;

import org.jacp.service.QuestionTestRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author saffchen created on 12.09.2023
 */
@Component
public class MessageProcessor {

    @Autowired
    private QuestionTestRequest service;

    @Autowired
    private JavaClassProcessor javaClassProcessor;

    public static String className;
    public static String testClassName;
    private String solution;

    public String processMessage(String message) {
        String solutionIdString = message.substring(message.indexOf("id=") + "id=".length(), message.indexOf(","));
        solution = message.substring(message.indexOf("solution=") + "solution=".length(), message.indexOf(", tags="));
        className = message.substring(message.indexOf("class") + "class".length(), message.indexOf("{")).trim();
        Long solutionId = Long.parseLong(solutionIdString);

        responseToQuestion(solutionId);
        return className;
    }

    public void responseToQuestion(Long id) {
        ResponseEntity<String> response = service.getImportAndTest(id);

        processRequestToQuestion(Objects.requireNonNull(response.getBody()));
    }

    public String processRequestToQuestion(String responseGetBody) {
        JSONObject jsonObject = new JSONObject(responseGetBody.substring(responseGetBody.indexOf("{")));

        String imports = jsonObject.getString("imports");
        String testImports = jsonObject.getString("testImports");
        String test = jsonObject.getString("test");

        testClassName = test.substring(test.indexOf("class") + "class".length(), test.indexOf("{")).trim();

        javaClassProcessor.createJavaClass(imports, testImports, className, testClassName, solution, test);
        return testClassName;
    }
}