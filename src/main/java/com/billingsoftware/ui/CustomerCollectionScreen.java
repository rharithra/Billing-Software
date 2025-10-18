package com.billingsoftware.ui;

import com.billingsoftware.dao.CustomerDAO;
import com.billingsoftware.dao.BillDAO;
import com.billingsoftware.model.Bill;
import com.billingsoftware.model.Customer;
import com.billingsoftware.util.ReceiptGenerator;
import com.billingsoftware.util.RoundedButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.List;



public class CustomerCollectionScreen extends JFrame {

    private JTextField nameField;
    private JLabel totalLabel;
    private Customer currentCustomer;
    private JComboBox<String> customerComboBox;

    private JScrollPane scrollPane;
    private JTextField paidAmountField;
    private JLabel currentBalance;

    private JTable table;
    private DefaultTableModel tableModel;
    private JTable pendingBillsTable;
    private DefaultTableModel pendingBillsModel;
    //DefaultTableModel pendingBillsModel = new DefaultTableModel(new String[]{"Bill ID", "Date", "Amount", "Paid", "Balance"}, 0);

    public CustomerCollectionScreen() {
        /*setTitle("Customer Balance & Collection");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));
        setResizable(false);
        getContentPane().setBackground(new Color(240, 248, 255));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        add(nameField);


        add(new JLabel("Customer Name:"));
        customerComboBox = new JComboBox<>();
        customerComboBox.setEditable(true);
        add(customerComboBox);

// Auto-suggestion logic
        JTextField editor = (JTextField) customerComboBox.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String input = editor.getText().trim();
                if (input.length() >= 1) {
                    List<String> matches = CustomerDAO.getMatchingCustomerNames(input); // Your DAO method
                    customerComboBox.setModel(new DefaultComboBoxModel<>(matches.toArray(new String[0])));
                    editor.setText(input);  // Retain user's typed text
                    customerComboBox.showPopup(); // Show dropdown
                }
            }
        });

        List<String> customerNames = CustomerDAO.getAllCustomerNames();
        for (String name : customerNames) {
            customerComboBox.addItem(name);
        }

        RoundedButton searchBtn = new RoundedButton("Search");
        add(searchBtn);

        // dummy component to align search button
        add(new JLabel(""));

        DefaultTableModel pendingBillsModel = new DefaultTableModel(new String[]{"Bill ID", "Date", "Amount", "Paid", "Balance"}, 0);
        JTable pendingBillsTable = new JTable(pendingBillsModel);
        JScrollPane pendingBillsScrollPane = new JScrollPane(pendingBillsTable);
        pendingBillsScrollPane.setPreferredSize(new Dimension(450, 100));
        add(new JLabel("Pending Bills:"));
        add(pendingBillsScrollPane);


        /*add(new JLabel("Current Balance:"));
        balanceLabel = new JLabel("0.0");
        add(balanceLabel);

        add(new JLabel("Current Balance:"));
        currentBalance = new JLabel("0.0");
        add(currentBalance);

        /*add(new JLabel("Paid Amount:"));
        paidField = new JTextField();
        add(paidField);

        add(new JLabel("Paid Amount:"));
        paidAmountField = new JTextField(10);
        add(paidAmountField);

        add(new JLabel("New Total:"));
        totalLabel = new JLabel("0.0");
        add(totalLabel);

        RoundedButton updateBtn = new RoundedButton("Record Payment");
        add(updateBtn);

        JButton closeBtn = new RoundedButton("Close");
        closeBtn.addActionListener(e -> dispose());
        add(closeBtn);*/

        setTitle("Customer Balance & Collection");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

// Row 0: Customer Name + ComboBox + Search Button
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Customer Name:"), gbc);

        customerComboBox = new JComboBox<>();
        customerComboBox.setEditable(true);
        gbc.gridx = 1; gbc.weightx = 1.0;
        add(customerComboBox, gbc);

        JTextField editor = (JTextField) customerComboBox.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String input = editor.getText().trim();
                if (input.length() >= 1) {
                    List<String> matches = CustomerDAO.getMatchingCustomerNames(input); // Your DAO method
                    customerComboBox.setModel(new DefaultComboBoxModel<>(matches.toArray(new String[0])));
                    editor.setText(input);  // Retain user's typed text
                    customerComboBox.showPopup(); // Show dropdown
                }
            }
        });

        List<String> customerNames = CustomerDAO.getAllCustomerNames();
        for (String name : customerNames) {
            customerComboBox.addItem(name);
        }

        RoundedButton searchBtn = new RoundedButton("Search");
        gbc.gridx = 2; gbc.weightx = 0;
        add(searchBtn, gbc);

