package com.example.quickcash;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class JobsActivity extends AppCompatActivity {

    private List<TempJob.Job> jobs; // Use TempJob.Job

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs); // Ensure this matches the layout file name

        ListView jobListView = findViewById(R.id.jobListView); // Ensure the ID matches the XML
        jobs = TempJob.getJobList(); // Fetch the list of jobs

        ArrayAdapter<TempJob.Job> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, jobs);
        jobListView.setAdapter(adapter);

        // Set up the back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(JobsActivity.this, RoleActivity.class);
            startActivity(intent);
            finish(); // Optional: Call finish() if you don't want to keep the JobsActivity in the back stack
        });

        jobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < jobs.size()) {
                    TempJob.Job selectedJob = jobs.get(position);
                    showAddPreferredJobDialog(selectedJob);
                } else {
                    Toast.makeText(JobsActivity.this, "Invalid job selection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showAddPreferredJobDialog(TempJob.Job job) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_add_preferred_job);

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Set the job description in the dialog
        dialog.setOnShowListener(dialogInterface -> {
            TextView jobDescriptionTextView = dialog.findViewById(R.id.jobDescriptionTextView);
            if (jobDescriptionTextView != null) {
                jobDescriptionTextView.setText("Add " + job.getJobTitle() + " at " + job.getEmployerName() + " to Preferred Jobs?");
            }

            // Handle the button click
            View addPreferredJobButton = dialog.findViewById(R.id.addPreferredJobButton);
            if (addPreferredJobButton != null) {
                addPreferredJobButton.setOnClickListener(v -> {
                    TempJob.addPreferredJob(job); // Add the job to the preferred list
                    dialog.dismiss(); // Dismiss the dialog
                    Toast.makeText(this, job.getJobTitle() + " added to Preferred Jobs!", Toast.LENGTH_SHORT).show();
                });
            }
        });

        dialog.show(); // Show the dialog
    }

}
