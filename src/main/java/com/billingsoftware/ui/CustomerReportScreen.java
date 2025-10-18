package com.billingsoftware.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.billingsoftware.util.RoundedButton;
import com.billingsoftware.dao.ReportDAO;
import com.billingsoftware.model.SalesData;
import com.toedter.calendar.JDateChooser;
import java.awt.*;

import java.sql.Date;
//import java.util.Date;
import java.util.Calendar;
import java.util.List;
public class CustomerReportScreen extends JFrame{
    private JTextField customerField;
    private JTextField productField;
    private JDateChooser fromDateChooser;
    private JDateChooser toDateChooser;
    private JTable table;
    private DefaultTableModel tableModel;

    public CustomerReportScreen() {
        setTitle("Customer-wise Sales Report");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initFilterPanel();
        initTable();

        setVisible(true);
    }

    private void initFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        fromDateChooser = new JDateChooser();
        fromDateChooser.setPreferredSize(new Dimension(120, 25));
        toDateChooser = new JDateChooser();
        toDateChooser.setPreferredSize(new Dimension(120, 25));
        customerField = new JTextField(15);
        //JLabel productLabel = new JLabel("Product (optional):");
        productField = new JTextField(15);

        row1.add(new JLabel("From:"));
        row1.add(fromDateChooser);
        row1.add(new JLabel("To:"));
        row1.add(toDateChooser);
        row1.add(new JLabel("Customer:"));
        row1.add(customerField);
        row1.add(new JLabel("product"));
        row1.add(productField);

        RoundedButton searchBtn = new RoundedButton("Search");
        RoundedButton exportBtn = new RoundedButton("Export");

        searchBtn.addActionListener(e -> performSearch());

        row2.add(searchBtn);
        row2.add(exportBtn);

        filterPanel.add(row1);
        filterPanel.add(row2);

        add(filterPanel, BorderLayout.NORTH);
    }

    private void initTable() {
        tableModel = new DefaultTableModel(new String[]{"Date", "Customer", "Product", "Qty", "Price", "Total"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }


    private void performSearch() {
        //String customerName = customerField.getText().trim();
        //String productName = customerField.getText().trim();
        String customerName = customerField.getText().trim();
        String productName = productField.getText().trim();
        java.util.Date fromDateUtil = fromDateChooser.getDate();
        java.util.Date toDateUtil = toDateChooser.getDate();

        if (toDateUtil != null) {
            // Add 1 day to include the full "to" date
            Calendar cal = Calendar.getInstance();
            cal.setTime(toDateUtil);
            cal.add(Calendar.DATE, 1);
            toDateUtil = cal.getTime();
        }

        /*if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Customer name is required.");
            return;
        }*/

        // Convert to java.sql.Date
        java.sql.Date fromDate = fromDateUtil != null ? new java.sql.Date(fromDateUtil.getTime()) : null;
        java.sql.Date toDate = toDateUtil != null ? new java.sql.Date(toDateUtil.getTime()) : null;

        List<SalesData> sales = ReportDAO.getCustomerWiseSales(customerName,productName,fromDate, toDate);
        tableModel.setRowCount(0);
        for (SalesData s : sales) {
            tableModel.addRow(new Object[]{
                    s.getDate(), s.getCustomerName(), s.getProductName(), s.getQty(), s.getPrice(), s.getTotal()
            });
        }
    }

}
