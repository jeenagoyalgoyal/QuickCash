package com.example.quickcash;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseCRUD {
    private final FirebaseDatabase database;

    private DatabaseReference passwordRef = null;
    private DatabaseReference emailRef = null;

    private String extractedEmailAddress;
    private String extractedPassword;

    public FirebaseCRUD(FirebaseDatabase database) {
        this.database = database;
        this.initializeDatabaseRefs();
        this.initializeDatabaseRefListeners();
    }

    protected void initializeDatabaseRefs() {
        this.passwordRef = getPasswordRef();
        this.emailRef = getEmailAddressRef();
    }

    protected DatabaseReference getEmailAddressRef() {
        return this.database.getReference("emailAddress");
    }

    protected DatabaseReference getPasswordRef() {
        return this.database.getReference("password");
    }

    protected void initializeDatabaseRefListeners() {
        this.setEmailListener();
        this.setPasswordListener();
    }

    protected void setEmailListener() {
        this.emailRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                extractedEmailAddress = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors here
            }
        });
    }

    protected void setPasswordListener() {
        this.passwordRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                extractedPassword = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors here
            }
        });
    }

    public String getExtractedEmailAddress() {
        return this.extractedEmailAddress;
    }

    public String getExtractedPassword() {
        if (extractedPassword != null) {
            return this.extractedPassword;
        } else {
            return "Password not yet available";
        }
    }
}
