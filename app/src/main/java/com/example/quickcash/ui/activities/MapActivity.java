package com.example.quickcash.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.quickcash.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Button backButton;
    private boolean useDummyData = true; // Flag to use dummy data for testing

    //Details
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;
    private ArrayList<String> titles;
    private ArrayList<Integer> salaries;
    private ArrayList<String> durations;
    private ArrayList<String> companies;
    private ArrayList<String> jobTypes;
    private ArrayList<String> datesOfStart;
    private ArrayList<String> requirements;

    private Map<String, Integer> markerToJobIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        markerToJobIndex = new HashMap<>();

        if (useDummyData) {
            // Initialize dummy data for testing
            initializeDummyData();
        } else {
            // Get the job data from the intent
            Intent intent = getIntent();
            if (intent != null) {
                latitudes = (ArrayList<Double>) intent.getSerializableExtra("latitudes");
                longitudes = (ArrayList<Double>) intent.getSerializableExtra("longitudes");
                salaries = intent.getIntegerArrayListExtra("salaries");
                titles = intent.getStringArrayListExtra("titles");
                durations = intent.getStringArrayListExtra("durations");
                companies = intent.getStringArrayListExtra("companies");

            }
        }

        // Initialize back button
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Request location permissions if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initializeDummyData() {
        // Create ArrayLists for dummy data
        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        titles = new ArrayList<>();
        salaries = new ArrayList<>();
        durations = new ArrayList<>();
        companies = new ArrayList<>();
        jobTypes = new ArrayList<>();
        datesOfStart = new ArrayList<>();
        requirements = new ArrayList<>();


        // Add dummy job locations around Halifax
        // Job 1 - Downtown Halifax
        latitudes.add(44.6488);
        longitudes.add(-63.5752);
        jobTypes.add("Full-Time");
        datesOfStart.add("29-06-2004");
        requirements.add("random random random\n random random random");
        companies.add("Microsoft");
        titles.add("Software Developer");
        salaries.add(75000);
        durations.add("10");


        // Job 2 - Dartmouth
        latitudes.add(44.6667);
        longitudes.add(-63.5667);
        companies.add("FERRY CO.");
        titles.add("Web Designer");
        salaries.add(65000);
        durations.add("1");
        jobTypes.add("Full-Time");
        datesOfStart.add("29-06-2004");
        requirements.add("random random random\n random random random");

        // Job 3 - Bedford
        latitudes.add(44.7213);
        longitudes.add(-63.6582);
        companies.add("BUS CO.");
        titles.add("Data Analyst");
        salaries.add(70000);
        durations.add("10");
        jobTypes.add("Full-Time");
        datesOfStart.add("29-06-2004");
        requirements.add("random random random\n random random random");

        // Job 4 - Halifax Shopping Centre area
        latitudes.add(44.6497);
        longitudes.add(-63.6088);
        companies.add("Walmart");
        titles.add("UX Researcher");
        salaries.add(80000);
        durations.add("25");
        jobTypes.add("Full-Time");
        datesOfStart.add("29-06-2004");
        requirements.add("random random random\n random random random");

        // Job 5 - Dalhousie University area
        latitudes.add(44.6366);
        longitudes.add(-63.5917);
        companies.add("Dalhousie");
        titles.add("Teaching Assistant");
        salaries.add(25000);
        durations.add("10");
        jobTypes.add("Full-Time");
        datesOfStart.add("29-06-2004");
        requirements.add("random random random\n random random random");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        // Enable the location layer if permission is granted
        enableMyLocation();

        // Add job markers and setup bounds
        if (latitudes != null && longitudes != null && titles != null &&
                salaries != null && durations != null && !latitudes.isEmpty()) {

            addJobMarkers();
        } else {
            showNoJobsMessage();
        }

        // Enable zoom controls and other UI settings
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        // Get current location after setting up markers
        getCurrentLocation();
    }

    private void addJobMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        boolean hasValidMarkers = false;

        for (int i = 0; i < latitudes.size(); i++) {
            try {
                double lat = latitudes.get(i);
                double lng = longitudes.get(i);

                LatLng position = new LatLng(lat, lng);

                // Create marker with job details
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(position)
                        .title(titles.get(i));

                // Add marker to map and store its reference
                Marker marker = mMap.addMarker(markerOptions);
                if (marker != null) {
                    markerToJobIndex.put(marker.getId(), i);
                }

                // Include this position in bounds calculation
                builder.include(position);
                hasValidMarkers = true;
            } catch (NumberFormatException | NullPointerException e) {
                // Skip invalid coordinates and log error
                Log.e("Error adding:", Objects.requireNonNull(e.getMessage()));
            }
        }

        // If we have valid markers, set bounds with padding
        if (hasValidMarkers) {
            final int padding = 100;
            final LatLngBounds bounds = builder.build();

            // Post to UI thread with delay to ensure map is properly initialized
            mMap.setOnMapLoadedCallback(() ->
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
            );
        }
    }

    private void showNoJobsMessage() {
        Snackbar.make(findViewById(R.id.map),
                "No job locations available to display",
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(MapActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
        // Get the index of the job from the marker
        Integer jobIndex = markerToJobIndex.get(marker.getId());
        if (jobIndex != null) {
            // Show job details
            showJobDetailsDialog(jobIndex);
            return true;
        }
        return false;
    }

    private void showJobDetailsDialog(int jobIndex) {

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_job_details);

        TextView companyText = dialog.findViewById(R.id.companyNameText);
        TextView durationText = dialog.findViewById(R.id.durationText);
        TextView titleText = dialog.findViewById(R.id.jobTitleText);
        TextView jobTypeText = dialog.findViewById(R.id.jobTypeText);
        TextView requirementsText = dialog.findViewById(R.id.requirementsText);
        TextView salaryText = dialog.findViewById(R.id.salaryText);
        TextView dateOfStart = dialog.findViewById(R.id.dateOfStart);

        titleText.setText(titles.get(jobIndex));
        jobTypeText.setText(jobTypes.get(jobIndex));
        dateOfStart.setText(datesOfStart.get(jobIndex));
        requirementsText.setText(requirements.get(jobIndex));
        companyText.setText(companies.get(jobIndex));
        salaryText.setText(String.format("$%,d/year", salaries.get(jobIndex)));
        durationText.setText(durations.get(jobIndex));

        // Set dialog width to match parent with margins
        Window window = dialog.getWindow();

        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }

        dialog.show();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null && (latitudes == null || latitudes.isEmpty())) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(currentLatLng, 17));
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
                getCurrentLocation();
            } else {
                Snackbar.make(findViewById(R.id.map),
                        "Location permission is required for better experience",
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }
}