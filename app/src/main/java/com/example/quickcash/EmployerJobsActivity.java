package com.example.quickcash;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
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
    private final static String TAG = "EmployerJobsActivity";
    private RecyclerView recyclerView;
    private ImageButton backButton;
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
        Log.d(TAG, "EMail: "+emailID);
        if(this.emailID!=null){
            this.emailID = this.emailID.replace(".", ",");
        } else{
            Toast.makeText(this, "Please Login properly.", Toast.LENGTH_LONG).show();
        }

        // Initialize the RecyclerView and the job list
        recyclerView = findViewById(R.id.employerJobPostingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobList = new ArrayList<>();
        adapter = new EmployerJobsAdapter(jobList, FirebaseDatabase.getInstance().getReference());
        recyclerView.setAdapter(adapter);

        // Initialize the Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
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
                    if(job.getEmployerId().equals(emailID)){
                        if (job != null) {
                            String jobId = jobSnapshot.getKey();
                            job.setJobId(jobId);
                            jobList.add(job); // Add job to jobList

                            // Fetch number of applications for this job
                            DatabaseReference applicationsRef = jobSnapshot.child("Applications").getRef();
                            applicationsRef.addValueEventListener(new ValueEventListener() {
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
                adapter.notifyDataSetChanged(); // Notify adapter to display jobs after data has been fetched
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployerJobsActivity.this, "Failed to load job postings", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
