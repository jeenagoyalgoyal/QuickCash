package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppliedJobsActivity extends AppCompatActivity {

    private ListView listViewAppliedJobs;
    private ProgressBar progressBar;
    private DatabaseReference userApplicationsRef;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> jobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_applied_jobs);

        // Initialize UI components
        listViewAppliedJobs = findViewById(R.id.listViewAppliedJobs);
        progressBar = findViewById(R.id.progressBar);

        // Show progress bar while data loads
        progressBar.setVisibility(View.VISIBLE);

        // Retrieve userEmail from the intent
        String userEmail = getIntent().getStringExtra("userEmail");
        if (userEmail != null) {
            userEmail = userEmail.replace(".", ","); // Sanitize email for Firebase
        } else {
            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        // Initialize Firebase reference using the sanitized email
        userApplicationsRef = FirebaseDatabase.getInstance().getReference("Users").child(userEmail).child("appliedJobs");

        // Initialize list and adapter
        jobList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, jobList);
        listViewAppliedJobs.setAdapter(adapter);

        // Fetch applied jobs from the database
        fetchAppliedJobs();
        // Set up the back button functionality
        setupBackButton();
    }

    private void fetchAppliedJobs() {
        userApplicationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                        // Fetch job details using the correct keys
                        String jobTitle = jobSnapshot.child("jobTitle").getValue(String.class);
                        String companyName = jobSnapshot.child("companyName").getValue(String.class);
                        if (jobTitle != null && companyName != null) {
                            jobList.add(jobTitle + " at " + companyName);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AppliedJobsActivity.this, "No jobs applied to yet", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AppliedJobsActivity", "Error fetching data", error.toException());
                Toast.makeText(AppliedJobsActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Sets up the back button to navigate to the homepage.
     */
    private void setupBackButton() {
        ImageButton backButton = findViewById(R.id.backButton);
        // Set an onClick listener to navigate to the homepage when clicked
        backButton.setOnClickListener(v -> navigateToHomepage());
    }

    /**
     * Navigates to the EmployeeHomepageActivity and finishes the current activity.
     */
    private void navigateToHomepage() {
        startActivity(new Intent(this, EmployeeHomepageActivity.class));
        finish(); // Close the current activity
    }
}
