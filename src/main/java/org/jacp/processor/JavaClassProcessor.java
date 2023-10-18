package org.jacp.processor;

import org.jacp.utils.JavaClassCreator;
import org.jacp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author saffchen created on 13.09.2023
 */
@Component
public class JavaClassProcessor {

    @Autowired
    private StartDockerJava startDockerJava;

    @Autowired
    private JavaClassCreator javaClassCreator;

    public void createJavaClass(String imports, String testImports, String className, String testClassName, String solution, String test, String randomPackageName) {
        String sourcePath = String.format(StringUtils.sourcePath, randomPackageName);
        String testPath = String.format(StringUtils.testPath, randomPackageName);
        File file = new File(sourcePath);
        File testFile = new File(testPath);
        file.mkdirs();
        testFile.mkdirs();

        javaClassCreator.classCreator(file, className, imports, solution);
        javaClassCreator.classCreator(testFile, testClassName, testImports, test);

        startDockerJava.startContainers(randomPackageName);
    }
}
