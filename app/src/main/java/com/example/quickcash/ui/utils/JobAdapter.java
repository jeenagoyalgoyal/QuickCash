package com.example.quickcash.ui.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quickcash.R;
import com.example.quickcash.ui.models.JobToMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private List<JobToMap> jobList;

    public JobAdapter(List<JobToMap> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobToMap job = jobList.get(position);
        holder.jobTitle.setText(job.getJobTitle());
        holder.salary.setText(String.format("$%.2f", job.getSalary()));
        holder.duration.setText(job.getDuration());
        // Set other fields if needed
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, salary, duration;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            salary = itemView.findViewById(R.id.salary);
            duration = itemView.findViewById(R.id.duration);
            // Initialize other UI elements if present
        }
    }
}
