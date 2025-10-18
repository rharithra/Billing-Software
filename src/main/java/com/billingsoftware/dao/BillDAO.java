package com.billingsoftware.dao;

import com.billingsoftware.model.Bill;
import com.billingsoftware.model.BillItem;
import com.billingsoftware.model.Product;
import com.billingsoftware.model.ProductSales;
import com.billingsoftware.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class BillDAO {
    public static boolean saveBill(String customerName, String type, double total, double paid, double balance,
                                   List<Product> products, List<Integer> quantities, List<Double> prices) {

        Connection conn = null;
        PreparedStatement billStmt = null;
        PreparedStatement itemStmt = null;
        PreparedStatement stockStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Insert into bills
            String billSql = "INSERT INTO bills (customer_name, bill_type, total_amount, paid_amount, balance_amount) VALUES (?, ?, ?, ?, ?)";
            billStmt = conn.prepareStatement(billSql, Statement.RETURN_GENERATED_KEYS);
            billStmt.setString(1, customerName);
            billStmt.setString(2, type);
            billStmt.setDouble(3, total);
            billStmt.setDouble(4, paid);
            billStmt.setDouble(5, balance);

            int affected = billStmt.executeUpdate();
            if (affected == 0) {
                conn.rollback();
                return false;
            }

            rs = billStmt.getGeneratedKeys();
            rs.next();
            int billId = rs.getInt(1);

            // 2. Insert into bill_items
            String itemSql = "INSERT INTO bill_items (bill_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            itemStmt = conn.prepareStatement(itemSql);

            for (int i = 0; i < products.size(); i++) {
                itemStmt.setInt(1, billId);
                itemStmt.setInt(2, products.get(i).getId());
                itemStmt.setInt(3, quantities.get(i));
                itemStmt.setDouble(4, prices.get(i));
                itemStmt.addBatch();
            }

            itemStmt.executeBatch();

            // 3. Reduce stock
            String stockSql = "UPDATE products SET stock = stock - ? WHERE id = ?";
            stockStmt = conn.prepareStatement(stockSql);

            for (int i = 0; i < products.size(); i++) {
                stockStmt.setInt(1, quantities.get(i));
                stockStmt.setInt(2, products.get(i).getId());
                stockStmt.addBatch();
            }

            stockStmt.executeBatch();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (billStmt != null) billStmt.close();
                if (itemStmt != null) itemStmt.close();
                if (stockStmt != null) stockStmt.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int getLastInsertedBillId() {
        int id = 0;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM bills")) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /*public static List<Bill> getBillsByDate(LocalDate date) {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE DATE(created_at) = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            //stmt.setDate(1, java.sql.Date.valueOf(date));
            stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Bill b = new Bill();
                b.setBillNo(rs.getInt("bill_no"));
                b.setCustomerName(rs.getString("customer_name"));
                b.setTotalAmount(rs.getDouble("total"));
                b.setDate(rs.getDate("bill_date").toLocalDate());
                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }*/

    public static List<ProductSales> getSalesReport(Date from, Date to, String productName) {
        List<ProductSales> result = new ArrayList<>();

        String sql = "SELECT p.name AS product_name, SUM(bi.quantity) AS total_qty, SUM(bi.price * bi.quantity) AS total_amount " +
                "FROM bill_items bi " +
                "JOIN product p ON bi.product_id = p.id " +
                "JOIN bill b ON bi.bill_id = b.id " +
                "WHERE b.created_at BETWEEN ? AND ? ";

        if (productName != null && !productName.isEmpty()) {
            sql += "AND p.name LIKE ? ";
        }

        sql += "GROUP BY p.name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(from.getTime()));
            stmt.setDate(2, new java.sql.Date(to.getTime()));
            if (productName != null && !productName.isEmpty()) {
                stmt.setString(3, "%" + productName + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductSales ps = new ProductSales();
                ps.setProductName(rs.getString("product_name"));
                ps.setTotalQty(rs.getInt("total_qty"));
                ps.setTotalAmount(rs.getDouble("total_amount"));
                result.add(ps);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean updateBillBalance(int billId, double newPaidAmount, double newBalanceAmount) {
        String sql = "UPDATE bills SET paid_amount = ?, balance_amount = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, newPaidAmount);
            ps.setDouble(2, newBalanceAmount);
            ps.setInt(3, billId);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void updateCashReturn(int billId, double cashReturn) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE bills SET cash_return = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, cashReturn);
            stmt.setInt(2, billId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getLastCollectionId() {
        String sql = "SELECT MAX(id) FROM bill_collections";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getOriginalBillNoForCustomer(String customerName) {
        String sql = "SELECT id FROM bills WHERE customer_name = ? AND balance_amount > 0 ORDER BY created_at ASC LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, customerName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double getBillTotalByBillNo(int billNo) {
        String sql = "SELECT total_amount FROM bills WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, billNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_amount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static double getTotalPaidForBill(int billNo) {
        String sql = "SELECT SUM(paid_amount) FROM bill_collections WHERE bill_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, billNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static int getPendingCreditBillIdForCustomer(String customerName) {
        String sql = "SELECT id FROM bills WHERE customer_name = ? AND balance_amount > 0 ORDER BY created_at ASC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Not found
    }

    public static int saveBillCollection(int billId, double paidAmount) {
        String sql = "INSERT INTO bill_collections (bill_id,paid_amount) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, billId);
            ps.setDouble(2, paidAmount);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // return the generated receipt ID
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static double getBillAmount(int billId) {
        String sql = "SELECT total_amount FROM bills WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, billId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_amount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static double getBillPaidAmount(int billId) {
        String sql = "SELECT paid_amount FROM bills WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("paid_amount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static List<Bill> getPendingBillsForCustomer(String customerName) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT id, created_at, total_amount, paid_amount, balance_amount FROM bills WHERE customer_name = ? AND balance_amount > 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Bill bill = new Bill();
                bill.setBillNo(rs.getInt("id"));
                bill.setDate(rs.getDate("created_at").toLocalDate());
                bill.setTotalAmount(rs.getDouble("total_amount"));
                bill.setPaidAmount(rs.getDouble("paid_amount"));
                bill.setBalanceAmount(rs.getDouble("balance_amount"));
                bills.add(bill);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bills;
    }

    public static List<Bill> getBillsByDate(LocalDate date) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT id, customer_name, total_amount, paid_amount, balance_amount, created_at " +
                "FROM bills WHERE DATE(created_at) = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Bill bill = new Bill();
                bill.setBillNo(rs.getInt("id"));
                bill.setCustomerName(rs.getString("customer_name"));
                bill.setTotalAmount(rs.getDouble("total_amount"));
                bill.setPaidAmount(rs.getDouble("paid_amount"));
                bill.setBalanceAmount(rs.getDouble("balance_amount"));
                bill.setDate(rs.getDate("created_at").toLocalDate());
                bills.add(bill);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bills;
    }

    public static List<Bill> getBillsBetweenDates(Date from, Date to) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT id, customer_name, total_amount, paid_amount, balance_amount, created_at FROM bills WHERE DATE(created_at) BETWEEN ? AND ? ORDER BY created_at ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(from.getTime()));
            stmt.setDate(2, new java.sql.Date(to.getTime()));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setBillNo(rs.getInt("id"));
                bill.setCustomerName(rs.getString("customer_name"));
                bill.setTotalAmount(rs.getDouble("total_amount"));
                bill.setPaidAmount(rs.getDouble("paid_amount"));
                bill.setBalanceAmount(rs.getDouble("balance_amount"));
                bill.setDate(rs.getDate("created_at").toLocalDate());

                bills.add(bill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bills;
    }
    public static List<BillItem> getBillItemsByBillNo(int billNo) {
        List<BillItem> items = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT p.name, bi.quantity, bi.price FROM bill_items bi JOIN products p ON bi.product_id = p.id WHERE bi.bill_id = ?")) {

            ps.setInt(1, billNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BillItem item = new BillItem();
                item.setProductName(rs.getString("name"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public static List<Bill> getBillsByDateAndBillNo(Date from, Date to, String billNo) {
        List<Bill> list = new ArrayList<>();
        String query = "SELECT * FROM bills WHERE created_at BETWEEN ? AND ? AND id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, from);
            stmt.setDate(2, to);
            stmt.setString(3, billNo);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Bill b = new Bill();
                b.setDate(rs.getDate("created_at").toLocalDate());
                b.setBillNo(rs.getInt("id"));
                b.setCustomerName(rs.getString("customer_name"));
                b.setTotalAmount(rs.getDouble("total_amount"));
                b.setPaidAmount(rs.getDouble("paid_amount"));
                b.setBalanceAmount(rs.getDouble("balance_amount"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Bill> getBillByBillNo(String billNo) {
        List<Bill> list = new ArrayList<>();
        String query = "SELECT * FROM bills WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, billNo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Bill b = new Bill();
                b.setDate(rs.getDate("created_at").toLocalDate());
                b.setBillNo(rs.getInt("id"));
                b.setCustomerName(rs.getString("customer_name"));
                b.setTotalAmount(rs.getDouble("total_amount"));
                b.setPaidAmount(rs.getDouble("paid_amount"));
                b.setBalanceAmount(rs.getDouble("balance_amount"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


}
