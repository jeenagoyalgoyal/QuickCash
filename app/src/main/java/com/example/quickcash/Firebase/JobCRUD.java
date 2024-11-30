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
public class JobCRUD {

    private final DatabaseReference databaseReference;


    /**
     * Constructs a JobCRUD instance with a reference to the "Jobs" node in the
     * firebase database
     * @param firebaseDatabase
     */
    public JobCRUD(FirebaseDatabase firebaseDatabase) {
        databaseReference = firebaseDatabase.getReference("Jobs");
    }

    /**
     * Submits a job to the database, with a unique job id
     * @param job
     * @return returns successful
     */
    public Task<Boolean> submitJob(Job job) {
        String jobId = databaseReference.push().getKey();
        if (jobId != null) {
            job.setJobId(jobId);
            return databaseReference.child(jobId).setValue(job)
                    .continueWith(task -> task.isSuccessful());
        }
        return null;
    }

    /**
     * Gets a job based on the id
     * @param jobId
     * @return job object when found
     */
    public Task<Job> getJobById(String jobId) {
        TaskCompletionSource<Job> taskCompletionSource = new TaskCompletionSource<>();

        databaseReference.child(jobId).addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Uses a listener
             * @param snapshot The current data at the location
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Job job = snapshot.getValue(Job.class);
                taskCompletionSource.setResult(job);
            }

            /**
             * Uses a listener
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    /**
     * Gets a list of jobs posted
     * @param query
     * @return list of jobs
     */
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

    public Task<List<Job>> getJobsByLocation(String city) {
        TaskCompletionSource<List<Job>> taskCompletionSource = new TaskCompletionSource<>();
        Query query = databaseReference.orderByChild("location").equalTo(city);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    public Task<List<Application>> getApplicationsForJobsByQuery(Query query) {
        TaskCompletionSource<List<Application>> taskCompletionSource = new TaskCompletionSource<>();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Application> applications = new ArrayList<>();

                // Loop over all job snapshots
                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    Log.d("Applications fetching: ", "Job found: " + jobSnapshot.getKey()); // Log job ID

                    // Now loop over the applications under each job
                    DataSnapshot applicationsSnapshot = jobSnapshot.child("Applications");
                    Log.d("Applications fetching: ", "Applications snapshot found: " + applicationsSnapshot.getChildrenCount()); // Log number of applications

                    if (applicationsSnapshot.exists()) {
                        for (DataSnapshot applicationSnapshot : applicationsSnapshot.getChildren()) {
                            Application application = applicationSnapshot.getValue(Application.class);
                            if (application != null) {
                                applications.add(application);
                            }
                        }
                    } else {
                        Log.d("Applications fetching: ", "No applications found for this job.");
                    }
                }

                taskCompletionSource.setResult(applications); // Return the list of applications
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Applications fetching: ", "Database error: ", error.toException()); // Log any database error
                taskCompletionSource.setException(error.toException());
            }
        });

        return taskCompletionSource.getTask();
    }
}
