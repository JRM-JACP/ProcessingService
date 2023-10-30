package org.jacp.processor;

import org.jacp.dto.QuestionSolutionDto;
import org.jacp.dto.QuestionTestDto;
import org.jacp.entity.QuestionTestEntity;
import org.jacp.mapper.QuestionMapper;
import org.jacp.service.QuestionTestRequest;
import org.jacp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author saffchen created on 12.09.2023
 */

@Service
public class MessageProcessor {

    @Autowired
    private QuestionTestRequest service;

    @Autowired
    private QuestionMapper mapper;

    @Autowired
    private JavaClassDockerManager javaClassDockerManager;

    public static String generatePackageName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public void processMessage(QuestionSolutionDto question) {
        Long solutionId = question.getId();
        String content = question.getSolution();

        responseToQuestion(solutionId, content);
    }

    public void responseToQuestion(Long id, String content) {
        ResponseEntity<QuestionTestEntity> response = service.getImportAndTest(id);

        QuestionTestDto questionTestDto = mapper.testDto(response.getBody());

        processRequestToQuestion(questionTestDto, content);
    }

    public void processRequestToQuestion(QuestionTestDto questionTestDto, String content) {
        String imports = questionTestDto.getImports();
        String testImports = questionTestDto.getTestImports();
        String test = questionTestDto.getTest();
        String randomPackageName = generatePackageName();

        javaClassDockerManager.createAndRunJavaClassInDocker(imports, testImports, StringUtils.CLASS_NAME, StringUtils.TEST_CLASS_NAME, content, test, randomPackageName);
    }
}