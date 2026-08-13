package Utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private WebDriver driver;

    private WebDriverWait wait;

    public WaitUtils(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );
    }

    public void waitForVisibility(By locator) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    public void waitForUrlContains(String text) {

        wait.until(
                ExpectedConditions.urlContains(text)
        );
    }
}