package com.example.quickcash;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PreferredJobs {
    private DatabaseReference databaseReference;

    public PreferredJobs() {
        // Initialize Firebase Database reference
        this.databaseReference = FirebaseDatabase.getInstance().getReference("preferred_jobs");
    }

    public void addJob(TempJob job, String userID) {
        // Add job under the user's preferred jobs in the database

    }

    public void removeJob(TempJob job, String userID) {
        // Remove job from the user's preferred jobs in the database

    }

    public TempJob[] getJobs(String userID) {
        return new TempJob[0];
    }
}
