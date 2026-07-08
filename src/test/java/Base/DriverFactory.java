package Base;

import Utilities.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver =
            new ThreadLocal<>();

    public static void initDriver() {

        ConfigReader config = new ConfigReader();

        String browser = config.getBrowser();

        switch (browser.toLowerCase()) {

            case "chrome":
                driver.set(new ChromeDriver());
                break;

            case "firefox":
                driver.set(new FirefoxDriver());
                break;

            case "edge":
                driver.set(new EdgeDriver());
                break;

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