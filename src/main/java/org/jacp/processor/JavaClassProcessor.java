package org.jacp.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author saffchen created on 13.09.2023
 */
@Component
public class JavaClassProcessor extends Thread {
    static UUID uuid = UUID.randomUUID();
    static String randomPackageName = uuid.toString();

    @Autowired
    SourceClassCreator sourceClassCreator;
    @Autowired
    TestClassCreator testClassCreator;

    public String startCreateClass(String imports, String className, String solution, String testImports, String testClassName, String test) {
        sourceClassCreator.createClass(imports, className, solution);
        testClassCreator.createClass(testImports, testClassName, test);

        return randomPackageName;
    }
}
