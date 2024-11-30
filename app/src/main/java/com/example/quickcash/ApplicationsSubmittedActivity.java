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
        DatabaseReference applicationsRef = databaseReference.child("Jobs").child(jobId).child("Applications");
        applicationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                applicationList.clear();
                for (DataSnapshot applicationSnapshot : snapshot.getChildren()) {
                    Application application = applicationSnapshot.getValue(Application.class);
                    if (application != null) {
                        // Set the applicationId manually from the key
                        application.setApplicationId(applicationSnapshot.getKey());
                        applicationList.add(application);
                    }
                }
                applicationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ApplicationsSubmittedActivity.this, "Failed to load applications", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
