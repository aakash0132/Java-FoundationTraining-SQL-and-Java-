package com.techshop.dao;

import com.techshop.entity.Order;
import com.techshop.entity.OrderDetail;
import com.techshop.entity.Product;
import com.techshop.util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {
    public void addOrderDetail(OrderDetail detail) throws SQLException {
        String sql = "INSERT INTO order_details (order_id, product_id, quantity, subtotal, discount) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, detail.getOrder().getOrderId());
            stmt.setInt(2, detail.getProduct().getProductId());
            stmt.setInt(3, detail.getQuantity());
            stmt.setDouble(4, detail.calculateSubtotal());
            stmt.setDouble(5, detail.getDiscount());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                detail.setOrderDetailId(rs.getInt(1));
            }
        }
    }

    public List<OrderDetail> getOrderDetailsByOrder(int orderId) throws SQLException {
        List<OrderDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM order_details WHERE order_id = ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            OrderDAO orderDAO = new OrderDAO();
            ProductDAO productDAO = new ProductDAO();
            while (rs.next()) {
                Order order = orderDAO.getOrdersByCustomer(rs.getInt("order_id")).get(0); // Simplified
                Product product = productDAO.getProductById(rs.getInt("product_id"));
                OrderDetail detail = new OrderDetail(rs.getInt("order_detail_id"), order, product, rs.getInt("quantity"));
                detail.setDiscount(rs.getDouble("discount"));
                details.add(detail);
            }
        }
        return details;
    }
}