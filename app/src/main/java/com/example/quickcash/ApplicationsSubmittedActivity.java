package com.example.quickcash;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.adapter.ApplicationsAdapter;
import com.example.quickcash.model.Application;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.ArrayList;
import java.util.List;

public class ApplicationsSubmittedActivity extends AppCompatActivity {
    private final static String TAG = "Applications Page activity";
    private RecyclerView applicationsRecyclerView;
    private ApplicationsAdapter applicationsAdapter;
    private List<Application> applicationList;
    private DatabaseReference databaseReference;
    private String jobId;
    private TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications_submitted);


        // Get jobId and jobTitle passed from the Intent
        jobId = getIntent().getStringExtra("jobId");
        String jobTitle = getIntent().getStringExtra("jobTitle");

        // Initialize UI components
        pageTitle = findViewById(R.id.pageTitle);
        applicationsRecyclerView = findViewById(R.id.applicationsRecyclerView);

        // Set page title with job title
        pageTitle.setText("Applications for " + jobTitle);

        // Set up RecyclerView
        applicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        applicationList = new ArrayList<>();
        applicationsAdapter = new ApplicationsAdapter(applicationList, jobId, this);
        applicationsRecyclerView.setAdapter(applicationsAdapter);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Fetch applications for the selected job
        fetchApplicationsForJob(jobId);
    }

    private void fetchApplicationsForJob(String jobId) {
        Log.d(TAG, "jobID: " + jobId);
        DatabaseReference applicationsRef = databaseReference.child("Jobs").child(jobId).child("applications");

        applicationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                applicationList.clear();  // Clear the previous list of applications
                for (DataSnapshot applicationSnapshot : snapshot.getChildren()) {
                    // Extract application data, including applicationId
                    String applicantName = applicationSnapshot.child("Applicant Name").getValue(String.class);
                    Log.d(TAG, "Name: "+applicantName);
                    String applicantEmail = applicationSnapshot.child("Applicant Email").getValue(String.class);
                    Log.d(TAG, "Email: "+applicantEmail);
                    String applicantMessage = applicationSnapshot.child("Cover Letter").getValue(String.class);  // Cover Letter mapped to applicantMessage
                    Log.d(TAG, "Message: "+ applicantMessage);
                    String status = applicationSnapshot.child("Status").getValue(String.class);
                    String applicationId = applicationSnapshot.getKey();  // Firebase automatically assigns the key for each application

                    // Create the application object and set the applicationId
                    Application application = new Application(applicantName, applicantEmail, applicantMessage, status, applicationId);

                    // Add the application to the list
                    applicationList.add(application);
                }
                // Notify the adapter to refresh the view
                applicationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ApplicationsSubmittedActivity.this, "Failed to load applications", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
