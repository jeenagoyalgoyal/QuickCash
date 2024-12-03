package com.example.quickcash;

import static com.example.quickcash.filter.JobSearchFilter.isValidField;
import static com.example.quickcash.filter.JobSearchFilter.passesAdditionalJobFilters;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.Firebase.JobCRUD;
import com.example.quickcash.adapter.JobSearchAdapter;
import com.example.quickcash.model.Job;
import com.example.quickcash.model.JobLocation;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This class is used for searching jobs via using filters to find the
 * posted jobs in the database.
 */
public class JobSearchParameterActivity extends AppCompatActivity {

    private static final String TAG = "JobSearchParameter";

    // UI components
    private EditText jobTitle, companyName, minSalary, maxSalary, duration, location;
    private TextView errorText;
    private Button searchButton, mapButton, jobDetails;
    private RecyclerView recyclerView;

    // Adapter and Data
    private JobSearchAdapter jobSearchAdapter;
    private List<Job> jobList;
    private FirebaseDatabase jobsRef;
    private JobCRUD jobCRUD;

    // User info
    private String email, userID, manualLocation;

    // Map data
    private ArrayList<Double> latitudes = new ArrayList<>();
    private ArrayList<Double> longitudes = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Integer> salaries = new ArrayList<>();
    private ArrayList<String> durations = new ArrayList<>();
    private ArrayList<String> companies = new ArrayList<>();
    private ArrayList<String> jobTypes = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();

