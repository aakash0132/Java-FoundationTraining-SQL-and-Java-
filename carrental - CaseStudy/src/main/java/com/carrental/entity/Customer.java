package com.carrental.entity;

public class Customer {
    private int customerID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    // Default Constructor
    public Customer() {}

    // Parameterized Constructor
    public Customer(int customerID, String firstName, String lastName, String email, String phoneNumber) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public int getCustomerID() { return customerID; }
    public void setCustomerID(int customerID) { this.customerID = customerID; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) throw new IllegalArgumentException("First name cannot be empty");
        this.firstName = firstName;
    }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) throw new IllegalArgumentException("Last name cannot be empty");
        this.lastName = lastName;
    }
    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            throw new IllegalArgumentException("Invalid email format");
        this.email = email;
    }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}"))
            throw new IllegalArgumentException("Invalid phone number");
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Customer{ID=" + customerID + ", Name=" + firstName + " " + lastName +
               ", Email=" + email + ", Phone=" + phoneNumber + "}";
    }
}