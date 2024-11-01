package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.adapter.JobSearchAdapter;
import com.example.quickcash.model.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PreferredJobsActivity extends AppCompatActivity {

    private RecyclerView preferredJobRecyclerView;
    private JobSearchAdapter jobAdapter;
    private ArrayList<Job> preferredJobs;
    private DatabaseReference preferredJobsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_jobs);

        // Initialize RecyclerView and its adapter
        preferredJobRecyclerView = findViewById(R.id.recyclerView);
        preferredJobs = new ArrayList<>();
        jobAdapter = new JobSearchAdapter(preferredJobs);

        // Set LayoutManager and Adapter for RecyclerView
        preferredJobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        preferredJobRecyclerView.setAdapter(jobAdapter);

        // Get current user ID from Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "User ID is null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sanitize the user ID for Firebase
        userId = sanitizeEmail(userId);

        // Reference to preferred jobs in Firebase
        preferredJobsRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("preferredJobs");

        // Attach a listener to read data
        preferredJobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                preferredJobs.clear(); // Clear the list before adding new data
                if (!dataSnapshot.exists()) {
                    Toast.makeText(PreferredJobsActivity.this, "No preferred jobs found.", Toast.LENGTH_SHORT).show();
                    return; // Exit early if there are no jobs
                }

                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class); // Map to Job object
                    if (job != null) {
                        preferredJobs.add(job); // Add job to list if it's not null
                    }
                }
                jobAdapter.notifyDataSetChanged(); // Notify adapter of data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PreferredJobsActivity.this, "Failed to load preferred jobs: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Back button functionality
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PreferredJobsActivity.this, EmployeeHomepageActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Utility function to sanitize email for Firebase path
    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }
}
