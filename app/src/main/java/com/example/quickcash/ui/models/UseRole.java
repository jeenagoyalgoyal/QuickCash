package com.example.quickcash.ui.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UseRole {

    private static UseRole instance;
    private String currentRole;

    private DatabaseReference db;

    public UseRole(){
        currentRole = "employee";
        db= FirebaseDatabase.getInstance().getReference("users");
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

    public void switchRole(int id){
        if(currentRole.equals("employee")){
            currentRole = "employer";
        }
        else {
            currentRole= "employee";
        }
        updateDatabase(id, currentRole);
    }
    private void updateDatabase(int id, String role){
        db.child(String.valueOf(id)).child("role").setValue(role)
                .addOnSuccessListener( y -> {})
                .addOnFailureListener(e ->{});
    }

    public void setCurrentRole(int id, String currentRole) {
        this.currentRole=currentRole;
        updateDatabase(id, currentRole);
    }
}
