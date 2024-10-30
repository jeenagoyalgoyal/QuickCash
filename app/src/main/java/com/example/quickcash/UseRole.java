package com.example.quickcash;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UseRole {

    private static UseRole instance;
    private String currentRole;

    private DatabaseReference db;

    private UseRole() {
        db = FirebaseDatabase.getInstance().getReference("Users");
        currentRole = "employee";
    }

    public static UseRole getInstance() {
        if (instance == null) {
            instance = new UseRole();
        }
        return instance;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void switchRole(int id) {
        if (currentRole.equals("employee")) {
            currentRole = "employer";
        } else {
            currentRole = "employee";
        }
        updateDatabase(id, currentRole);
    }

    private void updateDatabase(int id, String role) {
        db.child(String.valueOf(id)).child("role").setValue(role)
                .addOnSuccessListener(y -> {})
                .addOnFailureListener(e -> {});
    }

    public void setCurrentRole(int id, String currentRole) {
        this.currentRole = currentRole;
        updateDatabase(id, currentRole);
    }


    // Fetches the role of the user that is logging in
    public void fetchUserRole(String email, OnRoleFetchedListener listener) {
        String validName = emailToValidNodeName(email);
        db.child(validName).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String role = dataSnapshot.getValue(String.class);

                    // Gets the specific role for user
                    if (role != null) {
                        currentRole = role.toLowerCase();
                        listener.onRoleFetched(role);
                    }

                    else {
                        listener.onRoleFetched(null);
                    }
                }

                else {
                    listener.onRoleFetched(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onRoleFetched(null);
            }
        });
    }

    // Email of user for firebase
    private String emailToValidNodeName(String email) {
        return email.replace(".", ",");
    }

    public interface OnRoleFetchedListener {
        void onRoleFetched(String role);
    }

    public void setCurrentRole(String role) {
        this.currentRole = role.toLowerCase(); // Ensure it's lowercase for consistency
    }
}
