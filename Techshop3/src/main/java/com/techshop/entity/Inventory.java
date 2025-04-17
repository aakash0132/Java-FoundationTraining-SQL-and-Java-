package com.techshop.entity;

import com.techshop.dao.InventoryDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Inventory {
    private int inventoryId;
    private Product product;
    private int quantityInStock;
    private LocalDate lastStockUpdate;

    public Inventory(int inventoryId, Product product, int quantityInStock, LocalDate lastStockUpdate) {
        this.inventoryId = inventoryId;
        this.product = product;
        this.quantityInStock = quantityInStock;
        this.lastStockUpdate = lastStockUpdate;
    }

    // Getters and Setters
    public int getInventoryId() { return inventoryId; }
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        this.product = product;
    }
    public int getQuantityInStock() { return quantityInStock; }
    public void setQuantityInStock(int quantityInStock) {
        if (quantityInStock < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.quantityInStock = quantityInStock;
    }
    public LocalDate getLastStockUpdate() { return lastStockUpdate; }
    public void setLastStockUpdate(LocalDate lastStockUpdate) { this.lastStockUpdate = lastStockUpdate; }

    public void addToInventory(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        this.quantityInStock += quantity;
        this.lastStockUpdate = LocalDate.now();
    }

    public void removeFromInventory(int quantity) {
        if (quantity <= 0 || quantity > quantityInStock) throw new IllegalArgumentException("Invalid quantity to remove");
        this.quantityInStock -= quantity;
        this.lastStockUpdate = LocalDate.now();
    }

    public void updateStockQuantity(int newQuantity) {
        setQuantityInStock(newQuantity);
        this.lastStockUpdate = LocalDate.now();
    }

    public boolean isProductAvailable(int quantityToCheck) {
        return quantityInStock >= quantityToCheck;
    }

    public double getInventoryValue() {
        return quantityInStock * product.getPrice();
    }

    public static List<Inventory> listLowStockProducts(int threshold) throws SQLException {
        InventoryDAO inventoryDAO = new InventoryDAO();
        return inventoryDAO.getLowStockProducts(threshold);
    }

    public static List<Inventory> listOutOfStockProducts() throws SQLException {
        InventoryDAO inventoryDAO = new InventoryDAO();
        return inventoryDAO.getOutOfStockProducts();
    }

    public static List<Inventory> listAllProducts() throws SQLException {
        InventoryDAO inventoryDAO = new InventoryDAO();
        return inventoryDAO.getAllInventory();
    }
}