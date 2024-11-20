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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * The EmployeeHomepageActivity class provides the user interface and functionality
 * for the employee's homepage in the QuickCash application. It allows employees
 * to search for jobs, view preferred employers, switch to employer mode, and interact
 * with job listings by location.
 */
public class EmployeeHomepageActivity extends AppCompatActivity implements LocationHelper.LocationResultListener {
    // Instance variables for role management, UI components, and location services
    private String currentRole = "employee";
    private UseRole useRole;
    private int id;
    private LocationHelper locationHelper;
    private JobCRUD jobCrud;
    private GoogleMap mMap;

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
    public Button preferredEmployers;
    private RecyclerView jobRecyclerView; // RecyclerView for displaying jobs
    private JobSearchAdapter jobAdapter;

    /**
     * Initializes the activity, sets up UI components, and handles role-based navigation.
     *
     * @param savedInstance The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.employee_dashboard);

        // Retrieve user details and location from intent
        Intent intentEmployeeDash = getIntent();
        id = intentEmployeeDash.getIntExtra("userID", -1);

        String email = intentEmployeeDash.getStringExtra("email");
        String manualLocation = intentEmployeeDash.getStringExtra("manualLocation"); // Retrieve manual location
        Log.d("Email recieved at dashboard: ", email);

        useRole = UseRole.getInstance();

        jobCrud = new JobCRUD(FirebaseDatabase.getInstance());

        // Initialize LocationHelper with this activity as the listener
        locationHelper = new LocationHelper(this, this);
        welcomeEmployee = findViewById(R.id.welcomeEmployee);
        // Initialize UI components
        searchJob = findViewById(R.id.searchJobButton);
        myProfile = findViewById(R.id.myProfileButton);
        workSchedule = findViewById(R.id.workScheduleButton);
        tasksProjects = findViewById(R.id.tasksProjectsButton);
        performanceReview = findViewById(R.id.performanceReviewsButton);
        employeeNotifications = findViewById(R.id.employeeNotifications);
        employerSwitch = findViewById(R.id.switchToEmployerButton);
        preferredEmployers = findViewById(R.id.preferredEmployersButton);
        preferredJobsButton = findViewById(R.id.preferredJobsButton);

        // Set up RecyclerView for displaying jobs
        jobRecyclerView = findViewById(R.id.jobsRecycler);
        jobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobRecyclerView.setNestedScrollingEnabled(true);


        // SWITCHES TO EMPLOYEE DASH
        employerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useRole.switchRole(id);
                Intent intentSwitchToEmployer = new Intent(EmployeeHomepageActivity.this, EmployerHomepageActivity.class);
                intentSwitchToEmployer.putExtra("email", email);
                startActivity(intentSwitchToEmployer);
            }
        });

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile = new Intent(EmployeeHomepageActivity.this, Profile.class);
                startActivity(intentProfile);
                finish();
            }
        });

        preferredEmployers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPreferredEmployers = new Intent(EmployeeHomepageActivity.this, PreferredEmployersActivity.class);
                intentPreferredEmployers.putExtra("email", email);
                startActivity(intentPreferredEmployers);
                finish();
            }
        });

        searchJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentJobSearchParameter = new Intent(EmployeeHomepageActivity.this, JobSearchParameterActivity.class);
                intentJobSearchParameter.putExtra("email", email);
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
        jobCrud.getJobsByLocation(city).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                List<Job> jobs = task.getResult();
                displayJobs(jobs);
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
        // Initialize the adapter with the retrieved job listings
        jobAdapter = new JobSearchAdapter(jobs);
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
