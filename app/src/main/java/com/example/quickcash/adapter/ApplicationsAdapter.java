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

import com.example.quickcash.R;
import com.example.quickcash.model.Application;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ApplicationViewHolder> {

    private final List<Application> applicationList;
    private final String jobID;
    private final Context context;

    public ApplicationsAdapter(List<Application> applicationList, String jobID, Context context) {
        this.applicationList = applicationList;
        this.jobID = jobID;
        this.context = context;
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item, parent, false);
        return new ApplicationViewHolder(itemView);
    }

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

    private void updateApplicationStatus(Application application, String status, ApplicationViewHolder holder) {
        Toast.makeText(this.context, "Updating application status...", Toast.LENGTH_LONG).show();

        DatabaseReference applicationRef = FirebaseDatabase.getInstance()
                .getReference("Jobs")
                .child(jobID)
                .child("applications")
                .child(application.getApplicationId());

        applicationRef.child("Status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Application " + status, Toast.LENGTH_SHORT).show();
                    holder.statusTextView.setText(status); // Update the UI status
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error updating status", Toast.LENGTH_SHORT).show();
                });

        if(status.equals("Accepted")){
            closeJob(application.getEmployeeID(), application.getApplicantName());
        }

    }

    private void closeJob(String empID, String name) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Jobs")
                .child(jobID)
                .child("status");
        ref.setValue("In-progress");

        ref = FirebaseDatabase.getInstance()
                .getReference("Jobs")
                .child(jobID).child("employeeId");
        ref.setValue(empID);

        ref = FirebaseDatabase.getInstance()
                .getReference("Jobs")
                .child(jobID).child("employeeName");
        ref.setValue(name);

    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView emailTextView;
        private final TextView coverLetterTextView;
        private final TextView statusTextView;
        private final Button acceptButton;
        private final Button rejectButton;

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
