package com.billingsoftware.ui;

import com.billingsoftware.dao.UserDAO;
import com.billingsoftware.model.User;
import com.billingsoftware.util.RoundedBorder;
import com.billingsoftware.util.RoundedButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginScreen extends JFrame{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private Point mouseClickPoint;

    int fieldWidth = 280;
    int fieldHeight = 30;
    int x = 50;

    public LoginScreen() {
        setUndecorated(true);
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 380, 400, 30, 30));
        setSize(380, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(44, 62, 80));
        loginPanel.setLayout(null);
        loginPanel.setBounds(0, 0, 380, 400);
        add(loginPanel);



        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setBounds(300, 0, 90, 35);
        buttonsPanel.setOpaque(false);

        JButton minBtn = new JButton("–");
        JButton closeBtn = new JButton("X");

        minBtn.setFont(new Font("Arial", Font.BOLD, 12));
        closeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        minBtn.setForeground(Color.WHITE);
        closeBtn.setForeground(Color.WHITE);
        minBtn.setBackground(new Color(108, 117, 125));
        closeBtn.setBackground(new Color(220, 53, 69));
        minBtn.setBorderPainted(false);
        closeBtn.setBorderPainted(false);
        minBtn.setFocusPainted(false);
        closeBtn.setFocusPainted(false);
        minBtn.setPreferredSize(new Dimension(45, 35));
        closeBtn.setPreferredSize(new Dimension(45, 35));

        closeBtn.addActionListener(e -> System.exit(0));
        minBtn.addActionListener(e -> setState(JFrame.ICONIFIED));

        buttonsPanel.add(minBtn);
        buttonsPanel.add(closeBtn);
        loginPanel.add(buttonsPanel);

        // Title Label
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 40, 380, 30);
        loginPanel.add(titleLabel);

        int x = 60;
        int fieldWidth = 260;
        int fieldHeight = 30;

       /* // Username
        //usernameField = new JTextField();
        usernameField = new RoundedPanel.PromptTextField("Username");
        usernameField.setBounds(x, 90, fieldWidth, fieldHeight);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(new RoundedBorder(8));
        loginPanel.add(usernameField);

        // Password
        //passwordField = new JPasswordField();
        passwordField = new RoundedPanel.PromptPasswordField("Password");
        passwordField.setBounds(x, 140, fieldWidth, fieldHeight);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(new RoundedBorder(8));
        loginPanel.add(passwordField);

        roleCombo = new JComboBox<>(new String[]{"ADMIN", "ENTRY"});
        roleCombo.setBounds(x, 185, fieldWidth, fieldHeight);
        roleCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        roleCombo.setBackground(Color.WHITE);
        roleCombo.setFocusable(false);
        roleCombo.setEditable(false);
        roleCombo.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));  // ← clean padding
        //loginPanel.add(roleCombo);*/

        JPanel centerPanel = new RoundedPanel(30, new Color(255, 255, 255));
        centerPanel.setBounds(30, 80, 320, 220);
        centerPanel.setLayout(new GridBagLayout());
        loginPanel.add(centerPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Color labelColor = new Color(255, 255, 255);

        // Username Label + Field
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        userLabel.setForeground(labelColor);
        centerPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new RoundedPanel.PromptTextField("Username");
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        //usernameField.setBorder(new RoundedBorder(10));
        usernameField.setPreferredSize(new Dimension(180, 36));
        centerPanel.add(usernameField, gbc);

        // Password Label + Field
        gbc.gridx = 0; gbc.gridy++;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        passLabel.setForeground(labelColor);
        centerPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new RoundedPanel.PromptPasswordField("Password");
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(180, 36));
        //passwordField.setBorder(new RoundedBorder(10));
        centerPanel.add(passwordField, gbc);

        // Role Label + Combo
        gbc.gridx = 0; gbc.gridy++;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(labelFont);
        roleLabel.setForeground(labelColor);
        centerPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{"ADMIN", "ENTRY"});
        roleCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        roleCombo.setPreferredSize(new Dimension(180, 36));
        //roleCombo.setBorder(new RoundedBorder(10));
        roleCombo.setFocusable(false);
        centerPanel.add(roleCombo, gbc);

        // ==== Login Button ====
        RoundedButton loginBtn = new RoundedButton("Login");
        loginBtn.setBounds((380 - 150) / 2, 320, 150, 40);
        loginBtn.setBackground(new Color(52, 152, 219));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 16));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginPanel.add(loginBtn);

        loginBtn.addActionListener(e -> handleLogin());


        // Login Button
        /*RoundedButton loginBtn = new RoundedButton("Login");
        loginBtn.setBounds((380 - 150) / 2, 250, 150, 40);
        loginBtn.setBackground(new Color(52, 152, 219));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 16));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginPanel.add(loginBtn);
        loginBtn.addActionListener(e -> handleLogin());
        setVisible(true);*/
        setVisible(true);
    }
    private JButton createWindowButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(100, 149, 237));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(40, 28));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(65, 105, 225));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(100, 149, 237));
            }
        });
        return btn;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String selectedRole = (String) roleCombo.getSelectedItem();

        User user = UserDAO.authenticate(username, password);

        if (user != null) {
            //if (user.getRole().equals(selectedRole)) {
            if (user.getRole().equalsIgnoreCase(selectedRole)){
                JOptionPane.showMessageDialog(this,
                        "Login successful! Role: " + user.getRole(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                dispose(); // Close login
                //new DashboardScreen(user); // ⬅️ open dashboard

                new DashboardScreen(user.getUsername(), user.getRole());


            } else {
                JOptionPane.showMessageDialog(this,
                        "You are not authorized to login as " + selectedRole,
                        "Role mismatch", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password.",
                    "Login failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg;

        public RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }


        static class PromptTextField extends JTextField {
            private final String placeholder;

            public PromptTextField(String placeholder) {
                this.placeholder = placeholder;
                setOpaque(true);
                setBackground(Color.WHITE);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.setColor(Color.GRAY);
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(placeholder, 10, y);
                    g2.dispose();
                }
            }
        }


        static class PromptPasswordField extends JPasswordField {
            private final String placeholder;

            public PromptPasswordField(String placeholder) {
                this.placeholder = placeholder;
                setOpaque(true);
                setBackground(Color.WHITE);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.setColor(Color.GRAY);
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(placeholder, 10, y);
                    g2.dispose();
                }
            }
        }

    }

            private void styleTextField(JTextField field) {
                field.setFont(new Font("Arial", Font.PLAIN, 14));
                field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            }

            private void styleTitleButton(JButton btn, Color bgColor) {
                btn.setFont(new Font("Arial", Font.BOLD, 14));
                btn.setForeground(Color.WHITE);
                btn.setBackground(bgColor);
                btn.setBorderPainted(false);
                btn.setFocusPainted(false);
                btn.setPreferredSize(new Dimension(45, 30));
            }


            private void styleFormLabel(JLabel label) {
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Arial", Font.BOLD, 14));
            }


            public static void main(String[] args) {
                SwingUtilities.invokeLater(LoginScreen::new);
            }

    }
