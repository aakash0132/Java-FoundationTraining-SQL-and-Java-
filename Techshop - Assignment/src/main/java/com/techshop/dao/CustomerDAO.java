package com.techshop.dao;

import com.techshop.entity.Customer;
import com.techshop.util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public void addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getAddress());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                customer.setCustomerId(rs.getInt(1));
            }
        }
    }

    public Customer getCustomerById(int customerId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(rs.getInt("customer_id"), rs.getString("first_name"),
                        rs.getString("last_name"), rs.getString("email"), rs.getString("phone"),
                        rs.getString("address"));
            }
        }
        return null;
    }

    public void updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ? WHERE customer_id = ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getAddress());
            stmt.setInt(6, customer.getCustomerId());
            stmt.executeUpdate();
        }
    }
}