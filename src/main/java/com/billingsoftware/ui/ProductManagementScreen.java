package com.billingsoftware.ui;

import com.billingsoftware.dao.ProductDAO;
import com.billingsoftware.model.Product;
import com.billingsoftware.util.RoundedButton;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ProductManagementScreen extends JFrame{
    private JTextField nameField, groupField, retailField, wholesaleField, stockField;
    //private JButton addBtn, updateBtn, deleteBtn;
    private RoundedButton addBtn, updateBtn, deleteBtn;

    public ProductManagementScreen() {
            setTitle("Product Management");
            setSize(500, 500);  // Increased size
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            // === Title ===
            JLabel titleLabel = new JLabel("Product Management", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
            add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

// Product Name
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Product Name:"), gbc);
        nameField = new JTextField(20);
        nameField.setFont(fieldFont);
        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);

// Group Name
        gbc.gridx = 0; gbc.gridy++;
        mainPanel.add(new JLabel("Group Name:"), gbc);
        groupField = new JTextField(20);
        groupField.setFont(fieldFont);
        gbc.gridx = 1;
        mainPanel.add(groupField, gbc);

// Retail Price
        gbc.gridx = 0; gbc.gridy++;
        mainPanel.add(new JLabel("Retail Price:"), gbc);
        retailField = new JTextField(20);
        retailField.setFont(fieldFont);
        gbc.gridx = 1;
        mainPanel.add(retailField, gbc);

// Wholesale Price
        gbc.gridx = 0; gbc.gridy++;
        mainPanel.add(new JLabel("Wholesale Price:"), gbc);
        wholesaleField = new JTextField(20);
        wholesaleField.setFont(fieldFont);
        gbc.gridx = 1;
        mainPanel.add(wholesaleField, gbc);

// Stock
        gbc.gridx = 0; gbc.gridy++;
        mainPanel.add(new JLabel("Stock:"), gbc);
        stockField = new JTextField(20);
        stockField.setFont(fieldFont);
        gbc.gridx = 1;
        mainPanel.add(stockField, gbc);

// Buttons panel (Add / Update / Delete)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));
        RoundedButton addBtn = new RoundedButton("Add");
        RoundedButton updateBtn = new RoundedButton("Update");
        RoundedButton deleteBtn = new RoundedButton("Delete");
        addBtn.setFont(labelFont); updateBtn.setFont(labelFont); deleteBtn.setFont(labelFont);
        buttonPanel.add(addBtn); buttonPanel.add(updateBtn); buttonPanel.add(deleteBtn);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

// ==== Quick Update Section ====
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel quickLabel = new JLabel("🔁 Quick Update (by Product Name)");
        quickLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(quickLabel, gbc);

// Product Name (for quick update)
        gbc.gridy++;
        mainPanel.add(new JLabel("Product Name:"), gbc);
        JTextField quickNameField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(quickNameField, gbc);

// Group
        gbc.gridx = 0; gbc.gridy++;
        mainPanel.add(new JLabel("New Group Name:"), gbc);
        JTextField quickGroupField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(quickGroupField, gbc);

// Retail
        gbc.gridx = 0; gbc.gridy++;
        mainPanel.add(new JLabel("New Retail Price:"), gbc);
        JTextField quickRetailField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(quickRetailField, gbc);

// Wholesale
        gbc.gridx = 0; gbc.gridy++;
        mainPanel.add(new JLabel("New Wholesale Price:"), gbc);
        JTextField quickWholesaleField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(quickWholesaleField, gbc);

// Stock
        gbc.gridx = 0; gbc.gridy++;
        mainPanel.add(new JLabel("New Stock:"), gbc);
        JTextField quickStockField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(quickStockField, gbc);

// Quick update button
        RoundedButton quickUpdateBtn = new RoundedButton("Update Selected Field(s)");
        quickUpdateBtn.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(quickUpdateBtn, gbc);

// === Wrap in scroll pane ===
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);


        // === Form Panel ===
            /*JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(12, 12, 12, 12);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            Font labelFont = new Font("Arial", Font.BOLD, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            // Fields
            nameField = new JTextField(20);
            groupField = new JTextField(20);
            retailField = new JTextField(20);
            wholesaleField = new JTextField(20);
            stockField = new JTextField(20);

            JLabel[] labels = {
                    new JLabel("Product Name:"), new JLabel("Group:"),
                    new JLabel("Retail Price:"), new JLabel("Wholesale Price:"),
                    new JLabel("Stock:")
            };
            JTextField[] fields = {nameField, groupField, retailField, wholesaleField, stockField};

            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0; gbc.gridy = i;
                labels[i].setFont(labelFont);
                formPanel.add(labels[i], gbc);

                gbc.gridx = 1;
                fields[i].setFont(fieldFont);
                fields[i].setMinimumSize(new Dimension(200, 30)); // prevent shrinking too small
                formPanel.add(fields[i], gbc);
            }

            // === Button Panel ===
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
            addBtn = new RoundedButton("Add");
            updateBtn = new RoundedButton("Update");
            deleteBtn = new RoundedButton("Delete");

            Font buttonFont = new Font("Arial", Font.BOLD, 16);
            Dimension buttonSize = new Dimension(110, 40);

            //JButton[] buttons = {addBtn, updateBtn, deleteBtn};
        RoundedButton[] buttons = {addBtn, updateBtn, deleteBtn};
            for (RoundedButton btn : buttons) {
                btn.setFont(buttonFont);
                btn.setPreferredSize(buttonSize);
                buttonPanel.add(btn);
            }

        JLabel quickUpdateLabel = new JLabel("🔁 Quick Update (by Product Name)");
        quickUpdateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        quickUpdateLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

// Fields for quick update
        JTextField quickNameField = new JTextField(20);
        JTextField quickGroupField = new JTextField(20);
        JTextField quickRetailField = new JTextField(20);
        JTextField quickWholesaleField = new JTextField(20);
        JTextField quickStockField = new JTextField(20);

        JButton quickUpdateBtn = new RoundedButton("Update Selected Field(s)");
        quickUpdateBtn.setFont(new Font("Arial", Font.BOLD, 14));
        quickUpdateBtn.setBackground(new Color(75, 125, 175));
        quickUpdateBtn.setForeground(Color.WHITE);


        gbc.gridy++; // Leave one row
        gbc.gridwidth = 2;
        formPanel.add(quickUpdateLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        formPanel.add(new JLabel("Product Name (required):"), gbc);
        gbc.gridx = 1;
        formPanel.add(quickNameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("New Group Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(quickGroupField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("New Retail Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(quickRetailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("New Wholesale Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(quickWholesaleField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("New Stock:"), gbc);
        gbc.gridx = 1;
        formPanel.add(quickStockField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(quickUpdateBtn, gbc);*/
;

            // === Wrapper Panel to center content ===
            /*JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(formPanel, BorderLayout.CENTER);
            centerPanel.add(buttonPanel, BorderLayout.SOUTH);
            centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));

            // Prevent shrinking
            centerPanel.setMinimumSize(new Dimension(400, 300));
            add(centerPanel, BorderLayout.CENTER);

            setMinimumSize(new Dimension(500, 500)); */ // Prevent extreme shrink
            setVisible(true);

        addBtn.addActionListener(e -> {
            Product p = getProductFromForm();

            try {
                if (ProductDAO.addProduct(p)) {
                    JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    notifyViewProductScreen();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Product with the same Name and Group already exists.",
                            "Duplicate Entry",
                            JOptionPane.WARNING_MESSAGE);
                    //JOptionPane.showMessageDialog(this, "❌ Failed to add product.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(this,
                        "Product with the same Name and Group already exists.",
                        "Duplicate Entry",
                        JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "A database error occurred while adding product.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        updateBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter Product ID to Update:");
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr.trim());
                    Product p = getProductFromForm();
                    p.setId(id);
                    if (ProductDAO.updateProduct(p)) {
                        JOptionPane.showMessageDialog(this, "Product updated.");
                        clearForm();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid ID.");
                }
            }
        });

        quickUpdateBtn.addActionListener(e -> {
            String name = quickNameField.getText().trim();
            String newGroup = quickGroupField.getText().trim();
            String retailStr = quickRetailField.getText().trim();
            String wholesaleStr = quickWholesaleField.getText().trim();
            String stockStr = quickStockField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Product name is required for quick update.");
                return;
            }

            boolean success = ProductDAO.updateProductByName(name, newGroup, retailStr, wholesaleStr, stockStr);
            if (success) {
                JOptionPane.showMessageDialog(null, "Product updated successfully.");
                // clear fields
                quickGroupField.setText("");
                quickRetailField.setText("");
                quickWholesaleField.setText("");
                quickStockField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "No product found or update failed.");
            }
        });


        deleteBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter Product ID to Delete:");
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr.trim());
                    if (ProductDAO.deleteProduct(id)) {
                        JOptionPane.showMessageDialog(this, "Product deleted.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid ID.");
                }
            }
        });

        setVisible(true);
    }

    private Product getProductFromForm() {
        try {
            String name = nameField.getText().trim();
            String group = groupField.getText().trim();
            double retail = Double.parseDouble(retailField.getText().trim());
            double wholesale = Double.parseDouble(wholesaleField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());

            Product p = new Product();
            p.setName(name);
            p.setGroupName(group);
            p.setPriceRetail(retail);
            p.setPriceWholesale(wholesale);
            p.setStock(stock);
            return p;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number input.");
            return null;
        }
    }

    private void notifyViewProductScreen() {
        try {
            ViewProductScreen.refresh();
        } catch (Exception e) {
            // View screen might not be open; just ignore
        }
    }

    private void clearForm() {
        nameField.setText("");
        groupField.setText("");
        retailField.setText("");
        wholesaleField.setText("");
        stockField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProductManagementScreen::new);
    }
}
