package Utilities;

import io.qameta.allure.Allure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AllureUtils {

    public static void attachScreenshot(String filePath) {

        System.out.println("Allure File Path : " + filePath);


        File file = new File(filePath);

        System.out.println("Exists : " + file.exists());


        try (InputStream is = new FileInputStream(file)) {

            Allure.addAttachment("Failure Screenshot",
                    "image/png",
                    is,
                    ".png");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}