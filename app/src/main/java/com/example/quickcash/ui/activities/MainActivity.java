package com.example.quickcash.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.FragmentActivity;
import com.example.quickcash.R;
import com.example.quickcash.ui.models.Job;
import com.example.quickcash.ui.repositories.FirebaseCRUD;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText searchQuery;
    private Button btnSearch, btnShowMap;
    private FirebaseCRUD firebaseCRUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchQuery = findViewById(R.id.searchQuery);
        btnSearch = findViewById(R.id.btnSearch);
        btnShowMap = findViewById(R.id.btnShowMap);

        // Initialize Firebase CRUD and Map
        firebaseCRUD = new FirebaseCRUD();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Button to search jobs and fetch data
        btnSearch.setOnClickListener(v -> searchJobs(searchQuery.getText().toString()));

        // Button to display job locations on map
        btnShowMap.setOnClickListener(v -> displayJobsOnMap());
    }

    private void searchJobs(String query) {
        firebaseCRUD.searchJobs(query, new FirebaseCRUD.JobDataCallback() {
            @Override
            public void onCallback(List<Job> jobList) {
                // Once jobs are fetched, show them on the map
                displayJobsOnMap(jobList);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Set default map view (example coordinates)
        LatLng defaultLocation = new LatLng(37.7749, -122.4194); // San Francisco
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
    }

    private void displayJobsOnMap(List<Job> jobList) {
        if (mMap != null) {
            mMap.clear();  // Clear previous markers

            for (Job job : jobList) {
                LatLng jobLocation = new LatLng(job.getLatitude(), job.getLongitude());
                mMap.addMarker(new MarkerOptions().position(jobLocation)
                        .title(job.getJobTitle())
                        .snippet("Salary: " + job.getSalary() + "\nDuration: " + job.getDuration()));
            }

            // Optionally move the camera to the first job's location
            if (!jobList.isEmpty()) {
                LatLng firstLocation = new LatLng(jobList.get(0).getLatitude(), jobList.get(0).getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10));
            }
        }
    }
}
