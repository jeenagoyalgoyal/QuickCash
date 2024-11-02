package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickcash.ui.models.JobToMap;
import com.example.quickcash.ui.utils.Validators.JobSearchValidator;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.R;
import com.example.quickcash.ui.utils.repositories.FirebaseCRUD;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private FirebaseCRUD firebaseCRUD;
    private List<JobToMap> jobList;
    private Button showMapButton;

    private TextView jobTitle;
    private TextView minSalary;
    private TextView maxSalary;
    private TextView duration;
    private TextView location;
    private Button searchButton;

    private String locationQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_search_parameter);

        // Initialize Firebase
        firebaseCRUD = new FirebaseCRUD(FirebaseDatabase.getInstance());

        // Initialize Show Map Button
        showMapButton = findViewById(R.id.showMapButton);

        boolean flag = false;

        jobTitle = findViewById(R.id.jobTitle);
        String jobTitleString = jobTitle.getText().toString().trim();
        minSalary = findViewById(R.id.minSalary);
        String minSalaryString = minSalary.getText().toString().trim();
        maxSalary = (findViewById(R.id.maxSalary));
        String maxSalaryString = maxSalary.getText().toString().trim();
        duration = findViewById(R.id.companyName);
        String durationString = duration.getText().toString().trim();
        location = findViewById(R.id.location);
        String locationString = location.getText().toString().trim();

        if (JobSearchValidator.isValidSalaryString(minSalaryString, maxSalaryString)) {
            flag = true;
        } else if (JobSearchValidator.isValidJobTitle(jobTitleString)) {
            flag = true;
        } else if (JobSearchValidator.isValidDuration(durationString)) {
            flag = true;
        } else if (JobSearchValidator.isValidLocation(locationString)) {
            flag = true;
        }

        if (flag) {
            showMapWithJobs();
        } else {
            Toast.makeText(SearchActivity.this, "All fields can't be empty!", Toast.LENGTH_LONG).show();
        }
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

        for (JobToMap job : jobList) {
            titles.add(job.getJobTitle());
            salaries.add(String.valueOf(job.getSalary()));
            durations.add(job.getExpectedDuration());
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
