package Utilities;


import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScreenshotUtils {
    private static final Logger logger =
            LogManager.getLogger(ScreenshotUtils.class);

        public static String captureScreenshot(
                WebDriver driver,
                String testName) {

            TakesScreenshot ts =
                    (TakesScreenshot) driver;

            File src =
                    ts.getScreenshotAs(
                            OutputType.FILE);

            String path =
                    "./Screenshots/"
                            + testName
                            + ".png";

            File dest =
                    new File(path);

            try {

                Files.copy(src.toPath(),
                        dest.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                logger.info("Screenshot saved successfully");


            } catch (IOException e) {
                throw new RuntimeException("Unable to capture screenshot", e);
            }
            return "../Screenshots/" + testName + ".png";
        }
    }

