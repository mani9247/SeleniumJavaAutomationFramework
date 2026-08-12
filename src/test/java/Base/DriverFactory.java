package Base;

import Utilities.ConfigReader;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.time.Duration;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver() {

        ConfigReader config = new ConfigReader();

        String browser = System.getProperty("browser");

        if (browser == null || browser.isBlank()) {
            browser = config.getBrowser();
        }

        System.out.println("Execution : " + config.getExecution());
        System.out.println("Grid URL : " + config.getGridUrl());
        System.out.println("Browser : " + browser);

        try {

            // =====================================================
            // CHROME
            // =====================================================

            if (browser.equalsIgnoreCase("chrome")) {

                ChromeOptions chromeOptions = new ChromeOptions();

                // Docker / Selenium Grid stability
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-gpu");

                chromeOptions.setAcceptInsecureCerts(true);
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

                if ("true".equalsIgnoreCase(
                        System.getProperty("headless"))) {

                    chromeOptions.addArguments("--headless=new");
                }

                if (config.getExecution().equalsIgnoreCase("grid")) {

                    driver.set(
                            new RemoteWebDriver(
                                    URI.create(config.getGridUrl()).toURL(),
                                    chromeOptions
                            )
                    );

                } else {

                    driver.set(
                            new ChromeDriver(chromeOptions)
                    );
                }
            }


            // =====================================================
            // FIREFOX
            // =====================================================

            else if (browser.equalsIgnoreCase("firefox")) {

                FirefoxOptions firefoxOptions = new FirefoxOptions();

                firefoxOptions.setAcceptInsecureCerts(true);

                // Important for Jenkins/Grid navigation timeout
                firefoxOptions.setPageLoadStrategy(
                        PageLoadStrategy.EAGER
                );

                if ("true".equalsIgnoreCase(
                        System.getProperty("headless"))) {

                    firefoxOptions.addArguments("-headless");
                }

                if (config.getExecution().equalsIgnoreCase("grid")) {

                    driver.set(
                            new RemoteWebDriver(
                                    URI.create(config.getGridUrl()).toURL(),
                                    firefoxOptions
                            )
                    );

                } else {

                    driver.set(
                            new FirefoxDriver(firefoxOptions)
                    );
                }
            }


            // =====================================================
                         // EDGE
            // =====================================================

            else if (browser.equalsIgnoreCase("edge")) {

                EdgeOptions edgeOptions = new EdgeOptions();

                edgeOptions.addArguments("--disable-dev-shm-usage");
                edgeOptions.addArguments("--no-sandbox");
                edgeOptions.addArguments("--disable-gpu");
                edgeOptions.addArguments("--remote-allow-origins=*");

                edgeOptions.setAcceptInsecureCerts(true);

                edgeOptions.setPageLoadStrategy(
                        PageLoadStrategy.EAGER
                );

                if ("true".equalsIgnoreCase(
                        System.getProperty("headless"))) {

                    edgeOptions.addArguments("--headless=new");
                }

                if (config.getExecution().equalsIgnoreCase("grid")) {

                    System.out.println(
                            "Creating Edge RemoteWebDriver..."
                    );

                    driver.set(
                            new RemoteWebDriver(
                                    URI.create(
                                            config.getGridUrl()
                                    ).toURL(),
                                    edgeOptions
                            )
                    );

                    System.out.println(
                            "Edge RemoteWebDriver created successfully."
                    );

                } else {

                    driver.set(
                            new EdgeDriver(edgeOptions)
                    );
                }
            }


            // =====================================================
            // INVALID BROWSER
            // =====================================================

            else {

                throw new RuntimeException(
                        "Invalid Browser : " + browser
                );
            }


            // =====================================================
                           // WINDOW SIZE
            // =====================================================

            System.out.println("DEBUG Execution = [" + config.getExecution() + "]");
            System.out.println("DEBUG Driver Class = " + getDriver().getClass().getName());

            if (config.getExecution().equalsIgnoreCase("grid")) {

                System.out.println("Grid execution - skipping window resize");

            } else {

                try {

                    getDriver()
                            .manage()
                            .window()
                            .maximize();

                } catch (Exception e) {

                    System.out.println(
                            "Window maximize skipped: " + e.getMessage()
                    );
                }
            }

            // =====================================================
            // TIMEOUTS
            // =====================================================

            getDriver()
                    .manage()
                    .timeouts()
                    .implicitlyWait(
                            Duration.ofSeconds(
                                    config.getImplicitWait()
                            )
                    );

            getDriver()
                    .manage()
                    .timeouts()
                    .pageLoadTimeout(
                            Duration.ofSeconds(
                                    config.getPageLoadTimeout()
                            )
                    );


            // =====================================================
            // OPEN APPLICATION
            // =====================================================

            System.out.println("-------------------------------------");
            System.out.println("Browser      : " + browser);
            System.out.println("Execution    : " + config.getExecution());
            System.out.println("Grid URL     : " + config.getGridUrl());
            System.out.println("Thread ID    : " + Thread.currentThread().getId());
            System.out.println("-------------------------------------");

            System.out.println(
                    "Opening URL : " + config.getUrl()
            );

            getDriver().get(config.getUrl());

            System.out.println(
                    "Application opened successfully."
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to initialize driver : "
                            + e.getMessage(),
                    e
            );
        }
    }


    // =====================================================
    // GET DRIVER
    // =====================================================

    public static WebDriver getDriver() {

        return driver.get();
    }


    // =====================================================
    // QUIT DRIVER
    // =====================================================

    public static void quitDriver() {

        if (driver.get() != null) {

            driver.get().quit();

            driver.remove();
        }
    }
}