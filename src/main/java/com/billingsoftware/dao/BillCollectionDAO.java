package com.billingsoftware.dao;

import com.billingsoftware.model.BillCollection;
import com.billingsoftware.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillCollectionDAO {
    public static boolean insertCollection(int billId, double paidAmount, double balanceAfter, LocalDate date) {
        String sql = "INSERT INTO bill_collections (bill_id, paid_amount, balance_after, paid_on) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, billId);
            ps.setDouble(2, paidAmount);
            ps.setDouble(3, balanceAfter);
            ps.setDate(4, Date.valueOf(date));

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<BillCollection> getCollectionsByBillId(int billId) {
        List<BillCollection> list = new ArrayList<>();
        String sql = "SELECT * FROM bill_collections WHERE bill_id = ? ORDER BY paid_on";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BillCollection bc = new BillCollection();
                bc.setId(rs.getInt("id"));
                bc.setBillId(rs.getInt("bill_id"));
                bc.setPaidAmount(rs.getDouble("paid_amount"));
                bc.setBalanceAfter(rs.getDouble("balance_after"));
                bc.setPaidOn(rs.getDate("paid_on").toLocalDate());
                list.add(bc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
