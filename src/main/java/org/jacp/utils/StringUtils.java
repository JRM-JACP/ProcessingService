package org.jacp.utils;

/**
 * @author saffchen created on 16.10.2023
 */

public class StringUtils {
    public static String randomPackageName;
    public static String sourcePath = "toResult/%s/source";
    public static String testPath = "toResult/%s/test";
    public static String className = "Solution";
    public static String testClassName = "SolutionTest";
    public static String PACKAGE = "package org.jacp;";
    public static String reportHostPath = "testReports/fromContainer/%s/";
    public static String hostPath = "./toResult/";
    public static String testHostPath = "./toResult/%s/test/%s.java";
    public static String exampleContainerPath = "/ProcessingService/src/main/java/org/jacp";
    public static String exampleHostPath = "%s%s/source/%s.java";
    public static String testContainerHostPath = "/ProcessingService/src/test/java/org/jacp";
    public static String txtReportContainerPath = "/ProcessingService/target/surefire-reports/org.jacp.%s.txt";
    public static String xmlReportContainerPath = "/ProcessingService/target/surefire-reports/TEST-org.jacp.%s.xml";
    public static String txtReportHostPath = "%s%s.txt";
    public static String xmlReportHostPath = "%sTEST-%s.xml";
}
