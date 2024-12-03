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

    public Task<List<Job>> getInProgressJobs(String emailID){
        TaskCompletionSource<List<Job>> taskCompletionSource = new TaskCompletionSource<>();

        Query query = databaseReference.orderByChild("employerId").equalTo(emailID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Job> jobs = new ArrayList<>();
                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);
                    if (job != null && job.getStatus().equals("In-progress")) {
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

    /**
     * Deletes a job from the database based on the job ID.
     *
     * @param jobId The unique ID of the job to delete.
     * @return A Task representing the completion status of the operation.
     */
    public Task<Boolean> deleteJobById(String jobId) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        databaseReference.child(jobId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("JobCRUD", "Job with ID " + jobId + " deleted successfully.");
                taskCompletionSource.setResult(true);
            } else {
                Log.e("JobCRUD", "Failed to delete job with ID " + jobId, task.getException());
                taskCompletionSource.setResult(false);
            }
        });

        return taskCompletionSource.getTask();
    }

    public void setStatusOfJobToCompleted(String jobId) {
        databaseReference.child(jobId).child("status").setValue("complete");
    }
}