    /**
     * On create, initialize the job search parameter form
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_search_parameter);
        init();

        // Getting email and user ID
        Intent intentPreferredEmployers = getIntent();
        this.manualLocation = intentPreferredEmployers.getStringExtra("manualLocation");
        this.email = intentPreferredEmployers.getStringExtra("email");
        if (email != null && !email.isEmpty()) {
            this.userID = email.replace(".", ",");
        }
        searchButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When user clicks search button, check the filters are filled
             *
             * @param view
             */
            @Override
            public void onClick(View view) {
                if (allEmptyFields()) {
                    errorText.setText("All Fields are empty");
                } else if (checkSalaryField()) {
                    errorText.setText("Enter Valid Salary Range");
                } else {
                    errorText.setText(""); // Clear any previous error
                    performSearch();
                }
            }
        });


        /**
         * When user clicks show map button, check the filters are filled
         * @param view
         */
        mapButton.setOnClickListener(view -> {
            errorText.setText(""); // Clear any previous error
            if (allEmptyFields()) {
                errorText.setText("All Fields are empty");
            } else if (checkSalaryField()) {
                errorText.setText("Enter Valid Salary Range");
            } else {
                errorText.setText(""); // Clear any previous error
                Log.d(TAG, "Starting map search");
                queryJobsForMap();
            }
        });

        jobSearchAdapter = new JobSearchAdapter(this, jobList); // Pass 'this' as the context
        recyclerView.setAdapter(jobSearchAdapter);

        // Set up the back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(JobSearchParameterActivity.this, EmployeeHomepageActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("manualLocation", manualLocation);
            startActivity(intent);
            finish(); // Optional: Call finish() if you don't want to keep the  in the back stack
        });
    }

    /**
     * Method initializes the job search form input variables
     */
    public void init() {
        jobList = new ArrayList<>();

        jobTitle = findViewById(R.id.jobTitle);
        companyName = findViewById(R.id.buildingName);
        minSalary = findViewById(R.id.minSalary);
        maxSalary = findViewById(R.id.maxSalary);
        duration = findViewById(R.id.duration);
        location = findViewById(R.id.location);
        errorText = findViewById(R.id.jspErrorDisplay);
        searchButton = findViewById(R.id.search_job_parameter);
        mapButton = findViewById(R.id.showMapButton);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobSearchAdapter = new JobSearchAdapter(this, jobList);
        recyclerView.setAdapter(jobSearchAdapter);
        jobsRef = FirebaseDatabase.getInstance();
        jobCRUD = new JobCRUD(jobsRef);
    }


    /**
     * This method finds the results from the job search
     */
    private void performSearch() {

        Query query = createQuery();

        // Clear previous search results
        jobList.clear();
        jobSearchAdapter.notifyDataSetChanged();

        jobCRUD.getJobsByQuery(query).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Job> jobs = task.getResult();
                jobList.clear();
                for (Job j : jobs) {
                    if (( j.getStatus().equals("pending")) && passesAdditionalFilters(j)) {
                        jobList.add(j);
                    }
                }
                if (jobList != null && !jobList.isEmpty()) {
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


    /**
     * Creates a firebase query with the input by user
     *
     * @return query
     */

    private Query createQuery() {
        // Get search parameters
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String jobLocation = location.getText().toString().trim();
        String salary = "salary";

        Query query = jobsRef.getReference("Jobs");

        // Apply filters based on non-empty inputs
        if (isValidField(title)) {
            query = query.orderByChild("jobTitle").equalTo(title);
        } else if (isValidField(company)) {
            query = query.orderByChild("buildingName").equalTo(company);
        } else if (isValidField(jobLocation)) {
            String locationKey = normalizeLocationString(jobLocation);
            query = query.orderByChild("location").startAt(locationKey).endAt(locationKey + "\uf8ff");
        } else if (isValidField(minSalStr) && isValidField(maxSalStr)) {
            int minSal = Integer.parseInt(minSalStr);
            int maxSal = Integer.parseInt(maxSalStr);
            query = query.orderByChild(salary).startAt(minSal).endAt(maxSal);
        } else if (isValidField(minSalStr)) {
            int minSal = Integer.parseInt(minSalStr);
            query = query.orderByChild(salary).startAt(minSal);
        } else if (isValidField(maxSalStr)) {
            int maxSal = Integer.parseInt(maxSalStr);
            query = query.orderByChild(salary).endAt(maxSal);
        } else if (isValidField(jobDuration)) {
            query = query.orderByChild("expectedDuration").equalTo(jobDuration);
        }

        return query;
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
                    //The following lines can be added to prevent showing jobs with no proper location
                    //JobLocation jobLocation = job.getJobLocation();
                    // && jobLocation!=null
                    if ((job.getStatus()==null || job.getStatus().equals("pending")) && passesAdditionalFilters(job, partialAddress, company, minSalStr, maxSalStr, jobDuration)) {
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

        for (Job job : jobList) {
            JobLocation location = job.getJobLocation();
            if (location != null) {
                double lat = location.getLat();
                double lng = location.getLng();
                // Add location if coordinates are present
                if (lat != 0 || lng != 0) {  // Changed validation to be less strict
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

    /**
     * Filters for the user to see if it's valid
     *
     * @param job
     * @return true if valid, false otherwise
     */
    private boolean passesAdditionalFilters(Job job) {
        // Get the user input again
        String title = jobTitle.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String minSalStr = minSalary.getText().toString().trim();
        String maxSalStr = maxSalary.getText().toString().trim();
        String jobDuration = duration.getText().toString().trim();
        String jobLocation = location.getText().toString().trim();

        return passesAdditionalJobFilters(job, title, company, minSalStr, maxSalStr, jobDuration, jobLocation);
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

    // Tests the job title (can be empty)
    public static boolean isValidJobTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }

    // Tests the job title (can be empty)
    public static boolean isValidCompany(String company) {
        return company != null && !company.trim().isEmpty();
    }

    // Tests salary is within boundary
    public static boolean isValidSalary(int minSalary, int maxSalary) {
        return minSalary >= 0 && maxSalary >= 0 && minSalary <= maxSalary;
    }

    // Tests valid duration
    public static boolean isValidDuration(String duration) {
        return duration != null && !duration.trim().isEmpty();
    }

    // Tests valid location
    public static boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty();
    }

    /**
     * Checks if fields are all empty
     *
     * @return true if all empty
     */
    public boolean allEmptyFields() {
        return jobTitle.getText().toString().trim().isEmpty() &&
                companyName.getText().toString().trim().isEmpty() &&
                minSalary.getText().toString().trim().isEmpty() &&
                maxSalary.getText().toString().trim().isEmpty() &&
                duration.getText().toString().trim().isEmpty() &&
                location.getText().toString().trim().isEmpty();
    }

    /**
     * Validates to see if salary is valid
     *
     * @return false if empty
     */
    public boolean checkSalaryField() {
        String minS = minSalary.getText().toString().trim();
        String maxS = maxSalary.getText().toString().trim();

        if (minS.isEmpty() || maxS.isEmpty()) {
            return false;
        } else {
            return !isValidSalary(Integer.parseInt(minS), Integer.parseInt(maxS));
        }
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

    /**
     * Called when the activity is resumed after being paused or restarted.
     * This method clears the job list and updates the adapter to prevent duplicate
     * entries in the RecyclerView. It also resets the error message text, if any.
     *
     * This ensures the displayed data is refreshed and consistent when returning
     * to this activity from another activity (e.g., the map activity).
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Clear the list and notify the adapter
        jobList.clear();
        performSearch();
        errorText.setText(""); // Clear any error messages
    }
}
