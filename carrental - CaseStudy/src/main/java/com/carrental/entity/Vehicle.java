package com.carrental.entity;

public class Vehicle {
    private int vehicleID;
    private String make;
    private String model;
    private int year;
    private double dailyRate;
    private String status;
    private int passengerCapacity;
    private double engineCapacity;

    // Default Constructor
    public Vehicle() {}

    // Parameterized Constructor
    public Vehicle(int vehicleID, String make, String model, int year, double dailyRate, String status, int passengerCapacity, double engineCapacity) {
        this.vehicleID = vehicleID;
        this.make = make;
        this.model = model;
        this.year = year;
        this.dailyRate = dailyRate;
        this.status = status;
        this.passengerCapacity = passengerCapacity;
        this.engineCapacity = engineCapacity;
    }

    // Getters and Setters
    public int getVehicleID() { return vehicleID; }
    public void setVehicleID(int vehicleID) { this.vehicleID = vehicleID; }
    public String getMake() { return make; }
    public void setMake(String make) {
        if (make == null || make.trim().isEmpty()) throw new IllegalArgumentException("Make cannot be empty");
        this.make = make;
    }
    public String getModel() { return model; }
    public void setModel(String model) {
        if (model == null || model.trim().isEmpty()) throw new IllegalArgumentException("Model cannot be empty");
        this.model = model;
    }
    public int getYear() { return year; }
    public void setYear(int year) {
        if (year < 1900 || year > 2025) throw new IllegalArgumentException("Invalid year");
        this.year = year;
    }
    public double getDailyRate() { return dailyRate; }
    public void setDailyRate(double dailyRate) {
        if (dailyRate < 0) throw new IllegalArgumentException("Daily rate cannot be negative");
        this.dailyRate = dailyRate;
    }
    public String getStatus() { return status; }
    public void setStatus(String status) {
        if (!status.equals("available") && !status.equals("notAvailable"))
            throw new IllegalArgumentException("Status must be 'available' or 'notAvailable'");
        this.status = status;
    }
    public int getPassengerCapacity() { return passengerCapacity; }
    public void setPassengerCapacity(int passengerCapacity) {
        if (passengerCapacity <= 0) throw new IllegalArgumentException("Passenger capacity must be positive");
        this.passengerCapacity = passengerCapacity;
    }
    public double getEngineCapacity() { return engineCapacity; }
    public void setEngineCapacity(double engineCapacity) {
        if (engineCapacity <= 0) throw new IllegalArgumentException("Engine capacity must be positive");
        this.engineCapacity = engineCapacity;
    }

    @Override
    public String toString() {
        return "Vehicle{ID=" + vehicleID + ", Make=" + make + ", Model=" + model + ", Year=" + year +
               ", DailyRate=$" + dailyRate + ", Status=" + status + ", PassengerCapacity=" + passengerCapacity +
               ", EngineCapacity=" + engineCapacity + "}";
    }
}