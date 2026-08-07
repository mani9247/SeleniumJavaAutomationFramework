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

        if (driver == null) {
            throw new RuntimeException("WebDriver is NULL.");
        }

        try {

            System.out.println("======================================");
            System.out.println("Taking Screenshot...");
            System.out.println("Driver : " + driver);
            System.out.println("Driver Class : " + driver.getClass());
            System.out.println("Current URL : " + driver.getCurrentUrl());

            // Capture screenshot as bytes (better for Grid & RemoteWebDriver)
            byte[] screenshot =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.BYTES);

            // Create Screenshots folder if it doesn't exist
            File folder = new File(System.getProperty("user.dir")
                    + File.separator + "Screenshots");

            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Destination file
            File dest = new File(folder,
                    testName + ".png");

            // Save screenshot
            Files.write(dest.toPath(), screenshot);

            System.out.println("Screenshot Saved : "
                    + dest.getAbsolutePath());

            logger.info("Screenshot saved successfully : {}",
                    dest.getAbsolutePath());

            System.out.println("======================================");

            return dest.getAbsolutePath();

        } catch (IOException e) {

            logger.error("Failed to save screenshot", e);
            throw new RuntimeException("Unable to save screenshot", e);

        } catch (Exception e) {

            logger.error("Failed to capture screenshot", e);
            throw new RuntimeException("Unable to capture screenshot", e);

        }

    }

}