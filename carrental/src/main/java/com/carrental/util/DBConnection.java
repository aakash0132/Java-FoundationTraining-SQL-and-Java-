package com.carrental.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/carrentaldb?user=root&password=iamroot@007";
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(CONNECTION_STRING);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}