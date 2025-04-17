package com.techshop.dao;

import com.techshop.entity.Order;
import com.techshop.util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public void addOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (customer_id, order_date, total_amount, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getCustomer().getCustomerId());
            stmt.setDate(2, java.sql.Date.valueOf(order.getOrderDate()));
            stmt.setDouble(3, order.getTotalAmount());
            stmt.setString(4, order.getStatus());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                order.setOrderId(rs.getInt(1));
            }
        }
    }

    public List<Order> getOrdersByCustomer(int customerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id = ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            CustomerDAO customerDAO = new CustomerDAO();
            while (rs.next()) {
                Customer customer = customerDAO.getCustomerById(rs.getInt("customer_id"));
                orders.add(new Order(rs.getInt("order_id"), customer,
                        rs.getDate("order_date").toLocalDate(), rs.getString("status")));
            }
        }
        return orders;
    }

    public void updateOrder(Order order) throws SQLException {
        String sql = "UPDATE orders SET total_amount = ?, status = ? WHERE order_id = ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, order.getTotalAmount());
            stmt.setString(2, order.getStatus());
            stmt.setInt(3, order.getOrderId());
            stmt.executeUpdate();
        }
    }
}