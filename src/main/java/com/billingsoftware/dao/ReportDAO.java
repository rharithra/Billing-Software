package com.billingsoftware.dao;

import com.billingsoftware.model.ProductSalesReport;
import com.billingsoftware.model.SalesData;
import com.billingsoftware.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ReportDAO {
    public  List<ProductSalesReport> getProductWiseSales(Timestamp fromDate, Timestamp toDate, String productName, String groupName) {
        List<ProductSalesReport> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT p.name AS product_name, p.group_name AS group_name, SUM(bi.quantity) AS total_qty, SUM(bi.price) AS total_amount " +
                        "FROM bill_items bi " +
                        "JOIN products p ON bi.product_id = p.id " +
                        "JOIN bills b ON bi.bill_id = b.id " +
                        "WHERE b.created_at BETWEEN ? AND ?"
        );

        // Add conditions
        if (!productName.trim().isEmpty()) {
            sql.append(" AND p.name LIKE ?");
        }
        if (!groupName.trim().isEmpty()) {
            sql.append(" AND p.group_name LIKE ?");
        }

        sql.append(" GROUP BY p.name, p.group_name");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            stmt.setTimestamp(paramIndex++, fromDate);
            stmt.setTimestamp(paramIndex++, toDate);

            if (!productName.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + productName.trim() + "%");
            }

            if (!groupName.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + groupName.trim() + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductSalesReport psr = new ProductSalesReport();
                psr.setProductName(rs.getString("product_name"));
                psr.setGroupName(rs.getString("group_name"));
                psr.setQuantity(rs.getInt("total_qty"));
                psr.setTotalAmount(rs.getDouble("total_amount"));
                list.add(psr);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<String> getAllGroupNames() {
        List<String> groups = new ArrayList<>();
        String sql = "SELECT DISTINCT group_name FROM products ORDER BY group_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                groups.add(rs.getString("group_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }
    public static List<SalesData> getCustomerWiseSales(String customerName, String productName,Date fromDate, Date toDate) {
        List<SalesData> list = new ArrayList<>();

        StringBuilder query = new StringBuilder(
                "SELECT b.created_at, b.customer_name, p.name AS product_name, bd.quantity, bd.price, " +
                        "(bd.quantity * bd.price) AS total " +
                        "FROM bills b " +
                        "JOIN bill_items bd ON b.id = bd.bill_id " +
                        "JOIN products p ON bd.product_id = p.id " +
                        "WHERE 1=1 "
        );

        if (fromDate != null && toDate != null) {
            query.append("AND b.created_at >= ? AND b.created_at < ? ");
        }
        if (customerName != null && !customerName.isEmpty()) {
            query.append("AND b.customer_name = ? ");
        }
        if (productName != null && !productName.isEmpty()) {
            query.append("AND p.name = ? ");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {

            int index = 1;

            if (fromDate != null && toDate != null) {
                ps.setDate(index++, fromDate);
                ps.setDate(index++, toDate);
            }
            if (customerName != null && !customerName.isEmpty()) {
                ps.setString(index++, customerName);
            }
            if (productName != null && !productName.isEmpty()) {
                ps.setString(index++, productName);
            }

        /*StringBuilder query = new StringBuilder(
                "SELECT b.created_at, b.customer_name, p.name AS product_name, bd.quantity, bd.price, (bd.quantity * bd.price) AS total " +
                        "FROM bills b " +
                        "JOIN bill_items bd ON b.id = bd.bill_id " +
                        "JOIN products p ON bd.product_id = p.id " +
                        "WHERE b.created_at BETWEEN ? AND ?"

        );

        if (customerName != null && !customerName.trim().isEmpty()) {
            query.append(" AND b.customer_name LIKE ?");
        }
        if (productName != null && !productName.trim().isEmpty()) {
            query.append(" AND LOWER(p.name) LIKE ?");
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query.toString())) {

            ps.setDate(1, fromDate);
            ps.setDate(2, toDate);
            int paramIndex = 3;

            if (customerName != null && !customerName.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + customerName + "%");
            }
            if (productName != null && !productName.trim().isEmpty()) {
                //ps.setString(paramIndex++, "%" + productName + "%");
                ps.setString(paramIndex++, "%" + productName.toLowerCase() + "%");
            }*/

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SalesData data = new SalesData();
                data.setDate(rs.getDate("created_at"));
                data.setCustomerName(rs.getString("customer_name"));
                data.setProductName(rs.getString("product_name"));
                data.setQty(rs.getInt("quantity"));
                data.setPrice(rs.getDouble("price"));
                data.setTotal(rs.getDouble("total"));
                list.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }



    /*public static List<SalesData> getCustomerWiseSales(String customerName, String productName, Date fromDate, Date toDate) {
        List<SalesData> list = new ArrayList<>();
        StringBuilder query = new StringBuilder(
                "SELECT b.date, p.name AS product_name, bi.qty, bi.price, (bi.qty * bi.price) AS total " +
                        "FROM bill b " +
                        "JOIN bill_item bi ON b.id = bi.bill_id " +
                        "JOIN product p ON bi.product_id = p.id " +
                        "WHERE b.customer_name = ? "
        );

        if (productName != null && !productName.trim().isEmpty()) {
            query.append("AND p.name LIKE ? ");
        }

        if (fromDate != null && toDate != null) {
            query.append("AND b.date BETWEEN ? AND ? ");
        } else if (fromDate != null) {
            query.append("AND b.date >= ? ");
        } else if (toDate != null) {
            query.append("AND b.date <= ? ");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;
            stmt.setString(paramIndex++, customerName);

            if (productName != null && !productName.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + productName.trim() + "%");
            }

            if (fromDate != null && toDate != null) {
                stmt.setDate(paramIndex++, fromDate);
                stmt.setDate(paramIndex++, toDate);
            } else if (fromDate != null) {
                stmt.setDate(paramIndex++, fromDate);
            } else if (toDate != null) {
                stmt.setDate(paramIndex++, toDate);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SalesData s = new SalesData();
                s.setDate(rs.getDate("date"));
                s.setProductName(rs.getString("product_name"));
                s.setQty(rs.getInt("qty"));
                s.setPrice(rs.getDouble("price"));
                s.setTotal(rs.getDouble("total"));
                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }*/


}
