package org.jacp.processor;

import org.jacp.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author saffchen created on 19.10.2023
 */

@Service
public class JavaClassCreator {
    public void classCreator(File file, String className, String imports, String solution) {
        try {
            String filePath = String.format(file + "/%s.java", className);
            BufferedWriter sourceClassWriter = new BufferedWriter(new FileWriter(filePath));
            sourceClassWriter.write(StringUtils.PACKAGE);
            sourceClassWriter.newLine();
            sourceClassWriter.write(imports + "\n" + solution);
            sourceClassWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
