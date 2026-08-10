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

    @DataProvider(name = "loginData", parallel = false)
    public Object[][] getData() throws IOException {
        ConfigReader config = new ConfigReader();

        return ExcelUtils.getExcelData(
                config.getExcelPath(),
                "LoginData");

    }
    @Test(dataProvider = "loginData")
    public  void verifyLogin(String username,
                             String password,String expectedResult){
        LoginPage login =
                new LoginPage(DriverFactory.getDriver());

        login.enterUsername(username);

        login.enterPassword(password);
        login.clickLogin();

        if (expectedResult.equalsIgnoreCase("Pass")) {

            Assert.assertTrue(
                    login.isDashboardDisplayed(),
                    "Dashboard is not displayed"
            );

            // TEMPORARY DIAGNOSTIC
            System.out.println("Dashboard is displayed.");
            System.out.println("URL before screenshot: "
                    + DriverFactory.getDriver().getCurrentUrl());
            System.out.println("Title before screenshot: "
                    + DriverFactory.getDriver().getTitle());

            Utilities.ScreenshotUtils.captureScreenshot(
                    DriverFactory.getDriver(),
                    "DirectScreenshot"
            );

            Assert.fail("Intentional Failure");

        } else {

            Assert.assertTrue(
                    login.isErrorMessageDisplayed(),
                    "Error message is not displayed for invalid login."
            );
        }

        System.out.println("--------------------------------");
        logger.info("Username : {}", username);
        logger.info("Password : {}", password);
        System.out.println("Expected : " + expectedResult);
    }

    }

