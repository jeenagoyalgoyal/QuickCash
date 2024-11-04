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

    // RecyclerView to display the list of preferred jobs
    private RecyclerView preferredJobRecyclerView;
    // Adapter for binding job data to the RecyclerView
    private PreferredJobAdapter jobAdapter;
    // List to hold preferred job objects
    private ArrayList<Job> preferredJobs;
    // Reference to the Firebase database for preferred jobs
    private DatabaseReference preferredJobsRef;
    // Firebase authentication instance for user management
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the preferred jobs activity layout
        setContentView(R.layout.activity_preferred_jobs);

        // Initialize UI components and set up data
        initializeUI();
        String userId = getCurrentUserId();

        // Check if the user ID is valid
        if (userId == null) {
            showToast("User ID is null or empty");
            return; // Exit if user ID is not available
        }

        // Sanitize the user ID for use in Firebase database reference
        userId = sanitizeEmail(userId);
        // Set up Firebase reference to the user's preferred jobs
        setupFirebaseReference(userId);
        // Set up a listener to retrieve preferred jobs data
        setupDataListener();
        // Set up the back button functionality
        setupBackButton();
    }

    /**
     * Initializes the UI components for the activity.
     * Sets up the RecyclerView and its adapter.
     */
    private void initializeUI() {
        // Find the RecyclerView in the layout
        preferredJobRecyclerView = findViewById(R.id.recyclerView);
        // Initialize the list to hold preferred jobs
        preferredJobs = new ArrayList<>();
        // Create an adapter for the RecyclerView
        jobAdapter = new PreferredJobAdapter(preferredJobs);
        // Set the layout manager for the RecyclerView
        preferredJobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set the adapter to the RecyclerView
        preferredJobRecyclerView.setAdapter(jobAdapter);
    }

    /**
     * Retrieves the current user's ID from Firebase Authentication.
     *
     * @return The user's email if authenticated, otherwise null.
     */
    private String getCurrentUserId() {
        mAuth = FirebaseAuth.getInstance();
        // Return the current user's email or null if not authenticated
        return (mAuth.getCurrentUser() != null) ? mAuth.getCurrentUser().getEmail() : null;
    }

    /**
     * Sets up the Firebase database reference for the user's preferred jobs.
     *
     * @param userId The sanitized user ID to reference in the database.
     */
    private void setupFirebaseReference(String userId) {
        preferredJobsRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId)
                .child("preferredJobs");
    }

    /**
     * Sets up a listener to retrieve preferred jobs data from Firebase.
     * Updates the RecyclerView when data changes.
     */
    private void setupDataListener() {
        preferredJobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the current list of preferred jobs
                preferredJobs.clear();
                // Iterate through the retrieved job data
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    // Convert the snapshot to a Job object
                    Job job = jobSnapshot.getValue(Job.class);
                    // Add the job to the list if not null
                    if (job != null) {
                        preferredJobs.add(job);
                    }
                }
                // Notify the adapter that the data has changed
                jobAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Show a toast message if data retrieval fails
                showToast("Failed to load preferred jobs.");
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

    /**
     * Displays a toast message to the user.
     *
     * @param message The message to display in the toast.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Sanitizes the user's email by replacing periods with commas.
     *
     *   * @param email The email address to sanitize.
     *      * @return The sanitized email address with periods replaced by commas.
     *      */

    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }
}


