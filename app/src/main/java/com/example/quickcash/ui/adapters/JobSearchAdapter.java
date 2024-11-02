package com.example.quickcash.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.ui.activities.MapActivity;
import com.example.quickcash.models.Job;
import com.example.quickcash.R;
import com.example.quickcash.ui.utils.LocationHelper;

import java.util.List;
import java.util.ArrayList;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_search_result_view, parent, false);
        return new JobViewHolder(v);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        Job job = jobList.get(position);

        // Set the job details
        holder.jobTypeResult.setText("Job Title: " + job.getJobTitle());
        holder.companyResult.setText("Company: " + job.getCompanyName());
        holder.locationResult.setText("Location: " + job.getLocation());
        holder.salaryResult.setText("Salary: $" + String.format("%,d", job.getSalary()));
        holder.durationResult.setText("Duration: " + job.getExpectedDuration());

        // Set up map button click handler
        holder.showMapButton.setOnClickListener(view -> {
            Intent mapIntent = new Intent(context, MapActivity.class);

            // Create lists for map data
            ArrayList<Double> latitudes = new ArrayList<>();
            ArrayList<Double> longitudes = new ArrayList<>();
            ArrayList<String> titles = new ArrayList<>();
            ArrayList<Integer> salaries = new ArrayList<>();
            ArrayList<String> durations = new ArrayList<>();
            ArrayList<String> companies = new ArrayList<>();

            // Get location coordinates
            LocationHelper.LocationResult locationResult = LocationHelper.getCoordinates(context, job.getLocation());

            // Add job data to lists
            latitudes.add(locationResult.latitude);
            longitudes.add(locationResult.longitude);
            titles.add(job.getJobTitle());
            salaries.add(job.getSalary());
            durations.add(job.getExpectedDuration());
            companies.add(job.getCompanyName());

            // Add data to intent
            mapIntent.putExtra("latitudes", latitudes);
            mapIntent.putExtra("longitudes", longitudes);
            mapIntent.putIntegerArrayListExtra("salaries", salaries);
            mapIntent.putStringArrayListExtra("titles", titles);
            mapIntent.putStringArrayListExtra("durations", durations);
            mapIntent.putStringArrayListExtra("companies", companies);

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