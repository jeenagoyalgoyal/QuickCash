package com.example.quickcash;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.Firebase.EmployerCRUD;
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
    /**
     * Called when the activity is created.
     * Initializes the UI components and fetches job postings from Firebase.
     *
     * @param savedInstanceState a Bundle containing saved state if the activity is being recreated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_adapter_employer);
        this.mAuth = FirebaseAuth.getInstance();
        this.emailID = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
        Log.d(TAG, "Email: " + emailID);
        if (this.emailID != null) {
            this.emailID = this.emailID.replace(".", ",");
        } else {
            Toast.makeText(this, "Please Login properly.", Toast.LENGTH_LONG).show();
        }

        // Initialize the RecyclerView and the job list
        recyclerView = findViewById(R.id.employerJobPostingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobList = new ArrayList<>();
        adapter = new EmployerJobsAdapter(jobList);
        recyclerView.setAdapter(adapter);

        // Initialize the Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
        // Fetch job postings from Firebase
        fetchJobPostings();
    }
    /**
     * Fetches job postings from Firebase Realtime Database based on the employer's email ID.
     */
    private void fetchJobPostings() {
        EmployerCRUD employerCRUD = new EmployerCRUD();
        employerCRUD.getJobsByEmailID(emailID, this).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                jobList.clear(); // Clear the existing list
                jobList.addAll(task.getResult()); // Add the fetched jobs
                adapter.notifyDataSetChanged(); // Notify the adapter
                Log.d("Fetching, JobList Size", String.valueOf(jobList.size()));
            } else {
                Toast.makeText(this, "Failed to fetch job postings", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
