package com.example.quickcash;

public class UseRole {

    private static UseRole instance;
    private String currentRole = "employee";

    public UseRole(){
        currentRole = "employee";
    }

    public static UseRole getInstance(){
        if(instance == null){
            instance = new UseRole();
        }
        return instance;
    }

    public String getCurrentRole(){
        return currentRole;
    }

    public void switchRole(){
        if(currentRole.equals("employee")){
            currentRole = "employer";
        }
        else {
            currentRole= "employee";
        }
    }
}
