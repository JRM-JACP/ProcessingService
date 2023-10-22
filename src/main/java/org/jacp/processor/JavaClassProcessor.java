package org.jacp.processor;

import org.jacp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author saffchen created on 13.09.2023
 */

@Service
public class JavaClassProcessor {

    @Autowired
    private StartDockerJava startDockerJava;

    @Autowired
    private JavaClassCreator javaClassCreator;

    public void createJavaClass(String imports, String testImports, String className, String testClassName, String solution, String test, String randomPackageName) {
        String sourcePath = String.format(StringUtils.SOURCE_PATH, randomPackageName);
        String testPath = String.format(StringUtils.TEST_PATH, randomPackageName);
        File file = new File(sourcePath);
        File testFile = new File(testPath);
        file.mkdirs();
        testFile.mkdirs();

        javaClassCreator.classCreator(file, className, imports, solution);
        javaClassCreator.classCreator(testFile, testClassName, testImports, test);

        startDockerJava.startContainers(randomPackageName);
    }
}
