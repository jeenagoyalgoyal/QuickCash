package com.example.quickcash.ui.repositories;

import androidx.annotation.NonNull;

import com.example.quickcash.ui.models.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FirebaseCRUD {
    private final FirebaseDatabase database;

    private DatabaseReference passwordRef = null;
    private DatabaseReference emailRef = null;
    private DatabaseReference nameRef = null;

    private String extractedEmailAddress;
    private String extractedPassword;
    private String extractedName;

    public FirebaseCRUD(FirebaseDatabase database) {
        this.database = database;
        this.initializeDatabaseRefs();
        this.initializeDatabaseRefListeners();
    }

    private void registerToLogin() {
    }

    protected void initializeDatabaseRefs() {
        this.nameRef = getNameRef();
        this.passwordRef = getPasswordRef();
        this.emailRef = getEmailAddressRef();
    }

    private DatabaseReference getNameRef() {
        return this.database.getReference("Name");
    }

    protected DatabaseReference getEmailAddressRef() {
        return this.database.getReference("emailAddress");
    }

    protected DatabaseReference getPasswordRef(){
        return this.database.getReference("password");
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
                // Handle error if needed
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
                // Handle error if needed
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

    // New interface for callback
    public interface JobDataCallback {
        void onCallback(List<Job> jobList);
    }

    // New method to search for jobs
    public void searchJobs(String query, final JobDataCallback callback) {
        DatabaseReference jobsRef = database.getReference("Jobs");

        // Query to search jobs by jobTitle
        jobsRef.orderByChild("jobTitle").startAt(query).endAt(query + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Job> jobList = new ArrayList<>();
                        for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                            Job job = jobSnapshot.getValue(Job.class);
                            if (job != null) {
                                jobList.add(job);
                            }
                        }
                        callback.onCallback(jobList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors here if needed
                    }
                });
    }
}
