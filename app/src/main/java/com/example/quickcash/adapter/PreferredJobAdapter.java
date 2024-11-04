/**
 * PreferredJobAdapter is a RecyclerView adapter that binds a list of preferred job objects to the RecyclerView in the
 * PreferredJobsActivity. It is responsible for creating and managing the view holders that display job information
 * in a list format.
 *
 * <p>This adapter handles the following functionalities:</p>
 * <ul>
 *     <li>Inflating the layout for each item in the list of preferred jobs.</li>
 *     <li>Binding job data to the views within each item layout.</li>
 *     <li>Managing the visibility of additional job details and buttons for each job item.</li>
 * </ul>
 *
 * <p>Key components of this adapter include:</p>
 * <ul>
 *     <li>{@link PreferredJobViewHolder} - A static inner class that holds the views for each job item and binds the job data.</li>
 *     <li>{@link Job} - The model class representing a job, containing details such as job title, company name, location, salary, and expected duration.</li>
 * </ul>
 *
 * <p>Lifecycle:</p>
 * <ul>
 *     <li>onCreateViewHolder(ViewGroup parent, int viewType) - Creates a new view holder for a job item by inflating the item layout.</li>
 *     <li>onBindViewHolder(PreferredJobViewHolder holder, int position) - Binds the job data to the views in the view holder.</li>
 *     <li>getItemCount() - Returns the total number of job items in the list.</li>
 * </ul>
 *
 * <p>View Holder:</p>
 * <ul>
 *     <li>{@link PreferredJobViewHolder} contains references to the TextViews and Buttons used to display job details.</li>
 *     <li>It manages the visibility of additional job details and handles click events for showing and hiding job information.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>This adapter should be used in conjunction with a RecyclerView to display a list of preferred jobs. It is initialized
 * with a list of Job objects and is responsible for rendering each job in the list.</p>
 *
 */

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
