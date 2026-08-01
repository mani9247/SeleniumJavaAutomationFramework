package Utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ScreenshotUtils {

    private static final Logger logger =
            LogManager.getLogger(ScreenshotUtils.class);

    public static String captureScreenshot(WebDriver driver, String testName) {

        byte[] screenshot =
                ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);

        File dest = new File(
                System.getProperty("user.dir")
                        + File.separator
                        + "Screenshots"
                        + File.separator
                        + testName
                        + ".png");

        dest.getParentFile().mkdirs();

        try {

            Files.write(dest.toPath(), screenshot);

            logger.info("Screenshot saved successfully: {}", dest.getAbsolutePath());

        } catch (IOException e) {

            throw new RuntimeException("Unable to save screenshot", e);

        }

        return dest.getAbsolutePath();
    }
}