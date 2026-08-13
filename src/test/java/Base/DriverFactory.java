package Base;

import org.openqa.selenium.WebDriver;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver =
            new ThreadLocal<>();

    public static void setDriver(WebDriver webDriver) {
        driver.set(webDriver);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void unload() {

        WebDriver webDriver = driver.get();

        if (webDriver != null) {
            webDriver.quit();
        }

        driver.remove();
    }
}