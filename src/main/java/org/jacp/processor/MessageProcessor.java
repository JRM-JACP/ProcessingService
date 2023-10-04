package org.jacp.processor;

import org.jacp.controller.ProcessingController;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author saffchen created on 12.09.2023
 */
@Component
public class MessageProcessor {

    @Autowired
    private ProcessingController processingController;
    @Autowired
    private JavaClassProcessor javaClassProcessor;

    public String processMessage(String message) {
        String solutionIdString = message.substring(message.indexOf("id=") + "id=".length(), message.indexOf(","));
        String solution = message.substring(message.indexOf("solution=") + "solution=".length(), message.indexOf(", tags="));
        String className = message.substring(message.indexOf("class") + "class".length(), message.indexOf("{"));
        Long solutionId = Long.parseLong(solutionIdString);

        ResponseEntity<String> response = processingController.getImportAndTest(solutionId);

        String responseGetBody = response.getBody();

        assert responseGetBody != null;
        JSONObject jsonObject = new JSONObject(responseGetBody.substring(responseGetBody.indexOf("{")));

        String imports = jsonObject.getString("imports");
        String testImports = jsonObject.getString("testImports");
        String test = jsonObject.getString("test");

        String testClassName = test.substring(test.indexOf("class") + "class".length(), test.indexOf("{"));

        return javaClassProcessor.createJavaClass(imports, testImports, className, testClassName, solution, test);
    }
}
