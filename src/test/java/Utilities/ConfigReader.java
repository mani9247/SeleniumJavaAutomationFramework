package Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private final Properties properties;

    public ConfigReader() {

        properties = new Properties();

        try {

            FileInputStream fis =
                    new FileInputStream(
                            "src/test/resources/qa.properties"
                    );

            properties.load(fis);
            fis.close();

            System.out.println("Loaded configuration : qa.properties");

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load qa.properties",
                    e
            );
        }
    }

    // Browser
    public String getBrowser() {

        return properties.getProperty("browser");
    }

    // Execution mode
    public String getExecution() {

        return properties.getProperty("execution");
    }

    // Selenium Grid URL
    public String getGridUrl() {

        return properties.getProperty("gridUrl");
    }

    // Application URL
    public String getAppUrl() {

        return properties.getProperty("url");
    }

    // Implicit wait
    public int getImplicitWait() {

        return Integer.parseInt(
                properties.getProperty("implicitWait")
        );
    }

    // Explicit wait
    public int getExplicitWait() {

        return Integer.parseInt(
                properties.getProperty("explicitWait")
        );
    }

    // Page load timeout
    public int getPageLoadTimeout() {

        return Integer.parseInt(
                properties.getProperty("pageLoadTimeout")
        );
    }

    // Excel path
    public String getExcelPath() {

        return properties.getProperty("excelPath");
    }
}