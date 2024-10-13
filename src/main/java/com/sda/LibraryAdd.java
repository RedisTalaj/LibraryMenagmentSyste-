package com.sda;
import java.sql.*;

public class LibraryAdd {
    public static final String JDBC_URL = "jdbc:mysql://localhost:3308/bibloteka";
    public static final String USERNAME = "user";
    public static final String PASSWORD = "user123";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            if (connection != null) {
                System.out.println("Lidhja me bazen e te dhenave u krijua me sukses!");
            }

            statement = connection.createStatement();



            System.out.println("Data inserted successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
