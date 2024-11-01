package com.example.quickcash.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickcash.model.Job;
import com.example.quickcash.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class PreferredJobAdapter extends RecyclerView.Adapter<PreferredJobAdapter.PreferredJobViewHolder> {
    private List<Job> preferredJobList;
    private DatabaseReference preferredJobsRef;

    public static class PreferredJobViewHolder extends RecyclerView.ViewHolder {
        public TextView jobTypeResult;
        public TextView companyResult;
        public TextView locationResult;
        public TextView salaryResult;
        public TextView durationResult;
        public Button showMapButton;
        public Button showViewButton;
        public Button cancelButton;

        public PreferredJobViewHolder(View itemView) {
            super(itemView);
            jobTypeResult = itemView.findViewById(R.id.jobTypeResult);
            companyResult = itemView.findViewById(R.id.companyResult);
            locationResult = itemView.findViewById(R.id.locationResult);
            salaryResult = itemView.findViewById(R.id.salaryResult);
            durationResult = itemView.findViewById(R.id.durationResult);
            showMapButton = itemView.findViewById(R.id.showMap);
            showViewButton = itemView.findViewById(R.id.ViewButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);

            // Initially hide the additional fields
            companyResult.setVisibility(View.GONE);
            locationResult.setVisibility(View.GONE);
            salaryResult.setVisibility(View.GONE);
            durationResult.setVisibility(View.GONE);

        }
    }

    public PreferredJobAdapter(List<Job> preferredJobList) {
        this.preferredJobList = preferredJobList;
    }

    @Override
    public PreferredJobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.preferred_job_adapter, parent, false);
        PreferredJobViewHolder vh = new PreferredJobViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PreferredJobViewHolder holder, int position) {
        Job job = preferredJobList.get(position);
        holder.jobTypeResult.setText("Job Title: " + job.getJobTitle());


        // Pass the context from the holder's itemView
        holder.showViewButton.setOnClickListener(view -> {
            if (holder.companyResult.getVisibility() == View.GONE) {
                holder.companyResult.setText("Company: " + job.getCompanyName());
                holder.locationResult.setText("Location: " + job.getLocation());
                holder.salaryResult.setText("Salary: $" + job.getSalary());
                holder.durationResult.setText("Duration: " + job.getExpectedDuration());

                holder.companyResult.setVisibility(View.VISIBLE);
                holder.locationResult.setVisibility(View.VISIBLE);
                holder.salaryResult.setVisibility(View.VISIBLE);
                holder.durationResult.setVisibility(View.VISIBLE);
                // Hide the View button and show the Cancel button
                holder.showViewButton.setVisibility(View.GONE);
                holder.cancelButton.setVisibility(View.VISIBLE);
            } else {
                // Hide the additional fields if they are already visible
                holder.companyResult.setVisibility(View.GONE);
                holder.locationResult.setVisibility(View.GONE);
                holder.salaryResult.setVisibility(View.GONE);
                holder.durationResult.setVisibility(View.GONE);


            }
        });

        // Set up the Cancel button to hide additional fields
        holder.cancelButton.setOnClickListener(view -> {
            holder.companyResult.setVisibility(View.GONE);
            holder.locationResult.setVisibility(View.GONE);
            holder.salaryResult.setVisibility(View.GONE);
            holder.durationResult.setVisibility(View.GONE);

            // Show the View button and hide the Cancel button
            holder.showViewButton.setVisibility(View.VISIBLE);
            holder.cancelButton.setVisibility(View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return preferredJobList.size();
    }

}

