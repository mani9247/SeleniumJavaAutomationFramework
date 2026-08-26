package Pages;

import Base.BasePage;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Step;

public class LoginPage extends BasePage {

    private static final Logger logger =
            LogManager.getLogger(LoginPage.class);

    public LoginPage(WebDriver driver) {

        super(driver);
    }

    private By txtUsername =
            By.name("username");

    private By txtPassword =
            By.name("password");

    private By btnLogin =
            By.xpath("//button[@type='submit']");

    private By invalidCredentials =
            By.xpath(
                    "//p[contains(@class,'oxd-alert-content-text')]"
            );

    @Step("Enter username: {user}")
    public void enterUsername(String user) {

        actions.type(txtUsername, user);

        logger.info(
                "Entering username: {}",
                user
        );
    }

    public void enterPassword(String pwd) {

        Allure.step(
                "Enter password",
                () -> actions.type(txtPassword, pwd)
        );

        logger.info("Entering password");
    }

    @Step("Click Login button")
    public void clickLogin() {

        actions.click(btnLogin);

        logger.info("Clicking Login button");
    }

    @Step("Verify login error message is displayed")
    public boolean isErrorMessageDisplayed() {

        try {

            return actions.isDisplayed(
                    invalidCredentials
            );

        } catch (Exception e) {

            logger.error(
                    "Error message was not displayed"
            );

            return false;
        }
    }

    @Step("Verify Dashboard is displayed")
    public boolean isDashboardDisplayed() {

        try {

            wait.waitForUrlContains(
                    "dashboard"
            );

            logger.info(
                    "Dashboard displayed successfully"
            );

            return true;

        } catch (Exception e) {

            logger.error(
                    "Dashboard is not displayed"
            );

            return false;
        }
    }
}