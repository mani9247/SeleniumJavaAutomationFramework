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
            System.getProperty("gridUrl", "http://localhost:4444/wd/hub");

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
            System.out.println("Starting browser: " + browser);
            System.out.println("Grid URL: " + GRID_URL);
            System.out.println("======================================");

            switch (browser.toLowerCase()) {

                case "chrome":

                    ChromeOptions chromeOptions = new ChromeOptions();

                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--disable-extensions");
                    chromeOptions.addArguments("--disable-background-networking");
                    chromeOptions.addArguments("--disable-software-rasterizer");

                    chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

                    webDriver = new RemoteWebDriver(
                            new URL(GRID_URL),
                            chromeOptions
                    );

                    break;


                case "firefox":

                    FirefoxOptions firefoxOptions = new FirefoxOptions();

                    firefoxOptions.addArguments("-headless");

                    firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

                    webDriver = new RemoteWebDriver(
                            new URL(GRID_URL),
                            firefoxOptions
                    );

                    break;


                case "edge":

                    EdgeOptions edgeOptions = new EdgeOptions();

                    /*
                     * IMPORTANT FOR EDGE IN DOCKER
                     */
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--no-sandbox");
                    edgeOptions.addArguments("--disable-dev-shm-usage");
                    edgeOptions.addArguments("--disable-gpu");
                    edgeOptions.addArguments("--disable-extensions");
                    edgeOptions.addArguments("--disable-background-networking");
                    edgeOptions.addArguments("--disable-software-rasterizer");

                    /*
                     * Prevent some browser features from causing
                     * renderer/network delays.
                     */
                    edgeOptions.addArguments("--disable-features=Translate");
                    edgeOptions.addArguments("--disable-features=BackForwardCache");

                    edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

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


            driver.set(webDriver);

            /*
             * Selenium timeouts
             */
            getDriver().manage()
                    .timeouts()
                    .implicitlyWait(Duration.ofSeconds(10));

            getDriver().manage()
                    .timeouts()
                    .pageLoadTimeout(Duration.ofSeconds(60));

            getDriver().manage()
                    .timeouts()
                    .scriptTimeout(Duration.ofSeconds(30));


            /*
             * Navigate to application
             */
            System.out.println(
                    "Opening application: " + BASE_URL
            );

            getDriver().get(BASE_URL);

            System.out.println(
                    "Application opened successfully."
            );

        } catch (MalformedURLException e) {

            throw new RuntimeException(
                    "Invalid Selenium Grid URL: " + GRID_URL,
                    e
            );

        } catch (Exception e) {

            quitDriver();

            throw new RuntimeException(
                    "Failed to initialize driver for browser: "
                            + browser
                            + " | Grid URL: "
                            + GRID_URL,
                    e
            );
        }
    }


    public static void quitDriver() {

        try {

            if (driver.get() != null) {

                driver.get().quit();

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