// Row 1: Pending Bills label
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 3;
        add(new JLabel("Pending Bills:"), gbc);

// Row 2: Pending Bills Table
        pendingBillsModel = new DefaultTableModel(new String[]{"Bill ID", "Date", "Amount", "Paid", "Balance"}, 0);
        pendingBillsTable = new JTable(pendingBillsModel);
        JScrollPane pendingBillsScrollPane = new JScrollPane(pendingBillsTable);
        gbc.gridy = 2; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(pendingBillsScrollPane, gbc);

// Reset for next rows
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        gbc.gridwidth = 1;

// Row 3: Current Balance
        gbc.gridy = 3; gbc.gridx = 0;
        add(new JLabel("Current Balance:"), gbc);

        currentBalance = new JLabel("0.0");
        gbc.gridx = 1;
        add(currentBalance, gbc);

// Row 4: Paid Amount
        gbc.gridy = 4; gbc.gridx = 0;
        add(new JLabel("Paid Amount:"), gbc);

        paidAmountField = new JTextField(10);
        gbc.gridx = 1;
        add(paidAmountField, gbc);

// Row 5: New Total
        gbc.gridy = 5; gbc.gridx = 0;
        add(new JLabel("New Total:"), gbc);

        totalLabel = new JLabel("0.0");
        gbc.gridx = 1;
        add(totalLabel, gbc);

