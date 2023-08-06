package com.webauto.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

    private static ExtentReports extent;

    public static ExtentReports createInstance(String reportPath) {
        com.aventstack.extentreports.reporter.ExtentSparkReporter sparkReporter = new com.aventstack.extentreports.reporter.ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Test Automation Report");
        sparkReporter.config().setEncoding("utf-8");
        sparkReporter.config().setReportName("Test Results");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        return extent;
    }

    public static ExtentReports getInstance() {
        return extent;
    }
}
