package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.R;
import com.example.quickcash.ui.models.Job;
import com.example.quickcash.repositories.FirebaseCRUD;
import com.example.quickcash.ui.utils.LocationHelper;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private FirebaseCRUD firebaseCRUD;
    private EditText locationInput;
    private Button showMapButton;
    private List<Job> jobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_search_parameter);

        // Initialize Firebase
        firebaseCRUD = new FirebaseCRUD(FirebaseDatabase.getInstance());
        jobList = new ArrayList<>();

        // Initialize views
        locationInput = findViewById(R.id.Location);
        showMapButton = findViewById(R.id.showMapButton);

        // Set up map button click listener
        showMapButton.setOnClickListener(v -> performLocationSearch());
    }

    private void performLocationSearch() {
        String locationQuery = locationInput.getText().toString().trim();

        if (locationQuery.isEmpty()) {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use firebaseCRUD to search for jobs
        firebaseCRUD.searchJobs(locationQuery, new FirebaseCRUD.JobDataCallback() {
            @Override
            public void onCallback(List<Job> jobs) {
                jobList = jobs;
                if (!jobs.isEmpty()) {
                    // Get coordinates for the entered location
                    LocationHelper.LocationResult locationResult = LocationHelper.getCoordinates(SearchActivity.this, locationQuery);

                    if (locationResult.isSuccess()) {
                        showJobsOnMap(locationResult.latitude, locationResult.longitude);
                    } else {
                        Toast.makeText(SearchActivity.this, "Could not find location: " + locationQuery, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "No jobs found in " + locationQuery, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(SearchActivity.this, "Error retrieving jobs: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showJobsOnMap(double centerLat, double centerLng) {
        Intent mapIntent = new Intent(this, MapActivity.class);

        ArrayList<Double> latitudes = new ArrayList<>();
        ArrayList<Double> longitudes = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<Integer> salaries = new ArrayList<>();
        ArrayList<String> durations = new ArrayList<>();
        ArrayList<String> companies = new ArrayList<>();

        // Add all jobs from the search results
        for (Job job : jobList) {
            LocationHelper.LocationResult jobLocation = LocationHelper.getCoordinates(this, job.getLocation());

            if (jobLocation.isSuccess()) {
                latitudes.add(jobLocation.latitude);
                longitudes.add(jobLocation.longitude);
                titles.add(job.getJobTitle());
                salaries.add(job.getSalary());
                durations.add(job.getExpectedDuration());
                companies.add(job.getCompanyName());
            }
        }

        // Add all data to intent
        mapIntent.putExtra("latitudes", latitudes);
        mapIntent.putExtra("longitudes", longitudes);
        mapIntent.putStringArrayListExtra("titles", titles);
        mapIntent.putIntegerArrayListExtra("salaries", salaries);
        mapIntent.putStringArrayListExtra("durations", durations);
        mapIntent.putStringArrayListExtra("companies", companies);

        // Add center location coordinates for map focusing
        mapIntent.putExtra("centerLat", centerLat);
        mapIntent.putExtra("centerLng", centerLng);

        startActivity(mapIntent);
    }
}
