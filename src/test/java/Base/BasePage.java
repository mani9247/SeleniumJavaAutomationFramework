package Base;

import Utilities.ElementActions;
import Utilities.WaitUtils;

import org.openqa.selenium.WebDriver;

public class BasePage {

    protected WebDriver driver;

    protected WaitUtils wait;
    protected ElementActions actions;

    public BasePage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WaitUtils(driver);

        this.actions = new ElementActions(driver);
    }
}