// Row 6: Buttons
        RoundedButton updateBtn = new RoundedButton("Record Payment");
        gbc.gridy = 6; gbc.gridx = 0;
        add(updateBtn, gbc);

        RoundedButton closeBtn = new RoundedButton("Close");
        Dimension buttonSize = new Dimension(150, 40);
        updateBtn.setPreferredSize(buttonSize);
        closeBtn.setPreferredSize(buttonSize);
        closeBtn.addActionListener(e -> dispose());
        gbc.gridx = 1;
        add(closeBtn, gbc);


        // Logic
        searchBtn.addActionListener(e -> {
            String name = (String) customerComboBox.getEditor().getItem();
            currentCustomer = CustomerDAO.getCustomerByName(name);

            if (currentCustomer != null) {
                if (!currentCustomer.getType().equalsIgnoreCase("credit")) {
                    JOptionPane.showMessageDialog(this, "Customer is not credit type.");
                    currentCustomer = null;
                    return;
                }

                // ✅ Load pending bills first
                List<Bill> pendingBills = BillDAO.getPendingBillsForCustomer(name);
                //c.setRowCount(0);
                pendingBillsModel.setRowCount(0);

                for (Bill bill : pendingBills) {
                    pendingBillsModel.addRow(new Object[]{
                            bill.getBillNo(),
                            bill.getDate(),
                            bill.getTotalAmount(),
                            bill.getPaidAmount(),
                            bill.getBalanceAmount()
                    });
                }

                if (pendingBills.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "This customer has no outstanding bills.");
                    currentCustomer = null;
                    currentBalance.setText("0.0");
                    totalLabel.setText("0.0");
                    paidAmountField.setText("");
                    return;
                }

                // ✅ If pending bills exist, calculate total balance
                double totalBalance = pendingBills.stream().mapToDouble(Bill::getBalanceAmount).sum();
                currentBalance.setText(String.valueOf(totalBalance));
                totalLabel.setText(String.valueOf(totalBalance));
                paidAmountField.setText("");

            } else {
                JOptionPane.showMessageDialog(this, "Customer not found.");
            }
        });





        /*updateBtn.addActionListener(e -> {
            if (currentCustomer == null) {
                JOptionPane.showMessageDialog(this, "Search a customer first.");
                return;
            }
            String paidText = paidField.getText().trim();

            if (paidText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the paid amount.");
                return;
            }

            try {

                double paidAmount = Double.parseDouble(paidText);
                double currentBalance = currentCustomer.getBalance();
                double newBalance = currentBalance - paidAmount;

                if (paidAmount < 0) {
                    JOptionPane.showMessageDialog(this, "Amount cannot be negative.");
                    return;
                }

                if (paidAmount > currentBalance) {
                    JOptionPane.showMessageDialog(this, "Paid amount exceeds current balance.");
                    return;
                }

                if (CustomerDAO.updateBalance(currentCustomer.getName(), newBalance)) {
                    currentCustomer.setBalance(newBalance);
                    //balanceLabel.setText(String.valueOf(newBalance));
                    currentBalance.setText(String.valueOf(newBalance));
                    totalLabel.setText(String.valueOf(newBalance));
                    JOptionPane.showMessageDialog(this, "Payment recorded successfully!");
                    int choice = JOptionPane.showConfirmDialog(this, "Payment recorded. Print receipt?", "Print", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        String customerName = currentCustomer.getName();
                        //double paidAmount = Double.parseDouble(paidAmountField.getText().trim());
                        double newTotal = currentCustomer.getBalance();
                        LocalDate date = LocalDate.now();
                        ReceiptGenerator.printReceipt(customerName, paidAmount, newTotal, date);
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update balance.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid numeric amount.");
            }

            /*try {
                double paidAmount = Double.parseDouble(paidField.getText().trim());
                double currentBalance = currentCustomer.getBalance();
                double newBalance = currentBalance - paidAmount;

                if (paidAmount < 0) {
                    JOptionPane.showMessageDialog(this, "Paid amount cannot be negative.");
                    return;
                }

                if (paidAmount > currentBalance) {
                    JOptionPane.showMessageDialog(this, "Paid amount exceeds current balance.");
                    return;
                }

                boolean success = CustomerDAO.updateBalance(currentCustomer.getName(), newBalance);

                if (success) {
                    currentCustomer.setBalance(newBalance); // Update in-memory object
                    totalLabel.setText(String.valueOf(newBalance));
                    balanceLabel.setText(String.valueOf(newBalance));
                    JOptionPane.showMessageDialog(this, "Payment recorded successfully!");

                    // Ask for print
                    int choice = JOptionPane.showConfirmDialog(this, "Payment recorded. Print receipt?", "Print", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        String customerName = currentCustomer.getName();
                        LocalDate date = LocalDate.now();

                        // Call your custom ReceiptGenerator (you'll need to implement this class)
                        ReceiptGenerator.printReceipt(customerName, paidAmount, newBalance, date);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update balance.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid paid amount.");
            }
        });*/

        /*updateBtn.addActionListener(e -> {
            if (currentCustomer == null) {
                JOptionPane.showMessageDialog(this, "Search a customer first.");
                return;
            }

            String paidText = paidAmountField.getText().trim();

            if (paidText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the paid amount.");
                return;
            }

            try {
                double paidAmount = Double.parseDouble(paidText);
                double currentBal = Double.parseDouble(currentBalance.getText().trim());
                double newBalance = currentBal - paidAmount;
                int collectionId = BillDAO.getLastCollectionId(); // You'll need to implement this
                String customerName = currentCustomer.getName();
                int selectedRow = pendingBillsTable.getSelectedRow();;
                //int billNo = BillDAO.getOriginalBillNoForCustomer(customerName);


                //double totalBillAmount = BillDAO.getBillTotalByBillNo(billNo);
                //double totalPaidSoFar = BillDAO.getTotalPaidForBill(billNo);


                if (paidAmount < 0) {
                    JOptionPane.showMessageDialog(this, "Amount cannot be negative.");
                    return;
                }

                if (paidAmount > currentBal) {
                    JOptionPane.showMessageDialog(this, "Paid amount exceeds current balance.");
                    return;
                }


                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a bill to record payment for.");
                    return;
                }
                int billNo = (int) pendingBillsModel.getValueAt(selectedRow, 0);
                //int billNo = (int) pendingBillsModel.getValueAt(selectedRow, 0);

                boolean success = CustomerDAO.updateBalance(currentCustomer.getName(), newBalance);

                if (success) {
                    currentCustomer.setBalance(newBalance);
                    currentBalance.setText(String.valueOf(newBalance));
                    totalLabel.setText(String.valueOf(newBalance));
                    JOptionPane.showMessageDialog(this, "Payment recorded successfully!");

                    int choice = JOptionPane.showConfirmDialog(this, "Payment recorded. Print receipt?", "Print", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        //String customerName = currentCustomer.getName();
                        LocalDate date = LocalDate.now();
                        //ReceiptGenerator.printReceipt(customerName, paidAmount, newBalance, date,
                                //collectionId, billNo, totalBillAmount, totalPaidSoFar);
                        double billAmount = BillDAO.getBillAmount(billNo);

                        int receiptId = BillDAO.saveBillCollection(billNo, paidAmount);

                        if (receiptId > 0) {
                            // Update the bill’s paid and balance in the bills table
                            double currentPaid = BillDAO.getBillPaidAmount(billNo);
                            double newPaid = currentPaid + paidAmount;
                            double newBalanceAmount = billAmount - newPaid;

                            // Update the bill's record
                            BillDAO.updateBillBalance(billNo, newPaid, newBalanceAmount);

                            // Optional: update overall customer balance too
                            double updatedCustomerBalance = currentCustomer.getBalance() - paidAmount;
                            CustomerDAO.updateBalance(currentCustomer.getName(), updatedCustomerBalance);

                            LocalDate today = LocalDate.now();

                            // ✅ Use newBalanceAmount for the correct per-bill balance
                            ReceiptGenerator.printReceipt(
                                    receiptId,
                                    currentCustomer.getName(),
                                    billNo,
                                    billAmount,
                                    paidAmount,
                                    newBalanceAmount,
                                    today
                            );
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to save collection.");
                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update balance.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid numeric amount.");
            }
        });*/

        updateBtn.addActionListener(e -> {
            if (currentCustomer == null) {
                JOptionPane.showMessageDialog(this, "Search a customer first.");
                return;
            }

            String paidText = paidAmountField.getText().trim();

            if (paidText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the paid amount.");
                return;
            }

            try {
                double paidAmount = Double.parseDouble(paidText);
                double currentBal = Double.parseDouble(currentBalance.getText().trim());

                if (paidAmount < 0) {
                    JOptionPane.showMessageDialog(this, "Amount cannot be negative.");
                    return;
                }

                if (paidAmount > currentBal) {
                    JOptionPane.showMessageDialog(this, "Paid amount exceeds current balance.");
                    return;
                }

                int selectedRow = pendingBillsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a bill to record payment for.");
                    return;
                }

                int billNo = (int) pendingBillsModel.getValueAt(selectedRow, 0);
                double billAmount = BillDAO.getBillAmount(billNo);

                int receiptId = BillDAO.saveBillCollection(billNo, paidAmount);

                if (receiptId > 0) {
                    // 1. Update bill's paid and balance
                    double currentPaid = BillDAO.getBillPaidAmount(billNo);
                    double newPaid = currentPaid + paidAmount;
                    double newBalanceAmount = billAmount - newPaid;

                    BillDAO.updateBillBalance(billNo, newPaid, newBalanceAmount);

                    // 2. Recalculate and update customer total balance based on unpaid bills
                    CustomerDAO.recalculateAndUpdateCustomerBalance(currentCustomer.getName());

                    // 3. Refresh UI with new balance
                    double finalBalance = CustomerDAO.getCustomerBalanceByName(currentCustomer.getName());
                    currentBalance.setText(String.valueOf(finalBalance));
                    totalLabel.setText(String.valueOf(finalBalance));
                    currentCustomer.setBalance(finalBalance);

                    JOptionPane.showMessageDialog(this, "Payment recorded successfully!");

                    // 4. Ask to print receipt
                    int choice = JOptionPane.showConfirmDialog(this, "Payment recorded. Print receipt?", "Print", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        LocalDate today = LocalDate.now();
                        ReceiptGenerator.printReceipt(
                                receiptId,
                                currentCustomer.getName(),
                                billNo,
                                billAmount,
                                paidAmount,
                                newBalanceAmount,
                                today
                        );
                    }

                    // Optional: Refresh pending bills table
                    loadPendingBills(currentCustomer.getName());

                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save payment.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid numeric amount.");
            }
        });





        setVisible(true);

    }
    private void loadPendingBills(String customerName) {
        List<Bill> pendingBills = BillDAO.getPendingBillsForCustomer(customerName);

        // Clear existing table data
        pendingBillsModel.setRowCount(0);

        // Add fresh rows
        for (Bill bill : pendingBills) {
            pendingBillsModel.addRow(new Object[]{
                    bill.getBillNo(),
                    bill.getTotalAmount(),
                    bill.getPaidAmount(),
                    bill.getBalanceAmount(),
                    bill.getDate()
            });
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerCollectionScreen::new);
    }
}
