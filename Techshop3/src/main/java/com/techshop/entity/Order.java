package com.techshop.entity;

import com.techshop.dao.OrderDetailDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Order {
    private int orderId;
    private Customer customer;
    private LocalDate orderDate;
    private double totalAmount;
    private String status;

    public Order(int orderId, Customer customer, LocalDate orderDate, String status) {
        this.orderId = orderId;
        this.customer = customer;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = 0.0;
    }

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) {
        if (customer == null) throw new IllegalArgumentException("Customer cannot be null");
        this.customer = customer;
    }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void calculateTotalAmount() throws SQLException {
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        List<OrderDetail> details = orderDetailDAO.getOrderDetailsByOrder(orderId);
        totalAmount = 0.0;
        for (OrderDetail detail : details) {
            totalAmount += detail.calculateSubtotal();
        }
    }

    public String getOrderDetails() throws SQLException {
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        List<OrderDetail> details = orderDetailDAO.getOrderDetailsByOrder(orderId);
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId).append(", Customer: ").append(customer.getFirstName())
          .append(" ").append(customer.getLastName()).append(", Date: ").append(orderDate)
          .append(", Status: ").append(status).append(", Total: $").append(totalAmount).append("\nDetails:\n");
        for (OrderDetail detail : details) {
            sb.append(detail.getOrderDetailInfo()).append("\n");
        }
        return sb.toString();
    }

    public void updateOrderStatus(String status) {
        setStatus(status);
    }

    public void cancelOrder() throws SQLException {
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        List<OrderDetail> details = orderDetailDAO.getOrderDetailsByOrder(orderId);
        InventoryDAO inventoryDAO = new InventoryDAO();
        for (OrderDetail detail : details) {
            inventoryDAO.addToInventory(detail.getProduct().getProductId(), detail.getQuantity());
        }
        setStatus("Cancelled");
    }
}