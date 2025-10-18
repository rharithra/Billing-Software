package com.billingsoftware.model;

public class ProductSalesReport {
    private String productName;
    private String groupName;
    private int quantity;
    private double totalAmount;

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}
