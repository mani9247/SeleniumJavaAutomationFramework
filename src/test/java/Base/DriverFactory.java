package Base;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private static final String GRID_URL =
            System.getProperty(
                    "gridUrl",
                    "http://localhost:4444/wd/hub"
            );

    private static final String BASE_URL =
            System.getProperty(
                    "baseUrl",
                    "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"
            );

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void initDriver(String browser) {

        try {

            WebDriver webDriver;

            System.out.println("======================================");
            System.out.println("Starting browser : " + browser);
            System.out.println("Grid URL         : " + GRID_URL);
            System.out.println("Base URL         : " + BASE_URL);
            System.out.println("======================================");


            switch (browser.toLowerCase()) {

                // =========================================================
                // CHROME
                // =========================================================

                case "chrome":

                    ChromeOptions chromeOptions = new ChromeOptions();

                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--disable-extensions");
                    chromeOptions.addArguments("--disable-background-networking");
                    chromeOptions.addArguments("--disable-software-rasterizer");
                    chromeOptions.addArguments("--window-size=1920,1080");

                    chromeOptions.setPageLoadStrategy(
                            PageLoadStrategy.EAGER
                    );

                    webDriver = new RemoteWebDriver(
                            new URL(GRID_URL),
                            chromeOptions
                    );

                    break;


                // =========================================================
                // FIREFOX
                // =========================================================

                case "firefox":

                    FirefoxOptions firefoxOptions =
                            new FirefoxOptions();

                    firefoxOptions.addArguments("-headless");

                    firefoxOptions.setPageLoadStrategy(
                            PageLoadStrategy.EAGER
                    );

                    webDriver = new RemoteWebDriver(
                            new URL(GRID_URL),
                            firefoxOptions
                    );

                    break;


                // =========================================================
                // EDGE
                // =========================================================

                case "edge":

                    EdgeOptions edgeOptions =
                            new EdgeOptions();

                    /*
                     * Headless mode is important because
                     * Jenkins + Docker does not need a visible browser.
                     */
                    edgeOptions.addArguments("--headless=new");

                    /*
                     * Docker stability
                     */
                    edgeOptions.addArguments("--no-sandbox");
                    edgeOptions.addArguments("--disable-dev-shm-usage");
                    edgeOptions.addArguments("--disable-gpu");

                    /*
                     * Browser window
                     */
                    edgeOptions.addArguments(
                            "--window-size=1920,1080"
                    );

                    /*
                     * Disable unnecessary browser features
                     */
                    edgeOptions.addArguments("--disable-extensions");
                    edgeOptions.addArguments(
                            "--disable-background-networking"
                    );
                    edgeOptions.addArguments(
                            "--disable-software-rasterizer"
                    );

                    /*
                     * Disable features that can sometimes
                     * cause delays in automated environments.
                     */
                    edgeOptions.addArguments(
                            "--disable-features=Translate"
                    );

                    edgeOptions.addArguments(
                            "--disable-features=BackForwardCache"
                    );

                    /*
                     * Disable first-run browser behaviour.
                     */
                    edgeOptions.addArguments(
                            "--no-first-run"
                    );

                    edgeOptions.addArguments(
                            "--no-default-browser-check"
                    );

                    /*
                     * Reduce background activity.
                     */
                    edgeOptions.addArguments(
                            "--disable-background-timer-throttling"
                    );

                    edgeOptions.addArguments(
                            "--disable-renderer-backgrounding"
                    );

                    edgeOptions.addArguments(
                            "--disable-backgrounding-occluded-windows"
                    );

                    /*
                     * Do not wait unnecessarily for every page
                     * resource to finish loading.
                     */
                    edgeOptions.setPageLoadStrategy(
                            PageLoadStrategy.EAGER
                    );

                    webDriver = new RemoteWebDriver(
                            new URL(GRID_URL),
                            edgeOptions
                    );

                    break;


                default:

                    throw new IllegalArgumentException(
                            "Unsupported browser: " + browser
                    );
            }


            // =============================================================
            // STORE DRIVER
            // =============================================================

            driver.set(webDriver);


            // =============================================================
            // SELENIUM TIMEOUTS
            // =============================================================

            getDriver()
                    .manage()
                    .timeouts()
                    .implicitlyWait(
                            Duration.ofSeconds(10)
                    );

            getDriver()
                    .manage()
                    .timeouts()
                    .pageLoadTimeout(
                            Duration.ofSeconds(45)
                    );

            getDriver()
                    .manage()
                    .timeouts()
                    .scriptTimeout(
                            Duration.ofSeconds(30)
                    );


            // =============================================================
            // BROWSER WINDOW
            // =============================================================

            try {

                getDriver()
                        .manage()
                        .window()
                        .setSize(
                                new org.openqa.selenium.Dimension(
                                        1920,
                                        1080
                                )
                        );

            } catch (Exception e) {

                System.out.println(
                        "Unable to set browser window size: "
                                + e.getMessage()
                );
            }


            // =============================================================
            // OPEN APPLICATION
            // =============================================================

            System.out.println(
                    "Opening application..."
            );

            System.out.println(
                    "URL: " + BASE_URL
            );

            getDriver().get(BASE_URL);

            System.out.println(
                    "Application opened successfully."
            );

            System.out.println(
                    "Current URL: "
                            + getDriver().getCurrentUrl()
            );

            System.out.println(
                    "Page title: "
                            + getDriver().getTitle()
            );

        }


        // =============================================================
        // INVALID GRID URL
        // =============================================================

        catch (MalformedURLException e) {

            quitDriver();

            throw new RuntimeException(
                    "Invalid Selenium Grid URL: "
                            + GRID_URL,
                    e
            );
        }


        // =============================================================
        // OTHER DRIVER FAILURE
        // =============================================================

        catch (Exception e) {

            quitDriver();

            throw new RuntimeException(
                    "Failed to initialize driver for browser: "
                            + browser
                            + " | Grid URL: "
                            + GRID_URL
                            + " | Error: "
                            + e.getMessage(),
                    e
            );
        }
    }


    // =============================================================
    // QUIT DRIVER
    // =============================================================

    public static void quitDriver() {

        try {

            WebDriver webDriver = driver.get();

            if (webDriver != null) {

                System.out.println(
                        "Closing browser..."
                );

                webDriver.quit();
            }

        } catch (Exception e) {

            System.out.println(
                    "Error while quitting driver: "
                            + e.getMessage()
            );

        } finally {

            driver.remove();
        }
    }
}