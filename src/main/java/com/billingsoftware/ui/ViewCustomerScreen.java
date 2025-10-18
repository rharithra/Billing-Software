package com.billingsoftware.ui;

import com.billingsoftware.dao.CustomerDAO;
import com.billingsoftware.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
public class ViewCustomerScreen extends JFrame{
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel model;
    private List<Customer> allCustomers;

    public ViewCustomerScreen() {
        setTitle("View Customers");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        searchField = new JTextField();
        searchField.setToolTipText("Search by name or phone...");
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.addCaretListener(e -> filterCustomers());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        add(topPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{
                "ID", "Name", "Phone", "Type", "Balance", "Email", "Created At"
        }, 0);

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadCustomers();

        setVisible(true);
    }

    private void loadCustomers() {
        allCustomers = CustomerDAO.getAllCustomer();
        showInTable(allCustomers);
    }

    private void filterCustomers() {
        String query = searchField.getText().trim().toLowerCase();
        List<Customer> filtered = allCustomers.stream()
                .filter(c -> c.getName().toLowerCase().contains(query)
                        || c.getPhone().toLowerCase().contains(query))
                .collect(Collectors.toList());
        showInTable(filtered);
    }

    private void showInTable(List<Customer> customers) {
        model.setRowCount(0);
        for (Customer c : customers) {
            model.addRow(new Object[]{
                    c.getId(), c.getName(), c.getPhone(),
                    c.getType(), c.getBalance(),
                    c.getEmail(), c.getCreatedAt()
            });
        }
    }
}
