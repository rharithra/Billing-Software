package com.billingsoftware.util;

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class EmbeddedDBTest {
    public static void start() {
        DB db = null;
        try {
            // 1️⃣ Setup embedded DB
            DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
            config.setPort(3307);

            db = DB.newEmbeddedDB(config.build());
            db.start();
            //db.createDB("billing_db");
            db.run("CREATE DATABASE IF NOT EXISTS billing_db;");
            db.source("schema.sql");

            // 2️⃣ Connect JDBC
            String url = "jdbc:mysql://localhost:3307/billing_db";
            Connection conn = DriverManager.getConnection(url, "root", "");

            // 3️⃣ Create + insert + select
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(50))");
            stmt.execute("INSERT INTO test_table (id, name) VALUES (1, 'Hari')");

            ResultSet rs = stmt.executeQuery("SELECT * FROM test_table");
            while (rs.next()) {
                System.out.println("✅ Row: id=" + rs.getInt("id") + ", name=" + rs.getString("name"));
            }

            conn.close();
            db.stop();

            System.out.println("✅ Embedded MariaDB is working fine!");

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Ensure DB stops properly to release port 3307
            if (db != null) {
                try {
                    db.stop();
                    System.out.println("🛑 Embedded DB stopped.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
        /*try {
            // Step 1: Configure DB
            DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
            config.setPort(3307); // Avoid default 3306
            config.setUnpackingFromClasspath(false);
            config.setBaseDir("C:/Users/LENOVO/AndroidStudioProjects/billing-system/resources/ch/vorburger/mariadb4j/mariadb-10.2.11/win32");
             db = DB.newEmbeddedDB(config.build());

            // Step 2: Start DB
            db.start();
            db.createDB("billing_db"); // Optional: Creates DB if not exists

            // Step 3: Connect with JDBC
            String url = "jdbc:mysql://localhost:3307/billing_db";
            String user = "root"; // Default for MariaDB4j
            String password = ""; // Empty by default

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {

                // Step 4: Test SQL
                stmt.execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(50))");
                stmt.execute("INSERT INTO test_table VALUES (1, 'Hello')");

                ResultSet rs = stmt.executeQuery("SELECT * FROM test_table");
                while (rs.next()) {
                    System.out.println("✅ Row: " + rs.getInt("id") + ", " + rs.getString("name"));
                }
            }

            System.out.println("✅ Embedded MariaDB is working!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            // Step 5: Stop DB properly
            if (db != null) {
                try {
                    db.stop();
                    System.out.println("🛑 Embedded DB stopped successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }*/
    }

