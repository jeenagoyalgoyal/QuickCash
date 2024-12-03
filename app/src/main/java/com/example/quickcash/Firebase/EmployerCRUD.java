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
     * Constructs an {@code EmployerCRUD} instance with a reference to the "Jobs" node in the Firebase database.
     */
    public EmployerCRUD() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Jobs");
    }

    /**
     * Updates the status of a specific job application in the database.
     *
     * @param jobID       The unique identifier for the job.
     * @param application The application whose status needs to be updated.
     * @param status      The new status of the application (e.g., "Accepted", "Rejected").
     * @return A {@link Task} representing the asynchronous operation of updating the status.
     */
    public Task<Void> changeApplicationStatusByJobId(String jobID, Application application, String status) {
        DatabaseReference applicationRef = FirebaseDatabase.getInstance()
                .getReference("Jobs")
                .child(jobID)
                .child("applications")
                .child(application.getApplicationId());

        return applicationRef.child("Status").setValue(status);
    }

    /**
     * Closes a job by updating its status and assigning it to a specific employee.
     *
     * @param jobID         The unique identifier for the job to be closed.
     * @param employeeID    The ID of the employee who accepted the job.
     * @param applicantName The name of the employee assigned to the job.
     */
    public void closeJob(String jobID, String employeeID, String applicantName) {
        DatabaseReference jobRef = databaseReference.child(jobID);
        jobRef.child("status").setValue("In-progress");
        jobRef.child("employeeId").setValue(employeeID);
        jobRef.child("employeeName").setValue(applicantName);
    }

    /**
     * Retrieves the count of applications for a given job and passes it to a callback.
     *
     * @param job      The {@link Job} object whose applications are being queried.
     * @param listener The callback listener to handle the application count or errors.
     */
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

    /**
     * Retrieves all jobs posted by a specific employer using their email ID.
     *
     * @param emailID The email ID of the employer.
     * @param context The context used for displaying error messages if necessary.
     * @return A {@link Task} containing a list of {@link Job} objects posted by the employer.
     */
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

    /**
     * Updates the status of a specific job application for a user.
     *
     * @param employeeID The ID of the employee whose application status needs to be updated.
     * @param applicationId      The ID of the job being updated.
     * @param status     The new status of the job application (e.g., "Accepted", "Rejected").
     */
    public void changeStatusOfUserJobsApplied(String employeeID, String applicationId, String status) {
        employeeID = employeeID.replace(".", ",");
        DatabaseReference userJobsRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(employeeID)
                .child("appliedJobs")
                .child(applicationId)
                .child("Status");

        userJobsRef.setValue(status);
    }

    public void resetToPending(String jobID) {
        DatabaseReference jobRef = databaseReference.child(jobID);
        jobRef.child("status").setValue("pending");
        jobRef.child("employeeId").setValue("");
        jobRef.child("employeeName").setValue("");
    }


    /**
     * A callback interface for handling application counts and errors.
     */
    public interface OnApplicationCountListener {
        /**
         * Called when the application count is successfully retrieved.
         *
         * @param count The count of applications as a string.
         */
        void onCountRetrieved(String count);

        /**
         * Called when an error occurs while retrieving the application count.
         *
         * @param error The error message.
         */
        void onError(String error);
    }
}
