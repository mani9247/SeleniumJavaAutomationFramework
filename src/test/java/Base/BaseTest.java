package Base;

import Utilities.ConfigReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

public class BaseTest {

    private static final Logger logger =
            LogManager.getLogger(BaseTest.class);

    protected ConfigReader config;

    protected WebDriver driver;


    // =========================================================
    // SETUP
    // =========================================================

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(
            @Optional("chrome") String browser) {

        try {

            System.out.println("======================================");
            System.out.println("Starting Test");
            System.out.println("======================================");

            config = new ConfigReader();

            String browserFromConfig =
                    config.getBrowser();

            String gridUrl =
                    config.getGridUrl();

            String appUrl =
                    config.getAppUrl();

            /*
             * If browser is supplied from testng.xml,
             * use that browser.
             *
             * Otherwise use browser from properties file.
             */
            if (browser == null ||
                    browser.trim().isEmpty()) {

                browser = browserFromConfig;
            }

            System.out.println(
                    "Browser : " + browser
            );

            System.out.println(
                    "Grid URL: " + gridUrl
            );

            System.out.println(
                    "App URL : " + appUrl
            );


            // =================================================
            // CREATE REMOTE DRIVER
            // =================================================

            driver =
                    createDriver(
                            browser,
                            gridUrl
                    );


            if (driver == null) {

                throw new RuntimeException(
                        "WebDriver creation returned null."
                );
            }


            // =================================================
            // STORE DRIVER IN DRIVER FACTORY
            // =================================================

            DriverFactory.setDriver(driver);


            // =================================================
            // STORE DRIVER IN TESTNG RESULT
            //
            // This is important for TestListener.
            // It prevents driver=null when taking screenshots.
            // =================================================

            ITestResult result =
                    Reporter.getCurrentTestResult();

            if (result != null) {

                result.setAttribute(
                        "driver",
                        driver
                );
            }


            // =================================================
            // BROWSER SETTINGS
            // =================================================

            driver.manage()
                    .window()
                    .maximize();

            driver.manage()
                    .timeouts()
                    .implicitlyWait(
                            Duration.ofSeconds(0)
                    );

            driver.manage()
                    .timeouts()
                    .pageLoadTimeout(
                            Duration.ofSeconds(60)
                    );


            // =================================================
            // OPEN APPLICATION
            // =================================================

            driver.get(appUrl);


            System.out.println(
                    "Current URL: "
                            + driver.getCurrentUrl()
            );

            System.out.println(
                    "Page Title: "
                            + driver.getTitle()
            );

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "Thread  : "
                            + Thread.currentThread().getId()
            );

            System.out.println(
                    "======================================"
            );


            logger.info(
                    "WebDriver initialized successfully. Browser: {}",
                    browser
            );

        } catch (Exception e) {

            logger.error(
                    "Failed to initialize WebDriver",
                    e
            );

            /*
             * If driver creation failed, make sure
             * DriverFactory does not contain stale driver.
             */
            try {

                DriverFactory.removeDriver();

            } catch (Exception ignored) {
            }

            driver = null;


            throw new RuntimeException(
                    "Failed to initialize WebDriver. "
                            + "Browser: "
                            + browser
                            + " | Grid URL: "
                            + (
                            config != null
                                    ? config.getGridUrl()
                                    : "unknown"
                    )
                            + " | App URL: "
                            + (
                            config != null
                                    ? config.getAppUrl()
                                    : "unknown"
                    ),
                    e
            );
        }
    }


    // =========================================================
    // CREATE DRIVER
    // =========================================================

    private WebDriver createDriver(
            String browser,
            String gridUrl)
            throws MalformedURLException {

        if (browser == null) {

            throw new IllegalArgumentException(
                    "Browser cannot be null."
            );
        }

        if (gridUrl == null ||
                gridUrl.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Grid URL cannot be null or empty."
            );
        }


        browser =
                browser.trim()
                        .toLowerCase();


        URL url =
                URI.create(gridUrl)
                        .toURL();


        switch (browser) {

            case "chrome":

                ChromeOptions chromeOptions =
                        new ChromeOptions();

                /*
                 * Selenium Grid Chrome container
                 * already runs Chrome appropriately.
                 */
                return new RemoteWebDriver(
                        url,
                        chromeOptions
                );


            case "firefox":

                FirefoxOptions firefoxOptions =
                        new FirefoxOptions();

                return new RemoteWebDriver(
                        url,
                        firefoxOptions
                );


            case "edge":

                EdgeOptions edgeOptions =
                        new EdgeOptions();

                return new RemoteWebDriver(
                        url,
                        edgeOptions
                );


            default:

                throw new IllegalArgumentException(
                        "Unsupported browser: "
                                + browser
                                + ". Supported browsers: "
                                + "chrome, firefox, edge"
                );
        }
    }


    // =========================================================
    // TEARDOWN
    // =========================================================

    @AfterMethod(alwaysRun = true)
    public void tearDown(
            ITestResult result) {

        System.out.println(
                "Closing browser. Thread: "
                        + Thread.currentThread().getId()
        );

        /*
         * IMPORTANT:
         *
         * Do NOT remove the driver from DriverFactory
         * before TestListener gets a chance to use it.
         *
         * TestListener executes based on TestNG lifecycle.
         *
         * We have already stored the driver inside
         * ITestResult as well.
         */


        WebDriver currentDriver =
                null;


        try {

            /*
             * First try the driver stored in TestNG result.
             */
            if (result != null) {

                currentDriver =
                        (WebDriver) result
                                .getAttribute("driver");
            }


            /*
             * If not available, get it from DriverFactory.
             */
            if (currentDriver == null) {

                currentDriver =
                        DriverFactory.getDriver();
            }


            if (currentDriver != null) {

                try {

                    currentDriver.quit();

                    logger.info(
                            "Browser closed successfully."
                    );

                } catch (Exception e) {

                    logger.warn(
                            "Error while closing browser",
                            e
                    );
                }
            }

        } finally {

            /*
             * Remove ThreadLocal driver only AFTER
             * browser.quit().
             */
            try {

                DriverFactory.removeDriver();

            } catch (Exception e) {

                logger.warn(
                        "Unable to remove driver from DriverFactory",
                        e
                );
            }

            driver = null;
        }
    }
}