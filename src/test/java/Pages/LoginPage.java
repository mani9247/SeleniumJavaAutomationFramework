package Pages;


import Base.BasePage;
import Utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.PageFactory;


public class LoginPage extends BasePage {
    private static final Logger logger =
            LogManager.getLogger(LoginPage.class);

    public LoginPage(WebDriver driver){

        super(driver);
    }
    private By txtUsername = By.name("username");

    private By txtPassword = By.name("password");

    private By btnLogin =
            By.xpath("//button[@type='submit']");

    private By invalidCredentials =
            By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']");

    public void enterUsername(String user){

        actions.type(txtUsername,user);
        logger.info("Entering username");
    }


    public void enterPassword(String pwd) {

        actions.type(txtPassword,pwd);
        logger.info("Entering password");

    }

    public void clickLogin() {

        actions.click(btnLogin);
        logger.info("Clicking Login Button");
    }
    public boolean isErrorMessageDisplayed() {

        try {
            return actions.isDisplayed(invalidCredentials);

        } catch (Exception e) {

            return false;
        }
    }
    public boolean isDashboardDisplayed() {
        try {
            wait.waitForUrlContains("dashboard");
            logger.info("Dashboard displayed successfully");
            return true;
        } catch (Exception e) {
            logger.error("Dashboard is not displayed");
            return false;
        }
    }
}

