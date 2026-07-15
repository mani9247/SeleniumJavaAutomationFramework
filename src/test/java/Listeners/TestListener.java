package Listeners;


import Base.DriverFactory;
import Reports.ExtentManager;
import Utilities.ScreenshotUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestListener implements ITestListener {
    private static final Logger logger =
            LogManager.getLogger(TestListener.class);

    ExtentReports extent = ExtentManager.getReports();

    private static ThreadLocal<ExtentTest> test =
            new ThreadLocal<>();;

    @Override
    public void onTestStart(ITestResult result){
        String testName = result.getMethod().getMethodName();

        Object[] data = result.getParameters();

        if (data.length >= 2) {

            testName = testName + " [" + data[0] + ", " + data[1] + "]";
        }

        test.set(extent.createTest(testName));

        logger.info("Test Started : " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed");
        logger.info("Test Passed : " + result.getName());
        test.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {

        test.get().fail(result.getThrowable());
        logger.error("Test Failed : " + result.getName());

        try {

            String path = ScreenshotUtils.captureScreenshot(
                    DriverFactory.getDriver(),
                    result.getName());

            test.get().addScreenCaptureFromPath(path);

        } catch (Exception e) {

            e.printStackTrace();
        }
        test.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("Test Skipped");
        test.remove();
        System.out.println(
                "Skipped : "
                        + result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {

        extent.flush();

        System.out.println("Execution Completed");
    }
}
