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

import java.util.List;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ApplicationViewHolder> {

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
        holder.bind(application, jobID, context);
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {

        private final TextView applicantName;
        private final TextView applicantEmail;
        private final TextView applicantMessage;
        private final Button acceptButton;
        private final Button rejectButton;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantName = itemView.findViewById(R.id.applicantName);
            applicantEmail = itemView.findViewById(R.id.applicantEmail);
            applicantMessage = itemView.findViewById(R.id.applicantMessage);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }

        public void bind(Application application, String jobID, Context context) {
            applicantName.setText("Name: " + application.getApplicantName());
            applicantEmail.setText("Email: " + application.getApplicantEmail());
            applicantMessage.setText("Message: " + application.getApplicantMessage());

            // Get a reference to the application in the database
            DatabaseReference applicationRef = FirebaseDatabase.getInstance()
                    .getReference("Jobs")
                    .child(jobID)
                    .child("Applications")
                    .child(application.getApplicationId());

            // Handle accept button click
            acceptButton.setOnClickListener(view -> {
                applicationRef.child("status").setValue("Accepted");
                handleJobListing();
                notifyEmployerAndEmployee(true, context);
            });

            // Handle reject button click
            rejectButton.setOnClickListener(view -> {
                applicationRef.child("status").setValue("Rejected");
                notifyEmployerAndEmployee(false, context);
            });
        }

        private void handleJobListing() {
            //Make sure job is closed so that no one else can apply
            //Probably setting status to "Closed"
        }

        private void notifyEmployerAndEmployee(Boolean accepted, Context context) {

            if(accepted){
                //Do something for when employee accepted
                Toast.makeText(context, "Accepted!", Toast.LENGTH_LONG).show();

            } else {
                //Do something for when employee rejected
                Toast.makeText(context, "Unfortunately, Rejected!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
