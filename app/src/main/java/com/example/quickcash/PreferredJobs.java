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
        // Create a unique key for each job under the user's preferred jobs
        String jobID = job.getJobID(); // Assumes TempJob has a getJobID method
        DatabaseReference userJobsRef = databaseReference.child(userID).child(jobID);

        // Store the job details in the database
        userJobsRef.setValue(job)
                .addOnSuccessListener(aVoid -> {
                    // Optional: handle successful addition (e.g., show success message)
                    System.out.println("Job added to preferred jobs.");
                })
                .addOnFailureListener(e -> {
                    // Optional: handle failure (e.g., log error)
                    System.err.println("Failed to add job: " + e.getMessage());
                });
    }


    public void removeJob(TempJob job, String userID) {
        // Remove job from the user's preferred jobs in the database

    }

    public TempJob[] getJobs(String userID) {
        return new TempJob[0];
    }
}
