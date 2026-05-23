package com.student;

import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class that reads values from config.properties.
 * This replaces hardcoded credentials in DBConnection and LoginController.
 *
 * Usage:  AppConfig.get("db.password")
 */
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

    /**
     * Returns the value for the given key, or an empty string if not found.
     */
    public static String get(String key) {
        return props.getProperty(key, "");
    }
}