package Base;

import Utilities.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;


public class DriverFactory {

    private static ThreadLocal<WebDriver> driver =
            new ThreadLocal<>();

    public static void initDriver() {

        ConfigReader config = new ConfigReader();
        String browser = System.getProperty("browser");

        if (browser == null || browser.isBlank()) {
            browser = config.getBrowser();
        }
        try
        {
            switch (browser.toLowerCase()) {

                case "chrome": {
                    ChromeOptions options = new ChromeOptions();

                    if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
                        options.addArguments("--headless=new");
                    }

                    if (config.getExecution().equalsIgnoreCase("grid")) {
                        driver.set(new RemoteWebDriver(new URL(config.getGridUrl()), options));

                    } else {

                        driver.set(new ChromeDriver(options));

                    }
                    break;
                }
                case "firefox": {
                    FirefoxOptions options = new FirefoxOptions();

                    if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
                        options.addArguments("-headless");
                    }

                    if (config.getExecution().equalsIgnoreCase("grid")) {

                        driver.set(new RemoteWebDriver(
                                new URL(config.getGridUrl()),
                                options));

                    } else {

                        driver.set(new FirefoxDriver(options));

                    }

                    break;
                }

                case "edge": {
                    EdgeOptions options = new EdgeOptions();

                    if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
                        options.addArguments("--headless=new");
                    }

                    if (config.getExecution().equalsIgnoreCase("grid")) {

                        driver.set(new RemoteWebDriver(
                                new URL(config.getGridUrl()),
                                options));

                    } else {

                        driver.set(new EdgeDriver(options));
                    }
                    break;
                }
                default:
                    throw new RuntimeException("Invalid Browser");
            }

            getDriver().manage().window().maximize();
            getDriver().manage().timeouts().implicitlyWait(
                    Duration.ofSeconds(config.getImplicitWait()));

            getDriver().manage().timeouts().pageLoadTimeout(
                    Duration.ofSeconds(config.getPageLoadTimeout()));

            getDriver().get(config.getUrl());
            System.out.println(
                    "Thread ID : "
                            + Thread.currentThread().getId());


        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if(getDriver() != null){
            getDriver().quit();
            driver.remove();

        }
    }

}