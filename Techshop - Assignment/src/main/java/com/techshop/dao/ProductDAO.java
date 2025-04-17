package com.techshop.dao;

import com.techshop.entity.Product;
import com.techshop.util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (product_name, description, price) VALUES (?, ?, ?)";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                product.setProductId(rs.getInt(1));
            }
        }
    }

    public Product getProductById(int productId) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(rs.getInt("product_id"), rs.getString("product_name"),
                        rs.getString("description"), rs.getDouble("price"));
            }
        }
        return null;
    }

    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET product_name = ?, description = ?, price = ? WHERE product_id = ?";
        try (Connection conn = DBConnUtil g.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getProductId());
            stmt.executeUpdate();
        }
    }

    public List<Product> searchProducts(String keyword) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE product_name LIKE ? OR description LIKE ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(rs.getInt("product_id"), rs.getString("product_name"),
                        rs.getString("description"), rs.getDouble("price")));
            }
        }
        return products;
    }
}