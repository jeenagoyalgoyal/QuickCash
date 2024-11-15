package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quickcash.Firebase.JobCRUD;
import com.example.quickcash.adapter.JobSearchAdapter;
import com.example.quickcash.model.Job;
import com.example.quickcash.model.JobLocation;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class JobSearchParameterActivity extends AppCompatActivity {

    private static final String TAG = "JobSearchParameter";

    // UI components
    private EditText jobTitle, companyName, minSalary, maxSalary, duration, location;
    private TextView errorText;
    private Button searchButton, mapButton;
    private RecyclerView recyclerView;

    // Adapter and Data
    private JobSearchAdapter jobSearchAdapter;
    private List<Job> jobList;
    private FirebaseDatabase jobsRef;
    private JobCRUD jobCRUD;

    // User info
    private String email, userID;

    // Map data
    private ArrayList<Double> latitudes = new ArrayList<>();
    private ArrayList<Double> longitudes = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Integer> salaries = new ArrayList<>();
    private ArrayList<String> durations = new ArrayList<>();
    private ArrayList<String> companies = new ArrayList<>();
    private ArrayList<String> jobTypes = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_search_parameter);

        jobList = new ArrayList<>();
        init();

        // Getting email and user ID
        Intent intentPreferredEmployers = getIntent();
        this.email = intentPreferredEmployers.getStringExtra("email");
        if (email != null && !email.isEmpty()) {
            this.userID = email.replace(".", ",");
        }

        searchButton.setOnClickListener(view -> {
            if (checkSalaryField()) {
                errorText.setText("Enter Valid Salary Range");
            } else {
                errorText.setText(""); // Clear any previous error
                performSearch();
            }
        });

        mapButton.setOnClickListener(view -> {
            errorText.setText(""); // Clear any previous error
            if (checkSalaryField()) {
                errorText.setText("Enter Valid Salary Range");
            } else {
                errorText.setText(""); // Clear any previous error
                Log.d(TAG, "Starting map search");

                queryJobsForMap();
            }
        });
    }

    public void init() {
        jobTitle = findViewById(R.id.jobTitle);
        companyName = findViewById(R.id.companyName);
        minSalary = findViewById(R.id.minSalary);
        maxSalary = findViewById(R.id.maxSalary);
        duration = findViewById(R.id.duration);
        location = findViewById(R.id.location);
        errorText = findViewById(R.id.jspErrorDisplay);
        searchButton = findViewById(R.id.search_job_parameter);
        mapButton = findViewById(R.id.showMapButton);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobSearchAdapter = new JobSearchAdapter(jobList);
        recyclerView.setAdapter(jobSearchAdapter);


        jobsRef = FirebaseDatabase.getInstance();
        jobCRUD = new JobCRUD(jobsRef);
    }

    private void queryJobsForMap() {
        //Search for jobs
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String partialAddress = location.getText().toString().trim().toLowerCase();

        // Create a query to get all jobs if no filters are applied
        Query query = jobsRef.getReference("Jobs");
        // Only apply title filter if specified
        if (isValidField(title)) {
            query = query.orderByChild("jobTitle").equalTo(title);
        }

        Log.d(TAG, "Performing search with filters - Title: '" + title +
                "', Company: '" + company + "', Location: '" + partialAddress + "'");

        jobCRUD.getJobsByQuery(query).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Job> jobs = task.getResult();

                for (Job job : jobs) {
                    if(passesAdditionalFilters(job, partialAddress, company, minSalStr, maxSalStr, jobDuration)){
                        jobList.add(job);
                    }
                }

                if (!jobList.isEmpty()) {
                    errorText.setText(""); // Clear any previous error
                } else {
                    errorText.setText("No Results Found");
                }
            } else {
                errorText.setText("Failed to retrieve jobs.");
            }
        });

        // Clear previous map data
        latitudes.clear();
        longitudes.clear();
        titles.clear();
        salaries.clear();
        durations.clear();
        companies.clear();
        jobTypes.clear();
        locations.clear();

        // Use existing filtered results
        processJobsForMap();
    }

    private void processJobsForMap() {

        Log.d(TAG, "Processing " + jobList.size() + " jobs for map");


        Log.d(TAG, "Started processJobsForMaps()");
        for (Job job : jobList) {
            Log.d(TAG, "Location was not null");
            JobLocation location = job.getJobLocation();
            if (location != null) {
                double lat = location.getLat();
                double lng = location.getLng();
                Log.d(TAG, "Location was not null");
                // Add location if coordinates are present
                if (lat != 0 || lng != 0) {  // Changed validation to be less strict
                    Log.d(TAG, "Location was not 0, 0");
                    latitudes.add(lat);
                    longitudes.add(lng);
                    titles.add(job.getJobTitle());
                    salaries.add(job.getSalary());
                    durations.add(job.getExpectedDuration());
                    companies.add(job.getCompanyName());
                    jobTypes.add(job.getJobType());
                    locations.add(location.getAddress());

                    // Get location string - try all possible sources
                    String locationStr = null;
                    if (job.getJobLocation() != null && job.getJobLocation().getAddress() != null
                            && !job.getJobLocation().getAddress().trim().isEmpty()) {
                        locationStr = job.getJobLocation().getAddress();
                    } else if (job.getLocation() != null && !job.getLocation().trim().isEmpty()) {
                        locationStr = job.getLocation();
                    }

                    if (locationStr != null) {
                        locations.add(locationStr);
                        Log.d(TAG, "Added location for job " + job.getJobTitle() + ": " + locationStr);
                    } else {
                        locations.add(String.format("(%.6f, %.6f)", lat, lng));
                        Log.d(TAG, String.format("No location string found for job %s, using coordinates",
                                job.getJobTitle()));
                    }
                }
            }
        }

        if (!latitudes.isEmpty()) {
            Log.d(TAG, "Launching map with " + latitudes.size() + " locations");
            launchMapActivity();
        } else {
            Log.e(TAG, "No valid locations found in " + jobList.size() + " jobs");
            errorText.setText("No jobs with location data found");
        }
    }

    private void launchMapActivity() {
        Intent intent = new Intent(this, GoogleSearchMapActivity.class);
        intent.putExtra("latitudes", latitudes);
        intent.putExtra("longitudes", longitudes);
        intent.putStringArrayListExtra("titles", titles);
        intent.putIntegerArrayListExtra("salaries", salaries);
        intent.putStringArrayListExtra("durations", durations);
        intent.putStringArrayListExtra("companies", companies);
        intent.putStringArrayListExtra("jobTypes", jobTypes);
        intent.putStringArrayListExtra("locations", locations);
        startActivity(intent);
    }

    private void performSearch() {
        // Collect user input
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String partialAddress = location.getText().toString().trim().toLowerCase();

        // Create a query to get all jobs if no filters are applied
        Query query = jobsRef.getReference("Jobs");

        // Only apply title filter if specified
        if (isValidField(title)) {
            query = query.orderByChild("jobTitle").equalTo(title);
        }

        jobList.clear();
        jobSearchAdapter.notifyDataSetChanged();

        Log.d(TAG, "Performing search with filters - Title: '" + title +
                "', Company: '" + company + "', Location: '" + partialAddress + "'");

        jobCRUD.getJobsByQuery(query).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Job> jobs = task.getResult();

                for (Job job : jobs) {
                    if (passesAdditionalFilters(job, partialAddress, company, minSalStr, maxSalStr, jobDuration)) {
                        jobList.add(job);
                    }
                }

                if (!jobList.isEmpty()) {
                    errorText.setText(""); // Clear any previous error
                } else {
                    errorText.setText("No Results Found");
                }

                jobSearchAdapter.notifyDataSetChanged();
            } else {
                errorText.setText("Failed to retrieve jobs.");
            }
        });
    }

    private boolean passesAdditionalFilters(Job job, String searchAddress, String company,
                                            String minSalStr, String maxSalStr, String jobDuration) {
        // Check company name
        if (isValidField(company)) {
            if (!job.getCompanyName().equalsIgnoreCase(company)) {
                return false;
            }
        }

        // Check salary range
        int jobSalary = job.getSalary();
        if (isValidField(minSalStr)) {
            try {
                int minSal = Integer.parseInt(minSalStr);
                if (jobSalary < minSal) {
                    return false;
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing minimum salary: " + e.getMessage());
                return false;
            }
        }
        if (isValidField(maxSalStr)) {
            try {
                int maxSal = Integer.parseInt(maxSalStr);
                if (jobSalary > maxSal) {
                    return false;
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing maximum salary: " + e.getMessage());
                return false;
            }
        }

        // Check job duration
        if (isValidField(jobDuration)) {
            if (!job.getExpectedDuration().equalsIgnoreCase(jobDuration)) {
                return false;
            }
        }

        // Enhanced location matching
        if (isValidField(searchAddress)) {
            boolean locationMatches = false;
            String normalizedSearch = normalizeLocationString(searchAddress);

            // Check main location field
            String jobMainLocation = job.getLocation() != null ? normalizeLocationString(job.getLocation()) : "";
            if (jobMainLocation.contains(normalizedSearch)) {
                locationMatches = true;
            }

            // Check JobLocation object if available
            if (!locationMatches && job.getJobLocation() != null) {
                String jobLocationAddress = job.getJobLocation().getAddress() != null ?
                        normalizeLocationString(job.getJobLocation().getAddress()) : "";
                if (jobLocationAddress.contains(normalizedSearch)) {
                    locationMatches = true;
                }

                // If search term looks like coordinates, check lat/lng
                if (!locationMatches && isCoordinateSearch(normalizedSearch)) {
                    double searchLat = job.getJobLocation().getLat();
                    double searchLng = job.getJobLocation().getLng();
                    locationMatches = isNearbyCoordinate(searchLat, searchLng, normalizedSearch);
                }
            }

            if (!locationMatches) {
                return false;
            }
        }

        // Job passes all filters
        return true;
    }

    private String normalizeLocationString(String location) {
        if (location == null) return "";
        // Remove special characters and extra spaces, convert to lowercase
        return location.replaceAll("[^a-zA-Z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();
    }

    private boolean isCoordinateSearch(String search) {
        // Check if the search string matches coordinate format (lat,lng)
        return search.matches("^-?\\d+\\.?\\d*,-?\\d+\\.?\\d*$");
    }

    private boolean isNearbyCoordinate(double jobLat, double jobLng, String searchCoords) {
        try {
            String[] parts = searchCoords.split(",");
            double searchLat = Double.parseDouble(parts[0]);
            double searchLng = Double.parseDouble(parts[1]);

            // Calculate rough distance (this is an approximation)
            double distance = Math.sqrt(
                    Math.pow(jobLat - searchLat, 2) +
                            Math.pow(jobLng - searchLng, 2)
            );

            // Consider locations within ~5km as nearby (rough approximation)
            return distance < 0.05;
        } catch (Exception e) {
            Log.e(TAG, "Error parsing coordinates: " + e.getMessage());
            return false;
        }
    }

    public boolean checkSalaryField() {
        String minS = minSalary.getText().toString().trim();
        String maxS = maxSalary.getText().toString().trim();

        if (minS.isEmpty() || maxS.isEmpty()) {
            return false;
        }

        try {
            int minSal = Integer.parseInt(minS);
            int maxSal = Integer.parseInt(maxS);

            return minSal > maxSal;
        } catch (NumberFormatException e) {
            return true; // Invalid number format
        }
    }

    private boolean isValidField(String field) {
        return field != null && !field.isEmpty();
    }
}
