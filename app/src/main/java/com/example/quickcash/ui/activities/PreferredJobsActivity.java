package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.R;
import com.example.quickcash.utils.adapter.PreferredJobAdapter;
import com.example.quickcash.models.Job;
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
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_jobs);

        initializeUI();
        String userId = getCurrentUserId();

        if (userId == null) {
            showToast("User ID is null or empty");
            return;
        }

        userId = sanitizeEmail(userId);
        setupFirebaseReference(userId);
        setupDataListener();
        setupBackButton();
    }

    private void initializeUI() {
        preferredJobRecyclerView = findViewById(R.id.recyclerView);
        preferredJobs = new ArrayList<>();
        jobAdapter = new PreferredJobAdapter(preferredJobs);
        preferredJobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        preferredJobRecyclerView.setAdapter(jobAdapter);
    }

    private String getCurrentUserId() {
        mAuth = FirebaseAuth.getInstance();
        return (mAuth.getCurrentUser() != null) ? mAuth.getCurrentUser().getEmail() : null;
    }

    private void setupFirebaseReference(String userId) {
        preferredJobsRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId)
                .child("preferredJobs");
    }

    private void setupDataListener() {
        preferredJobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                preferredJobs.clear();
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
                showToast("Failed to load preferred jobs.");
            }
        });
    }

    private void setupBackButton() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> navigateToHomepage());
    }

    private void navigateToHomepage() {
        startActivity(new Intent(this, EmployeeHomepageActivity.class));
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }
}
