package Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    Properties prop;

    public ConfigReader() {
        try {

        String env = System.getProperty("env");
        if (env == null) {
            env = "qa";
        }
        FileInputStream fis =
                    new FileInputStream("src/test/resources/"+ env+ ".properties");

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
        String path = prop.getProperty("excelPath");

        if (path == null) {
            throw new RuntimeException("excelPath is missing in qa.properties");
        }

        return path;
    }
    public int getImplicitWait() {
        String value = prop.getProperty("implicitWait");

        if (value == null) {
            throw new RuntimeException("implicitWait is missing in config.properties");
        }
        return Integer.parseInt(value);
    }

    public int getExplicitWait() {

        String value = prop.getProperty("explicitWait");

        if (value == null) {
            throw new RuntimeException("explicitWait is missing in qa.properties");
        }
        return Integer.parseInt(value);
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(prop.getProperty("pageLoadTimeout"));
    }

    public String getExecution() {
        return prop.getProperty("execution");
    }

    public String getGridUrl() {
        return prop.getProperty("gridUrl");
    }
}
