package Tests;

import Base.BaseTest;
import Base.DriverFactory;
import Pages.LoginPage;

import Utilities.ConfigReader;
import Utilities.ExcelUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTest extends BaseTest {

    private static final Logger logger =
            LogManager.getLogger(LoginTest.class);

    @DataProvider(name = "loginData", parallel = true)
    public Object[][] getData() throws IOException {

        ConfigReader config = new ConfigReader();

        return ExcelUtils.getExcelData(
                config.getExcelPath(),
                "LoginData"
        );
    }

    @Test(dataProvider = "loginData")
    public void verifyLogin(
            String username,
            String password,
            String expectedResult) {

        System.out.println(
                "Running test on thread: "
                        + Thread.currentThread().getId()
        );

        LoginPage login =
                new LoginPage(DriverFactory.getDriver());

        login.enterUsername(username);

        login.enterPassword(password);

        login.clickLogin();

        if (expectedResult.equalsIgnoreCase("Pass")) {

            Assert.assertTrue(
                    login.isDashboardDisplayed(),
                    "Dashboard is not displayed after successful login."
            );

            logger.info(
                    "Dashboard displayed successfully for username: {}",
                    username
            );

        } else {

            Assert.assertTrue(
                    login.isErrorMessageDisplayed(),
                    "Error message is not displayed for invalid login."
            );

            logger.info(
                    "Invalid login handled correctly for username: {}",
                    username
            );
        }

        logger.info("--------------------------------");
        logger.info("Username : {}", username);
        logger.info("Expected : {}", expectedResult);
    }
}