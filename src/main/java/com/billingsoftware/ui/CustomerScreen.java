package com.billingsoftware.ui;

import com.billingsoftware.dao.CustomerDAO;
import com.billingsoftware.model.Customer;
import com.billingsoftware.util.RoundedButton;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.SQLIntegrityConstraintViolationException;

public class CustomerScreen extends JFrame{
        public CustomerScreen() {
            setTitle("Customer Registration");
            setSize(500, 350);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            // ===== Title Label =====
            JLabel titleLabel = new JLabel("Customer Registration", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
            add(titleLabel, BorderLayout.NORTH);

            // Use GridBagLayout for clean form alignment
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(new Color(245, 245, 245));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;

            Font labelFont = new Font("Arial", Font.BOLD, 14);
            Font fieldFont = new Font("Arial", Font.PLAIN, 14);

            // Components
            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setFont(labelFont);
            JTextField nameField = new JTextField(20);
            nameField.setFont(fieldFont);

            JLabel phoneLabel = new JLabel("Phone:");
            phoneLabel.setFont(labelFont);
            JTextField phoneField = new JTextField(20);
            phoneField.setFont(fieldFont);

            JLabel phoneErrorLabel = new JLabel();
            phoneErrorLabel.setForeground(Color.RED);
            phoneErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));

