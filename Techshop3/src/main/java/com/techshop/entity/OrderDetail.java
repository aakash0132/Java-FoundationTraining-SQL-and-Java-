package com.techshop.entity;

public class OrderDetail {
    private int orderDetailId;
    private Order order;
    private Product product;
    private int quantity;
    private double discount;

    public OrderDetail(int orderDetailId, Order order, Product product, int quantity) {
        this.orderDetailId = orderDetailId;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.discount = 0.0;
    }

    // Getters and Setters
    public int getOrderDetailId() { return orderDetailId; }
    public void setOrderDetailId(int orderDetailId) { this.orderDetailId = orderDetailId; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) {
        if (order == null) throw new IllegalArgumentException("Order cannot be null");
        this.order = order;
    }
    public Product getProduct() { return product; }
    public void setProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        this.product = product;
    }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        this.quantity = quantity;
    }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double calculateSubtotal() {
        return (product.getPrice() * quantity) - discount;
    }

    public String getOrderDetailInfo() {
        return "Order Detail ID: " + orderDetailId + ", Product: " + product.getProductName() +
               ", Quantity: " + quantity + ", Subtotal: $" + calculateSubtotal();
    }

    public void updateQuantity(int newQuantity) {
        setQuantity(newQuantity);
    }

    public void addDiscount(double discount) {
        if (discount < 0) throw new IllegalArgumentException("Discount cannot be negative");
        this.discount = discount;
    }
}