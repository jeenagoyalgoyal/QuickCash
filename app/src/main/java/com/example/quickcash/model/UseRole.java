package com.example.quickcash.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
/**
 * Singleton class to manage and switch user roles (e.g., employee or employer) in the application.
 * Handles interactions with Firebase to persist user role data.
 */
public class UseRole {

    private static UseRole instance;
    private String currentRole;

    private DatabaseReference db;
    /**
     * Private constructor to initialize the Firebase database reference and default role.
     */
    private UseRole() {
        db = FirebaseDatabase.getInstance().getReference("Users");
        currentRole = "employee";
    }
    /**
     * Returns the singleton instance of {@code UseRole}.
     *
     * @return the single instance of the {@code UseRole} class
     */
    public static UseRole getInstance() {
        if (instance == null) {
            instance = new UseRole();
        }
        return instance;
    }
    /**
     * Retrieves the current role of the user.
     *
     * @return the current role as a string (e.g., "employee" or "employer")
     */
    public String getCurrentRole() {
        return currentRole;
    }
    /**
     * Switches the user's role between "employee" and "employer" and updates the role in the database.
     *
     * @param id the user's unique ID
     */
    public void switchRole(String id) {
        if (currentRole.equals("employee")) {
            currentRole = "employer";
        } else {
            currentRole = "employee";
        }
        updateDatabase(id, currentRole);
    }
    /**
     * Updates the user's role in the Firebase database.
     *
     * @param id   the user's unique ID
     * @param role the role to update
     */
    private void updateDatabase(String id, String role) {
        db.child(id).child("role").setValue(role)
                .addOnSuccessListener(y -> {})
                .addOnFailureListener(e -> {});
    }
    /**
     * Sets the current role of the user and updates it in the database.
     *
     * @param id          the user's unique ID
     * @param currentRole the role to set (e.g., "employee" or "employer")
     */
    public void setCurrentRole(String id, String currentRole) {
        this.currentRole = currentRole;
        updateDatabase(id, currentRole);
    }


    /**
     * Fetches the user's role from Firebase using their email and invokes the provided callback upon completion.
     *
     * @param email    the user's email address
     * @param listener the callback interface to handle the fetched role
     */
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

    /**
     * Converts a user's email to a valid Firebase database node name.
     * Replaces periods in the email with commas for compatibility with Firebase keys.
     *
     * @param email the user's email address
     * @return a sanitized string suitable for use as a Firebase node name
     */
    private String emailToValidNodeName(String email) {
        return email.replace(".", ",");
    }
    /**
     * Interface to handle the result of fetching a user's role from Firebase.
     */
    public interface OnRoleFetchedListener {
        void onRoleFetched(String role);
    }
    /**
     * Sets the current role of the user without updating the database.
     *
     * @param role the role to set (e.g., "employee" or "employer")
     */
    public void setCurrentRole(String role) {
        this.currentRole = role.toLowerCase(); // Ensure it's lowercase for consistency
    }
}
