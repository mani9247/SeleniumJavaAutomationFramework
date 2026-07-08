package Base;

import Utilities.ElementActions;
import Utilities.WaitUtils;
import org.openqa.selenium.WebDriver;

public class BasePage {

    protected WebDriver driver;

    protected WaitUtils wait;

    protected ElementActions actions;

    public BasePage(WebDriver driver){

        this.driver = driver;

        wait = new WaitUtils(driver);

        actions = new ElementActions(driver);
    }
}
