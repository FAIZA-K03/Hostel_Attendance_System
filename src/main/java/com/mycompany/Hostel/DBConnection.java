package com.mycompany.Hostel;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;


public class DBConnection {

    private static final String URL  = "jdbc:mysql://localhost:3306/hostel_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "";  // Change to your MySQL root password

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e1) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e2) {
                JOptionPane.showMessageDialog(null,
                    "MySQL JDBC Driver not found.\nAdd mysql-connector-java.jar to your project libraries.",
                    "Driver Missing", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Database Connection Failed!\n\nMake sure:\n" +
                "1. MySQL is running\n" +
                "2. hostel_db database exists\n" +
                "3. Username/password are correct\n\nError: " + e.getMessage(),
                "Connection Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
