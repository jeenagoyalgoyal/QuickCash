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
        String jobID = job.getJobID(); // Assumes TempJob has a getJobID method
        DatabaseReference userJobRef = databaseReference.child(userID).child(jobID);

        // Remove the job from Firebase
        userJobRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Optional: handle successful removal (e.g., show success message)
                    System.out.println("Job removed from preferred jobs.");
                })
                .addOnFailureListener(e -> {
                    // Optional: handle failure (e.g., log error)
                    System.err.println("Failed to remove job: " + e.getMessage());
                });
    }


    public void getJobs(String userID, OnJobsRetrievedListener listener) {
        DatabaseReference userJobsRef = databaseReference.child(userID);
        userJobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<TempJob> jobList = new ArrayList<>();
                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    TempJob job = jobSnapshot.getValue(TempJob.class);
                    jobList.add(job);
                }
                listener.onJobsRetrieved(jobList.toArray(new TempJob[0]));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error fetching jobs: " + error.getMessage());
                listener.onJobsRetrieved(null); // Notify listener of failure
            }
        });
    }

    // Define an interface for callback
    public interface OnJobsRetrievedListener {
        void onJobsRetrieved(TempJob[] jobs);
    }

}
