package com.example.quickcash.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickcash.models.Job;
import com.example.quickcash.R;

import java.util.List;

public class PreferredJobAdapter extends RecyclerView.Adapter<PreferredJobAdapter.PreferredJobViewHolder> {

    private final List<Job> preferredJobList;

    public PreferredJobAdapter(List<Job> preferredJobList) {
        this.preferredJobList = preferredJobList;
    }

    @Override
    public PreferredJobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.preferred_job_adapter, parent, false);
        return new PreferredJobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PreferredJobViewHolder holder, int position) {
        Job job = preferredJobList.get(position);

        holder.bind(job);
    }

    @Override
    public int getItemCount() {
        return preferredJobList.size();
    }

    public static class PreferredJobViewHolder extends RecyclerView.ViewHolder {
        private final TextView jobTypeResult;
        private final TextView companyResult;
        private final TextView locationResult;
        private final TextView salaryResult;
        private final TextView durationResult;
        private final Button showMapButton;
        private final Button showViewButton;
        private final Button cancelButton;

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

            initializeView();
        }

        private void initializeView() {
            // Initially hide the additional fields and cancel button
            toggleDetailVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
        }

        private void toggleDetailVisibility(int visibility) {
            companyResult.setVisibility(visibility);
            locationResult.setVisibility(visibility);
            salaryResult.setVisibility(visibility);
            durationResult.setVisibility(visibility);
        }

        public void bind(Job job) {
            jobTypeResult.setText("Job Title: " + job.getJobTitle());

            showViewButton.setOnClickListener(view -> {
                if (companyResult.getVisibility() == View.GONE) {
                    showJobDetails(job);
                }
            });

            cancelButton.setOnClickListener(view -> hideJobDetails());
        }

        private void showJobDetails(Job job) {
            companyResult.setText("Company: " + job.getCompanyName());
            locationResult.setText("Location: " + job.getLocation());
            salaryResult.setText("Salary: $" + job.getSalary());
            durationResult.setText("Duration: " + job.getExpectedDuration());

            toggleDetailVisibility(View.VISIBLE);
            showViewButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.VISIBLE);
        }

        private void hideJobDetails() {
            toggleDetailVisibility(View.GONE);
            showViewButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.GONE);
        }
    }
}
