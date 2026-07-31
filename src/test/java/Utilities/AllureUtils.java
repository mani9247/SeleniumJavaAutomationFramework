package Utilities;

import io.qameta.allure.Allure;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AllureUtils {

    public static void attachScreenshot(String filePath) {

        try (InputStream is = new FileInputStream(filePath)) {

            Allure.addAttachment("Failure Screenshot",
                    "image/png",
                    is,
                    ".png");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}