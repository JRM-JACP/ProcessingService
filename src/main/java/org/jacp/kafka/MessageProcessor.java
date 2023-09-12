package org.jacp.kafka;

import lombok.AllArgsConstructor;
import org.jacp.controller.ProcessingController;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author saffchen created on 12.09.2023
 */
@Component
public class MessageProcessor{

    private final ProcessingController processingController;

    public MessageProcessor(ProcessingController processingController) {
        this.processingController = processingController;
    }

    public void processMessage(String message){
        String solutionIdString = message.substring(message.indexOf("id=") + "id=".length(), message.indexOf(","));
        Long solutionId = Long.parseLong(solutionIdString);
        ResponseEntity<String> response = processingController.getImportAndTest(solutionId);

        String solution = message.substring(message.indexOf("solution=") + "solution=".length(), message.indexOf(", tags="));

        String responseGetBody = response.getBody();
        JSONObject jsonObject = new JSONObject(responseGetBody.substring(responseGetBody.indexOf("{")));

        String imports = jsonObject.getString("imports");
        String test = jsonObject.getString("test");

        System.out.println(imports);
        System.out.println(solution);
        System.out.println(test);
    }
}
