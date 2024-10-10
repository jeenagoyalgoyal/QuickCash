package com.example.quickcash;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UseRole {

    private static UseRole instance;
    private String currentRole = "employee";

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

    public void switchRole(String id){
        if(currentRole.equals("employee")){
            currentRole = "employer";
        }
        else {
            currentRole= "employee";
        }
        updateDatabase(id, currentRole);
    }
    private void updateDatabase(String id, String role){
        db.child(id).child("role").setValue(role)
                .addOnSuccessListener( y -> {
            //success
        })
                .addOnFailureListener(e ->{
                    //fail
                });
    }

    public void setCurrentRole(String employee) {
        this.currentRole=currentRole;
    }
}
