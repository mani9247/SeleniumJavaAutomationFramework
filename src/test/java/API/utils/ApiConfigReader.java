package API.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiConfigReader {

    private static final Properties properties = new Properties();

    static {

        // Default environment = QA
        String env = System.getProperty("env", "qa").toLowerCase();

        String fileName = "api-" + env + ".properties";

        try (InputStream input =
                     ApiConfigReader.class
                             .getClassLoader()
                             .getResourceAsStream(fileName)) {

            if (input == null) {
                throw new RuntimeException(
                        "Configuration file not found: " + fileName
                );
            }

            properties.load(input);

            System.out.println("=================================");
            System.out.println("API Environment : " + env.toUpperCase());
            System.out.println("Config File     : " + fileName);
            System.out.println("Base URL        : " +
                    properties.getProperty("baseUrl"));
            System.out.println("=================================");

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to load API configuration: " + fileName,
                    e
            );
        }
    }


    public static String get(String key) {

        String value = properties.getProperty(key);

        if (value == null) {
            throw new RuntimeException(
                    "Property not found: " + key
            );
        }

        return value;
    }
}