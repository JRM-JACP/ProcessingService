package org.jacp.processor;

import org.jacp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author saffchen created on 13.09.2023
 */

@Service
public class JavaClassDockerManager {

    @Autowired
    private DockerJavaContainerStarter dockerJavaContainerStarter;

    @Autowired
    private JavaClassCreator javaClassCreator;

    public void createAndRunJavaClassInDocker(String imports, String testImports, String className, String testClassName, String content, String testContent, String randomPackageName) {
        String sourcePath = String.format(StringUtils.SOURCE_PATH, randomPackageName);
        String testPath = String.format(StringUtils.TEST_PATH, randomPackageName);

        javaClassCreator.createClass(sourcePath, className, imports, content);
        javaClassCreator.createClass(testPath, testClassName, testImports, testContent);

        dockerJavaContainerStarter.startContainers(randomPackageName);
    }
}
