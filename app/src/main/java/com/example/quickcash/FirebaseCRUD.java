package com.example.quickcash;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    protected DatabaseReference getPasswordRef(){
        return this.database.getReference("password");
    }


    protected void initializeDatabaseRefListeners() {
        //Incomplete method, add the missing code.
        this.setEmailListener();
        this.setPasswordListener();
    }


    protected void setEmailListener() {
        this.emailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                extractedEmailAddress = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    protected void setPasswordListener() {
        this.passwordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                extractedPassword = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public String getExtractedEmailAddress() {
        return this.extractedEmailAddress;
    }

    public String getExtractedPassword() {
        //buggy code, fix the bug!
        return this.extractedPassword;
    }

}
