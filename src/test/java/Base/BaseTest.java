package Base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class BaseTest {

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setup(String browser) {

        System.out.println(
                "======================================"
        );

        System.out.println(
                "TEST SETUP"
        );

        System.out.println(
                "Browser: " + browser
        );

        DriverFactory.initDriver(browser);

        System.out.println(
                "Driver initialized successfully"
        );

        System.out.println(
                "======================================"
        );
    }


    public WebDriver getDriver() {

        return DriverFactory.getDriver();
    }


    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        System.out.println(
                "Closing browser..."
        );

        DriverFactory.quitDriver();

        System.out.println(
                "Browser closed."
        );
    }
}