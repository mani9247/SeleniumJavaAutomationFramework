package Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private Properties prop;
    private String env;

    public ConfigReader() {

        env = System.getProperty("env");

        if (env == null || env.isBlank()) {
            env = "qa";
        }

        String configPath =
                "src/test/resources/" + env + ".properties";

        try (FileInputStream fis =
                     new FileInputStream(configPath)) {

            prop = new Properties();
            prop.load(fis);

            System.out.println(
                    "Loaded configuration : " + env + ".properties");

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to load configuration file: "
                            + configPath, e);
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

        if (path == null || path.isBlank()) {
            throw new RuntimeException(
                    "excelPath is missing in "
                            + env + ".properties");
        }

        return path;
    }

    public int getImplicitWait() {

        String value = prop.getProperty("implicitWait");

        if (value == null || value.isBlank()) {
            throw new RuntimeException(
                    "implicitWait is missing in "
                            + env + ".properties");
        }

        return Integer.parseInt(value);
    }

    public int getExplicitWait() {

        String value = prop.getProperty("explicitWait");

        if (value == null || value.isBlank()) {
            throw new RuntimeException(
                    "explicitWait is missing in "
                            + env + ".properties");
        }

        return Integer.parseInt(value);
    }

    public int getPageLoadTimeout() {

        String value = prop.getProperty("pageLoadTimeout");

        if (value == null || value.isBlank()) {
            throw new RuntimeException(
                    "pageLoadTimeout is missing in "
                            + env + ".properties");
        }

        return Integer.parseInt(value);
    }

    public String getExecution() {
        return prop.getProperty("execution");
    }

    public String getGridUrl() {
        return prop.getProperty("gridUrl");
    }
}