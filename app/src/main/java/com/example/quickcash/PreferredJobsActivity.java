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
import com.example.quickcash.adapter.PreferredJobAdapter;
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
    private PreferredJobAdapter jobAdapter;
    private ArrayList<Job> preferredJobs;
    private DatabaseReference preferredJobsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_jobs);

        preferredJobRecyclerView = findViewById(R.id.recyclerView);
        preferredJobs = new ArrayList<>();
        jobAdapter = new PreferredJobAdapter(preferredJobs);

        preferredJobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        preferredJobRecyclerView.setAdapter(jobAdapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "User ID is null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        userId = sanitizeEmail(userId); // Sanitize user ID for Firebase

        preferredJobsRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("preferredJobs");

        // Attach a listener to read data
        preferredJobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                preferredJobs.clear(); // Clear the list before adding new data
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);
                    if (job != null) {
                        preferredJobs.add(job);
                    }
                }
                jobAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PreferredJobsActivity.this, "Failed to load preferred jobs.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PreferredJobsActivity.this, EmployeeHomepageActivity.class);
            startActivity(intent);
            finish(); // Optional: Call finish() if you don't want to keep the  in the back stack
        });
    }

    // Utility function to sanitize email for Firebase path
    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }
}
