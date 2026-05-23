package com.student;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final Properties props = new Properties();

    static {
        try {
            InputStream input = AppConfig.class.getResourceAsStream(
                    "/com/student/config.properties"
            );
            if (input != null) {
                props.load(input);
                input.close();
                System.out.println("✅ config.properties loaded successfully.");
            } else {
                System.err.println("❌ config.properties not found!");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to load config.properties: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return props.getProperty(key, "");
    }
}