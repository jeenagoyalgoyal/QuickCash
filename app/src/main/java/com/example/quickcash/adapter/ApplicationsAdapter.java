package com.example.quickcash.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.quickcash.Firebase.EmployerCRUD;
import com.example.quickcash.FirebaseMessaging.JobAcceptedNotification;
import com.example.quickcash.R;
import com.example.quickcash.model.Application;

import java.util.List;

/**
 * Adapter class to manage and display a list of job applications in a RecyclerView.
 * This class handles displaying application details and allows the employer to accept or reject applications.
 */
public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ApplicationViewHolder> {

    private final List<Application> applicationList;
    private final String jobID;
    private final Context context;
    private EmployerCRUD emCRUD;
    private JobAcceptedNotification jobAcceptedNotification;
    /**
     * Constructor to initialize the adapter with application list, job ID, and context.
     *
     * @param applicationList the list of applications to display
     * @param jobID the ID of the job to which the applications belong
     * @param context the context in which the adapter is used
     */
    public ApplicationsAdapter(List<Application> applicationList, String jobID, Context context) {
        this.applicationList = applicationList;
        this.jobID = jobID;
        this.context = context;
        emCRUD = new EmployerCRUD();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        jobAcceptedNotification = new JobAcceptedNotification(context, requestQueue);

    }
    /**
     * Creates a new view holder for an application item.
     *
     * @param parent the parent view group to hold the item
     * @param viewType the type of the view
     * @return the new {@link ApplicationViewHolder} for the application item
     */
    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item, parent, false);
        return new ApplicationViewHolder(itemView);
    }
    /**
     * Binds the data from the application list to the view holder.
     *
     * @param holder the view holder to bind the data to
     * @param position the position of the application in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
        Application application = applicationList.get(position);

        // Set the text of the views to the application data
        holder.nameTextView.setText(application.getApplicantName());
        holder.emailTextView.setText(application.getApplicantEmail());
        holder.coverLetterTextView.setText(application.getApplicantMessage());
        holder.statusTextView.setText(application.getStatus());

        // Set up the accept/reject buttons
        holder.acceptButton.setOnClickListener(view -> {
            updateApplicationStatus(application, "Accepted", holder);
        });

        holder.rejectButton.setOnClickListener(view -> {
            updateApplicationStatus(application, "Rejected", holder);
        });
    }
    /**
     * Updates the status of the application and performs relevant actions such as sending notifications
     * and closing the job listing if the application is accepted.
     *
     * @param application the application whose status is being updated
     * @param status the new status (either "Accepted" or "Rejected")
     * @param holder the view holder to update the status in the UI
     */
    private void updateApplicationStatus(Application application, String status, ApplicationViewHolder holder) {
        Toast.makeText(context, "Updating application status...", Toast.LENGTH_SHORT).show();

        // Update application status in the database
        emCRUD.changeApplicationStatusByJobId(jobID, application, status).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                holder.statusTextView.setText(status);
                Toast.makeText(context, "Application status: " + status, Toast.LENGTH_SHORT).show();

                if (status.equals("Accepted")) {
                    // Notify the employee
                    jobAcceptedNotification.sendJobNotificationToEmployee(application.getEmployeeID());

                    // Close the job listing(Right now we just handle it to still allow accept or reject)
                    emCRUD.closeJob(jobID, application.getEmployeeID(), application.getApplicantName());
                }
            } else {
                Toast.makeText(context, "Failed to update application status.", Toast.LENGTH_SHORT).show();
            }
        });

        // Update user's applied job status
        emCRUD.changeStatusOfUserJobsApplied(application.getEmployeeID(), application.getApplicationId(), status);
        if (status.equals("Accepted")) {
            ((Activity) context).finish();
        } else {
            emCRUD.resetToPending(jobID);
        }
    }
    /**
     * Returns the total number of applications in the list.
     *
     * @return the size of the application list
     */
    @Override
    public int getItemCount() {
        return applicationList.size();
    }
    /**
     * View holder class for holding references to the views for displaying application data.
     */
    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView emailTextView;
        private final TextView coverLetterTextView;
        private final TextView statusTextView;
        private final Button acceptButton;
        private final Button rejectButton;
        /**
         * Constructor to initialize the view holder with references to the UI components.
         *
         * @param itemView the root view of the application item layout
         */
        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.applicantName);
            emailTextView = itemView.findViewById(R.id.applicantEmail);
            coverLetterTextView = itemView.findViewById(R.id.applicantMessage);
            statusTextView = itemView.findViewById(R.id.statusTextView); // Ensure this TextView exists
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }
}
