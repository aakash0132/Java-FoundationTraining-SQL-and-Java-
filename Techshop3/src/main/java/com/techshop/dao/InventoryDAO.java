package com.techshop.dao;

import com.techshop.entity.Inventory;
import com.techshop.entity.Product;
import com.techshop.util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {
    public void addInventory(Inventory inventory) throws SQLException {
        String sql = "INSERT INTO inventory (product_id, quantity_in_stock, last_stock_update) VALUES (?, ?, ?)";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, inventory.getProduct().getProductId());
            stmt.setInt(2, inventory.getQuantityInStock());
            stmt.setDate(3, java.sql.Date.valueOf(inventory.getLastStockUpdate()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                inventory.setInventoryId(rs.getInt(1));
            }
        }
    }

    public boolean isProductAvailable(int productId, int quantity) throws SQLException {
        String sql = "SELECT quantity_in_stock FROM inventory WHERE product_id = ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity_in_stock") >= quantity;
            }
        }
        return false;
    }

    public void addToInventory(int productId, int quantity) throws SQLException {
        String sql = "UPDATE inventory SET quantity_in_stock = quantity_in_stock + ?, last_stock_update = ? WHERE product_id = ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setInt(3, productId);
            stmt.executeUpdate();
        }
    }

    public void removeFromInventory(int productId, int quantity) throws SQLException {
        String sql = "UPDATE inventory SET quantity_in_stock = quantity_in_stock - ?, last_stock_update = ? WHERE product_id = ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setInt(3, productId);
            stmt.executeUpdate();
        }
    }

    public List<Inventory> getLowStockProducts(int threshold) throws SQLException {
        List<Inventory> lowStock = new ArrayList<>();
        String sql = "SELECT * FROM inventory WHERE quantity_in_stock < ?";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, threshold);
            ResultSet rs = stmt.executeQuery();
            ProductDAO productDAO = new ProductDAO();
            while (rs.next()) {
                Product product = productDAO.getProductById(rs.getInt("product_id"));
                lowStock.add(new Inventory(rs.getInt("inventory_id"), product,
                        rs.getInt("quantity_in_stock"), rs.getDate("last_stock_update").toLocalDate()));
            }
        }
        return lowStock;
    }

    public List<Inventory> getOutOfStockProducts() throws SQLException {
        List<Inventory> outOfStock = new ArrayList<>();
        String sql = "SELECT * FROM inventory WHERE quantity_in_stock = 0";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            ProductDAO productDAO = new ProductDAO();
            while (rs.next()) {
                Product product = productDAO.getProductById(rs.getInt("product_id"));
                outOfStock.add(new Inventory(rs.getInt("inventory_id"), product,
                        rs.getInt("quantity_in_stock"), rs.getDate("last_stock_update").toLocalDate()));
            }
        }
        return outOfStock;
    }

    public List<Inventory> getAllInventory() throws SQLException {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT * FROM inventory";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            ProductDAO productDAO = new ProductDAO();
            while (rs.next()) {
                Product product = productDAO.getProductById(rs.getInt("product_id"));
                inventoryList.add(new Inventory(rs.getInt("inventory_id"), product,
                        rs.getInt("quantity_in_stock"), rs.getDate("last_stock_update").toLocalDate()));
            }
        }
        return inventoryList;
    }
}