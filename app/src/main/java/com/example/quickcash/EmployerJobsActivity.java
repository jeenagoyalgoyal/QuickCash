package com.example.quickcash;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.adapter.EmployerJobsAdapter;
import com.example.quickcash.model.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmployerJobsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmployerJobsAdapter adapter;
    private List<Job> jobList;
    private DatabaseReference databaseReference;
    private String emailID, userID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_adapter_employer);
        this.mAuth = FirebaseAuth.getInstance();
        this.emailID = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;

        if (emailID != null) {
            this.userID = emailID.replace(".", ",");
        } else {
            Toast.makeText(this, "Please login to the app before continuing", Toast.LENGTH_LONG).show();
        }

        // Initialize the RecyclerView and the job list
        recyclerView = findViewById(R.id.employerJobPostingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobList = new ArrayList<>();
        adapter = new EmployerJobsAdapter(jobList, FirebaseDatabase.getInstance().getReference());
        recyclerView.setAdapter(adapter);

        // Initialize the Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Fetch job postings from Firebase
        fetchJobPostings();
    }

    private void fetchJobPostings() {

        databaseReference.child("Jobs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                HashMap<String, Integer> applicationCounts = new HashMap<>();

                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);
                    if (job != null) {
                        String jobId = jobSnapshot.getKey();
                        if (job.getEmployerId().equals(userID)) {
                            job.setJobId(jobId);
                            jobList.add(job);

                            // Fetch number of applications for this job
                            DatabaseReference applicationsRef = jobSnapshot.child("Applications").getRef();
                            applicationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot applicationsSnapshot) {
                                    applicationCounts.put(jobId, (int) applicationsSnapshot.getChildrenCount());
                                    adapter.setApplicationCounts(applicationCounts); // Pass updated counts to the adapter
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(EmployerJobsActivity.this, "Failed to load applications", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployerJobsActivity.this, "Failed to load job postings", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
