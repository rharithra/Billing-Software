package com.billingsoftware.ui;

import com.billingsoftware.util.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardScreen extends JFrame {

    private String userRole;
    private String username;

    JPanel imagePanel = new JPanel(new BorderLayout());
    JPanel rightPanel = new JPanel();

    public DashboardScreen(String username, String role) {
        this.username = username;
        this.userRole = role;

        setTitle("Dashboard - " + role.toUpperCase());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600); // Wider to look balanced
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        // Main container with a soft background
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(235, 235, 235)); // Light gray

        JPanel cardPanel = new JPanel(new BorderLayout());

        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(800, 500));
        cardPanel.setBackground(new Color(250, 250, 250)); // Card background
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        /*cardPanel.setLayout(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(800, 500));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));*/

        JPanel gradientPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                //Color leftColor = new Color(100, 149, 237)
                Color leftColor = new Color(24, 6, 189); // Cornflower Blue
                Color rightColor = new Color(255, 255, 255); // White
                //GradientPaint gp = new GradientPaint(0, 0, leftColor, w, 0, rightColor);
                GradientPaint gp = new GradientPaint(0, 0, leftColor, 300, 0, rightColor);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
// Make sure children don't override background
        imagePanel.setOpaque(false);
        rightPanel.setOpaque(false);

// Add image and buttons to the gradientPanel
        gradientPanel.setOpaque(true);
        gradientPanel.add(imagePanel, BorderLayout.WEST);
        gradientPanel.add(rightPanel, BorderLayout.CENTER);
// Finally, add gradientPanel to the bordered cardPanel
        cardPanel.add(gradientPanel, BorderLayout.CENTER);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setOpaque(true);
// And add cardPanel to the main frame
        add(cardPanel, BorderLayout.CENTER);

       /* cardPanel.add(imagePanel, BorderLayout.WEST);
        cardPanel.add(rightPanel, BorderLayout.CENTER);
        add(cardPanel, BorderLayout.CENTER);*/

        // === Left Image ===
        imagePanel.setPreferredSize(new Dimension(300, 0));
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon("C:/Users/LENOVO/OneDrive/Pictures/Sora/BSoft1.png"));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //JPanel imagePanel = new JPanel(new BorderLayout());
        //imagePanel.setBackground(new Color(250, 250, 250));
        //imagePanel.setPreferredSize(new Dimension(300, 0));
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // === Right Buttons and Welcome ===
        //JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(250, 250, 250));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        rightPanel.add(welcomeLabel);

        // Buttons
        String[] labels = {
                "Billing", "View Products", "Add Product", "Check Stock",
                "Reports", "Register User", "Add Customer", "View Customers" ,"Customer Collection"
        };

        for (String label : labels) {
            if (!"admin".equalsIgnoreCase(userRole)) {
                if (label.equals("Add Product") || label.equals("Reports")
                        || label.equals("Register User")) {
                    continue;
                }
            }

            JButton btn = new RoundedButton(label);
            btn.setMaximumSize(new Dimension(250, 45));
            btn.setFont(new Font("Arial", Font.BOLD, 15));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            Color normalColor = new Color(30, 144, 255); // DodgerBlue
            Color hoverColor = new Color(0, 120, 215);   // Darker Blue

            btn.setBackground(normalColor);
            btn.setForeground(Color.WHITE);
            /*btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Mouse hover effect
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(hoverColor);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(normalColor);
                }
            });*/
            btn.addActionListener(e -> openScreen(label));
            rightPanel.add(btn);
            rightPanel.add(Box.createVerticalStrut(10));
        }

        // Logout button
        JButton logoutBtn = new RoundedButton("Logout");
        logoutBtn.setBackground(new Color(110, 95, 163));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setMaximumSize(new Dimension(200, 40));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

// Hover effect
        Color logoutNormal = new Color(110, 95, 163);
        Color logoutHover = new Color(85, 70, 130);
        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setBackground(logoutHover);
            }

            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(logoutNormal);
            }
        });
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginScreen();
        });
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(logoutBtn);

        // Add to card panel
        cardPanel.add(imagePanel, BorderLayout.WEST);
        cardPanel.add(rightPanel, BorderLayout.CENTER);

        // Add card panel to container
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        container.add(cardPanel, gbc);

        // Final add to JFrame
        add(container);
    }

    private void openScreen(String label) {
        switch (label) {
            case "Billing": new BillingScreen(); break;
            case "View Products": new ViewProductScreen(); break;
            case "Add Product": new ProductManagementScreen(); break;
            case "Check Stock": new CheckStockScreen(); break;
            case "Reports": new ReportsScreen(); break;
            case "Register User": new RegisterUserScreen(); break;
            case "Add Customer": new CustomerScreen(); break;
            case "View Customers":
                new ViewCustomerScreen();
                break;
            case "Customer Collection": new CustomerCollectionScreen(); break;
        }
    }
    /*private String userRole;
    private String username;

    public DashboardScreen(String username, String role) {
        setTitle("Dashboard - " + role.toUpperCase());
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        this.userRole = role;
        this.username = username;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // === Left Image Panel ===
        //ImageIcon icon = new ImageIcon("C:\\Users\\LENOVO\\OneDrive\\Pictures\\Sora\\BSoft.png"); // Path to your image
       // JLabel imageLabel = new JLabel(icon);
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon("C:/Users/LENOVO/OneDrive/Pictures/Sora/BSoft2.png"));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.setPreferredSize(new Dimension(250, 0)); // Fixed width for left image
        add(imagePanel, BorderLayout.WEST);

        // === Right Side - Welcome and Buttons ===
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        contentPanel.add(welcomeLabel);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] labels = {
                "Billing", "View Products", "Add Product", "Check Stock",
                "Reports", "Register User", "Add Customer", "Customer Collection"
        };

        for (String label : labels) {
            if (!"admin".equalsIgnoreCase(userRole)) {
                if (label.equals("Add Product") || label.equals("Reports")
                        || label.equals("Register User") || label.equals("Add Customer")) {
                    continue;
                }
            }

            JButton btn = new RoundedButton(label);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            //btn.setMaximumSize(new Dimension(260, 45));
            //btn.setFont(new Font("Arial", Font.PLAIN, 14));
            btn.setMaximumSize(new Dimension(280, 55)); // wider and taller
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            buttonPanel.add(Box.createVerticalStrut(10));
            buttonPanel.add(btn);

            // Actions
            btn.addActionListener(e -> {
                switch (label) {
                    case "Billing": new BillingScreen(); break;
                    case "View Products": new ViewProductScreen(); break;
                    case "Add Product": new ProductManagementScreen(); break;
                    case "Check Stock": new CheckStockScreen(); break;
                    case "Reports": new ReportsScreen(); break;
                    case "Register User": new RegisterUserScreen(); break;
                    case "Add Customer": new CustomerScreen(); break;
                    case "Customer Collection": new CustomerCollectionScreen(); break;
                }
            });
        }

        // Logout button
        JButton logoutBtn = new RoundedButton("Logout");
        logoutBtn.setBackground(new Color(110, 95, 163));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(200, 40));
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginScreen();
        });

        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(logoutBtn);

        // Add all to content panel
        contentPanel.add(buttonPanel);

        // Center panel wrapper with padding
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        rightPanel.add(contentPanel, gbc);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // padding

        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }*/




}
