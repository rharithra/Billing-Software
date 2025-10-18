package com.billingsoftware.ui;

import com.billingsoftware.dao.ProductDAO;
import com.billingsoftware.model.Product;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
public class ViewProductScreen extends JFrame {
    private JTable productTable;
    private static DefaultTableModel tableModel;
    private JTextField searchField;
    //JComboBox<String> groupComboBox = new JComboBox<>();
    private JComboBox<String> groupComboBox;
    public ViewProductScreen() {
        setTitle("View Products");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Search bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(25);
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        //add(topPanel, BorderLayout.NORTH);

        groupComboBox = new JComboBox<>();
        groupComboBox.addItem("All Groups");
        for (String g : ProductDAO.getAllGroupNames()) {
            groupComboBox.addItem(g);
        }
        topPanel.add(new JLabel("Group:"));
        topPanel.add(groupComboBox);
        add(topPanel, BorderLayout.NORTH);




        // Table setup
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Group", "Retail", "Wholesale"}, 0
        );

        productTable = new JTable(tableModel);
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Load all initially
        loadFilteredProducts("");



// Add ActionListener to trigger filter
        groupComboBox.addActionListener(e -> filterByGroup());

        // Add dynamic search as-you-type
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterProducts();
            }

            public void removeUpdate(DocumentEvent e) {
                filterProducts();
            }

            public void changedUpdate(DocumentEvent e) {
                filterProducts();
            }
        });

        setVisible(true);
    }


    private void filterProducts() {
        String keyword = searchField.getText().trim().toLowerCase();
        loadFilteredProducts(keyword);
    }

    private void loadFilteredProducts(String keyword) {
        tableModel.setRowCount(0);
        List<Product> products = ProductDAO.getAllProducts();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(keyword) ||
                    p.getGroupName().toLowerCase().contains(keyword)) {
                tableModel.addRow(new Object[]{
                        p.getId(), p.getName(), p.getGroupName(),
                        p.getPriceRetail(), p.getPriceWholesale(), p.getStock()
                });
            }
        }
    }
    private static   void loadProductData() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        List<Product> list = ProductDAO.getAllProducts();
        for (Product p : list) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getGroupName(),
                    p.getPriceRetail(), p.getPriceWholesale(), p.getStock()
            });
        }
    }
    private void filterByGroup() {
        String selectedGroup = (String) groupComboBox.getSelectedItem();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(productTable.getModel());
        productTable.setRowSorter(sorter);

        if (selectedGroup.equals("All Groups")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + selectedGroup, 2)); // 2 = group column index
        }
    }

    // 🔁 Call this from other screens to refresh
    public static void refresh() {
        loadProductData();
    }
}
