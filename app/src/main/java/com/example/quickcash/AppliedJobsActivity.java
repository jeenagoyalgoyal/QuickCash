package com.example.quickcash;
/**
 * AppliedJobsActivity
 *
 * This class represents an Android activity that displays the list of jobs a user has applied for.
 * It retrieves job data from a Firebase Realtime Database and shows it in a ListView.
 * The activity also provides a back button to navigate to the homepage.
 *
 * Key Features:
 * 1. **Firebase Integration**: Connects to Firebase to fetch and display applied job data for the current user.
 * 2. **UI Components**: Includes a ListView to display jobs and a ProgressBar to indicate loading.
 * 3. **Data Formatting**: Formats job details including job title, company name, and application status.
 * 4. **Navigation**: Provides a back button to return to the homepage.
 *
 * Main Components:
 * - **ListView (listViewAppliedJobs)**: Displays the list of applied jobs.
 * - **ProgressBar (progressBar)**: Shows loading indicator while data is being fetched.
 * - **DatabaseReference (userApplicationsRef)**: Points to the user's applied jobs node in Firebase.
 * - **ArrayList (jobList)**: Stores the list of job details.
 * - **ArrayAdapter (adapter)**: Manages the data display in the ListView.
 *
 * Key Methods:
 * - **onCreate()**: Initializes the UI components, sets up Firebase references, and triggers data fetching.
 * - **fetchAppliedJobs()**: Listens for changes in the Firebase database and updates the job list accordingly.
 * - **setupBackButton()**: Configures the back button to navigate to the homepage when clicked.
 * - **navigateToHomepage()**: Handles the navigation to the EmployeeHomepageActivity.
 *
 * Usage Flow:
 * 1. The activity starts by initializing UI components and Firebase references.
 * 2. It fetches the user's applied jobs from Firebase and displays them in a formatted ListView.
 * 3. The user can navigate back to the homepage using the back button.
 *
 * Error Handling:
 * - If the user's email is not found, an error message is shown, and the process stops.
 * - If there is an error fetching data from Firebase, an error message is displayed, and the progress bar is hidden.
 */

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
/**
 * Handles the display of jobs that the user has applied for, including job details and statuses.
 */
public class AppliedJobsActivity extends AppCompatActivity {

    private ListView listViewAppliedJobs;
    private ProgressBar progressBar;
    private DatabaseReference userApplicationsRef;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> jobList;
    /**
     * Initializes the activity, UI components, and Firebase references, and starts fetching applied jobs data.
     *
     * @param savedInstanceState If the activity is being re-initialized, this contains the saved state data.
     */
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
    /**
     * Fetches the list of jobs the user has applied for from the Firebase database and updates the ListView.
     */
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
                        String status = jobSnapshot.child("Status").getValue(String.class);

                        if (jobTitle != null && companyName != null) {
                            // Format the string to include job title, company name, and status on separate lines
                            String jobDetails = jobTitle + "\n" + companyName; // Job title and company name on separate lines
                            if (status != null) {
                                jobDetails += "\nStatus: " + status; // Add status on a new line
                            }
                            jobList.add(jobDetails);
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
     * Sets up the back button to navigate to the homepage when clicked.
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
