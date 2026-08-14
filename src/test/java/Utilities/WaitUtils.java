package Utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private WebDriver driver;

    private WebDriverWait wait;

    public WaitUtils(WebDriver driver) {

        if (driver == null) {

            throw new IllegalArgumentException(
                    "WebDriver cannot be null"
            );
        }

        this.driver = driver;

        this.wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(20)
                );
    }

    public WebElement waitForVisibility(By locator) {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        locator
                )
        );
    }

    public void waitForUrlContains(String urlText) {

        wait.until(
                ExpectedConditions.urlContains(
                        urlText
                )
        );
    }

    public WebElement waitForElementToBeClickable(By locator) {

        return wait.until(
                ExpectedConditions.elementToBeClickable(
                        locator
                )
        );
    }
}