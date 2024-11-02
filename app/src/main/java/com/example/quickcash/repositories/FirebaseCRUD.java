package com.example.quickcash.repositories;

import androidx.annotation.NonNull;
import com.example.quickcash.ui.models.Job;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class FirebaseCRUD {
    private final FirebaseDatabase database;
    private final DatabaseReference jobsRef;

    // Default constructor
    public FirebaseCRUD() {
        this.database = FirebaseDatabase.getInstance();
        this.jobsRef = database.getReference("Jobs");
    }

    // Existing constructor (if needed elsewhere)
    public FirebaseCRUD(FirebaseDatabase database) {
        this.database = database;
        this.jobsRef = database.getReference("Jobs");
    }

    public void searchJobs(String location, final JobDataCallback callback) {
        // Convert location to lowercase for case-insensitive search
        final String searchLocation = location.toLowerCase();

        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Job> jobList = new ArrayList<>();

                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);
                    if (job != null && isJobInLocation(job, searchLocation)) {
                        jobList.add(job);
                    }
                }

                callback.onCallback(jobList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    private boolean isJobInLocation(Job job, String searchLocation) {
        if (job.getLocation() == null) {
            return false;
        }

        // Convert job location to lowercase for case-insensitive comparison
        String jobLocation = job.getLocation().toLowerCase();

        // Check if the job location contains the search term
        return jobLocation.contains(searchLocation);
    }

    public interface JobDataCallback {
        void onCallback(List<Job> jobList);
        void onError(String error);
    }
}
