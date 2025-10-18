package com.billingsoftware.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
public class DBConnection {

    private static Connection connection;
    private static DB db;

    /*static {
        try {
            // Setup Embedded MariaDB Configuration
            DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
            config.setPort(3307);
            //config.setDataDir("embedded-db");
            //config.setBaseDir("C:/Users/LENOVO/AndroidStudioProjects/billing-system/resources/ch/vorburger/mariadb4j/mariadb-10.2.11/win64");

            // Start Embedded MariaDB
            db = DB.newEmbeddedDB(config.build());
            db.start();
            db.run("CREATE DATABASE IF NOT EXISTS billing_db;");

            // Establish JDBC Connection
            String url = "jdbc:mysql://localhost:3307/billing_db";
            String user = "root";
            String password = "";

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Embedded MariaDB connection established.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("❌ Embedded MariaDB startup failed: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Connection not initialized properly.");
        }
        return connection;
    }

    public static void stopDB() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ JDBC Connection closed.");
            }
            if (db != null) {
                db.stop();
                System.out.println("✅ Embedded MariaDB stopped.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        try {
            // Setup embedded MariaDB configuration
            DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
            config.setPort(3307);
            config.setDataDir("embedded-db");
            config.setUnpackingFromClasspath(false);
            config.setBaseDir("C:/Users/LENOVO/AndroidStudioProjects/billing-system/resources/ch/vorburger/mariadb4j/mariadb-10.2.11/win64");
            db = DB.newEmbeddedDB(config.build());
            //db.start();

            // Create the DB if it doesn't exist
            db.createDB("billing_db");

            String URL = "jdbc:mysql://localhost:3307/billing_db";
            String USER = "root";
            String PASSWORD = ""; // MariaDB4j default: no password

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;

        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("❌ Embedded DB startup failed: " + e.getMessage());
        }
    }

    // Optional test
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Embedded DB connection successful!");
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }
    }*/

    private static final String URL = "jdbc:mysql://localhost:3306/billing_db";
    private static final String USER = "billing_user";
    private static final String PASSWORD = "billing123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Optional: quick test
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Database connection successful!");
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }
    }
}
