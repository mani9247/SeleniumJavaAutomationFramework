package Tests;

import Base.BaseTest;
import Base.DriverFactory;
import Pages.LoginPage;
import TestData.LoginData;
import Utilities.ConfigReader;
import Utilities.ExcelUtils;

import io.qameta.allure.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import io.qameta.allure.model.Parameter;


public class LoginTest extends BaseTest {

    private static final Logger logger =
            LogManager.getLogger(LoginTest.class);

    @DataProvider(name = "loginData", parallel = false)
    public Object[][] getData() throws IOException {

        ConfigReader config = new ConfigReader();

        Object[][] excelData =
                ExcelUtils.getExcelData(
                        config.getExcelPath(),
                        "LoginData"
                );

        Object[][] testData =
                new Object[excelData.length][1];

        for (int i = 0; i < excelData.length; i++) {

            testData[i][0] =
                    new LoginData(
                            excelData[i][0].toString(),
                            excelData[i][1].toString(),
                            excelData[i][2].toString()
                    );
        }

        return testData;
    }

    @Test(dataProvider = "loginData")
    @Description("Verify valid and invalid login functionality")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Login")
    @Story("User Login Validation")
    public void verifyLogin(
            LoginData data) {

        String testName;

        if (data.getExpectedResult().equalsIgnoreCase("Pass")) {

            testName =
                    "Valid Login - "
                            + data.getUsername();

        } else if (data.getUsername().equalsIgnoreCase("Admin")) {

            testName =
                    "Invalid Login - Wrong Password";

        } else {

            testName =
                    "Invalid Login - Wrong Username";
        }

        Allure.getLifecycle().updateTestCase(
                testResult ->
                        testResult.setName(testName)
        );
        Allure.parameter(
                "Browser",
                currentBrowser
        );

        Allure.parameter(
                "Environment",
                "QA"
        );

        Allure.parameter(
                "Execution",
                "Selenium Grid"
        );

        Allure.parameter(
                "Username",
                data.getUsername()
        );

        Allure.parameter(
                "Expected Result",
                data.getExpectedResult()
        );

        // =========================================================
// HIDE AUTOMATIC TESTNG DATAPROVIDER PARAMETER
// =========================================================

        Allure.getLifecycle().updateTestCase(testResult -> {

            testResult.getParameters()
                    .stream()
                    .filter(parameter ->
                            parameter.getName() != null &&
                                    parameter.getName().startsWith("arg")
                    )
                    .forEach(parameter ->
                            parameter.setMode(Parameter.Mode.HIDDEN)
                    );
        });

        logger.info(
                "Username : {}",
                data.getUsername()
        );

        logger.info(
                "Expected : {}",
                data.getExpectedResult()
        );

        LoginPage login =
                new LoginPage(
                        DriverFactory.getDriver()
                );

        login.enterUsername(
                data.getUsername()
        );

        login.enterPassword(
                data.getPassword()
        );

        login.clickLogin();

        if (data.getExpectedResult()
                .equalsIgnoreCase("Pass")) {

            Assert.assertTrue(
                    login.isDashboardDisplayed(),
                    "Dashboard is not displayed after successful login."
            );

            logger.info(
                    "Login successful for user: {}",
                    data.getUsername()
            );

        } else {

            Assert.assertTrue(
                    login.isErrorMessageDisplayed(),
                    "Error message is not displayed for invalid login."
            );

            logger.info(
                    "Invalid login correctly rejected for user: {}",
                    data.getUsername()
            );
        }
    }
}