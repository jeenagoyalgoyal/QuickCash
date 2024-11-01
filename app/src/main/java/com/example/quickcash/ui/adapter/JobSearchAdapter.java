package com.example.quickcash.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.ui.models.Job;
import com.example.quickcash.R;

import java.util.List;

public class JobSearchAdapter extends RecyclerView.Adapter<JobSearchAdapter.JobViewHolder> {
    private List<Job> jobList;

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        public TextView jobTypeResult;
        public TextView companyResult;
        public TextView locationResult;
        public TextView salaryResult;
        public TextView durationResult;
        public Button showMapButton;

        public JobViewHolder(View itemView) {
            super(itemView);
            jobTypeResult = itemView.findViewById(R.id.jobTypeResult);
            companyResult = itemView.findViewById(R.id.companyResult);
            locationResult = itemView.findViewById(R.id.locationResult);
            salaryResult = itemView.findViewById(R.id.salaryResult);
            durationResult = itemView.findViewById(R.id.durationResult);
            showMapButton = itemView.findViewById(R.id.showMap);
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
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }
}
