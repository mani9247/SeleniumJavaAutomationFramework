package Utilities;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public class AllureUtils {
    public static void attachScreenshot(WebDriver driver) {

        byte[] screenshot =
                ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);

        Allure.addAttachment(
                "Failure Screenshot",
                new ByteArrayInputStream(screenshot));

    }
}
