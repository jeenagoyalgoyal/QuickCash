package com.example.quickcash.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.R;
import com.example.quickcash.model.Job;
import com.example.quickcash.model.JobLocation;
import com.example.quickcash.MapActivity;

import java.util.ArrayList;
import java.util.List;

public class JobSearchAdapter extends RecyclerView.Adapter<JobSearchAdapter.JobViewHolder> {
    private List<Job> jobList;
    private Context context;

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

    public JobSearchAdapter(List<Job> jobList, Context context) {
        this.jobList = jobList;
        this.context = context;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_search_result_view, parent, false);
        return new JobViewHolder(v);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        Job job = jobList.get(position);

        // Set the job details
        holder.jobTypeResult.setText("Job Title: " + job.getJobTitle());
        holder.companyResult.setText("Company: " + job.getCompanyName());

        // Get location using the helper method from Job class
        JobLocation jobLocation = job.getJobLocation();
        if (jobLocation != null) {
            holder.locationResult.setText("Location: " + jobLocation.getAddress());
        } else {
            holder.locationResult.setText("Location: Not specified");
        }

        holder.salaryResult.setText("Salary: $" + String.format("%,d", job.getSalary()));
        holder.durationResult.setText("Duration: " + job.getExpectedDuration());

        // Set up map button click handler
        holder.showMapButton.setOnClickListener(view -> {
            JobLocation location = job.getJobLocation();
            if (location == null) {
                Toast.makeText(context, "No location data available for this job", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent mapIntent = new Intent(context, MapActivity.class);

            // Create lists for map data
            ArrayList<Double> latitudes = new ArrayList<>();
            ArrayList<Double> longitudes = new ArrayList<>();
            ArrayList<String> titles = new ArrayList<>();
            ArrayList<Integer> salaries = new ArrayList<>();
            ArrayList<String> durations = new ArrayList<>();
            ArrayList<String> companies = new ArrayList<>();
            ArrayList<String> jobTypes = new ArrayList<>();

            // Add job data to lists
            latitudes.add(location.getLat());
            longitudes.add(location.getLng());
            titles.add(job.getJobTitle());
            salaries.add(job.getSalary());
            durations.add(job.getExpectedDuration());
            companies.add(job.getCompanyName());
            jobTypes.add(job.getJobType());

            // Add data to intent
            mapIntent.putExtra("latitudes", latitudes);
            mapIntent.putExtra("longitudes", longitudes);
            mapIntent.putIntegerArrayListExtra("salaries", salaries);
            mapIntent.putStringArrayListExtra("titles", titles);
            mapIntent.putStringArrayListExtra("durations", durations);
            mapIntent.putStringArrayListExtra("companies", companies);
            mapIntent.putStringArrayListExtra("jobTypes", jobTypes);

            context.startActivity(mapIntent);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public void updateJobs(List<Job> newJobs) {
        this.jobList.clear();
        this.jobList.addAll(newJobs);
        notifyDataSetChanged();
    }
}
