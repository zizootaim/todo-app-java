package com.example.finaltodo.User;

public class User {
    private int id;
    private String name;

    public User(int userId, String userName){
        id = userId;
        name = userName;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
