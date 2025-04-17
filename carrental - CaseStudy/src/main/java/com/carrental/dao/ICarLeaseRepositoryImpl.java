package com.carrental.dao;

import com.carrental.entity.*;
import com.carrental.myexceptions.*;
import com.carrental.util.DBConnection; 

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ICarLeaseRepositoryImpl implements ICarLeaseRepository {
    @Override
    public void addCar(Vehicle car) throws SQLException {
        String sql = "INSERT INTO vehicle (make, model, year, daily WARNate, status, passengerCapacity, engineCapacity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, car.getMake());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setDouble(4, car.getDailyRate());
            stmt.setString(5, car.getStatus());
            stmt.setInt(6, car.getPassengerCapacity());
            stmt.setDouble(7, car.getEngineCapacity());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                car.setVehicleID(rs.getInt(1));
            }
        }
    }

    @Override
    public void removeCar(int carID) throws SQLException, CarNotFoundException {
        String sql = "DELETE FROM vehicle WHERE vehicleID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carID);
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new CarNotFoundException("Car with ID " + carID + " not found");
        }
    }

    @Override
    public List<Vehicle> listAvailableCars() throws SQLException {
        List<Vehicle> cars = new ArrayList<>();
        String sql = "SELECT * FROM vehicle WHERE status = 'available'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cars.add(new Vehicle(rs.getInt("vehicleID"), rs.getString("make"), rs.getString("model"),
                        rs.getInt("year"), rs.getDouble("dailyRate"), rs.getString("status"),
                        rs.getInt("passengerCapacity"), rs.getDouble("engineCapacity")));
            }
        }
        return cars;
    }

    @Override
    public List<Vehicle> listRentedCars() throws SQLException {
        List<Vehicle> cars = new ArrayList<>();
        String sql = "SELECT * FROM vehicle WHERE status = 'notAvailable'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cars.add(new Vehicle(rs.getInt("vehicleID"), rs.getString("make"), rs.getString("model"),
                        rs.getInt("year"), rs.getDouble("dailyRate"), rs.getString("status"),
                        rs.getInt("passengerCapacity"), rs.getDouble("engineCapacity")));
            }
        }
        return cars;
    }

    @Override
    public Vehicle findCarById(int carID) throws SQLException, CarNotFoundException {
        String sql = "SELECT * FROM vehicle WHERE vehicleID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Vehicle(rs.getInt("vehicleID"), rs.getString("make"), rs.getString("model"),
                        rs.getInt("year"), rs.getDouble("dailyRate"), rs.getString("status"),
                        rs.getInt("passengerCapacity"), rs.getDouble("engineCapacity"));
            }
            throw new CarNotFoundException("Car with ID " + carID + " not found");
        }
    }

    @Override
    public void addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer (firstName, lastName, email, phoneNumber) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                customer.setCustomerID(rs.getInt(1));
            }
        }
    }

    @Override
    public void removeCustomer(int customerID) throws SQLException, CustomerNotFoundException {
        String sql = "DELETE FROM customer WHERE customerID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerID);
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new CustomerNotFoundException("Customer with ID " + customerID + " not found");
        }
    }

    @Override
    public List<Customer> listCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                customers.add(new Customer(rs.getInt("customerID"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("email"), rs.getString("phoneNumber")));
            }
        }
        return customers;
    }

    @Override
    public Customer findCustomerById(int customerID) throws SQLException, CustomerNotFoundException {
        String sql = "SELECT * FROM customer WHERE customerID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(rs.getInt("customerID"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("email"), rs.getString("phoneNumber"));
            }
            throw new CustomerNotFoundException("Customer with ID " + customerID + " not found");
        }
    }

    @Override
    public Lease createLease(int customerID, int carID, LocalDate startDate, LocalDate endDate, String type) throws SQLException, CarNotFoundException, CustomerNotFoundException {
        // Validate customer and car
        findCustomerById(customerID);
        Vehicle car = findCarById(carID);
        if (!car.getStatus().equals("available")) {
            throw new CarNotFoundException("Car with ID " + carID + " is not available");
        }

        String sql = "INSERT INTO lease (vehicleID, customerID, startDate, endDate, type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, carID);
            stmt.setInt(2, customerID);
            stmt.setDate(3, Date.valueOf(startDate));
            stmt.setDate(4, Date.valueOf(endDate));
            stmt.setString(5, type);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int leaseID = rs.getInt(1);
                // Update car status
                String updateSql = "UPDATE vehicle SET status = 'notAvailable' WHERE vehicleID = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, carID);
                    updateStmt.executeUpdate();
                }
                return new Lease(leaseID, carID, customerID, startDate, endDate, type);
            }
        }
        return null;
    }

    @Override
    public void returnCar(int leaseID) throws SQLException, LeaseNotFoundException {
        String sql = "SELECT vehicleID FROM lease WHERE leaseID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, leaseID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int vehicleID = rs.getInt("vehicleID");
                String updateSql = "UPDATE vehicle SET status = 'available' WHERE vehicleID = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, vehicleID);
                    updateStmt.executeUpdate();
                }
            } else {
                throw new LeaseNotFoundException("Lease with ID " + leaseID + " not found");
            }
        }
    }

    @Override
    public List<Lease> listActiveLeases() throws SQLException {
        List<Lease> leases = new ArrayList<>();
        String sql = "SELECT * FROM lease WHERE endDate >= CURDATE()";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                leases.add(new Lease(rs.getInt("leaseID"), rs.getInt("vehicleID"), rs.getInt("customerID"),
                        rs.getDate("startDate").toLocalDate(), rs.getDate("endDate").toLocalDate(),
                        rs.getString("type")));
            }
        }
        return leases;
    }

    @Override
    public List<Lease> listLeaseHistory() throws SQLException {
        List<Lease> leases = new ArrayList<>();
        String sql = "SELECT * FROM lease";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                leases.add(new Lease(rs.getInt("leaseID"), rs.getInt("vehicleID"), rs.getInt("customerID"),
                        rs.getDate("startDate").toLocalDate(), rs.getDate("endDate").toLocalDate(),
                        rs.getString("type")));
            }
        }
        return leases;
    }

    @Override
    public void recordPayment(Lease lease, double amount) throws SQLException, LeaseNotFoundException {
        String checkSql = "SELECT leaseID FROM lease WHERE leaseID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, lease.getLeaseID());
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                throw new LeaseNotFoundException("Lease with ID " + lease.getLeaseID() + " not found");
            }
        }

        String sql = "INSERT INTO payment (leaseID, paymentDate, amount) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lease.getLeaseID());
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
        }
    }
}