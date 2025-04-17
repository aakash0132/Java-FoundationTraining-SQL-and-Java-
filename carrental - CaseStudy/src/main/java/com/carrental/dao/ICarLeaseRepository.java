package com.carrental.dao;

import com.carrental.entity.*;
import com.carrental.myexceptions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ICarLeaseRepository {
    // Car Management
    void addCar(Vehicle car) throws SQLException;
    void removeCar(int carID) throws SQLException, CarNotFoundException;
    List<Vehicle> listAvailableCars() throws SQLException;
    List<Vehicle> listRentedCars() throws SQLException;
    Vehicle findCarById(int carID) throws SQLException, CarNotFoundException;

    // Customer Management
    void addCustomer(Customer customer) throws SQLException;
    void removeCustomer(int customerID) throws SQLException, CustomerNotFoundException;
    List<Customer> listCustomers() throws SQLException;
    Customer findCustomerById(int customerID) throws SQLException, CustomerNotFoundException;

    // Lease Management
    Lease createLease(int customerID, int carID, LocalDate startDate, LocalDate endDate, String type) throws SQLException, CarNotFoundException, CustomerNotFoundException;
    void returnCar(int leaseID) throws SQLException, LeaseNotFoundException;
    List<Lease> listActiveLeases() throws SQLException;
    List<Lease> listLeaseHistory() throws SQLException;

    // Payment Handling
    void recordPayment(Lease lease, double amount) throws SQLException, LeaseNotFoundException;
}