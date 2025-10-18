package com.billingsoftware.ui;

import com.billingsoftware.dao.BillDAO;
import com.billingsoftware.model.Bill;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

import static com.itextpdf.text.xml.xmp.DublinCoreProperties.setTitle;

import static java.awt.AWTEventMulticaster.add;

public class DailySalesReportScreen extends JFrame{
    public DailySalesReportScreen() {
        setTitle("Daily Sales Report");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Bill No", "Customer", "Amount", "Date"}, 0);
        JTable table = new JTable(model);

        List<Bill> bills = BillDAO.getBillsByDate(LocalDate.now());
        for (Bill b : bills) {
            model.addRow(new Object[]{b.getBillNo(), b.getCustomerName(), b.getTotalAmount(), b.getDate()});
        }

        add(new JScrollPane(table));
        setVisible(true);
    }
}
