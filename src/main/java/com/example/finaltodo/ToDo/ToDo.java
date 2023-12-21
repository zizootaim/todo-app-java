package com.example.finaltodo.ToDo;

public class ToDo {
    private int id;
    private String description;
    private String status;

    public ToDo(int todoID, String todoDescription, String todoStatus){
        id = todoID;
        description = todoDescription;
        status = todoStatus;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}
