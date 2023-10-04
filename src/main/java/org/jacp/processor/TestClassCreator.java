package org.jacp.processor;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author saffchen created on 04.10.2023
 */
@Component
public class TestClassCreator implements ClassCreator {

    String testPath = String.format("toResult/%s/test", JavaClassProcessor.randomPackageName);
    File testFile = new File(testPath);

    @Override
    public void createClass(String imports, String className, String test) {
        testFile.mkdirs();
        try {
            String testFilePath = String.format(testFile + "/%s.java", className.trim());
            BufferedWriter testClassWriter = new BufferedWriter(new FileWriter(testFilePath));
            testClassWriter.write("package toCheck;");
            testClassWriter.newLine();
            testClassWriter.write(imports + "import toResult." + className.trim() + ";\n" + test);
            testClassWriter.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
