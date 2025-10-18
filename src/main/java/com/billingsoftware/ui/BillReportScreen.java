package com.billingsoftware.ui;

import com.billingsoftware.dao.BillDAO;
import com.billingsoftware.model.Bill;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import com.toedter.calendar.JDateChooser;
public class BillReportScreen extends JFrame{
    public BillReportScreen() {
        setTitle("Bill Wise Report");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top Panel for Date and Search Button
        JPanel topPanel = new JPanel(new FlowLayout());

        //JLabel dateLabel = new JLabel("Enter Date (yyyy-mm-dd):");
        JTextField dateField = new JTextField(10);
        JDateChooser fromDateChooser = new JDateChooser();
        JDateChooser toDateChooser = new JDateChooser();
        //JButton searchButton = new JButton("Search");
        JLabel billNoLabel = new JLabel("Bill No:");
        JTextField billNoField = new JTextField(10);

        //topPanel.add(dateLabel);
        //topPanel.add(dateField);
        //topPanel.add(searchButton);
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("From:"));
        filterPanel.add(fromDateChooser);
        filterPanel.add(new JLabel("To:"));
        filterPanel.add(toDateChooser);
        filterPanel.add(billNoLabel);
        filterPanel.add(billNoField);

        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Clear");
        filterPanel.add(searchBtn);
        filterPanel.add(clearBtn);

        // Table and Model
        DefaultTableModel billReportModel = new DefaultTableModel(new String[]{"Date", "Bill No", "Customer", "Total", "Paid", "Balance"}, 0);
        JTable billTable = new JTable(billReportModel);
        JLabel totalSumLabel = new JLabel("Total: 0.0 | Paid: 0.0 | Balance: 0.0");
        JScrollPane scrollPane = new JScrollPane(billTable);

        /*add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);*/

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.add(totalSumLabel);

// Add all panels to the frame
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Search Button Action
        /*searchBtn.addActionListener(e -> {
            try {
                LocalDate selectedDate = LocalDate.parse(dateField.getText().trim());

                List<Bill> bills = BillDAO.getBillsByDate(selectedDate);
                billReportModel.setRowCount(0); // Clear table

                for (Bill b : bills) {
                    billReportModel.addRow(new Object[]{
                            b.getDate(),
                            b.getBillNo(),
                            b.getCustomerName(),
                            b.getTotalAmount(),
                            b.getPaidAmount(),
                            b.getBalanceAmount()
                    });
                }

                if (bills.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No bills found for the selected date.");
                }

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-mm-dd.");
            }
        });*/

        searchBtn.addActionListener(e -> {
            Date from = fromDateChooser.getDate();
            Date to = toDateChooser.getDate();

            /*if (from == null || to == null) {
                JOptionPane.showMessageDialog(this, "Please select both From and To dates.");
                return;
            }*/

            //java.sql.Date sqlFromDate = new java.sql.Date(from.getTime());
            //java.sql.Date sqlToDate = new java.sql.Date(to.getTime());

            String billNo = billNoField.getText().trim();

            //List<Bill> bills = BillDAO.getBillsBetweenDates(sqlFromDate, sqlToDate);

            List<Bill> bills;

           /* if (!billNo.isEmpty()) {
                // Use DAO that filters by both date and bill no
                bills = BillDAO.getBillsByDateAndBillNo(sqlFromDate, sqlToDate, billNo);
            } else {
                bills = BillDAO.getBillsBetweenDates(sqlFromDate, sqlToDate);
            }*/

            if (!billNo.isEmpty() && from != null && to != null) {
                java.sql.Date sqlFromDate = new java.sql.Date(from.getTime());
                java.sql.Date sqlToDate = new java.sql.Date(to.getTime());
               // bills = BillDAO.getBillsByDateAndBillNo(new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime()), billNo);
                bills = BillDAO.getBillsByDateAndBillNo(sqlFromDate, sqlToDate, billNo);
            } else if (!billNo.isEmpty()) {
                // Filter only by bill number
                bills = BillDAO.getBillByBillNo(billNo);
            } else if (from != null && to != null) {
                java.sql.Date sqlFromDate = new java.sql.Date(from.getTime());
                java.sql.Date sqlToDate = new java.sql.Date(to.getTime());
                //bills = BillDAO.getBillsBetweenDates(new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime()));
                bills = BillDAO.getBillsBetweenDates(sqlFromDate, sqlToDate);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter Bill No or select date range.");
                return;
            }

            double totalSum = 0, paidSum = 0, balanceSum = 0;

            billReportModel.setRowCount(0);
            for (Bill b : bills) {
                billReportModel.addRow(new Object[]{
                        b.getDate(),
                        b.getBillNo(),
                        b.getCustomerName(),
                        b.getTotalAmount(),
                        b.getPaidAmount(),
                        b.getBalanceAmount()
                });

                totalSum += b.getTotalAmount();
                paidSum += b.getPaidAmount();
                balanceSum += b.getBalanceAmount();
            }

            totalSumLabel.setText(String.format("Total: %.2f | Paid: %.2f | Balance: %.2f", totalSum, paidSum, balanceSum));
        });

        setVisible(true);
    }
}
