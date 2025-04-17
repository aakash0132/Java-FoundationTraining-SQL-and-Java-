package com.techshop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/techshopdb?user=root&password=iamroot@007";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(CONNECTION_STRING);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}