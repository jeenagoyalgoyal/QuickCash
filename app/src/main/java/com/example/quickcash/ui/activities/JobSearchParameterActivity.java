package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.R;
import com.example.quickcash.models.Job;
import com.example.quickcash.models.JobLocation;
import com.example.quickcash.utils.adapter.JobSearchAdapter;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class JobSearchParameterActivity extends AppCompatActivity {
    private static final String TAG = "JobSearchParameter";

    private EditText jobTitle;
    private EditText companyName;
    private EditText minSalary;
    private EditText maxSalary;
    private EditText duration;
    private EditText location;
    private TextView jspErrorDisplay;
    private Button searchButton;
    private Button mapButton;
    private RecyclerView recyclerView;
    private JobSearchAdapter jobSearchAdapter;
    private List<Job> jobList;
    private DatabaseReference jobsRef;

    public static boolean isValidJobTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }

    public static boolean isValidSalary(int min, int max) {
        return min >= 0 && max >= 0 && max >= min;
    }

    public static boolean isValidDuration(String duration) {
        return duration != null && !duration.trim().isEmpty();
    }

    public static boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_search_parameter);
        init();
        setupListeners();
        // Set up the back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(JobSearchParameterActivity.this, EmployeeHomepageActivity.class);
            startActivity(intent);
            finish(); // Optional: Call finish() if you don't want to keep the  in the back stack
        });
    }

    private void init() {
        try {
            jobsRef = FirebaseDatabase.getInstance().getReference("Jobs");
            jobList = new ArrayList<>();

            // Initialize views
            jobTitle = findViewById(R.id.jobTitle);
            companyName = findViewById(R.id.companyName);
            minSalary = findViewById(R.id.minSalary);
            maxSalary = findViewById(R.id.maxSalary);
            duration = findViewById(R.id.duration);
            location = findViewById(R.id.location);
            jspErrorDisplay = findViewById(R.id.jspErrorDisplay);
            searchButton = findViewById(R.id.search_job_parameter);
            mapButton = findViewById(R.id.showMapButton);
            recyclerView = findViewById(R.id.recyclerView);

            // Setup RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            jobSearchAdapter = new JobSearchAdapter(jobList);
            recyclerView.setAdapter(jobSearchAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing: " + e.getMessage());
            Toast.makeText(this, "Error initializing app", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupListeners() {
        searchButton.setOnClickListener(v -> performSearch());
        mapButton.setOnClickListener(v -> performSearchForMap());
    }

    private void performSearch() {
        if (checkSalaryField()) {
            jspErrorDisplay.setText("Enter Valid Salary Range");
            return;
        }
        jspErrorDisplay.setText("");
        queryJobs();
    }

    private void queryJobs() {
        jobList.clear();
        jobSearchAdapter.notifyDataSetChanged();
        Log.d(TAG, "Starting job search query");

        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Log.d(TAG, "Number of jobs in database: " + dataSnapshot.getChildrenCount());

                    for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                        processTheJobsFromSnapshot(jobSnapshot);
                    }

                    Log.d(TAG, "Found " + jobList.size() + " matching jobs");

                    if (jobList.isEmpty()) {
                        jspErrorDisplay.setText("No Results Found");
                    }
                    jobSearchAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(TAG, "Error processing jobs: " + e.getMessage());
                    jspErrorDisplay.setText("Error loading jobs");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                jspErrorDisplay.setText("Failed to retrieve jobs");
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }

    /**
     * Processes a job entry from a Firebase DataSnapshot, extracts the job information,
     * and applies filtering criteria to determine if it should be added to a list of jobs.
     * Logs details about the job being processed, including any errors encountered.
     *
     * @param jobSnapshot the DataSnapshot object containing job data retrieved from Firebase.
     *                    It is expected to map to a {@code Job} object.
     */
    private void processTheJobsFromSnapshot(DataSnapshot jobSnapshot) {
        try {
            Job job = jobSnapshot.getValue(Job.class);
            Log.d(TAG, "Processing job: " + jobSnapshot.getKey());

            if (job != null) {
                Log.d(TAG, "Job title: " + job.getJobTitle());
                Log.d(TAG, "Location data: " + job.getLocation());

                if (passesFilters(job)) {
                    Log.d(TAG, "Job passed filters: " + job.getJobTitle());
                    jobList.add(job);
                } else {
                    Log.d(TAG, "Job did not pass filters: " + job.getJobTitle());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error processing job: " + e.getMessage());
        }
    }

    private boolean passesFilters(Job job) {
        try {
            Log.d(TAG, "Checking filters for job: " + job.getJobTitle());

            String titleQuery = jobTitle.getText().toString().trim().toLowerCase();
            String companyQuery = companyName.getText().toString().trim().toLowerCase();
            String minSalStr = minSalary.getText().toString().trim();
            String maxSalStr = maxSalary.getText().toString().trim();
            String durationQuery = duration.getText().toString().trim().toLowerCase();
            String locationQuery = location.getText().toString().trim().toLowerCase();

            // If location is specified, check it
            if (!locationQuery.isEmpty()) {
                Object locationObj = job.getLocation();
                String jobLocationStr;

                if (locationObj instanceof String) {
                    jobLocationStr = ((String) locationObj).toLowerCase();
                } else if (job.getJobLocation() != null) {
                    jobLocationStr = job.getJobLocation().getAddress().toLowerCase();
                } else {
                    return false;
                }

                if (!jobLocationStr.contains(locationQuery)) {
                    Log.d(TAG, "Failed location filter. Job location: " + jobLocationStr +
                            ", Query: " + locationQuery);
                    return false;
                }
            }

            // Check other non-empty filters
            if (!titleQuery.isEmpty() && !job.getJobTitle().toLowerCase().contains(titleQuery)) {
                Log.d(TAG, "Failed title filter");
                return false;
            }

            if (!companyQuery.isEmpty() && !job.getCompanyName().toLowerCase().contains(companyQuery)) {
                Log.d(TAG, "Failed company filter");
                return false;
            }

            if (!durationQuery.isEmpty() &&
                    !job.getExpectedDuration().toLowerCase().contains(durationQuery)) {
                Log.d(TAG, "Failed duration filter");
                return false;
            }

            // Salary range check
            if (!minSalStr.isEmpty() || !maxSalStr.isEmpty()) {
                int jobSalary = job.getSalary();
                if (!minSalStr.isEmpty() && jobSalary < Integer.parseInt(minSalStr)) {
                    Log.d(TAG, "Failed min salary filter");
                    return false;
                }
                if (!maxSalStr.isEmpty() && jobSalary > Integer.parseInt(maxSalStr)) {
                    Log.d(TAG, "Failed max salary filter");
                    return false;
                }
            }

            Log.d(TAG, "Job passed all filters");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error in filters: " + e.getMessage());
            return false;
        }
    }

    /**
     * Initiates a job search with a salary range validation for map-based results.
     *
     * <p>This method first checks if the salary field contains a valid range using {@code checkSalaryField()}.
     * If the salary range is invalid, it sets an error message on {@code jspErrorDisplay} and stops further processing.
     * If the salary is valid, it clears any error message and proceeds with the search by calling {@code queryJobsForMap()}.
     *
     * @see #checkSalaryField()
     * @see #queryJobsForMap()
     */
    private void performSearchForMap() {
        if (checkSalaryField()) {
            jspErrorDisplay.setText("Enter Valid Salary Range");
            return;
        }
        jspErrorDisplay.setText("");
        queryJobsForMap();
    }

    /**
     * Queries job data for map-based results and processes each job to extract relevant information.
     *
     * <p>This method initiates a one-time read operation on a Firebase reference {@code jobsRef} to retrieve
     * job postings. For each job that meets specified filters, it extracts details like latitude, longitude,
     * title, salary, duration, company, and job type. The method defaults to Halifax coordinates for unspecified
     * locations and custom coordinates for Montreal; additional cities can be added as needed.
     *
     * <p>If jobs are found, this information is passed to {@code launchMapActivity()} to display results on a map.
     * If no jobs match the criteria, a "No Results Found" message is displayed. In case of errors, appropriate
     * messages are logged and displayed on {@code jspErrorDisplay}.
     *
     * @see #passesFilters(Job)
     * @see #launchMapActivity(ArrayList, ArrayList, ArrayList, ArrayList, ArrayList, ArrayList, ArrayList)
     */
    private void queryJobsForMap() {
        Log.d(TAG, "Starting map search");
        ArrayList<Double> latitudes = new ArrayList<>();
        ArrayList<Double> longitudes = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<Integer> salaries = new ArrayList<>();
        ArrayList<String> durations = new ArrayList<>();
        ArrayList<String> companies = new ArrayList<>();
        ArrayList<String> jobTypes = new ArrayList<>();

        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Log.d(TAG, "Processing " + dataSnapshot.getChildrenCount() + " jobs for map");
                    boolean foundResults = false;

                    for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                        try {
                            Job job = jobSnapshot.getValue(Job.class);
                            if (job != null && passesFilters(job)) {
                                // Get the location string
                                Object locationObj = job.getLocation();
                                String locationStr = locationObj instanceof String ?
                                        (String) locationObj : "";

                                // Default to Halifax coordinates for testing
                                // In a real app, you'd want to use geocoding here
                                double lat = 44.6488;
                                double lng = -63.5752;

                                if (locationStr.toLowerCase().contains("montreal")) {
                                    lat = 45.5017;
                                    lng = -73.5673;
                                }
                                // Add more cities as needed

                                Log.d(TAG, "Found job for map: " + job.getJobTitle() +
                                        " at " + lat + "," + lng);

                                latitudes.add(lat);
                                longitudes.add(lng);
                                titles.add(job.getJobTitle());
                                salaries.add(job.getSalary());
                                durations.add(job.getExpectedDuration());
                                companies.add(job.getCompanyName());
                                jobTypes.add(job.getJobType());
                                foundResults = true;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error processing job for map: " + e.getMessage());
                        }
                    }

                    if (!foundResults) {
                        Log.d(TAG, "No jobs found for map");
                        jspErrorDisplay.setText("No Results Found");
                    } else {
                        Log.d(TAG, "Launching map with " + latitudes.size() + " jobs");
                        launchMapActivity(latitudes, longitudes, titles, salaries,
                                durations, companies, jobTypes);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error in map search: " + e.getMessage());
                    jspErrorDisplay.setText("Error loading jobs");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                jspErrorDisplay.setText("Failed to retrieve jobs");
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }

    /**
     * Launches the {@code MapActivity} to display job listings on a map.
     *
     * <p>This method creates an {@code Intent} to start {@code MapActivity} and attaches job-related
     * data as extras, including latitude and longitude coordinates, job titles, salaries, durations,
     * company names, and job types. The data is passed as {@code ArrayList} objects, allowing
     * {@code MapActivity} to access and display multiple job listings simultaneously.
     *
     * @param latitudes an {@code ArrayList<Double>} containing latitude coordinates for the jobs.
     * @param longitudes an {@code ArrayList<Double>} containing longitude coordinates for the jobs.
     * @param titles an {@code ArrayList<String>} of job titles to be displayed on the map.
     * @param salaries an {@code ArrayList<Integer>} of salaries associated with the jobs.
     * @param durations an {@code ArrayList<String>} of job durations.
     * @param companies an {@code ArrayList<String>} of company names for the jobs.
     * @param jobTypes an {@code ArrayList<String>} of job types (e.g., full-time, part-time).
     */
    private void launchMapActivity(ArrayList<Double> latitudes, ArrayList<Double> longitudes,
                                   ArrayList<String> titles, ArrayList<Integer> salaries,
                                   ArrayList<String> durations, ArrayList<String> companies,
                                   ArrayList<String> jobTypes) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("latitudes", latitudes);
        intent.putExtra("longitudes", longitudes);
        intent.putStringArrayListExtra("titles", titles);
        intent.putIntegerArrayListExtra("salaries", salaries);
        intent.putStringArrayListExtra("durations", durations);
        intent.putStringArrayListExtra("companies", companies);
        intent.putStringArrayListExtra("jobTypes", jobTypes);
        startActivity(intent);
    }

    /**
     * Validates the salary range entered by the user.
     *
     * <p>This method retrieves and trims text from the minimum and maximum salary input fields.
     * If either field is empty, the method returns {@code false}, indicating that the salary range
     * is valid by default (or that no salary range was specified). It then attempts to parse both
     * values as integers and checks whether the minimum salary is greater than the maximum salary,
     * indicating an invalid range. If parsing fails due to non-numeric input, the method returns {@code true}
     * to signify an invalid salary range.
     *
     * @return {@code true} if the salary range is invalid (either non-numeric input or minimum greater than maximum);
     *         {@code false} if the salary range is valid or not specified.
     */
    public boolean checkSalaryField() {
        String minS = minSalary.getText().toString().trim();
        String maxS = maxSalary.getText().toString().trim();

        if (minS.isEmpty() || maxS.isEmpty()) {
            return false;
        }

        try {
            int min = Integer.parseInt(minS);
            int max = Integer.parseInt(maxS);
            return min > max;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
