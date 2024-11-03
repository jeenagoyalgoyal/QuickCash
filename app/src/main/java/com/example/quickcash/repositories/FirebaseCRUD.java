package com.example.quickcash.repositories;

import androidx.annotation.NonNull;

import android.util.Log;

import com.example.quickcash.models.Job;
import com.example.quickcash.models.JobLocation;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class FirebaseCRUD {
    private static final String TAG = "FirebaseCRUD";
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
                    try {
                        Job job = jobSnapshot.getValue(Job.class);
                        if (job != null && isJobInLocation(job, searchLocation)) {
                            jobList.add(job);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error processing job: " + e.getMessage());
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
        try {
            // First try to get the location as a JobLocation object
            JobLocation jobLocation = job.getJobLocation();
            if (jobLocation != null && jobLocation.getAddress() != null) {
                return jobLocation.getAddress().toLowerCase().contains(searchLocation);
            }

            // If that fails, try to get it as a string
            Object locationObj = job.getLocation();
            if (locationObj != null) {
                String locationStr = locationObj.toString().toLowerCase();
                return locationStr.contains(searchLocation);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking job location: " + e.getMessage());
        }
        return false;
    }

    public interface JobDataCallback {
        void onCallback(List<Job> jobList);

        void onError(String error);
    }
}
