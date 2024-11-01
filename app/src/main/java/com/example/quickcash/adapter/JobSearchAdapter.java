package com.example.quickcash.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.FirebaseCRUD;
import com.example.quickcash.model.Job;
import com.example.quickcash.R;
import com.google.firebase.auth.FirebaseAuth;
import com.example.quickcash.PreferredJobsActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JobSearchAdapter extends RecyclerView.Adapter<JobSearchAdapter.JobViewHolder> {
    private List<Job> jobList;
    private DatabaseReference preferredJobsRef;

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        public TextView jobTypeResult;
        public TextView companyResult;
        public TextView locationResult;
        public TextView salaryResult;
        public TextView durationResult;
        public Button showMapButton;
        public Button addToPreferredButton;

        public JobViewHolder(View itemView) {
            super(itemView);
            jobTypeResult = itemView.findViewById(R.id.jobTypeResult);
            companyResult = itemView.findViewById(R.id.companyResult);
            locationResult = itemView.findViewById(R.id.locationResult);
            salaryResult = itemView.findViewById(R.id.salaryResult);
            durationResult = itemView.findViewById(R.id.durationResult);
            showMapButton = itemView.findViewById(R.id.showMap);
            addToPreferredButton = itemView.findViewById(R.id.btnAddToPreferred);
        }
    }

    public JobSearchAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_search_result_view, parent, false);
        JobViewHolder vh = new JobViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.jobTypeResult.setText("Job Title: " + job.getJobTitle());
        holder.companyResult.setText("Company: " + job.getCompanyName());
        holder.locationResult.setText("Location: " + job.getLocation());
        holder.salaryResult.setText("Salary: $" + job.getSalary());
        holder.durationResult.setText("Duration: " + job.getExpectedDuration());

        holder.showMapButton.setOnClickListener(view -> {
            // Implement map functionality here
        });

        // Pass the context from the holder's itemView
        holder.addToPreferredButton.setOnClickListener(view -> {
            addJobToPreferredList(job, holder.itemView.getContext());
        });

    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public void addJobToPreferredList(Job job, Context context) {
        // Ensure the userId is correctly initialized from FirebaseAuth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;

        if (userId == null || userId.isEmpty()) {
            Log.e("AddJobError", "User ID is null or empty");
            return;
        }

        userId = sanitizeEmail(userId); // Sanitize user ID if it's an email

        DatabaseReference preferredJobsRef = FirebaseDatabase.getInstance()
                .getReference("Users").child(userId).child("preferredJobs");

        // Check if preferred jobs already exist
        preferredJobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // If the preferred jobs node doesn't exist, create an empty list
                    preferredJobsRef.setValue(new ArrayList<Job>());
                }

                // Now, push the job to the preferred job list
                preferredJobsRef.push().setValue(job)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("AddJobSuccess", "Job added successfully");
                            Toast.makeText(context, "Job added to preferred list!", Toast.LENGTH_SHORT).show(); // Display toast
                        })
                        .addOnFailureListener(e -> Log.e("AddJobError", "Failed to add job", e));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("AddJobError", "Database error: " + databaseError.getMessage());
            }
        });
    }



    // Utility function to sanitize email for Firebase path
    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }

}
