package com.example.quickcash.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final String TAG = "MapActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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
                // Get coordinates
                latitudes = (ArrayList<Double>) intent.getSerializableExtra("latitudes");
                longitudes = (ArrayList<Double>) intent.getSerializableExtra("longitudes");
                titles = intent.getStringArrayListExtra("titles");
                salaries = intent.getIntegerArrayListExtra("salaries");
                durations = intent.getStringArrayListExtra("durations");
                companies = intent.getStringArrayListExtra("companies");
                jobTypes = intent.getStringArrayListExtra("jobTypes");

                // Debug logging
                Log.d(TAG, "Received data in MapActivity");
                if (latitudes != null && !latitudes.isEmpty()) {
                    Log.d(TAG, "Received " + latitudes.size() + " locations");
                    for (int i = 0; i < latitudes.size(); i++) {
                        Log.d(TAG, String.format("Location %d: %s at (%f, %f)",
                                i, titles.get(i), latitudes.get(i), longitudes.get(i)));
                    }
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

        mMap.setOnMarkerClickListener(this);

        // Enable location if permitted
        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
        }

        // Add markers to map
        addMarkersToMap();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
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

    private void showJobDetailsDialog(int index) {
        try {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_job_details);

            // Set dialog width to 90% of screen width
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(
                        (int)(getResources().getDisplayMetrics().widthPixels * 0.9),
                        WindowManager.LayoutParams.WRAP_CONTENT
                );
                window.setBackgroundDrawableResource(android.R.color.transparent);
            }

            // Set values safely with proper formatting
            safeSetText(dialog, R.id.jobTitleText, titles, index);
            safeSetText(dialog, R.id.companyNameText, companies, index);
            safeSetText(dialog, R.id.jobTypeText, jobTypes, index);

            // Format salary with currency
            if (salaries != null && index < salaries.size()) {
                TextView salaryView = dialog.findViewById(R.id.salaryText);
                if (salaryView != null) {
                    salaryView.setText(String.format("$%d/hr", salaries.get(index)));
                }
            }

            // Format duration
            safeSetText(dialog, R.id.durationText, durations, index);

            // Set up close button
            MaterialButton closeButton = dialog.findViewById(R.id.closeButton);
            if (closeButton != null) {
                closeButton.setOnClickListener(v -> dialog.dismiss());
            }

            dialog.show();
        } catch (Exception e) {
            Log.e(TAG, "Error showing dialog", e);
            showError("Error displaying job details");
        }
    }

    private void safeSetText(Dialog dialog, int viewId, ArrayList<String> data, int index) {
        if (dialog != null && data != null && index < data.size()) {
            TextView view = dialog.findViewById(viewId);
            if (view != null) {
                String text = data.get(index);
                if (text != null && !text.trim().isEmpty()) {
                    view.setText(text);
                } else {
                    view.setText("Not specified");
                }
            }
        }
    }

    private void addMarkersToMap() {
        if (latitudes == null || longitudes == null || titles == null ||
                latitudes.isEmpty() || longitudes.isEmpty() || titles.isEmpty()) {
            Log.e(TAG, "No location data available");
            showError("No job locations to display");

            // Center on Halifax by default
            LatLng halifax = new LatLng(44.6488, -63.5752);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halifax, 13f));
            return;
        }

        Log.d(TAG, "Adding " + latitudes.size() + " markers to map");
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boolean hasValidMarkers = false;

        for (int i = 0; i < latitudes.size(); i++) {
            try {
                double lat = latitudes.get(i);
                double lng = longitudes.get(i);
                String title = titles.get(i);

                Log.d(TAG, String.format("Processing marker %d: %s at (%f, %f)",
                        i, title, lat, lng));

                LatLng position = new LatLng(lat, lng);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(title));

                if (marker != null) {
                    markerMap.put(marker.getId(), i);
                    boundsBuilder.include(position);
                    hasValidMarkers = true;
                    Log.d(TAG, "Successfully added marker for: " + title);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error adding marker " + i + ": " + e.getMessage());
            }
        }

        if (hasValidMarkers) {
            try {
                LatLngBounds bounds = boundsBuilder.build();
                int padding = 100;
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(13f));
                Log.d(TAG, "Successfully set map bounds and zoom");
            } catch (Exception e) {
                Log.e(TAG, "Error setting map bounds: " + e.getMessage());
                // Fallback to Halifax center
                LatLng halifax = new LatLng(44.6488, -63.5752);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halifax, 13f));
            }
        } else {
            Log.e(TAG, "No valid markers were created");
            showError("No valid locations found");
            // Center on Halifax
            LatLng halifax = new LatLng(44.6488, -63.5752);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halifax, 13f));
        }
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
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
            } else {
                showError("Location permission is required for full functionality");
            }
        }
    }

    private void showError(String message) {
        Log.e(TAG, message);
        Snackbar.make(findViewById(R.id.map_container), message, Snackbar.LENGTH_LONG).show();
    }
}
