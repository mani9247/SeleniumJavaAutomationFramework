package Utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ElementActions {

    private WebDriver driver;

    private WaitUtils wait;

    public ElementActions(WebDriver driver) {

        this.driver = driver;

        this.wait = new WaitUtils(driver);
    }

    public void type(By locator, String text) {

        wait.waitForVisibility(locator);

        WebElement element =
                driver.findElement(locator);

        element.clear();

        element.sendKeys(text);
    }

    public void click(By locator) {

        wait.waitForVisibility(locator);

        driver.findElement(locator).click();
    }

    public boolean isDisplayed(By locator) {

        try {

            wait.waitForVisibility(locator);

            return driver
                    .findElement(locator)
                    .isDisplayed();

        } catch (Exception e) {

            return false;
        }
    }
}