            phoneField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) { validatePhone(); }
                public void removeUpdate(DocumentEvent e) { validatePhone(); }
                public void insertUpdate(DocumentEvent e) { validatePhone(); }

                private void validatePhone() {
                    String phone = phoneField.getText().trim();
                    if (!phone.matches("\\d{0,10}")) {
                        phoneErrorLabel.setText("Only digits allowed.");
                    } else if (phone.length() != 10 && phone.length() != 0) {
                        phoneErrorLabel.setText("Phone must be exactly 10 digits.");
                    } else {
                        phoneErrorLabel.setText("");
                    }
                }
            });

            JLabel emailLabel = new JLabel("Email:");
            emailLabel.setFont(labelFont);
            JTextField emailField = new JTextField();
            emailField.setFont(fieldFont);

            JLabel emailErrorLabel = new JLabel();
            emailErrorLabel.setForeground(Color.RED);
            emailErrorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            emailField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) { validateEmail(); }
                public void removeUpdate(DocumentEvent e) { validateEmail(); }
                public void insertUpdate(DocumentEvent e) { validateEmail(); }

                private void validateEmail() {
                    String email = emailField.getText().trim();
                    if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                        emailErrorLabel.setText("Invalid email format.");
                    } else {
                        emailErrorLabel.setText("");
                    }
                }
            });

            JLabel typeLabel = new JLabel("Type:");
            typeLabel.setFont(labelFont);
            JRadioButton cashRadio = new JRadioButton("Cash");
            JRadioButton creditRadio = new JRadioButton("Credit");
            ButtonGroup typeGroup = new ButtonGroup();
            typeGroup.add(cashRadio);
            typeGroup.add(creditRadio);
            cashRadio.setSelected(true);
            cashRadio.setFont(fieldFont);
            creditRadio.setFont(fieldFont);

            JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            radioPanel.setBackground(new Color(245, 245, 245));
            radioPanel.add(cashRadio);
            radioPanel.add(creditRadio);

            JButton saveBtn = new RoundedButton("Save Customer");
            JButton updateBtn = new RoundedButton("Update Customer");
            //JButton updateByPhoneBtn = new JButton("Update By Phone");


            // Add components to panel with GridBagLayout

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(nameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.NONE;
            panel.add(phoneLabel, gbc);

            gbc.gridy++;
            panel.add(phoneErrorLabel, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(phoneField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.NONE;
            panel.add(emailLabel, gbc);

            gbc.gridy++;
            panel.add(emailErrorLabel, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(emailField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.fill = GridBagConstraints.NONE;
            panel.add(typeLabel, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(radioPanel, gbc);

            // Buttons panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            buttonPanel.setBackground(new Color(245, 245, 245));
            saveBtn.setFont(labelFont);
            updateBtn.setFont(labelFont);
            buttonPanel.add(saveBtn);
            buttonPanel.add(updateBtn);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(buttonPanel, gbc);

            add(panel);
            //JScrollPane scrollPane = new JScrollPane(panel);
            //add(scrollPane, BorderLayout.CENTER);

            JLabel updateSectionLabel = new JLabel("Update Customer Info by Phone");
            updateSectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
            updateSectionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

// === Update Panel ===
            JPanel updatePanel = new JPanel(new GridBagLayout());
            updatePanel.setBackground(new Color(245, 245, 245));
            GridBagConstraints ugbc = new GridBagConstraints();
            ugbc.insets = new Insets(8, 8, 8, 8);
            ugbc.anchor = GridBagConstraints.WEST;

            JLabel updatePhoneLabel = new JLabel("Phone:");
            updatePhoneLabel.setFont(labelFont);
            JTextField updatePhoneField = new JTextField(20);
            updatePhoneField.setFont(fieldFont);

            JLabel updateNameLabel = new JLabel("New Name (optional):");
            updateNameLabel.setFont(labelFont);
            JTextField updateNameField = new JTextField(20);
            updateNameField.setFont(fieldFont);

            JLabel updateEmailLabel = new JLabel("New Email (optional):");
            updateEmailLabel.setFont(labelFont);
            JTextField updateEmailField = new JTextField(20);
            updateNameField.setFont(fieldFont);

            JLabel updateTypeLabel = new JLabel("New Type (optional):");
            updateTypeLabel.setFont(labelFont);
            JComboBox<String> updateTypeCombo = new JComboBox<>(new String[]{"", "cash", "credit"});
            updateTypeCombo.setFont(fieldFont);

            JButton updateByPhoneBtn = new RoundedButton("Update By Phone");
            updateByPhoneBtn.setFont(labelFont);

// === Add to update panel ===
            ugbc.gridx = 0; ugbc.gridy = 0;
            updatePanel.add(updatePhoneLabel, ugbc);
            ugbc.gridx = 1;
            updatePanel.add(updatePhoneField, ugbc);

            ugbc.gridx = 0; ugbc.gridy = 1;
            updatePanel.add(updateNameLabel, ugbc);
            ugbc.gridx = 1;
            updatePanel.add(updateNameField, ugbc);

            ugbc.gridx = 0;
            ugbc.gridy = 2;
            updatePanel.add(updateEmailLabel,  ugbc);

            ugbc.gridx = 1;
            //ugbc.fill = GridBagConstraints.HORIZONTAL;
            updatePanel.add(updateEmailField,  ugbc);

            ugbc.gridx = 0; ugbc.gridy = 3;
            updatePanel.add(updateTypeLabel, ugbc);
            ugbc.gridx = 1;
            updatePanel.add(updateTypeCombo, ugbc);

            ugbc.gridx = 0; ugbc.gridy = 4;
            ugbc.gridwidth = 2;
            ugbc.anchor = GridBagConstraints.CENTER;
            updatePanel.add(updateByPhoneBtn, ugbc);

// === Combine panels vertically ===
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.setBackground(new Color(245, 245, 245));
            container.add(panel);
            container.add(updateSectionLabel);
            container.add(updatePanel);

           // add(container, BorderLayout.CENTER);
            JScrollPane scrollPane = new JScrollPane(container);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            add(scrollPane, BorderLayout.CENTER);

        saveBtn.addActionListener(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            if (!phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits.");
                return;
            }
            String type = cashRadio.isSelected() ? "cash" : "credit";
            String email = emailField.getText().trim();
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                JOptionPane.showMessageDialog(this, "Invalid email address.");
                return;
            }


           /* if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, Phone, and Email are required.");
                return;
            }*/
            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, Phone are required.");
                return;
            }

            Customer c = new Customer();
            c.setName(name);
            c.setPhone(phone);
            c.setType(type);
            c.setBalance(0); // Initially 0 balance
            c.setEmail(email);

            try {

            boolean saved = CustomerDAO.addCustomer(c);
            if (saved) {
                JOptionPane.showMessageDialog(this, "Customer saved successfully!");
                nameField.setText("");
                phoneField.setText("");
                emailField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save customer.");
            }
            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(this, "A customer already exists with this email or phone.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while saving customer.");
            }
        });

            updateBtn.addActionListener(e -> {
                String name = nameField.getText();
                String phone = phoneField.getText();
                String type = cashRadio.isSelected() ? "cash" : "credit";
                String email = emailField.getText();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name is required");
                    return;
                }

                Customer c = new Customer();
                c.setName(name);
                c.setPhone(phone);
                c.setType(type);
                c.setEmail(email);

                boolean updated = CustomerDAO.updateCustomer(c);
                if (updated) {
                    JOptionPane.showMessageDialog(this, "Customer updated successfully!");
                    nameField.setText("");
                    phoneField.setText("");
                    emailField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update customer.");
                }
            });

            updateByPhoneBtn.addActionListener(e -> {
                String phone = updatePhoneField.getText().trim();
                String newName = updateNameField.getText().trim();
                String newEmail = updateEmailField.getText().trim();
                String newType = (String) updateTypeCombo.getSelectedItem();

                if (phone.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter the customer's phone number.");
                    return;
                }

                boolean success = CustomerDAO.updateCustomerByPhone(phone, newName, newType,newEmail);
                //boolean success = CustomerDAO.updateCustomerByPhone(c);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Customer updated successfully.");
                    updatePhoneField.setText("");
                    updateNameField.setText("");
                    updateEmailField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "No customer found with that phone number.");
                }
            });



            setVisible(true);
    }
}
