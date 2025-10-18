package com.billingsoftware.ui;

import com.billingsoftware.dao.UserDAO;
import com.billingsoftware.model.User;

import javax.swing.*;
import java.awt.*;
public class RegisterUserScreen extends JFrame{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    public RegisterUserScreen() {
        setTitle("Register New User");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(20, 20, 350, 230);
        panel.setBackground(new Color(245, 250, 255));
        add(panel);

        JLabel title = new JLabel("Register Entry-Level User", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBounds(50, 10, 250, 30);
        panel.add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(40, 60, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(130, 60, 160, 25);
        panel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(40, 100, 80, 25);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(130, 100, 160, 25);
        panel.add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(40, 140, 80, 25);
        panel.add(roleLabel);

        roleCombo = new JComboBox<>(new String[]{"entry"});
        roleCombo.setBounds(130, 140, 160, 25);
        panel.add(roleCombo);

        JButton saveBtn = new JButton("Save");
        saveBtn.setBounds(130, 180, 100, 30);
        saveBtn.setBackground(new Color(51, 153, 255));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        panel.add(saveBtn);

        saveBtn.addActionListener(e -> handleUserRegistration());

        setVisible(true);
    }

    private void handleUserRegistration() {
        String newUsername = usernameField.getText();
        String newPassword = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        if (newUsername.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields.");
            return;
        }

        // Prompt for admin credentials
        String adminUsername = JOptionPane.showInputDialog(this, "Enter Admin Username:");
        String adminPassword = JOptionPane.showInputDialog(this, "Enter Admin Password:");

        if (adminUsername == null || adminPassword == null) return;

        User admin = UserDAO.authenticate(adminUsername, adminPassword);
        if (admin == null || !admin.getRole().equals("admin")) {
            JOptionPane.showMessageDialog(this, "Invalid admin credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert new user
        boolean success = UserDAO.registerUser(newUsername, newPassword, role);
        if (success) {
            JOptionPane.showMessageDialog(this, "User registered successfully.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
