package com.example.quickcash;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PreferredJobs {
    private DatabaseReference databaseReference;

    public PreferredJobs() {
        // Initialize Firebase Database reference
        this.databaseReference = FirebaseDatabase.getInstance().getReference("preferred_jobs");
    }

    public void addJob(TempJob job, String userID) {

    }


    public void removeJob(TempJob job, String userID) {

    }


    public void getJobs(String userID, OnJobsRetrievedListener listener) {

    }

    // Define an interface for callback
    public interface OnJobsRetrievedListener {
        void onJobsRetrieved(TempJob[] jobs);
    }

}
