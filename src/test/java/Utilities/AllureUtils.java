package Utilities;

import io.qameta.allure.Allure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class AllureUtils {

    public static void attachScreenshot(String filePath) {

        try {

            System.out.println("======================================");
            System.out.println("Allure File Path : " + filePath);

            File file = new File(filePath);

            System.out.println("Exists : " + file.exists());

            InputStream is = new FileInputStream(file);

            Allure.addAttachment(
                    "Failure Screenshot",
                    "image/png",
                    is,
                    ".png");

            System.out.println("Attachment Added Successfully");

            is.close();

        } catch (Exception e) {

            System.out.println("Attachment Failed");
            e.printStackTrace();

        }

    }

}