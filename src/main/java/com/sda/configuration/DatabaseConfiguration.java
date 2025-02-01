package com.sda.configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3308/library";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "user123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}

