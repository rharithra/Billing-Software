package com.billingsoftware.model;

import java.time.LocalDate;
public class Bill {

    private int billNo;
    private String customerName;
    private double totalAmount;

    private double paidAmount;
    private double balanceAmount;
    private LocalDate date;

    // Constructors
    public Bill() {}

    public Bill(int billNo, String customerName, double totalAmount, LocalDate date) {
        this.billNo = billNo;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.balanceAmount = balanceAmount;
        this.date = date;
    }

    // Getters and Setters
    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
