package com.example.quickcash;
import static com.example.quickcash.R.id.jobsRecycler;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

import java.util.List;

public class EmployeeHomepageActivity extends AppCompatActivity implements LocationHelper.LocationResultListener {

    private String currentRole = "employee";
    private UseRole useRole;
    private int id;
    private LocationHelper locationHelper;
    private JobCRUD jobCrud;
    private GoogleMap mMap;

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

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.employee_dashboard);

        Intent intentEmployeeDash = getIntent();
        id = intentEmployeeDash.getIntExtra("userID", -1);

        String email = intentEmployeeDash.getStringExtra("email");

        useRole = UseRole.getInstance();

        jobCrud = new JobCRUD(FirebaseDatabase.getInstance());

        // Initialize LocationHelper with this activity as the listener
        locationHelper = new LocationHelper(this, this);
        welcomeEmployee = findViewById(R.id.welcomeEmployee);
        // Role-specific buttons
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

        getUserLocation();

    }

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

    private void getUserLocation() {
        if (locationHelper.isLocationPermissionGranted()) {
            locationHelper.getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }
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

    private void displayJobs(List<Job> jobs) {
        // Initialize the adapter with the retrieved job listings
        jobAdapter = new JobSearchAdapter(jobs);
        jobRecyclerView.setAdapter(jobAdapter);
        jobAdapter.notifyDataSetChanged();
    }

    private void showNoJobsFoundMessage() {
        Toast.makeText(this, "No jobs found for this location.", Toast.LENGTH_SHORT).show();
    }

    private void promptForManualLocationEntry() {
        // Show a dialog to allow the user to manually enter their location
        // You could add an EditText dialog here for manual input
    }
    // Handle the result of permission request
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
