package com.carrental.entity;

import java.time.LocalDate;

public class Payment {
    private int paymentID;
    private int leaseID;
    private LocalDate paymentDate;
    private double amount;

    // Default Constructor
    public Payment() {}

    // Parameterized Constructor
    public Payment(int paymentID, int leaseID, LocalDate paymentDate, double amount) {
        this.paymentID = paymentID;
        this.leaseID = leaseID;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    // Getters and Setters
    public int getPaymentID() { return paymentID; }
    public void setPaymentID(int paymentID) { this.paymentID = paymentID; }
    public int getLeaseID() { return leaseID; }
    public void setLeaseID(int leaseID) { this.leaseID = leaseID; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Payment{ID=" + paymentID + ", LeaseID=" + leaseID + ", Date=" + paymentDate +
               ", Amount=$" + amount + "}";
    }
}