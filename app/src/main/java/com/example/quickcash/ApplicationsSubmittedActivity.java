package com.example.quickcash;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class ApplicationsSubmittedActivity extends AppCompatActivity {

    private RecyclerView applicationsRecyclerView;
    private ApplicationsAdapter applicationsAdapter;
    private List<Application> applicationList;
    private DatabaseReference databaseReference;
    private String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_posted_jobs);

        // Get jobId passed from previous activity
        jobId = getIntent().getStringExtra("jobId");

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Set up RecyclerView
        //applicationsRecyclerView = findViewById(R.id.applicationsRecyclerView);
        applicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        applicationList = new ArrayList<>();
        applicationsAdapter = new ApplicationsAdapter(applicationList);
        applicationsRecyclerView.setAdapter(applicationsAdapter);

        // Fetch applications from Firebase for the given jobId
        fetchApplicationsForJob(jobId);
    }

    private void fetchApplicationsForJob(String jobId) {
        DatabaseReference applicationsRef = databaseReference.child("Jobs").child(jobId).child("Applications");
        applicationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                applicationList.clear();
                for (DataSnapshot applicationSnapshot : snapshot.getChildren()) {
                    Application application = applicationSnapshot.getValue(Application.class);
                    if (application != null) {
                        applicationList.add(application);
                    }
                }
                applicationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //Toast.makeText(ApplicationsSubmittedActivity.this, "Failed to load applications", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
