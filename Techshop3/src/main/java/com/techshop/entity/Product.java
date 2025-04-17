package com.techshop.entity;

import com.techshop.dao.InventoryDAO;

import java.sql.SQLException;

public class Product {
    private int productId;
    private String productName;
    private String description;
    private double price;

    public Product(int productId, String productName, String description, double price) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) throw new IllegalArgumentException("Product name cannot be empty");
        this.productName = productName;
    }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }

    public String getProductDetails() {
        return "Product ID: " + productId + ", Name: " + productName + ", Description: " + description + ", Price: $" + price;
    }

    public void updateProductInfo(String productName, String description, double price) {
        setProductName(productName);
        setDescription(description);
        setPrice(price);
    }

    public boolean isProductInStock() throws SQLException {
        InventoryDAO inventoryDAO = new InventoryDAO();
        return inventoryDAO.isProductAvailable(productId, 1);
    }
}