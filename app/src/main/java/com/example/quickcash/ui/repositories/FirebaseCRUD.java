package com.example.quickcash.ui.repositories;

import androidx.annotation.NonNull;
import com.example.quickcash.ui.models.Job;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class FirebaseCRUD {
    private final FirebaseDatabase database;

    public FirebaseCRUD(FirebaseDatabase database) {
        this.database = database;
    }

    // Callback interface for job data
    public interface JobDataCallback {
        void onCallback(List<Job> jobList);
    }

    // Fetch all jobs method
    public void getAllJobs(final JobDataCallback callback) {
        database.getReference("Jobs")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Job> jobList = new ArrayList<>();
                        for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                            Job job = jobSnapshot.getValue(Job.class);
                            if (job != null) jobList.add(job);
                        }
                        callback.onCallback(jobList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors
                    }
                });
    }

    // Search jobs by jobTitle
    public void searchJobs(String query, final JobDataCallback callback) {
        database.getReference("Jobs").orderByChild("jobTitle")
                .startAt(query).endAt(query + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Job> jobList = new ArrayList<>();
                        for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                            Job job = jobSnapshot.getValue(Job.class);
                            if (job != null) jobList.add(job);
                        }
                        callback.onCallback(jobList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors
                    }
                });
    }
}
