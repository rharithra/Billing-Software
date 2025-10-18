package com.billingsoftware.model;

public class Product {
    private int id;
    private String name;
    private String groupName;
    private double priceRetail;
    private double priceWholesale;
    private int stock;

    // Getters & Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public double getPriceRetail() { return priceRetail; }
    public void setPriceRetail(double priceRetail) { this.priceRetail = priceRetail; }

    public double getPriceWholesale() { return priceWholesale; }
    public void setPriceWholesale(double priceWholesale) { this.priceWholesale = priceWholesale; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
