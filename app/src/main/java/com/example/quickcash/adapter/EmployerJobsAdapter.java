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

    public EmployerJobsAdapter(List<Job> jobList, DatabaseReference databaseReference) {
        this.jobList = jobList;
        this.databaseReference = databaseReference;
        fetchApplicationCounts(); // Fetch counts on initialization
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

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    private void fetchApplicationCounts() {
        for (Job job : jobList) {
            DatabaseReference applicationsRef = databaseReference.child("Jobs").child(job.getJobId()).child("Applications");
            applicationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    applicationCounts.put(job.getJobId(), (int) snapshot.getChildrenCount());
                    notifyDataSetChanged(); // Notify adapter to update the views
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    applicationCounts.put(job.getJobId(), 0);
                    notifyDataSetChanged(); // If error occurs, set count to 0
                }
            });
        }
    }

    public void setApplicationCounts(HashMap<String, Integer> applicationCounts) {
        this.applicationCounts = applicationCounts;
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

        public void bind(Job job, DatabaseReference databaseReference, int applicationCount) {
            jobTitleTextView.setText(job.getJobTitle());
            startDateTextView.setText("Start Date: " + job.getStartDate());
            urgencyTextView.setText("Urgency: " + job.getUrgency());
            noOfApplicationsTextView.setText("Applications: " + applicationCount);

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
