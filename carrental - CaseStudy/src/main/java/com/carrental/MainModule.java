package com.carrental;

import com.carrental.dao.ICarLeaseRepository;
import com.carrental.dao.ICarLeaseRepositoryImpl;
import com.carrental.entity.*;
import com.carrental.myexceptions.*;
import com.carrental.util.DBConnection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ICarLeaseRepository repository = new ICarLeaseRepositoryImpl();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nCar Rental System Menu:");
            System.out.println("1. Add Customer");
            System.out.println("2. Add Car");
            System.out.println("3. Create Lease");
            System.out.println("4. Return Car");
            System.out.println("5. Record Payment");
            System.out.println("6. List Available Cars");
            System.out.println("7. List Rented Cars");
            System.out.println("8. List Customers");
            System.out.println("9. List Active Leases");
            System.out.println("10. List Lease History");
            System.out.println("11. Find Car by ID");
            System.out.println("12. Find Customer by ID");
            System.out.println("13. Calculate Total Revenue");
            System.out.println("14. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1 -> addCustomer();
                    case 2 -> addCar();
                    case 3 -> createLease();
                    case 4 -> returnCar();
                    case 5 -> recordPayment();
                    case 6 -> listAvailableCars();
                    case 7 -> listRentedCars();
                    case 8 -> listCustomers();
                    case 9 -> listActiveLeases();
                    case 10 -> listLeaseHistory();
                    case 11 -> findCarById();
                    case 12 -> findCustomerById();
                    case 13 -> calculateTotalRevenue();
                    case 14 -> {
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

    private static void addCustomer() throws SQLException {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone number (10 digits): ");
        String phoneNumber = scanner.nextLine();

        Customer customer = new Customer(0, firstName, lastName, email, phoneNumber);
        repository.addCustomer(customer);
        System.out.println("Customer added successfully with ID: " + customer.getCustomerID());
    }

    private static void addCar() throws SQLException {
        System.out.print("Enter make: ");
        String make = scanner.nextLine();
        System.out.print("Enter model: ");
        String model = scanner.nextLine();
        System.out.print("Enter year: ");
        int year = scanner.nextInt();
        System.out.print("Enter daily rate: ");
        double dailyRate = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter status (available/notAvailable): ");
        String status = scanner.nextLine();
        System.out.print("Enter passenger capacity: ");
        int passengerCapacity = scanner.nextInt();
        System.out.print("Enter engine capacity: ");
        double engineCapacity = scanner.nextDouble();
        scanner.nextLine();

        Vehicle car = new Vehicle(0, make, model, year, dailyRate, status, passengerCapacity, engineCapacity);
        repository.addCar(car);
        System.out.println("Car added successfully with ID: " + car.getVehicleID());
    }

    private static void createLease() throws SQLException, CarNotFoundException, CustomerNotFoundException {
        System.out.print("Enter customer ID: ");
        int customerID = scanner.nextInt();
        System.out.print("Enter car ID: ");
        int carID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine();
        System.out.print("Enter lease type (Daily/Monthly): ");
        String type = scanner.nextLine();

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        Lease lease = repository.createLease(customerID, carID, startDate, endDate, type);

        Vehicle car = repository.findCarById(carID);
        double totalCost = lease.calculateTotalCost(car.getDailyRate());
        System.out.println("Lease created successfully with ID: " + lease.getLeaseID() + ", Total Cost: $" + totalCost);
    }

    private static void returnCar() throws SQLException, LeaseNotFoundException {
        System.out.print("Enter lease ID: ");
        int leaseID = scanner.nextInt();
        scanner.nextLine();
        repository.returnCar(leaseID);
        System.out.println("Car returned successfully for lease ID: " + leaseID);
    }

    private static void recordPayment() throws SQLException, LeaseNotFoundException {
        System.out.print("Enter lease ID: ");
        int leaseID = scanner.nextInt();
        System.out.print("Enter payment amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        Lease lease = repository.listLeaseHistory().stream()
                .filter(l -> l.getLeaseID() == leaseID)
                .findFirst()
                .orElseThrow(() -> new LeaseNotFoundException("Lease with ID " + leaseID + " not found"));
        repository.recordPayment(lease, amount);
        System.out.println("Payment recorded successfully for lease ID: " + leaseID);
    }

    private static void listAvailableCars() throws SQLException {
        List<Vehicle> cars = repository.listAvailableCars();
        if (cars.isEmpty()) {
            System.out.println("No available cars.");
        } else {
            cars.forEach(System.out::println);
        }
    }

    private static void listRentedCars() throws SQLException {
        List<Vehicle> cars = repository.listRentedCars();
        if (cars.isEmpty()) {
            System.out.println("No rented cars.");
        } else {
            cars.forEach(System.out::println);
        }
    }

    private static void listCustomers() throws SQLException {
        List<Customer> customers = repository.listCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers.");
        } else {
            customers.forEach(System.out::println);
        }
    }

    private static void listActiveLeases() throws SQLException {
        List<Lease> leases = repository.listActiveLeases();
        if (leases.isEmpty()) {
            System.out.println("No active leases.");
        } else {
            leases.forEach(System.out::println);
        }
    }

    private static void listLeaseHistory() throws SQLException {
        List<Lease> leases = repository.listLeaseHistory();
        if (leases.isEmpty()) {
            System.out.println("No lease history.");
        } else {
            leases.forEach(System.out::println);
        }
    }

    private static void findCarById() throws SQLException, CarNotFoundException {
        System.out.print("Enter car ID: ");
        int carID = scanner.nextInt();
        scanner.nextLine();
        Vehicle car = repository.findCarById(carID);
        System.out.println(car);
    }

    private static void findCustomerById() throws SQLException, CustomerNotFoundException {
        System.out.print("Enter customer ID: ");
        int customerID = scanner.nextInt();
        scanner.nextLine();
        Customer customer = repository.findCustomerById(customerID);
        System.out.println(customer);
    }

    private static void calculateTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(amount) AS total FROM payment";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                System.out.println("Total Revenue: $" + rs.getDouble("total"));
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