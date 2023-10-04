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
public class SourceClassCreator implements ClassCreator {

    String sourcePath = String.format("toResult/%s/source", JavaClassProcessor.randomPackageName);
    File file = new File(sourcePath);

    @Override
    public void createClass(String imports, String className, String solution) {
        file.mkdirs();
        try {
            String filePath = String.format(file + "/%s.java", className.trim());
            BufferedWriter sourceClassWriter = new BufferedWriter(new FileWriter(filePath));
            sourceClassWriter.write("package toResult;");
            sourceClassWriter.newLine();
            sourceClassWriter.write(imports + "\n" + solution);
            sourceClassWriter.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}