package com.example.quickcash.adapter;

import android.content.Intent;
import android.util.Log;
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
import com.google.firebase.database.FirebaseDatabase;
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
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("Jobs").child(job.getJobId()).child("applications");

        dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Count the number of child nodes
                String childCount = String.valueOf(dataSnapshot.getChildrenCount());
                holder.bind(job, childCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error: " + databaseError.getMessage());
            }
        });
        holder.bind(job, "0");


    }

    @Override
    public int getItemCount() {
        return jobList.size();
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

        public void bind(Job job,String applications) {
            jobTitleTextView.setText(job.getJobTitle());
            startDateTextView.setText("Start Date: " + job.getStartDate());
            urgencyTextView.setText("Urgency: " + job.getUrgency());
            Log.d("BIND", "bind " + applications);
            noOfApplicationsTextView.setText("Applications: " + applications);

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
