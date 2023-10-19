package org.jacp.processor;

import org.jacp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * @author saffchen created on 13.09.2023
 */
@Component
public class JavaClassProcessor {

    @Autowired
    private StartDockerJava startDockerJava;

    public void createJavaClass(String imports, String testImports, String className, String testClassName, String solution, String test, String randomPackageName) {
        String sourcePath = String.format(StringUtils.sourcePath, randomPackageName);
        String testPath = String.format(StringUtils.testPath, randomPackageName);
        File file = new File(sourcePath);
        File testFile = new File(testPath);
        file.mkdirs();
        testFile.mkdirs();
        try {
            String filePath = String.format(file + "/%s.java", className);
            BufferedWriter sourceClassWriter = new BufferedWriter(new FileWriter(filePath));
            sourceClassWriter.write(StringUtils.PACKAGE);
            sourceClassWriter.newLine();
            sourceClassWriter.write(imports + "\n" + solution);
            sourceClassWriter.close();

            String testFilePath = String.format(testFile + "/%s.java", testClassName);
            BufferedWriter testClassWriter = new BufferedWriter(new FileWriter(testFilePath));
            testClassWriter.write(StringUtils.PACKAGE);
            testClassWriter.newLine();
            testClassWriter.write(testImports + test);
            testClassWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        startDockerJava.startContainers(randomPackageName);
    }
}
