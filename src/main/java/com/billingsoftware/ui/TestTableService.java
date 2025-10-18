package com.billingsoftware.ui;

import com.billingsoftware.util.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestTableService {
    public static void initTestTable() throws Exception {
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(50))");
        stmt.execute("INSERT INTO test_table (id, name) VALUES (1, 'Hari') ON DUPLICATE KEY UPDATE name = 'Hari'");
        stmt.close();
    }

    public static void printTestTable() throws Exception {
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM test_table");
        while (rs.next()) {
            System.out.println("✅ Row: id=" + rs.getInt("id") + ", name=" + rs.getString("name"));
        }
        rs.close();
        stmt.close();
    }
}
