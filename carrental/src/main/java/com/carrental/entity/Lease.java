package com.carrental.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Lease {
    private int leaseID;
    private int vehicleID;
    private int customerID;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;

    // Default Constructor
    public Lease() {}

    // Parameterized Constructor
    public Lease(int leaseID, int vehicleID, int customerID, LocalDate startDate, LocalDate endDate, String type) {
        this.leaseID = leaseID;
        this.vehicleID = vehicleID;
        this.customerID = customerID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    // Getters and Setters
    public int getLeaseID() { return leaseID; }
    public void setLeaseID(int leaseID) { this.leaseID = leaseID; }
    public int getVehicleID() { return vehicleID; }
    public void setVehicleID(int vehicleID) { this.vehicleID = vehicleID; }
    public int getCustomerID() { return customerID; }
    public void setCustomerID(int customerID) { this.customerID = customerID; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getType() { return type; }
    public void setType(String type) {
        if (!type.equals("Daily") && !type.equals("Monthly"))
            throw new IllegalArgumentException("Type must be 'Daily' or 'Monthly'");
        this.type = type;
    }

    public double calculateTotalCost(double dailyRate) {
        long duration = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (type.equals("Daily")) {
            return dailyRate * duration;
        } else { // Monthly
            long months = ChronoUnit.MONTHS.between(startDate, endDate) + 1;
            return dailyRate * 30 * months; // Assuming 30 days per month
        }
    }

    @Override
    public String toString() {
        return "Lease{ID=" + leaseID + ", VehicleID=" + vehicleID + ", CustomerID=" + customerID +
               ", StartDate=" + startDate + ", EndDate=" + endDate + ", Type=" + type + "}";
    }
}