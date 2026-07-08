package Tests;

import Base.BaseTest;
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
                "LoginData");

    }
    @Test(dataProvider = "loginData")
    public  void verifyLogin(String username,
                             String password,String expectedResult){
        LoginPage login =
                new LoginPage(driver);

        login.enterUsername(username);

        login.enterPassword(password);
        login.clickLogin();

        if(expectedResult.equalsIgnoreCase("Pass")) {

            Assert.assertTrue(
                    login.isDashboardDisplayed(),
                    "Dashboard is not displayed after successful login."
            );

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

