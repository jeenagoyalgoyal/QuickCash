package com.example.quickcash.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.ApplicationsSubmittedActivity;
import com.example.quickcash.Firebase.EmployerCRUD;
import com.example.quickcash.R;
import com.example.quickcash.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
/**
 * Adapter class for displaying the list of jobs posted by the employer in a RecyclerView.
 * This class allows the employer to view job details and see the number of applications for each job.
 */
public class EmployerJobsAdapter extends RecyclerView.Adapter<EmployerJobsAdapter.JobViewHolder> {
    private final static String TAG = "EmployerJobsAdapter";
    private final List<Job> jobList;
    private HashMap<String, Integer> applicationCounts = new HashMap<>();
    /**
     * Constructor to initialize the adapter with the list of jobs.
     *
     * @param jobList the list of jobs posted by the employer
     */
    public EmployerJobsAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }
    /**
     * Creates a new view holder for a job item.
     *
     * @param parent the parent view group to hold the item
     * @param viewType the type of the view
     * @return the new {@link JobViewHolder} for the job item
     */
    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_posted_jobs, parent, false);
        return new JobViewHolder(itemView);
    }
    /**
     * Binds the data from the job list to the view holder.
     *
     * @param holder the view holder to bind the data to
     * @param position the position of the job in the list
     */
    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        EmployerCRUD emCRUD = new EmployerCRUD();
        Log.d(TAG, "JOB: "+job.getEmployerId());
        emCRUD.getApplicationsByJob(job, new EmployerCRUD.OnApplicationCountListener() {
            @Override
            public void onCountRetrieved(String applicationCount) {
                Log.d(TAG, "Reached"+ applicationCount);
                holder.bind(job, applicationCount); // Update UI with the retrieved count
            }

            @Override
            public void onError(String error) {
                System.err.println("Error retrieving application count: " + error);
            }
        });
    }

    /**
     * Returns the total number of jobs in the list.
     *
     * @return the size of the job list
     */
    @Override
    public int getItemCount() {
        return jobList.size();
    }
    /**
     * View holder class for holding references to the views for displaying job details.
     */
    public static class JobViewHolder extends RecyclerView.ViewHolder {
        private final TextView jobTitleTextView;
        private final TextView startDateTextView;
        private final TextView urgencyTextView;
        private final TextView noOfApplicationsTextView;
        private final Button viewApplicationsButton;
        /**
         * Constructor to initialize the view holder with references to the UI components.
         *
         * @param itemView the root view of the job item layout
         */
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.jobTitleTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            urgencyTextView = itemView.findViewById(R.id.urgencyTextView);
            noOfApplicationsTextView = itemView.findViewById(R.id.noOfApplicationsTextView);
            viewApplicationsButton = itemView.findViewById(R.id.viewApplicationsButton);
        }
        /**
         * Binds the job data and the application count to the views.
         *
         * @param job the job to display
         * @param applications the number of applications for the job
         */
        public void bind(Job job,String applications) {
            jobTitleTextView.setText(job.getJobTitle());
            startDateTextView.setText("Start Date: " + job.getStartDate());
            urgencyTextView.setText("Urgency: " + job.getUrgency());
            Log.d("BIND", "bind " + applications);
            noOfApplicationsTextView.setText("Applications: " + applications);

            // Handle "View Applications" button click
            viewApplicationsButton.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), ApplicationsSubmittedActivity.class);
                intent.putExtra("jobId", job.getJobId());
                intent.putExtra("jobTitle", job.getJobTitle());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
