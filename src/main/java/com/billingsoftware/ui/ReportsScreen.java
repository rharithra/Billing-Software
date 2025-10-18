package com.billingsoftware.ui;

import com.billingsoftware.util.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
public class ReportsScreen extends JFrame{

    private Point mouseClickPoint;
    public ReportsScreen() {
        setTitle("Reports");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setUndecorated(true); // Remove title bar
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Rounded corners
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        });

        // Drag support
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseClickPoint = e.getPoint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - mouseClickPoint.x, p.y + e.getY() - mouseClickPoint.y);
            }
        });

        // ==== Title Bar ====
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(100, 149, 237));
        titleBar.setPreferredSize(new Dimension(getWidth(), 40));

        JLabel title = new JLabel("" +
                "Reports", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        titleBar.add(title, BorderLayout.CENTER);

        // Close + Minimize buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        controlPanel.setOpaque(false);

        //JButton minimizeBtn = createControlButton("–");
        JButton minimizeBtn = createControlButton("\u2212");
        minimizeBtn.addActionListener(e -> setState(Frame.ICONIFIED));

        JButton closeBtn = createControlButton("X");
        closeBtn.addActionListener(e -> dispose());

        controlPanel.add(minimizeBtn);
        controlPanel.add(closeBtn);
        titleBar.add(controlPanel, BorderLayout.EAST);

        add(titleBar, BorderLayout.NORTH);

        // ==== Main Content ====
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        RoundedButton dailySalesBtn = new RoundedButton("Sales Report");
        RoundedButton customerReportBtn = new RoundedButton("Customer Report");
        RoundedButton billReportBtn = new RoundedButton("Bill Wise Report");

        Dimension buttonSize = new Dimension(200, 40);
        for (RoundedButton btn : new RoundedButton[]{dailySalesBtn, customerReportBtn, billReportBtn}) {
            btn.setMaximumSize(buttonSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(btn);
            centerPanel.add(Box.createVerticalStrut(15));
        }

        // Button Actions
        dailySalesBtn.addActionListener(e -> new SalesReportScreen());
        customerReportBtn.addActionListener(e -> new CustomerReportScreen());
        billReportBtn.addActionListener(e -> new BillReportScreen());

        //add(centerPanel, BorderLayout.CENTER);
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(new Color(245, 245, 245));
        wrapperPanel.add(centerPanel);
        add(wrapperPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Custom close/minimize button
    private JButton createControlButton(String text) {
        JButton btn = new JButton(text);
        /*btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(70, 130, 180));
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(35, 25));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));*/
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(70, 130, 180));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true); // Ensure content is filled
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(40, 30));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(60, 110, 160));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(70, 130, 180));
            }
        });
        return btn;
    }
        /*setTitle("Reports");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setUndecorated(true); // Remove default title bar
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Rounded corners on resize
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        });

        // Drag functionality
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseClickPoint = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - mouseClickPoint.x, p.y + e.getY() - mouseClickPoint.y);
            }
        });

        // === Content Panel ===
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 245, 245));

        RoundedButton dailySalesBtn = new RoundedButton("Sales Report");
        RoundedButton customerReportBtn = new RoundedButton("Customer Report");
        RoundedButton billReportBtn = new RoundedButton("Bill Wise Report");

        Dimension buttonSize = new Dimension(200, 40);
        for (RoundedButton btn : new RoundedButton[]{dailySalesBtn, customerReportBtn, billReportBtn}) {
            btn.setMaximumSize(buttonSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(btn);
            panel.add(Box.createVerticalStrut(10));
        }

        // Action Listeners
        dailySalesBtn.addActionListener(e -> new SalesReportScreen());
        customerReportBtn.addActionListener(e -> new CustomerReportScreen());
        billReportBtn.addActionListener(e -> new BillReportScreen());

        add(panel);
        setVisible(true);
    }*/

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReportsScreen::new);
    }
    }


