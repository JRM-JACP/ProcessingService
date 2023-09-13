package org.jacp.processor;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author saffchen created on 13.09.2023
 */
@Component
public class JavaClassProcessor {
    public void createJavaClass(String imports, String solution, String test) {
        File file = new File("src/main/java/org/jacp/processor/Upper.java");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("package " + MessageProcessor.class.getPackage().getName() + "\n");
            writer.write(imports + "\n" + solution);
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
