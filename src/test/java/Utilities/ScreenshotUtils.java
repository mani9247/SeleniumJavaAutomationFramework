package Utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileOutputStream;

public class ScreenshotUtils {

    private static final Logger logger =
            LogManager.getLogger(ScreenshotUtils.class);

    public static String captureScreenshot(WebDriver driver, String testName) {

        try {

            System.out.println("======================================");
            System.out.println("Taking Screenshot...");
            System.out.println("Driver : " + driver);
            System.out.println("Current URL : " + driver.getCurrentUrl());
            System.out.println("Title : " + driver.getTitle());

            byte[] screenshot =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.BYTES);

            String path =
                    System.getProperty("user.dir")
                            + File.separator
                            + "Screenshots"
                            + File.separator
                            + testName
                            + ".png";

            File file = new File(path);
            file.getParentFile().mkdirs();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(screenshot);
            fos.close();

            logger.info("Screenshot saved successfully : " + path);

            return path;

        } catch (Exception e) {

            throw new RuntimeException("Unable to capture screenshot", e);

        }

    }

}