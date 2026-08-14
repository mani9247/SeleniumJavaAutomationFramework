package Listeners;

import Base.DriverFactory;
import Utilities.ScreenshotUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class TestListener
        implements ITestListener {


    private static final Logger logger =
            LogManager.getLogger(TestListener.class);


    // =========================================================
    // TEST STARTED
    // =========================================================

    @Override
    public void onTestStart(
            ITestResult result) {

        logger.info(
                "Test Started : {}",
                result.getName()
        );

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Thread ID : "
                        + Thread.currentThread().getId()
        );

        /*
         * Get username / expected result if these
         * are available from DataProvider.
         *
         * We don't need them for screenshot logic.
         */
    }


    // =========================================================
    // TEST SUCCESS
    // =========================================================

    @Override
    public void onTestSuccess(
            ITestResult result) {

        logger.info(
                "Test Passed : {}",
                result.getName()
        );

        System.out.println(
                "======================================"
        );
    }


    // =========================================================
    // TEST FAILURE
    // =========================================================

    @Override
    public void onTestFailure(
            ITestResult result) {

        logger.error(
                "Test Failed : {}",
                result.getName()
        );


        WebDriver driver = null;


        // =====================================================
        // FIRST OPTION
        // Get driver from ITestResult
        // =====================================================

        try {

            driver =
                    (WebDriver) result
                            .getAttribute("driver");

        } catch (Exception e) {

            logger.warn(
                    "Unable to get driver from ITestResult",
                    e
            );
        }


        // =====================================================
        // SECOND OPTION
        // Get driver from DriverFactory
        // =====================================================

        if (driver == null) {

            try {

                driver =
                        DriverFactory.getDriver();

            } catch (Exception e) {

                logger.warn(
                        "Unable to get driver from DriverFactory",
                        e
                );
            }
        }


        // =====================================================
        // LOG DRIVER
        // =====================================================

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Taking Screenshot..."
        );

        System.out.println(
                "Driver : " + driver
        );


        // =====================================================
        // DRIVER NULL CHECK
        // =====================================================

        if (driver == null) {

            logger.error(
                    "Driver is NULL. "
                            + "Unable to capture screenshot for test: {}",
                    result.getName()
            );

            return;
        }


        // =====================================================
        // CAPTURE SCREENSHOT
        // =====================================================

        try {

            String screenshotPath =
                    ScreenshotUtils.captureScreenshot(
                            driver,
                            result.getName()
                    );


            if (screenshotPath != null) {

                logger.info(
                        "Screenshot captured successfully: {}",
                        screenshotPath
                );

                /*
                 * Store screenshot path inside TestNG result.
                 * This can be used later by Extent Reports.
                 */
                result.setAttribute(
                        "screenshotPath",
                        screenshotPath
                );

            } else {

                logger.warn(
                        "Screenshot path is null for test: {}",
                        result.getName()
                );
            }

        } catch (Exception e) {

            /*
             * IMPORTANT:
             *
             * Screenshot failure should NOT replace
             * the original test failure.
             */
            logger.error(
                    "Screenshot capture failed for test: "
                            + result.getName(),
                    e
            );
        }
    }


    // =========================================================
    // TEST SKIPPED
    // =========================================================

    @Override
    public void onTestSkipped(
            ITestResult result) {

        logger.warn(
                "Skipped : {}",
                result.getName()
        );
    }


    // =========================================================
    // TEST FAILED BUT WITHIN SUCCESS PERCENTAGE
    // =========================================================

    @Override
    public void onTestFailedButWithinSuccessPercentage(
            ITestResult result) {

        logger.warn(
                "Test failed but within success percentage : {}",
                result.getName()
        );
    }


    // =========================================================
    // SUITE START
    // =========================================================

    @Override
    public void onStart(
            ITestContext context) {

        logger.info(
                "Test Suite Started : {}",
                context.getName()
        );
    }


    // =========================================================
    // SUITE FINISH
    // =========================================================

    @Override
    public void onFinish(
            ITestContext context) {

        logger.info(
                "Test Suite Finished : {}",
                context.getName()
        );

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Execution Completed"
        );

        System.out.println(
                "======================================"
        );
    }
}