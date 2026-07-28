package Base;

import Utilities.ConfigReader;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
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

            switch (browser.toLowerCase()) {

                case "chrome":

                    ChromeOptions chromeOptions = new ChromeOptions();

                    // Docker/Grid Stability
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    chromeOptions.addArguments("--disable-gpu");

                    if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
                        chromeOptions.addArguments("--headless=new");
                    }

                    if (config.getExecution().equalsIgnoreCase("grid")) {

                        driver.set(new RemoteWebDriver(
                                new URL(config.getGridUrl()),
                                chromeOptions));

                    } else {

                        driver.set(new ChromeDriver(chromeOptions));

                    }

                    break;

                case "firefox":

                    FirefoxOptions firefoxOptions = new FirefoxOptions();

                    if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
                        firefoxOptions.addArguments("-headless");
                    }

                    if (config.getExecution().equalsIgnoreCase("grid")) {

                        driver.set(new RemoteWebDriver(
                                new URL(config.getGridUrl()),
                                firefoxOptions));

                    } else {

                        driver.set(new FirefoxDriver(firefoxOptions));

                    }

                    break;

                case "edge":

                    EdgeOptions edgeOptions = new EdgeOptions();

                    // Docker/Grid Stability
                    edgeOptions.addArguments("--disable-dev-shm-usage");
                    edgeOptions.addArguments("--no-sandbox");

                    if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
                        edgeOptions.addArguments("--headless=new");
                    }

                    if (config.getExecution().equalsIgnoreCase("grid")) {

                        driver.set(new RemoteWebDriver(
                                new URL(config.getGridUrl()),
                                edgeOptions));

                    } else {

                        driver.set(new EdgeDriver(edgeOptions));

                    }

                    break;

                default:
                    throw new RuntimeException("Invalid Browser : " + browser);

            }

            // Window handling
            if (config.getExecution().equalsIgnoreCase("grid")) {

                getDriver().manage().window().setSize(new Dimension(1920, 1080));

            } else {

                getDriver().manage().window().maximize();

            }

            // Waits
            getDriver().manage().timeouts().implicitlyWait(
                    Duration.ofSeconds(config.getImplicitWait()));

            getDriver().manage().timeouts().pageLoadTimeout(
                    Duration.ofSeconds(config.getPageLoadTimeout()));

            // Launch Application
            getDriver().get(config.getUrl());

            System.out.println("-------------------------------------");
            System.out.println("Browser      : " + browser);
            System.out.println("Execution : " + config.getExecution());
            System.out.println("Grid URL  : " + config.getGridUrl());
            System.out.println("Execution    : " + config.getExecution());
            System.out.println("Thread ID    : " + Thread.currentThread().getId());
            System.out.println("-------------------------------------");

        } catch (Exception e) {

            throw new RuntimeException("Failed to initialize driver : " + e.getMessage(), e);

        }
    }

    public static WebDriver getDriver() {

        return driver.get();

    }

    public static void quitDriver() {

        if (driver.get() != null) {

            driver.get().quit();

            driver.remove();

        }
    }
}