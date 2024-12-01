package com.example.quickcash.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.quickcash.model.Application;
import com.example.quickcash.model.Job;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides methods for creating, reading, and managing job
 * postings in the firebase database
 */
public class EmployerCRUD {

    private final DatabaseReference databaseReference;

    /**
     * Constructs a JobCRUD instance with a reference to the "Jobs" node in the
     * firebase database
     * @param firebaseDatabase
     */
    public EmployerCRUD(FirebaseDatabase firebaseDatabase) {
        databaseReference = firebaseDatabase.getReference("Jobs");
    }

    public Task<List<Job>> getJobsByQuery(Query query) {
        TaskCompletionSource<List<Job>> taskCompletionSource = new TaskCompletionSource<>();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Uses listener
             * @param snapshot The current data at the location
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Job> jobs = new ArrayList<>();
                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);
                    if (job != null) {
                        jobs.add(job);
                    }
                }
                taskCompletionSource.setResult(jobs);
            }

            /**
             * Uses listener
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

}
