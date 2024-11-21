package com.example.quickcash;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GoogleSearchMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "GoogleSearchMapActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final LatLng HALIFAX = new LatLng(44.6488, -63.5752);

    private GoogleMap mMap;
    private Map<String, Integer> markerMap;

    // Job data
    private ArrayList<String> latitudesStr;
    private ArrayList<String> longitudesStr;
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;
    private ArrayList<String> titles;
    private ArrayList<Integer> salaries;
    private ArrayList<String> durations;
    private ArrayList<String> companies;
    private ArrayList<String> jobTypes;
    private ArrayList<String> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_map);

        // Initialize
        markerMap = new HashMap<>();

        // Setup back button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Get intent data
        retrieveAndValidateData();

        // Setup map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void retrieveAndValidateData() {
        Intent intent = getIntent();

        if (intent != null) {
            try {
                Log.d(TAG, "Starting to retrieve data from intent");

                latitudes = (ArrayList<Double>) intent.getSerializableExtra("latitudes");
                longitudes = (ArrayList<Double>) intent.getSerializableExtra("longitudes");
                locations = intent.getStringArrayListExtra("locations");

                Log.d(TAG, "Retrieved coordinates - Latitudes: " +
                        (latitudes != null ? latitudes.size() : "null") + ", Longitudes: " +
                        (longitudes != null ? longitudes.size() : "null") +
                        ", Locations: " + (locations != null ? locations.size() : "null"));

                titles = intent.getStringArrayListExtra("titles");
                salaries = intent.getIntegerArrayListExtra("salaries");
                durations = intent.getStringArrayListExtra("durations");
                companies = intent.getStringArrayListExtra("companies");
                jobTypes = intent.getStringArrayListExtra("jobTypes");

            } catch (Exception e) {
                Log.e(TAG, "Error getting intent data: " + e.getMessage(), e);
                showError("Error loading job data");
            }
        } else {
            Log.e(TAG, "Received null intent");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Enable zoom controls and other UI settings
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        // Set up marker click listener
        mMap.setOnMarkerClickListener(this::onMarkerClick);

        // Enable location if permitted
        if (checkLocationPermission()) {
            try {
                mMap.setMyLocationEnabled(true);
            } catch (SecurityException e) {
                Log.e(TAG, "Error enabling location: " + e.getMessage());
            }
        }

        // Add markers to map
        addMarkersToMap();
    }

    private boolean onMarkerClick(Marker marker) {
        try {
            Integer index = markerMap.get(marker.getId());
            if (index != null) {
                showJobDetailsDialog(index);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling marker click", e);
        }
        return false;
    }

    private void addMarkersToMap() {
        if (latitudes == null || longitudes == null || titles == null) {
            Log.e(TAG, "Location data arrays are null");
            centerMapOnHalifax();
            showError("Error loading job locations");
            return;
        }

        if (latitudes.isEmpty() || longitudes.isEmpty() || titles.isEmpty()) {
            Log.e(TAG, "Location data arrays are empty");
            Log.d(TAG, String.format("Array sizes - Latitudes: %d, Longitudes: %d, Titles: %d",
                    latitudes.size(), longitudes.size(), titles.size()));
            centerMapOnHalifax();
            showError("No job locations to display");
            return;
        }

        // Validate that arrays have matching sizes
        if (latitudes.size() != longitudes.size() || latitudes.size() != titles.size()) {
            Log.e(TAG, String.format("Mismatched array sizes - Latitudes: %d, Longitudes: %d, Titles: %d",
                    latitudes.size(), longitudes.size(), titles.size()));
            centerMapOnHalifax();
            showError("Error with job location data");
            return;
        }

        Log.d(TAG, "Number of locations to display: " + latitudes.size());

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boolean hasValidMarkers = false;

        for (int i = 0; i < latitudes.size(); i++) {
            try {
                double lat = latitudes.get(i);
                double lng = longitudes.get(i);
                String title = titles.get(i);

                Log.d(TAG, String.format("Adding marker %d: %s at (%.6f, %.6f)",
                        i, title, lat, lng));

                LatLng position = new LatLng(lat, lng);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(position)
                        .title(title);

                // Add additional info to marker snippet if available
                StringBuilder snippet = new StringBuilder();
                if (companies != null && i < companies.size()) {
                    snippet.append(companies.get(i));
                }
                if (salaries != null && i < salaries.size()) {
                    snippet.append("\nSalary: $").append(salaries.get(i)).append("/hr");
                }
                if (durations != null && i < durations.size()) {
                    snippet.append("\nDuration: ").append(durations.get(i));
                }
                if (snippet.length() > 0) {
                    markerOptions.snippet(snippet.toString());
                }

                Marker marker = mMap.addMarker(markerOptions);

                if (marker != null) {
                    markerMap.put(marker.getId(), i);
                    boundsBuilder.include(position);
                    hasValidMarkers = true;
                    Log.d(TAG, "Successfully added marker " + i);
                } else {
                    Log.e(TAG, "Failed to add marker " + i);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error adding marker " + i + ": " + e.getMessage(), e);
            }
        }

        if (hasValidMarkers) {
            try {
                LatLngBounds bounds = boundsBuilder.build();
                int padding = 100; // offset from edges of the map in pixels
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                Log.d(TAG, "Successfully set map bounds");
            } catch (Exception e) {
                Log.e(TAG, "Error setting map bounds", e);
                centerMapOnHalifax();
            }
        } else {
            Log.e(TAG, "No valid markers were added");
            centerMapOnHalifax();
        }
    }

    private void showJobDetailsDialog(int index) {
        try {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_job_details);

            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(
                        (int) (getResources().getDisplayMetrics().widthPixels * 0.9),
                        WindowManager.LayoutParams.WRAP_CONTENT
                );
            }

            // Set job details
            if (titles != null && index < titles.size()) {
                TextView titleView = dialog.findViewById(R.id.jobTitleText);
                if (titleView != null) titleView.setText(titles.get(index));
            }

            if (companies != null && index < companies.size()) {
                TextView companyView = dialog.findViewById(R.id.companyNameText);
                if (companyView != null) companyView.setText(companies.get(index));
            }

            if (locations != null && index < locations.size()) {
                TextView locationView = dialog.findViewById(R.id.locationText);
                if (locationView != null) {
                    String locationText = locations.get(index);
                    if (locationText != null && !locationText.trim().isEmpty()) {
                        locationView.setText(locationText);
                    } else {
                        // Fallback to coordinate-based location if address is not available
                        locationView.setText(String.format("Location: (%.6f, %.6f)",
                                latitudes.get(index), longitudes.get(index)));
                    }
                }
            }

            if (salaries != null && index < salaries.size()) {
                TextView salaryView = dialog.findViewById(R.id.salaryText);
                if (salaryView != null) {
                    salaryView.setText(String.format("$%d/hr", salaries.get(index)));
                }
            }

            if (jobTypes != null && index < jobTypes.size()) {
                TextView jobTypeView = dialog.findViewById(R.id.jobTypeText);
                if (jobTypeView != null) jobTypeView.setText(jobTypes.get(index));
            }

            if (durations != null && index < durations.size()) {
                TextView durationView = dialog.findViewById(R.id.durationText);
                if (durationView != null) durationView.setText(durations.get(index));
            }

            Button closeButton = dialog.findViewById(R.id.closeButton);
            if (closeButton != null) {
                closeButton.setOnClickListener(v -> dialog.dismiss());
            }

            dialog.show();
        } catch (Exception e) {
            Log.e(TAG, "Error showing dialog", e);
            showError("Error displaying job details");
        }
    }

    private void centerMapOnHalifax() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HALIFAX, 12f));
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mMap != null) {
                    try {
                        mMap.setMyLocationEnabled(true);
                    } catch (SecurityException e) {
                        Log.e(TAG, "Security exception: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void showError(String message) {
        Log.e(TAG, message);
        Snackbar.make(findViewById(R.id.map_container), message, BaseTransientBottomBar.LENGTH_LONG).show();
    }
}
