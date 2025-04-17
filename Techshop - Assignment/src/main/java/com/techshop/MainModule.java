package com.techshop;

import com.techshop.dao.*;
import com.techshop.entity.*;
import com.techshop.exception.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CustomerDAO customerDAO = new CustomerDAO();
    private static final ProductDAO productDAO = new ProductDAO();
    private static final OrderDAO orderDAO = new OrderDAO();
    private static final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private static final InventoryDAO inventoryDAO = new InventoryDAO();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nTechShop Menu:");
            System.out.println("1. Register Customer");
            System.out.println("2. Update Customer Info");
            System.out.println("3. Add Product");
            System.out.println("4. Update Product Info");
            System.out.println("5. Place Order");
            System.out.println("6. Track Order Status");
            System.out.println("7. Update Inventory");
            System.out.println("8. Generate Sales Report");
            System.out.println("9. Search Products");
            System.out.println("10. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1 -> registerCustomer();
                    case 2 -> updateCustomerInfo();
                    case 3 -> addProduct();
                    case 4 -> updateProductInfo();
                    case 5 -> placeOrder();
                    case 6 -> trackOrderStatus();
                    case 7 -> updateInventory();
                    case 8 -> generateSalesReport();
                    case 9 -> searchProducts();
                    case 10 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                logError(e.getMessage());
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void registerCustomer() throws SQLException, InvalidDataException {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone (10 digits): ");
        String phone = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        Customer customer = new Customer(0, firstName, lastName, email, phone, address);
        customerDAO.addCustomer(customer);
        System.out.println("Customer registered successfully with ID: " + customer.getCustomerId());
    }

    private static void updateCustomerInfo() throws SQLException, InvalidDataException {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) throw new InvalidDataException("Customer not found");

        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new phone (10 digits): ");
        String phone = scanner.nextLine();
        System.out.print("Enter new address: ");
        String address = scanner.nextLine();

        customer.updateCustomerInfo(email, phone, address);
        customerDAO.updateCustomer(customer);
        System.out.println("Customer info updated successfully.");
    }

    private static void addProduct() throws SQLException, InvalidDataException {
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        Product product = new Product(0, productName, description, price);
        productDAO.addProduct(product);

        System.out.print("Enter initial stock quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        Inventory inventory = new Inventory(0, product, quantity, LocalDate.now());
        inventoryDAO.addInventory(inventory);

        System.out.println("Product added successfully with ID: " + product.getProductId());
    }

    private static void updateProductInfo() throws SQLException, InvalidDataException {
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        Product product = productDAO.getProductById(productId);
        if (product == null) throw new InvalidDataException("Product not found");

        System.out.print("Enter new product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();
        System.out.print("Enter new price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        product.updateProductInfo(productName, description, price);
        productDAO.updateProduct(product);
        System.out.println("Product info updated successfully.");
    }

    private static void placeOrder() throws SQLException, InsufficientStockException, IncompleteOrderException, PaymentFailedException, InvalidDataException {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) throw new InvalidDataException("Customer not found");

        Order order = new Order(0, customer, LocalDate.now(), "Processing");
        orderDAO.addOrder(order);

        System.out.print("Enter number of products in order: ");
        int numProducts = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numProducts; i++) {
            System.out.print("Enter product ID: ");
            int productId = scanner.nextInt();
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            Product product = productDAO.getProductById(productId);
            if (product == null) throw new IncompleteOrderException("Product not found");
            if (!inventoryDAO.isProductAvailable(productId, quantity))
                throw new InsufficientStockException("Insufficient stock for product ID: " + productId);

            inventoryDAO.removeFromInventory(productId, quantity);
            OrderDetail detail = new OrderDetail(0, order, product, quantity);
            orderDetailDAO.addOrderDetail(detail);
        }

        order.calculateTotalAmount();
        orderDAO.updateOrder(order);

        // Simulate payment processing
        System.out.print("Enter payment amount: ");
        double payment = scanner.nextDouble();
        scanner.nextLine();
        if (payment < order.getTotalAmount()) throw new PaymentFailedException("Payment declined: Insufficient amount");

        System.out.println("Order placed successfully with ID: " + order.getOrderId());
    }

    private static void trackOrderStatus() throws SQLException, InvalidDataException {
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();
        Order order = orderDAO.getOrdersByCustomer(0).stream()
                .filter(o -> o.getOrderId() == orderId)
                .findFirst()
                .orElse(null);
        if (order == null) throw new InvalidDataException("Order not found");

        System.out.println(order.getOrderDetails());
    }

    private static void updateInventory() throws SQLException, InvalidDataException {
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine();
        Product product = productDAO.getProductById(productId);
        if (product == null) throw new InvalidDataException("Product not found");

        System.out.print("Enter quantity to add (positive) or remove (negative): ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        if (quantity > 0) {
            inventoryDAO.addToInventory(productId, quantity);
        } else if (quantity < 0) {
            inventoryDAO.removeFromInventory(productId, -quantity);
        }
        System.out.println("Inventory updated successfully.");
    }

    private static void generateSalesReport() throws SQLException {
        List<Order> orders = orderDAO.getOrdersByCustomer(0); // Simplified: fetch all orders
        double totalSales = 0.0;
        for (Order order : orders) {
            totalSales += order.getTotalAmount();
        }
        System.out.println("Total Sales: $" + totalSales + ", Number of Orders: " + orders.size());
    }

    private static void searchProducts() throws SQLException {
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();
        List<Product> products = productDAO.searchProducts(keyword);
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            for (Product product : products) {
                System.out.println(product.getProductDetails());
            }
        }
    }

    private static void logError(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("error.log", true))) {
            writer.write(LocalDate.now() + ": " + message + "\n");
        } catch (IOException e) {
            System.out.println("Failed to log error: " + e.getMessage());
        }
    }
}