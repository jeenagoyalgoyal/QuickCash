package com.example.quickcash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickcash.Firebase.JobCRUD;
import com.example.quickcash.adapter.JobSearchAdapter;
import com.example.quickcash.model.Job;
import com.example.quickcash.model.JobLocation;
import com.example.quickcash.model.UseRole;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The EmployeeHomepageActivity class provides the user interface and functionality
 * for the employee's homepage in the QuickCash application. It allows employees
 * to search for jobs, view preferred employers, switch to employer mode, and interact
 * with job listings by location.
 */
public class EmployeeHomepageActivity extends AppCompatActivity implements LocationHelper.LocationResultListener {
    public static final String EMAIL = "email";
    // Instance variables for role management, UI components, and location services
    private String currentRole = "employee";
    private UseRole useRole;
    private int id;
    private LocationHelper locationHelper;
    private JobCRUD jobCrud;
    private GoogleMap mMap;
    private FirebaseDatabase jobsRef;
    private String selectedJobId;
    // UI components
    public TextView welcomeEmployee;
    public Button searchJob;
    public Button myProfile;
    public Button workSchedule;
    public Button tasksProjects;
    public Button performanceReview;
    public Button employeeNotifications;
    public Button employerSwitch;
    public Button preferredJobsButton;
    public Button appliedJobsButton;
    public Button preferredEmployers;
    private RecyclerView jobRecyclerView; // RecyclerView for displaying jobs
    private JobSearchAdapter jobAdapter;
    private String email;
    FirebaseAuth mAuth;

    /**
     * Initializes the activity, sets up UI components, and handles role-based navigation.
     *
     * @param savedInstance The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.employee_dashboard);

        //ID is retrieved
        this.mAuth = FirebaseAuth.getInstance();
        this.email = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
        this.email = this.email.replace(".", ",");

        // Retrieve user details and location from intent
        Intent intentEmployeeDash = getIntent();
        id = intentEmployeeDash.getIntExtra("userID", -1);
        String manualLocation = intentEmployeeDash.getStringExtra("manualLocation"); // Retrieve manual location

        useRole = UseRole.getInstance();
        useRole.setCurrentRole("Employee");

        jobCrud = new JobCRUD(FirebaseDatabase.getInstance());
        jobsRef = FirebaseDatabase.getInstance();


        // Initialize LocationHelper with this activity as the listener
        locationHelper = new LocationHelper(this, this);
        welcomeEmployee = findViewById(R.id.welcomeEmployee);

        // Initialize UI components
        searchJob = findViewById(R.id.searchJobButton);
        myProfile = findViewById(R.id.myProfileButton);
        /*workSchedule = findViewById(R.id.workScheduleButton);
        tasksProjects = findViewById(R.id.tasksProjectsButton);
        performanceReview = findViewById(R.id.performanceReviewsButton);
        employeeNotifications = findViewById(R.id.employeeNotifications);*/
        employerSwitch = findViewById(R.id.switchToEmployerButton);
        preferredEmployers = findViewById(R.id.preferredEmployersButton);
        preferredJobsButton = findViewById(R.id.preferredJobsButton);
        appliedJobsButton = findViewById(R.id.appliedJobsButton);

        // Set up RecyclerView for displaying jobs
        jobRecyclerView = findViewById(R.id.jobsRecycler);
        jobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobRecyclerView.setNestedScrollingEnabled(true);


