package org.jacp.utils;

import org.apache.maven.plugin.surefire.log.api.NullConsoleLogger;
import org.apache.maven.plugins.surefire.report.ReportTestSuite;
import org.apache.maven.plugins.surefire.report.SurefireReportParser;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ReportUtils {
    private String xmlReportHostPath = "testReports/fromContainer";

    public void getTestResults() {
        Path filePath = Path.of(xmlReportHostPath);
        final SurefireReportParser surefireReportParser = new SurefireReportParser(
                Collections.singletonList(filePath.toFile()),
                new NullConsoleLogger());
        final List<ReportTestSuite> reportTestSuites = surefireReportParser.parseXMLReportFiles();
        final Map<String, Object> summary = surefireReportParser.getSummary(reportTestSuites);

        System.out.println("TotalTest: " + summary.get("totalTests"));
        System.out.println("TotalFailures: " + summary.get("totalFailures"));
        System.out.println("TotalErrors: " + summary.get("totalErrors"));
        System.out.println("TotalSkipped: " + summary.get("totalSkipped"));

    }
}
