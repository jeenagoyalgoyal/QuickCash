package com.example.quickcash.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.PopupMenu;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.LoginActivity;
import com.example.quickcash.PreferredEmployersActivity;
import com.example.quickcash.RegistrationActivity;
import com.example.quickcash.model.Job;
import com.example.quickcash.R;

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
        public Button optionsButton;
        public LinearLayout jobSearchLinearLayout;

        public JobViewHolder(View itemView) {
            super(itemView);
            jobTypeResult = itemView.findViewById(R.id.jobTypeResult);
            companyResult = itemView.findViewById(R.id.companyResult);
            locationResult = itemView.findViewById(R.id.locationResult);
            salaryResult = itemView.findViewById(R.id.salaryResult);
            durationResult = itemView.findViewById(R.id.durationResult);
            showMapButton = itemView.findViewById(R.id.showMap);
            optionsButton = itemView.findViewById(R.id.optionsButton);
            jobSearchLinearLayout = itemView.findViewById(R.id.jobSearchLinearLayout);
        }
    }

    public JobSearchAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent=parent;
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

        holder.optionsButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(parent.getContext(),holder.optionsButton);
            popupMenu.getMenuInflater().inflate(R.menu.job_search_options,popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //set conditions for when different options are pressed
                    if (item.getTitle().equals("Add to Preferred Employers")){
                        Toast.makeText(parent.getContext(), "preferred employee added!", Toast.LENGTH_LONG).show();
                    }
                    return false;
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }
}
