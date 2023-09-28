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

    public String createJavaClass(String imports, String testImports, String className, String testClassName, String solution, String test) {
        UUID uuid = UUID.randomUUID();
        String randomPackageName = uuid.toString();
        String sourcePath = String.format("src/main/java/org/jacp/toResult/%s/source", randomPackageName);
        String testPath = String.format("src/main/java/org/jacp/toResult/%s/test", randomPackageName);
        File file = new File(sourcePath);
        File testFile = new File(testPath);
        file.mkdirs();
        testFile.mkdirs();
        try {
            String filePath = String.format(file + "/%s.java", className.trim());
            BufferedWriter sourceClassWriter = new BufferedWriter(new FileWriter(filePath));
            sourceClassWriter.write("package org.jacp.toResult;");
            sourceClassWriter.newLine();
            sourceClassWriter.write(imports + "\n" + solution);
            sourceClassWriter.close();

            String testFilePath = String.format(testFile + "/%s.java", testClassName.trim());
            BufferedWriter testClassWriter = new BufferedWriter(new FileWriter(testFilePath));
            testClassWriter.write("package org.jacp.toCheck;");
            testClassWriter.newLine();
            testClassWriter.write(testImports + "import org.jacp.toResult." + className.trim() + ";\n" + test);
            testClassWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return randomPackageName;
    }
}