        // SWITCHES TO EMPLOYEE DASH
        employerSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                useRole.switchRole(email);
                Intent intentSwitchToEmployer = new Intent(EmployeeHomepageActivity.this, EmployerHomepageActivity.class);
                intentSwitchToEmployer.putExtra(EMAIL, email);
                startActivity(intentSwitchToEmployer);
            }
        });

        myProfile.setOnClickListener(view -> {
            Intent intentProfile = new Intent(EmployeeHomepageActivity.this, Profile.class);
            intentProfile.putExtra("jobId", selectedJobId); // Pass jobId dynamically
            startActivity(intentProfile);
            finish();
        });


        preferredEmployers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPreferredEmployers = new Intent(EmployeeHomepageActivity.this, PreferredEmployersActivity.class);
                intentPreferredEmployers.putExtra(EMAIL, email);
                startActivity(intentPreferredEmployers);
                finish();
            }
        });

        searchJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentJobSearchParameter = new Intent(EmployeeHomepageActivity.this, JobSearchParameterActivity.class);
                intentJobSearchParameter.putExtra(EMAIL, email);
                startActivity(intentJobSearchParameter);
                finish();
            }
        });

        preferredJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile = new Intent(EmployeeHomepageActivity.this, PreferredJobsActivity.class);
                startActivity(intentProfile);
                finish();
            }
        });

        appliedJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Get the email of the current user
                Intent intent = new Intent(EmployeeHomepageActivity.this, AppliedJobsActivity.class);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
            }
        });


        if (manualLocation != null && !manualLocation.isEmpty()) {
            String[] latLng = manualLocation.split(",");
            loadJobsByLocation(getCityFromLatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1])));
        } else {
            loadJobsByLocation("Halifax");
        }

    }

    /**
     * Retrieves the city name from latitude and longitude using Geocoder.
     *
     * @param latitude  The latitude of the location.
     * @param longitude The longitude of the location.
     * @return The city name or "Halifax" if unavailable.
     */
    private String getCityFromLatLng(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Halifax";
    }

    /**
     * Callback when a location is retrieved.
     * Updates the map to display the user's location and loads jobs in the detected city.
     *
     * @param latitude  The latitude of the location.
     * @param longitude The longitude of the location.
     * @param address   The full address string.
     * @param city      The city name.
     */
    @Override
    public void onLocationRetrieved(double latitude, double longitude, String address, String city) {
        if (mMap != null) {
            // Center the map on the user's location
            LatLng userLocation = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
            mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
        }

        // Load jobs in this location
        loadJobsByLocation(city);
    }

    /**
     * Requests the user's current location or prompts for location permission if not granted.
     */
    private void getUserLocation() {
        if (locationHelper.isLocationPermissionGranted()) {
            locationHelper.getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    /**
     * Loads job listings for a specific city using the JobCRUD instance.
     *
     * @param city The city for which to load jobs.
     */
    private void loadJobsByLocation(String city) {
        Log.d("Employee Homepage","Recieved city: "+city);
        Query query = jobsRef.getReference("Jobs");

        jobCrud.getJobsByQuery(query).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                List<Job> jobs = task.getResult();

                // Filter jobs locally using .contains() on location
                List<Job> filteredJobs = new ArrayList<>();
                for (Job job : jobs) {
                    JobLocation jobLocation = job.getJobLocation(); //Make sure location is not null
                    if (job.getStatus().equals("pending") && jobLocation != null && jobLocation.getAddress().toLowerCase().contains(city.toLowerCase())) {
                        filteredJobs.add(job);
                    }
                }

                // Display filtered jobs
                displayJobs(filteredJobs);
            } else {
                showNoJobsFoundMessage();
                promptForManualLocationEntry();
            }
        });
    }

    /**
     * Displays a list of jobs in the RecyclerView using a JobSearchAdapter.
     *
     * @param jobs The list of job objects to display.
     */
    private void displayJobs(List<Job> jobs) {
        jobAdapter = new JobSearchAdapter(this, jobs);
        jobRecyclerView.setAdapter(jobAdapter);
        jobAdapter.notifyDataSetChanged();
    }

    /**
     * Displays a message to the user when no jobs are found for their location.
     */
    private void showNoJobsFoundMessage() {
        Toast.makeText(this, "No jobs found for this location.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Prompts the user to manually enter their location if location data is unavailable.
     */
    private void promptForManualLocationEntry() {
        // Show a dialog to allow the user to manually enter their location
        // You could add an EditText dialog here for manual input
    }

    /**
     * Handles the result of a location permission request.
     *
     * @param requestCode  The request code for the permission request.
     * @param permissions  The requested permissions.
     * @param grantResults The results for the permission requests.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationHelper.getCurrentLocation();
        } else {
            showNoJobsFoundMessage();
            promptForManualLocationEntry();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Initializes the GoogleMap instance when the map is ready.
     *
     * @param googleMap The GoogleMap instance to initialize.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
