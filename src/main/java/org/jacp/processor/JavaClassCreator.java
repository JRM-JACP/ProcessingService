package org.jacp.processor;

import lombok.extern.slf4j.Slf4j;
import org.jacp.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author saffchen created on 19.10.2023
 */

@Slf4j
@Service
public class JavaClassCreator {
    public void createClass(String sourcePath, String className, String imports, String content) {
        try {
            File sourceDirectory = new File(sourcePath);
            if (!sourceDirectory.exists()) {
                sourceDirectory.mkdirs();
            }
            String filePath = String.format(sourceDirectory + "/%s.java", className);
            BufferedWriter sourceClassWriter = new BufferedWriter(new FileWriter(filePath));
            sourceClassWriter.write(StringUtils.PACKAGE);
            sourceClassWriter.newLine();
            sourceClassWriter.write(imports + "\n" + content);
            sourceClassWriter.close();
        } catch (IOException e) {
            log.error("An error occurred: " + e);
            throw new RuntimeException(e);
        }
    }
}