package Base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseTest {

    protected WebDriver driver;

    public void setup(String browser) {

        try {

            String gridUrl = System.getProperty(
                    "gridUrl",
                    "http://localhost:4444"
            );

            System.out.println("==============================================");
            System.out.println("Browser  : " + browser);
            System.out.println("Grid URL : " + gridUrl);
            System.out.println("==============================================");

            if (browser.equalsIgnoreCase("chrome")) {

                ChromeOptions options = new ChromeOptions();

                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");

                driver = new RemoteWebDriver(
                        new URL(gridUrl),
                        options
                );

            } else if (browser.equalsIgnoreCase("edge")) {

                EdgeOptions options = new EdgeOptions();

                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");

                driver = new RemoteWebDriver(
                        new URL(gridUrl),
                        options
                );

            } else if (browser.equalsIgnoreCase("firefox")) {

                FirefoxOptions options = new FirefoxOptions();

                options.addArguments("--headless");

                driver = new RemoteWebDriver(
                        new URL(gridUrl),
                        options
                );

            } else {

                throw new IllegalArgumentException(
                        "Unsupported browser: " + browser
                );
            }

            System.out.println(
                    "Driver created successfully for: " + browser
            );

            driver.manage().window().maximize();

        } catch (MalformedURLException e) {

            throw new RuntimeException(
                    "Invalid Grid URL: " +
                            System.getProperty("gridUrl"),
                    e
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to initialize driver for browser: "
                            + browser
                            + " | Grid URL: "
                            + System.getProperty(
                            "gridUrl",
                            "http://localhost:4444"
                    )
                            + " | Error: "
                            + e.getMessage(),
                    e
            );
        }
    }

    public void tearDown() {

        if (driver != null) {

            driver.quit();
            driver = null;
        }
    }
}