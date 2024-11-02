package com.example.quickcash.ui.utils.Adapter;

import android.content.Context;
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

            //Intent for location and details on map
            Intent intentToMapSingleJob = new Intent(this.context, MapActivity.class);

            //Get lat long
            LocationHelper.LocationResult lh = LocationHelper.getCoordinates(this.context, job.getLocation());

            //Map only reads ArrayLists of String from intent
            ArrayList<Double> latitudes = new ArrayList<>();
            ArrayList<Double> longitudes = new ArrayList<>();
            ArrayList<String> titles = new ArrayList<>();
            ArrayList<Integer> salaries = new ArrayList<>();
            ArrayList<String> durations = new ArrayList<>();
            ArrayList<String> companies = new ArrayList<>();
            ArrayList<String> jobTypes = new ArrayList<>();
            ArrayList<String> datesOfStart = new ArrayList<>();
            ArrayList<String> requirements = new ArrayList<>();


            latitudes.add(lh.latitude);
            longitudes.add(lh.longitude);
            titles.add(job.getJobTitle());
            salaries.add(job.getSalary());
            durations.add(job.getExpectedDuration());
            companies.add(job.getCompanyName());
            datesOfStart.add(job.getStartDate());
            requirements.add(job.getRequirements());
            jobTypes.add(job.getJobType());

            intentToMapSingleJob.putExtra("latitudes", latitudes);
            intentToMapSingleJob.putExtra("longitudes", longitudes);
            intentToMapSingleJob.putIntegerArrayListExtra("salaries", salaries);
            intentToMapSingleJob.putStringArrayListExtra("titles", titles);
            intentToMapSingleJob.putStringArrayListExtra("durations", durations);
            intentToMapSingleJob.putStringArrayListExtra("companies", companies);
            intentToMapSingleJob.putStringArrayListExtra("jobTypes", jobTypes);
            intentToMapSingleJob.putStringArrayListExtra("datesOfStart", datesOfStart);
            intentToMapSingleJob.putStringArrayListExtra("requirements", requirements);

            context.startActivity(intentToMapSingleJob);
        });
    }


    @Override
    public int getItemCount() {
        return jobList.size();
    }
}

