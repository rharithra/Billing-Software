package com.billingsoftware.dao;

import com.billingsoftware.model.Customer;
import com.billingsoftware.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CustomerDAO {
    public static boolean addCustomer(Customer c) throws SQLException{
        String sql = "INSERT INTO customers (name, phone, type, balance,email) VALUES (?, ?, ?, ?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getPhone());
            stmt.setString(3, c.getType());
            stmt.setDouble(4, c.getBalance());
            stmt.setString(5, c.getEmail());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Customer getCustomerByName(String name) {
        String sql = "SELECT * FROM customers WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setPhone(rs.getString("phone"));
                c.setType(rs.getString("type"));
                c.setBalance(rs.getDouble("balance"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getAllCustomerNames() {
        List<String> names = new ArrayList<>();
        String query = "SELECT name FROM customers ORDER BY name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                names.add(rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return names;
    }
    public static List<Customer> getAllCustomer() {
        List<Customer> list = new ArrayList<>();
        String query = "SELECT * FROM customers ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setPhone(rs.getString("phone"));
                c.setType(rs.getString("type"));
                c.setBalance(rs.getDouble("balance"));
                c.setEmail(rs.getString("email"));
                //c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                c.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(c);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }



    public static boolean updateBalance(String name, double newBalance) {
        String sql = "UPDATE customers SET balance = ? WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.setString(2, name);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setPhone(rs.getString("phone"));
                c.setType(rs.getString("type"));
                c.setBalance(rs.getDouble("balance"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public static boolean recordPayment(int customerId, double amountPaid) {
        String sql = "UPDATE customers SET balance = balance - ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, amountPaid);
            stmt.setInt(2, customerId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Customer getCustomerByPhone(String phone) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE phone = ?")) {

            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setPhone(rs.getString("phone"));
                customer.setType(rs.getString("type"));
                customer.setBalance(rs.getDouble("balance"));
                return customer;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getMatchingCustomerNames(String input) {
        List<String> names = new ArrayList<>();
        String query = "SELECT DISTINCT name FROM customers WHERE name LIKE ? LIMIT 10";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, input + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }

    public static String getEmailByCustomerName(String customerName) {
        String email = null;
       // String query = "SELECT email FROM customers WHERE name = ?";
        String query = "SELECT email FROM customers WHERE LOWER(name) = LOWER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customerName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }

    public static boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET phone = ?, type = ?, email = ? WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getPhone());
            stmt.setString(2, customer.getType());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getName()); // Assuming name is unique identifier
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateCustomerByPhone(String phone, String newName, String newType, String newEmail) {
        if (phone == null || phone.trim().isEmpty()) return false;

        try (Connection conn = DBConnection.getConnection()) {
            // 1. Get existing customer
            String selectSql = "SELECT * FROM customers WHERE phone = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setString(1, phone);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                // 2. Use existing values if fields are empty
                String finalName = (newName == null || newName.trim().isEmpty()) ? rs.getString("name") : newName;
                String finalType = (newType == null || newType.trim().isEmpty()) ? rs.getString("type") : newType;
                String finalEmail = (newEmail == null || newEmail.trim().isEmpty()) ? rs.getString("email") : newEmail;

                // 3. Update with final values
                String updateSql = "UPDATE customers SET name = ?, type = ?, email = ? WHERE phone = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, finalName);
                updateStmt.setString(2, finalType);
                updateStmt.setString(3, finalEmail);
                updateStmt.setString(4, phone);

                return updateStmt.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void recalculateAndUpdateCustomerBalance(String name) {
        double totalPending = 0.0;
        String query = "SELECT SUM(balance_amount) FROM bills WHERE customer_name = ? AND balance_amount > 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalPending = rs.getDouble(1); // null-safe
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Update customer balance
        updateBalance(name, totalPending);
    }
    public static double getCustomerBalanceByName(String name) {
        String query = "SELECT balance FROM customers WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }





}
