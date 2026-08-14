package Tests;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.URL;


public class GridConnectionTest {

    @Test
    public void testGridConnection() throws Exception {

        System.out.println("======================================");
        System.out.println("GRID CONNECTION TEST");
        System.out.println("======================================");

        ChromeOptions options = new ChromeOptions();

        RemoteWebDriver driver =
                new RemoteWebDriver(
                        new URL("http://localhost:4444"),
                        options
                );

        System.out.println(">>> DRIVER CREATED");
        System.out.println(">>> SESSION ID: " + driver.getSessionId());

        driver.get("https://www.google.com");

        System.out.println(">>> GOOGLE OPENED");
        System.out.println(">>> TITLE: " + driver.getTitle());
        System.out.println(">>> URL: " + driver.getCurrentUrl());

        driver.quit();

        System.out.println(">>> DRIVER QUIT");
    }
}

