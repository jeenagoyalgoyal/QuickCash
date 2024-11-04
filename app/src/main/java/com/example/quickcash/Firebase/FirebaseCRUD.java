package com.example.quickcash.Firebase;

import androidx.annotation.NonNull;

import com.example.quickcash.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FirebaseCRUD {
    private final FirebaseDatabase database;
    private DatabaseReference passwordRef;
    private DatabaseReference emailRef;
    private DatabaseReference nameRef;
    private DatabaseReference preferredJobsRef;

    private String extractedEmailAddress;
    private String extractedPassword;
    private String extractedName;

    public FirebaseCRUD(FirebaseDatabase database) {
        this.database = database;
        this.initializeDatabaseRefs();
        this.initializeDatabaseRefListeners();
    }

    protected void initializeDatabaseRefs() {
        this.nameRef = getNameRef();
        this.passwordRef = getPasswordRef();
        this.emailRef = getEmailAddressRef();
        this.preferredJobsRef = getPreferredJobsRef();
    }

    private DatabaseReference getNameRef() {
        return this.database.getReference("Name");
    }

    protected DatabaseReference getEmailAddressRef() {
        return this.database.getReference("emailAddress");
    }

    protected DatabaseReference getPasswordRef() {
        return this.database.getReference("password");
    }

    protected DatabaseReference getPreferredJobsRef() {
        String userId = extractedEmailAddress != null ? sanitizeEmail(extractedEmailAddress) : "unknown_user";
        return this.database.getReference("Users").child(userId).child("preferredJobs");
    }

    private String sanitizeEmail(String email) {
        return email.replace(".", ",").replace("@", "_");
    }


    public void addPreferredJob(String jobId, Job job) {
        preferredJobsRef.child(jobId).setValue(job)
                .addOnSuccessListener(aVoid -> {
                    // Handle success, e.g., display a message
                })
                .addOnFailureListener(e -> {
                    // Handle failure, e.g., display an error message
                });
    }

    public void getPreferredJobs(ValueEventListener listener) {
        preferredJobsRef.addValueEventListener(listener);
    }

    public void removePreferredJob(String jobId) {
        preferredJobsRef.child(jobId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    protected void initializeDatabaseRefListeners() {
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
        return this.extractedPassword;
    }

    public String getExtractedName() {
        return this.extractedName;
    }
}
