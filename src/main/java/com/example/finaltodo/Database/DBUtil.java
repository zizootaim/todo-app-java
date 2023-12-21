package com.example.finaltodo.Database;

import com.example.finaltodo.User.User;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    private String url = "jdbc:mysql://localhost:3306/tododb";
    private String username = "root";
    private String password = "root";
    private static Connection connection = null;
    private static User loggedInUser = null;

    public DBUtil() {
        // Using Try Catch Block to handle exceptions like Errors
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void setUser(int id, String name){
        loggedInUser = new User(id, name);
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    // Getting the Connection to make Queries
    public static Connection getConnection() {
        return connection;
    }
}
