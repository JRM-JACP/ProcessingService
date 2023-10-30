package org.jacp.utils;

import org.springframework.stereotype.Component;

/**
 * @author saffchen created on 16.10.2023
 */

@Component
public class StringUtils {
    public static final String SOURCE_PATH = "toResult/%s/source";
    public static final String TEST_PATH = "toResult/%s/test";
    public static final String CLASS_NAME = "Solution";
    public static final String TEST_CLASS_NAME = "SolutionTest";
    public static final String PACKAGE = "package org.jacp;";
    public static final String REPORT_HOST_PATH = "testReports/fromContainer/%s/";
    public static final String HOST_PATH = "./toResult/";
    public static final String TEST_HOST_PATH = "./toResult/%s/test/%s.java";
    public static final String EXAMPLE_CONTAINER_PATH = "/ProcessingService/src/main/java/org/jacp";
    public static final String EXAMPLE_HOST_PATH = "%s%s/source/%s.java";
    public static final String TEST_CONTAINER_HOST_PATH = "/ProcessingService/src/test/java/org/jacp";
    public static final String TXT_REPORT_CONTAINER_PATH = "/ProcessingService/target/surefire-reports/org.jacp.%s.txt";
    public static final String XML_REPORT_CONTAINER_PATH = "/ProcessingService/target/surefire-reports/TEST-org.jacp.%s.xml";
    public static final String TXT_REPORT_HOST_PATH = "%s%s.txt";
    public static final String XML_REPORT_HOST_PATH = "%sTEST-%s.xml";
}