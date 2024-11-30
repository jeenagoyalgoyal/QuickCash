package com.example.quickcash.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.ApplicationsSubmittedActivity;
import com.example.quickcash.R;
import com.example.quickcash.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class EmployerJobsAdapter extends RecyclerView.Adapter<EmployerJobsAdapter.JobViewHolder> {
    private final List<Job> jobList;
    private final DatabaseReference databaseReference;
    private HashMap<String, Integer> applicationCounts = new HashMap<>();

    /**
     * Constructor for EmployerJobsAdapter.
     *
     * @param jobList           List of Job objects to display.
     * @param databaseReference Reference to the Firebase Realtime Database.
     */
    public EmployerJobsAdapter(List<Job> jobList, DatabaseReference databaseReference) {
        this.jobList = jobList;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_posted_jobs, parent, false);
        return new JobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        int applicationCount = applicationCounts.getOrDefault(job.getJobId(), 0); // Get count from map
        holder.bind(job, databaseReference, applicationCount);
    }

    public void setApplicationCounts(HashMap<String, Integer> applicationCounts) {
        this.applicationCounts = applicationCounts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        private final TextView jobTitleTextView;
        private final TextView startDateTextView;
        private final TextView urgencyTextView;
        private final TextView noOfApplicationsTextView;
        private final Button viewApplicationsButton;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.jobTitleTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            urgencyTextView = itemView.findViewById(R.id.urgencyTextView);
            noOfApplicationsTextView = itemView.findViewById(R.id.noOfApplicationsTextView);
            viewApplicationsButton = itemView.findViewById(R.id.viewApplicationsButton);

        }

        /**
         * Binds job data to the views.
         *
         * @param job               The Job object containing the data.
         * @param databaseReference Reference to the Firebase Realtime Database.
         */
        public void bind(Job job, DatabaseReference databaseReference, int applicationCount) {
            jobTitleTextView.setText(job.getJobTitle());
            startDateTextView.setText("Start Date: " + job.getStartDate());
            urgencyTextView.setText("Urgency: " + job.getUrgency());
            noOfApplicationsTextView.setText("Applications: " + applicationCount);

            // Fetch and display the number of applications
            DatabaseReference applicationsRef = databaseReference.child("Jobs").child(job.getJobId()).child("Applications");
            applicationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int applicationCount = (int) snapshot.getChildrenCount();
                    noOfApplicationsTextView.setText("Applications: " + applicationCount);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    noOfApplicationsTextView.setText("Applications: N/A");
                }
            });

            // Handle "View Applications" button click
            viewApplicationsButton.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), ApplicationsSubmittedActivity.class);
                intent.putExtra("jobId", job.getJobId());
                intent.putExtra("jobTitle", job.getJobTitle());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}