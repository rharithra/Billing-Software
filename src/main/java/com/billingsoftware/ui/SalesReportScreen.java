package com.billingsoftware.ui;

import com.billingsoftware.dao.ReportDAO;
import com.billingsoftware.model.ProductSalesReport;
import com.billingsoftware.model.SalesData;
import com.billingsoftware.util.ExcelExporter;
import com.billingsoftware.util.RoundedButton;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
//import java.security.Timestamp;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
public class SalesReportScreen extends JFrame{
    private JTextField productField;
    private JDateChooser fromDateChooser;

    private JDateChooser toDateChooser;


    private DefaultTableModel tableModel;
    private JComboBox<String> groupComboBox;
    private JLabel totalLabel;

    public SalesReportScreen() {
        setTitle("Product-wise Sales Report");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel with filters
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

// Row 1: From, To, Product, Group
        fromDateChooser = new JDateChooser();
        fromDateChooser.setPreferredSize(new Dimension(120, 25));
        toDateChooser = new JDateChooser();
        toDateChooser.setPreferredSize(new Dimension(120, 25));
        productField = new JTextField(12);

// Group ComboBox
        JLabel groupLabel = new JLabel("Group:");
        groupComboBox = new JComboBox<>();
        groupComboBox.setPreferredSize(new Dimension(120, 25));
        groupComboBox.addItem(""); // All option

// Load group names from DB
        List<String> groups = ReportDAO.getAllGroupNames();
        for (String group : groups) {
            groupComboBox.addItem(group);
        }

// Add to row1 in correct order
        row1.add(new JLabel("From:"));
        row1.add(fromDateChooser);
        row1.add(new JLabel("To:"));
        row1.add(toDateChooser);
        row1.add(new JLabel("Product:"));
        row1.add(productField);
        row1.add(groupLabel);
        row1.add(groupComboBox);

// Row 2: Buttons
        RoundedButton searchBtn = new RoundedButton("Search");
        RoundedButton exportBtn = new RoundedButton("Export to Excel");

        searchBtn.setPreferredSize(new Dimension(130, 28));
        exportBtn.setPreferredSize(new Dimension(160, 28));

        row2.add(searchBtn);
        row2.add(exportBtn);

// Add rows to main filter panel
        filterPanel.add(row1);
        filterPanel.add(row2);

        productField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                performLiveSearch();
            }

            public void removeUpdate(DocumentEvent e) {
                performLiveSearch();
            }

            public void changedUpdate(DocumentEvent e) {
                performLiveSearch();
            }

            private void performLiveSearch() {
                String productName = productField.getText().trim();
                String groupName = (String) groupComboBox.getEditor().getItem(); // if you want to filter with this too

                Date from = fromDateChooser.getDate();
                Date to = toDateChooser.getDate();
                if (from == null || to == null) return;;
                Timestamp sqlFrom = new Timestamp(from.getTime());
                Timestamp sqlTo = new Timestamp(to.getTime());

                //if (from == null || to == null) return;
                ;

                //java.sql.Date sqlFrom = new java.sql.Date(from.getTime());
               // java.sql.Date sqlTo = new java.sql.Date(to.getTime());

                ReportDAO dao = new ReportDAO();

                List<ProductSalesReport> items = dao.getProductWiseSales(sqlFrom, sqlTo, productName, groupName);
                tableModel.setRowCount(0);

                for (ProductSalesReport item : items) {
                    tableModel.addRow(new Object[]{
                            item.getProductName(),
                            item.getGroupName(),
                            item.getQuantity(),
                            item.getTotalAmount()
                    });
                }
            }
        });

// Add filter panel to the frame
        add(filterPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{"Product", "Group", "Quantity Sold", "Total Amount"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        totalLabel = new JLabel("Total: Qty = 0, Amount = 0.0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

// Layout
        setLayout(new BorderLayout());
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(totalLabel, BorderLayout.SOUTH);

        // Action: Load Report
        //searchBtn.addActionListener(e -> loadReport());
        searchBtn.addActionListener(e -> {
            Date fromDate = fromDateChooser.getDate();
            Date toDate = toDateChooser.getDate();

            if (fromDate == null || toDate == null) {
                JOptionPane.showMessageDialog(this, "Please select both From and To dates.");
                return;
            }

            // Normalize time range
            java.sql.Timestamp fromTimestamp = java.sql.Timestamp.valueOf(
                    LocalDateTime.ofInstant(fromDate.toInstant(), ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0).withNano(0));

            java.sql.Timestamp toTimestamp = java.sql.Timestamp.valueOf(
                    LocalDateTime.ofInstant(toDate.toInstant(), ZoneId.systemDefault()).withHour(23).withMinute(59).withSecond(59).withNano(0));

            // Call DAO correctly
            ReportDAO reportDAO = new ReportDAO();
            List<ProductSalesReport> reportList = reportDAO.getProductWiseSales(
                    fromTimestamp,
                    toTimestamp,
                    productField.getText().trim(),
                    groupComboBox.getSelectedItem() != null ? groupComboBox.getSelectedItem().toString().trim() : ""
            );

            updateTable(reportList);  // make sure updateTable takes List<ProductSalesReport>
            tableModel.setRowCount(0); // Clear existing rows

            int totalQty = 0;
            double totalAmount = 0.0;

            for (ProductSalesReport s : reportList) {
                tableModel.addRow(new Object[]{
                        s.getProductName(), s.getGroupName(), s.getQuantity(), s.getTotalAmount()
                });
                totalQty += s.getQuantity();
                totalAmount += s.getTotalAmount();
            }

            totalLabel.setText("Total: Qty = " + totalQty + ", Amount = ₹" + totalAmount);
        });

        // Action: Export Report
        exportBtn.addActionListener(e -> {
            String password = promptPassword();
            if (password == null || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Export cancelled or password empty.");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Excel File");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().endsWith(".xlsx")) {
                    fileToSave = new File(fileToSave + ".xlsx");
                }

                try {
                    ExcelExporter.exportTableWithPassword(table, fileToSave, password);
                    JOptionPane.showMessageDialog(this, "Exported successfully!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage());
                }
            }
        });

        setVisible(true);
    }

    /*private void loadReport() {
        String date = "";
        if (dateChooser.getDate() != null) {
            date = new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate());
        }

        String product = productField.getText().trim();
        String group = groupField.getText().trim();

        List<ProductSalesReport> list = ReportDAO.getProductWiseSales(date, product, group);
        tableModel.setRowCount(0);

        for (ProductSalesReport r : list) {
            tableModel.addRow(new Object[]{
                    r.getProductName(),
                    r.getGroupName(),
                    r.getQuantity(),
                    r.getTotalAmount()
            });
        }
    }*/

    private String promptPassword() {
        JPasswordField pwdField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(
                this,
                pwdField,
                "Security",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (option == JOptionPane.OK_OPTION) {
            return new String(pwdField.getPassword());
        }
        return null; // Cancel or empty
    }

    private void updateTable(List<ProductSalesReport> list) {
        tableModel.setRowCount(0);
        for (ProductSalesReport r : list) {
            tableModel.addRow(new Object[]{
                    r.getProductName(),
                    r.getGroupName(),
                    r.getQuantity(),
                    r.getTotalAmount()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SalesReportScreen::new);
    }
}
