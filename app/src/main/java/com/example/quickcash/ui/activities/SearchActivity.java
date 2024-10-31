package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.R;
import com.example.quickcash.ui.models.Job;
import com.example.quickcash.ui.repositories.FirebaseCRUD;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private FirebaseCRUD firebaseCRUD;
    private List<Job> jobList;
    private Button showMapButton;

    private EditText jobTitle;
    private EditText minSalary;
    private EditText maxSalary;
    private EditText duration;
    private EditText location;

    private String locationQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Initialize Firebase
        firebaseCRUD = new FirebaseCRUD(FirebaseDatabase.getInstance());

        // Initialize fields
        jobTitle = findViewById(R.id.JobTitle);
        minSalary = findViewById(R.id.MinSalary);
        maxSalary = findViewById(R.id.MaxSalary);
        duration = findViewById(R.id.Duration);
        location = findViewById(R.id.Location);

        // Initialize Show Map Button
        showMapButton = findViewById(R.id.showMapButton);

        jobList = new ArrayList<>();

        // Set up click listener for Show Map button
        showMapButton.setOnClickListener(v -> {
            locationQuery = location.getText().toString().trim();
            if (!locationQuery.isEmpty()) {
                performJobSearch(locationQuery);
            } else {
                Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performJobSearch(String query) {
        firebaseCRUD.searchJobs(query, jobs -> {
            jobList.clear();
            jobList.addAll(jobs);
            if (!jobList.isEmpty()) {
                showMapWithJobs();
            } else {
                Toast.makeText(this, "No jobs found for " + query, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMapWithJobs() {
        Intent mapIntent = new Intent(this, MapActivity.class);
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> salaries = new ArrayList<>();
        ArrayList<String> durations = new ArrayList<>();
        ArrayList<Double> latitudes = new ArrayList<>();
        ArrayList<Double> longitudes = new ArrayList<>();

        for (Job job : jobList) {
            titles.add(job.getJobTitle());
            salaries.add(String.valueOf(job.getSalary()));
            durations.add(job.getDuration());
            latitudes.add(job.getLatitude());
            longitudes.add(job.getLongitude());
        }

        mapIntent.putStringArrayListExtra("titles", titles);
        mapIntent.putStringArrayListExtra("salaries", salaries);
        mapIntent.putStringArrayListExtra("durations", durations);
        mapIntent.putExtra("latitudes", latitudes);
        mapIntent.putExtra("longitudes", longitudes);

        startActivity(mapIntent);
    }
}
