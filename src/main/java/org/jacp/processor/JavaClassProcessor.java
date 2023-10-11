package org.jacp.processor;

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
    StartDockerJava startDockerJava;

    public static String randomPackageName;

    public static String generatePackageName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public void createJavaClass(String imports, String testImports, String className, String testClassName, String solution, String test) {
        randomPackageName = generatePackageName();
        String sourcePath = String.format("toResult/%s/source", randomPackageName);
        String testPath = String.format("toResult/%s/test", randomPackageName);
        File file = new File(sourcePath);
        File testFile = new File(testPath);
        file.mkdirs();
        testFile.mkdirs();
        try {
            String filePath = String.format(file + "/%s.java", className);
            BufferedWriter sourceClassWriter = new BufferedWriter(new FileWriter(filePath));
            sourceClassWriter.write("package toResult;");
            sourceClassWriter.newLine();
            sourceClassWriter.write(imports + "\n" + solution);
            sourceClassWriter.close();

            String testFilePath = String.format(testFile + "/%s.java", testClassName);
            BufferedWriter testClassWriter = new BufferedWriter(new FileWriter(testFilePath));
            testClassWriter.write("package toCheck;");
            testClassWriter.newLine();
            testClassWriter.write(testImports + "import " + className + ";\n" + test);
            testClassWriter.close();

            startDockerJava.startContainers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
