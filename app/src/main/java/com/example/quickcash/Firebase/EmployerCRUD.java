package com.example.quickcash.Firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quickcash.EmployerJobsActivity;
import com.example.quickcash.model.Application;
import com.example.quickcash.model.Job;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class provides methods for creating, reading, and managing job
 * postings in the firebase database
 */
public class EmployerCRUD {

    private final DatabaseReference databaseReference;

    /**
     * Constructs a JobCRUD instance with a reference to the "Jobs" node in the
     * firebase database
     */
    public EmployerCRUD() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Jobs");
    }

    public Boolean changeApplicationStatusByJobId(String jobID, Application application, String status, Context context) {
        DatabaseReference applicationRef = FirebaseDatabase.getInstance()
                .getReference("Jobs")
                .child(jobID)
                .child("applications")
                .child(application.getApplicationId());
        AtomicReference<Boolean> flag = new AtomicReference<>(false);

        applicationRef.child("Status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Application " + status, Toast.LENGTH_SHORT).show();
                    flag.set(true);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error updating status", Toast.LENGTH_SHORT).show();
                    flag.set(false);
                });

        return flag.get();
    }

    public void closeJob(String employeeID, String applicantName, String jobID) {
        databaseReference.child(jobID).child("status");
        databaseReference.setValue("In-progress");

        databaseReference.child(jobID).child("employeeId");
        databaseReference.setValue(employeeID);

        databaseReference.child(jobID).child("employeeName");
        databaseReference.setValue(applicantName);
    }

    public void getApplicationsByJob(Job job, OnApplicationCountListener listener) {
        databaseReference.child(job.getJobId())
                .child("applications")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Pass the count to the callback
                        long count = dataSnapshot.getChildrenCount();
                        listener.onCountRetrieved(String.valueOf(count));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors and pass them to the callback
                        listener.onError(databaseError.getMessage());
                    }
                });
    }

    public Task<List<Job>> getJobsByEmailID(String emailID, Context context) {

        TaskCompletionSource<List<Job>> taskCompletionSource = new TaskCompletionSource<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Job> jobList = new ArrayList<>();
                Log.d("Fetching", emailID);
                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);

                    if (job != null) {

                        if (job.getEmployerId() != null && job.getEmployerId().equals(emailID)) {
                            Log.d("Fetching", job.getEmployerId());
                            String jobId = jobSnapshot.getKey();
                            job.setJobId(jobId);
                            jobList.add(job); // Add job to jobList
                        }
                    }
                }
                taskCompletionSource.setResult(jobList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to load job postings", Toast.LENGTH_SHORT).show();
            }
        });

        return taskCompletionSource.getTask();
    }

    // Define a callback interface
    public interface OnApplicationCountListener {
        void onCountRetrieved(String count);

        void onError(String error);
    }
}
