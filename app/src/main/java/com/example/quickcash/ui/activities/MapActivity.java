package com.example.quickcash.ui.activities;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.example.quickcash.R;
import com.example.quickcash.ui.models.Job;
import com.example.quickcash.ui.repositories.FirebaseCRUD;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseCRUD firebaseCRUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize FirebaseCRUD
        firebaseCRUD = new FirebaseCRUD(FirebaseDatabase.getInstance());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Fetch job locations from Firebase
        fetchJobLocations();
    }

    private void fetchJobLocations() {
        firebaseCRUD.getAllJobs(new FirebaseCRUD.JobDataCallback() {
            @Override
            public void onCallback(List<Job> jobList) {
                // Iterate through the jobs and add markers
                for (Job job : jobList) {
                    LatLng jobLocation = new LatLng(job.getLatitude(), job.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(jobLocation)
                            .title(job.getJobTitle())
                            .snippet("Salary: $" + job.getSalary() + "\nDuration: " + job.getDuration()));
                }

                // Optionally, move the camera to the first job's location
                if (!jobList.isEmpty()) {
                    Job firstJob = jobList.get(0);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(firstJob.getLatitude(), firstJob.getLongitude()), 10));
                }
            }
        });
    }
}
