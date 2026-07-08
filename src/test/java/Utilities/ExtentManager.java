package Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
    static ExtentReports extent;

    public static ExtentReports getReports() {

        if(extent == null){

            ExtentSparkReporter spark =
                    new ExtentSparkReporter("Reports/AutomationReport.html");

            spark.config().setDocumentTitle("Automation Report");
            spark.config().setReportName("OrangeHRM Automation");

            extent = new ExtentReports();

            extent.attachReporter(spark);

            extent.setSystemInfo("Tester","Manikanta");
            extent.setSystemInfo("Browser","Chrome");
            extent.setSystemInfo("Framework","Selenium + TestNG");

        }

        return extent;

    }

}
