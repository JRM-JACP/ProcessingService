package org.jacp.processor;

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

    public static String randomPackageName;

    public static String generatePackageName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public String createJavaClass(String imports, String testImports, String className, String testClassName, String solution, String test) {
        randomPackageName = generatePackageName();
        String sourcePath = String.format("toResult/%s/source", randomPackageName);
        String testPath = String.format("toResult/%s/test", randomPackageName);
        File file = new File(sourcePath);
        File testFile = new File(testPath);
        file.mkdirs();
        testFile.mkdirs();
        try {
            String filePath = String.format(file + "/%s.java", className.trim());
            BufferedWriter sourceClassWriter = new BufferedWriter(new FileWriter(filePath));
            sourceClassWriter.write("package toResult;");
            sourceClassWriter.newLine();
            sourceClassWriter.write(imports + "\n" + solution);
            sourceClassWriter.close();

            String testFilePath = String.format(testFile + "/%s.java", testClassName.trim());
            BufferedWriter testClassWriter = new BufferedWriter(new FileWriter(testFilePath));
            testClassWriter.write("package toCheck;");
            testClassWriter.newLine();
            testClassWriter.write(testImports + "import toResult." + className.trim() + ";\n" + test);
            testClassWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return randomPackageName;
    }
}
