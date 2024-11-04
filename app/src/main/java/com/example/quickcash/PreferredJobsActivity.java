/**
 * PreferredJobsActivity is an Android activity that displays a list of preferred jobs for the currently authenticated user.
 * It retrieves the user's preferred jobs from a Firebase Realtime Database and presents them in a RecyclerView.
 *
 * <p>This activity is responsible for the following functionalities:</p>
 * <ul>
 *     <li>Initializing the user interface components, including the RecyclerView for displaying jobs.</li>
 *     <li>Fetching the current user's ID from Firebase Authentication.</li>
 *     <li>Setting up a reference to the Firebase database to access the preferred jobs associated with the user.</li>
 *     <li>Listening for changes in the preferred jobs data and updating the RecyclerView accordingly.</li>
 *     <li>Handling navigation back to the homepage when the back button is pressed.</li>
 *     <li>Displaying toast messages for user feedback, such as errors in loading data.</li>
 * </ul>
 *
 * <p>Key components of this activity include:</p>
 * <ul>
 *     <li>{@link RecyclerView} - Displays the list of preferred jobs using a custom adapter.</li>
 *     <li>{@link PreferredJobAdapter} - Adapter that binds the job data to the RecyclerView.</li>
 *     <li>{@link FirebaseAuth} - Used to authenticate the user and retrieve their email.</li>
 *     <li>{@link DatabaseReference} - Reference to the Firebase database where the preferred jobs are stored.</li>
 * </ul>
 *
 * <p>Lifecycle:</p>
 * <ul>
 *     <li>onCreate(Bundle savedInstanceState) - Initializes the activity, sets up the UI, and starts data retrieval.</li>
 * </ul>
 *
 * <p>Note:</p>
 * <ul>
 *     <li>The user ID is sanitized by replacing periods with commas to ensure compatibility with Firebase database paths.</li>
 *     <li>Data retrieval is performed asynchronously, and the UI is updated in response to data changes.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>This activity should be launched when the user wants to view their preferred job listings. It is typically accessed from the main application interface.</p>
 *
 * @see PreferredJobAdapter
 * @see Job
 * @see FirebaseAuth
 * @see DatabaseReference
 */



package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
