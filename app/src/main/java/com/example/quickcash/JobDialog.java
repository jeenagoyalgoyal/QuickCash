package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickcash.R;
import com.example.quickcash.adapter.JobSearchAdapter;
import com.example.quickcash.EmployeeHomepageActivity;
import com.example.quickcash.EmployerHomepageActivity;
import com.example.quickcash.Firebase.JobCRUD;
import com.example.quickcash.model.Job;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class JobDialog extends AppCompatActivity {

    private RecyclerView itemsRecyclerView;
    private JobSearchAdapter jobAdapter;
    private String role;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_job);

        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        role = intent.getStringExtra("role");
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);

        loadJobsOrWorkers();
        navigateToDashboardAfterDelay();
    }

    private void loadJobsOrWorkers() {
        JobCRUD jobCrud = new JobCRUD(FirebaseDatabase.getInstance());
        if (role.equalsIgnoreCase("employee")) {
            // Load jobs in the user's area
            jobCrud.getJobsByLocation("user_city_based_on_lat_lng") // Replace with city lookup if needed
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<Job> jobs = task.getResult();
                            jobAdapter = new JobSearchAdapter(jobs);
                            itemsRecyclerView.setAdapter(jobAdapter);
                        }
                    });
        } else if (role.equalsIgnoreCase("employer")) {
            // Load workers for employer role (modify accordingly)
        }
    }

    private void navigateToDashboardAfterDelay() {
        new Handler().postDelayed(() -> {
            Intent dashboardIntent;
            if ("employee".equalsIgnoreCase(role)) {
                dashboardIntent = new Intent(JobDialog.this, EmployeeHomepageActivity.class);
            } else {
                dashboardIntent = new Intent(JobDialog.this, EmployerHomepageActivity.class);
            }
            startActivity(dashboardIntent);
            finish();
        }, 5000); // 5 seconds delay
    }
}
