package com.example.quickcash;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final LatLng HALIFAX = new LatLng(44.6488, -63.5752);

    private GoogleMap mMap;
    private Map<String, Integer> markerMap;

    // Job data
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;
    private ArrayList<String> titles;
    private ArrayList<Integer> salaries;
    private ArrayList<String> durations;
    private ArrayList<String> companies;
    private ArrayList<String> jobTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    latitudes = intent.getSerializableExtra("latitudes", ArrayList.class);
                    longitudes = intent.getSerializableExtra("longitudes", ArrayList.class);
                    titles = intent.getStringArrayListExtra("titles");
                    salaries = intent.getIntegerArrayListExtra("salaries");
                    durations = intent.getStringArrayListExtra("durations");
                    companies = intent.getStringArrayListExtra("companies");
                    jobTypes = intent.getStringArrayListExtra("jobTypes");
                } else {
                    latitudes = (ArrayList<Double>) intent.getSerializableExtra("latitudes");
                    longitudes = (ArrayList<Double>) intent.getSerializableExtra("longitudes");
                    titles = intent.getStringArrayListExtra("titles");
                    salaries = intent.getIntegerArrayListExtra("salaries");
                    durations = intent.getStringArrayListExtra("durations");
                    companies = intent.getStringArrayListExtra("companies");
                    jobTypes = intent.getStringArrayListExtra("jobTypes");
                }

                // Debug logging
                Log.d(TAG, "Received data in MapActivity");
                if (latitudes != null && !latitudes.isEmpty()) {
                    Log.d(TAG, "Received " + latitudes.size() + " locations");
                } else {
                    Log.e(TAG, "No locations received");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting intent data: " + e.getMessage());
                showError("Error loading job data");
            }
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
        if (latitudes == null || longitudes == null || titles == null ||
                latitudes.isEmpty() || longitudes.isEmpty() || titles.isEmpty()) {
            Log.e(TAG, "No valid location data available");
            centerMapOnHalifax();
            showError("No job locations to display");
            return;
        }

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boolean hasValidMarkers = false;

        for (int i = 0; i < latitudes.size(); i++) {
            try {
                double lat = latitudes.get(i);
                double lng = longitudes.get(i);
                String title = titles.get(i);

                LatLng position = new LatLng(lat, lng);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(title));

                if (marker != null) {
                    markerMap.put(marker.getId(), i);
                    boundsBuilder.include(position);
                    hasValidMarkers = true;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error adding marker " + i + ": " + e.getMessage());
            }
        }

        if (hasValidMarkers) {
            try {
                LatLngBounds bounds = boundsBuilder.build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            } catch (Exception e) {
                Log.e(TAG, "Error setting map bounds", e);
                centerMapOnHalifax();
            }
        } else {
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

            if (salaries != null && index < salaries.size()) {
                TextView salaryView = dialog.findViewById(R.id.salaryText);
                if (salaryView != null) {
                    salaryView.setText(String.format("$%d/hr", salaries.get(index)));
                }
            }

            if (durations != null && index < durations.size()) {
                TextView durationView = dialog.findViewById(R.id.durationText);
                if (durationView != null) durationView.setText(durations.get(index));
            }

            if (jobTypes != null && index < jobTypes.size()) {
                TextView jobTypeView = dialog.findViewById(R.id.jobTypeText);
                if (jobTypeView != null) jobTypeView.setText(jobTypes.get(index));
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
        Snackbar.make(findViewById(R.id.map_container), message, Snackbar.LENGTH_LONG).show();
    }
}
