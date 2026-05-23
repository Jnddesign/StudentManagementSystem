package com.student;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection connect() {
        try {
            String url      = AppConfig.get("db.url");
            String user     = AppConfig.get("db.user");
            String password = AppConfig.get("db.password");

            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connected to database successfully!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("❌ DRIVER NOT FOUND: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("❌ CONNECTION FAILED: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}