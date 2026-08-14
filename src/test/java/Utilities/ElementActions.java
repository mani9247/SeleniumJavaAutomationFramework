package Utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ElementActions {

    private WebDriver driver;

    private WaitUtils wait;

    public ElementActions(WebDriver driver) {

        if (driver == null) {

            throw new IllegalArgumentException(
                    "WebDriver cannot be null"
            );
        }

        this.driver = driver;

        this.wait =
                new WaitUtils(driver);
    }

    public void type(By locator, String text) {

        WebElement element =
                wait.waitForVisibility(locator);

        element.clear();

        element.sendKeys(text);
    }

    public void click(By locator) {

        WebElement element =
                wait.waitForElementToBeClickable(locator);

        element.click();
    }

    public boolean isDisplayed(By locator) {

        try {

            return wait
                    .waitForVisibility(locator)
                    .isDisplayed();

        } catch (Exception e) {

            return false;
        }
    }
}