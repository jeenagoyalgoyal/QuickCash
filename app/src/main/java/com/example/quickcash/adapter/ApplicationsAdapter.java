package com.example.quickcash.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.R;
import com.example.quickcash.model.Application;

import java.util.List;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ApplicationViewHolder> {

    private final List<Application> applicationList;

    public ApplicationsAdapter(List<Application> applicationList) {
        this.applicationList = applicationList;
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
        holder.bind(application);
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        private final TextView applicantNameTextView;
        private final TextView applicantEmailTextView;
        private final TextView applicantMessageTextView;
        private final Button acceptButton;
        private final Button rejectButton;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantNameTextView = itemView.findViewById(R.id.applicantName);
            applicantEmailTextView = itemView.findViewById(R.id.applicantEmail);
            applicantMessageTextView = itemView.findViewById(R.id.applicantMessage);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }

        public void bind(Application application) {
            applicantNameTextView.setText(application.getApplicantName());
            applicantEmailTextView.setText(application.getApplicantEmail());
            applicantMessageTextView.setText(application.getApplicantMessage());

            // Handle accept/reject button clicks (similar to the logic in your main activity)
        }
    }
}
