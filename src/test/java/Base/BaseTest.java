package Base;

import Utilities.ConfigReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
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


            // =================================================
            // BROWSER SELECTION
            // =================================================

            if (browser == null ||
                    browser.trim().isEmpty()) {

                browser = browserFromConfig;
            }

            browser = browser.trim().toLowerCase();


            System.out.println("Browser : " + browser);
            System.out.println("Grid URL: " + gridUrl);
            System.out.println("App URL : " + appUrl);


            // =================================================
            // CREATE DRIVER
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
            // TIMEOUTS
            // =================================================

            driver.manage()
                    .timeouts()
                    .implicitlyWait(
                            Duration.ofSeconds(0)
                    );

            driver.manage()
                    .timeouts()
                    .pageLoadTimeout(
                            Duration.ofSeconds(120)
                    );


            // =================================================
            // DO NOT USE maximize() WITH DOCKER GRID
            // =================================================

            /*
             * IMPORTANT:
             *
             * Do NOT use:
             *
             * driver.manage().window().maximize();
             *
             * Edge Docker container can hang on
             * maximizeCurrentWindow.
             *
             * Instead use a fixed window size.
             */

            try {

                driver.manage()
                        .window()
                        .setSize(
                                new Dimension(
                                        1920,
                                        1080
                                )
                        );

            } catch (Exception e) {

                logger.warn(
                        "Unable to set browser window size. Continuing...",
                        e
                );
            }


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

            System.out.println("======================================");

            System.out.println(
                    "Thread : "
                            + Thread.currentThread().getId()
            );

            System.out.println("======================================");


            logger.info(
                    "WebDriver initialized successfully. Browser: {}",
                    browser
            );

        } catch (Exception e) {

            logger.error(
                    "Failed to initialize WebDriver",
                    e
            );


            // =================================================
            // CLEANUP IF SETUP FAILS
            // =================================================

            try {

                if (driver != null) {

                    driver.quit();
                }

            } catch (Exception ignored) {
            }


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

        if (browser == null ||
                browser.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Browser cannot be null or empty."
            );
        }


        if (gridUrl == null ||
                gridUrl.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Grid URL cannot be null or empty."
            );
        }


        URL url =
                URI.create(gridUrl)
                        .toURL();


        switch (browser) {

            // =================================================
            // CHROME
            // =================================================

            case "chrome":

                ChromeOptions chromeOptions = new ChromeOptions();

                /*
                 * Set browser size through Chrome options.
                 * This avoids maximize() problems in Docker.
                 */

                chromeOptions.addArguments(
                        "--window-size=1920,1080"
                );

                chromeOptions.addArguments(
                        "--disable-dev-shm-usage"
                );

                chromeOptions.addArguments(
                        "--no-sandbox"
                );

                return new RemoteWebDriver(
                        url,
                        chromeOptions
                );


            // =================================================
            // FIREFOX
            // =================================================

            case "firefox":

                FirefoxOptions firefoxOptions =
                        new FirefoxOptions();

                firefoxOptions.addArguments(
                        "--width=1920"
                );

                firefoxOptions.addArguments(
                        "--height=1080"
                );

                return new RemoteWebDriver(
                        url,
                        firefoxOptions
                );


            // =================================================
            // EDGE
            // =================================================

            case "edge":

                EdgeOptions edgeOptions =
                        new EdgeOptions();

                /*
                 * IMPORTANT:
                 *
                 * Do not call maximize() for Edge Docker.
                 */

                edgeOptions.addArguments(
                        "--window-size=1920,1080"
                );

                edgeOptions.addArguments(
                        "--disable-dev-shm-usage"
                );

                edgeOptions.addArguments(
                        "--no-sandbox"
                );

                return new RemoteWebDriver(
                        url,
                        edgeOptions
                );


            // =================================================
            // DEFAULT
            // =================================================

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


        WebDriver currentDriver = null;


        try {

            // =================================================
            // FIRST GET DRIVER FROM TESTNG RESULT
            // =================================================

            if (result != null) {

                Object driverObject =
                        result.getAttribute("driver");

                if (driverObject instanceof WebDriver) {

                    currentDriver =
                            (WebDriver) driverObject;
                }
            }


            // =================================================
            // FALLBACK TO DRIVER FACTORY
            // =================================================

            if (currentDriver == null) {

                currentDriver =
                        DriverFactory.getDriver();
            }


            // =================================================
            // QUIT DRIVER
            // =================================================

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

            // =================================================
            // REMOVE THREADLOCAL DRIVER
            // =================================================

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