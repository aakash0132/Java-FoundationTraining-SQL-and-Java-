package com.techshop.entity;

import com.techshop.dao.OrderDAO;

import java.sql.SQLException;
import java.util.List;

public class Customer {
    private int customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    public Customer(int customerId, String firstName, String lastName, String email, String phone, String address) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // Getters and Setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
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
    public String getPhone() { return phone; }
    public void setPhone(String phone) {
        if (phone == null || !phone.matches("\\d{10}")) throw new IllegalArgumentException("Invalid phone number");
        this.phone = phone;
    }
    public String getAddress() { return address; }
    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) throw new IllegalArgumentException("Address cannot be empty");
        this.address = address;
    }

    public int calculateTotalOrders() throws SQLException {
        OrderDAO orderDAO = new OrderDAO();
        List<Order> orders = orderDAO.getOrdersByCustomer(customerId);
        return orders.size();
    }

    public String getCustomerDetails() {
        return "Customer ID: " + customerId + ", Name: " + firstName + " " + lastName +
               ", Email: " + email + ", Phone: " + phone + ", Address: " + address;
    }

    public void updateCustomerInfo(String email, String phone, String address) {
        setEmail(email);
        setPhone(phone);
        setAddress(address);
    }
}