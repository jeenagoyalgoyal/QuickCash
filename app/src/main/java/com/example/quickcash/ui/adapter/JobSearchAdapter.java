package com.example.quickcash.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickcash.ui.activities.MapActivity;
import com.example.quickcash.ui.models.Job;
import com.example.quickcash.R;
import com.example.quickcash.ui.utils.LocationHelper;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;

public class JobSearchAdapter extends RecyclerView.Adapter<JobSearchAdapter.JobViewHolder> {
    private List<Job> jobList;
    private ViewGroup parent;

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
        this.parent = parent;
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
            Intent intentToMapSingleJob = new Intent(parent.getContext(), MapActivity.class);
            LocationHelper.LocationResult lh = LocationHelper.getCoordinates(parent.getContext(), job.getLocation());

            //Map only reads ArrayLists of String from intent
            ArrayList<String> latitudes = new ArrayList<>();
            ArrayList<String> longitudes = new ArrayList<>();
            ArrayList<String> titles = new ArrayList<>();
            ArrayList<String> salaries = new ArrayList<>();
            ArrayList<String> durations = new ArrayList<>();
            ArrayList<String> companies = new ArrayList<>();

            latitudes.add(String.valueOf(lh.latitude));
            longitudes.add(String.valueOf(lh.longitude));
            titles.add(job.getJobTitle());
            salaries.add(String.valueOf(job.getSalary()));
            durations.add(job.getExpectedDuration());
            companies.add(job.getCompanyName());

            intentToMapSingleJob.putStringArrayListExtra("latitudes", latitudes);
            intentToMapSingleJob.putStringArrayListExtra("longitudes", longitudes);
            intentToMapSingleJob.putStringArrayListExtra("titles", titles);
            intentToMapSingleJob.putStringArrayListExtra("salaries", salaries);
            intentToMapSingleJob.putStringArrayListExtra("durations", durations);
            intentToMapSingleJob.putStringArrayListExtra("companies", companies);
            parent.getContext().startActivity(intentToMapSingleJob);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }
}

