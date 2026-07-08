package Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    Properties prop;

    public ConfigReader() {

        try {

            FileInputStream fis =
                    new FileInputStream("src/test/resources/config.properties");

            prop = new Properties();

            prop.load(fis);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public String getBrowser() {

        return prop.getProperty("browser");
    }

    public String getUrl() {

        return prop.getProperty("url");
    }

    public String getUsername() {

        return prop.getProperty("username");
    }

    public String getPassword() {

        return prop.getProperty("password");
    }
    public String getExcelPath() {
        return prop.getProperty("excelPath");
    }
    public int getImplicitWait() {
        String value = prop.getProperty("implicitWait");

        if (value == null) {
            throw new RuntimeException("implicitWait is missing in config.properties");
        }
        return Integer.parseInt(value);
    }

    public int getExplicitWait() {
        return Integer.parseInt(prop.getProperty("explicitWait"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(prop.getProperty("pageLoadTimeout"));
    }
}
