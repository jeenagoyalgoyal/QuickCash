package com.example.quickcash.Firebase;

import androidx.annotation.NonNull;

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

public class JobCRUD {

    private final DatabaseReference databaseReference;

    public JobCRUD(FirebaseDatabase firebaseDatabase) {
        databaseReference = firebaseDatabase.getReference("Jobs");
    }

    public Task<Boolean> submitJob(Job job) {
        String jobId = databaseReference.push().getKey();
        if (jobId != null) {
            job.setJobId(jobId);
            return databaseReference.child(jobId).setValue(job)
                    .continueWith(task -> task.isSuccessful());
        }
        return null;
    }

    public Task<Job> getJobById(String jobId) {
        TaskCompletionSource<Job> taskCompletionSource = new TaskCompletionSource<>();

        databaseReference.child(jobId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Job job = snapshot.getValue(Job.class);
                taskCompletionSource.setResult(job);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    public Task<List<Job>> getJobsByQuery(Query query) {
        TaskCompletionSource<List<Job>> taskCompletionSource = new TaskCompletionSource<>();

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



}
