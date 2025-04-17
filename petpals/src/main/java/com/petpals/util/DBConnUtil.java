package com.petpals.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {
    public static Connection getConnection() {
        String connString = DBPropertyUtil.getConnectionString();
        try {
            return DriverManager.getConnection(connString);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}