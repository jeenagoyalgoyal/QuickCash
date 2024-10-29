package com.example.quickcash.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.content.Intent;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private ArrayList<String> latitudes;
    private ArrayList<String> longitudes;
    private ArrayList<String> titles;
    private ArrayList<String> salaries;
    private ArrayList<String> durations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Get the job data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            latitudes = intent.getStringArrayListExtra("latitudes");
            longitudes = intent.getStringArrayListExtra("longitudes");
            titles = intent.getStringArrayListExtra("titles");
            salaries = intent.getStringArrayListExtra("salaries");
            durations = intent.getStringArrayListExtra("durations");
        }

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Enable the location layer if permission is granted
        enableMyLocation();

        // Add job markers and setup bounds
        if (latitudes != null && longitudes != null && titles != null &&
                salaries != null && durations != null && !latitudes.isEmpty()) {

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            boolean hasValidMarkers = false;

            for (int i = 0; i < latitudes.size(); i++) {
                try {
                    double lat = Double.parseDouble(latitudes.get(i));
                    double lng = Double.parseDouble(longitudes.get(i));
                    double salary = Double.parseDouble(salaries.get(i));

                    LatLng position = new LatLng(lat, lng);

                    // Create marker with job details
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(position)
                            .title(titles.get(i))
                            .snippet(String.format("Salary: $%.2f\nDuration: %s",
                                    salary, durations.get(i)));

                    // Add marker to map
                    mMap.addMarker(markerOptions);

                    // Include this position in bounds calculation
                    builder.include(position);
                    hasValidMarkers = true;
                } catch (NumberFormatException | NullPointerException e) {
                    // Skip invalid coordinates
                    continue;
                }
            }

            // If we have valid markers, set bounds with padding
            if (hasValidMarkers) {
                final int padding = 100;
                final LatLngBounds bounds = builder.build();

                // Post to UI thread with delay to ensure map is properly initialized
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                    }
                });
            }
        }

        // Enable zoom controls and other UI settings
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        // Get current location after setting up markers
        getCurrentLocation();
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
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLatLng = new LatLng(location.getLatitude(),
                                        location.getLongitude());
                                // Only move to current location if we don't have any job markers
                                if (latitudes == null || latitudes.isEmpty()) {
                                    mMap.moveCamera(CameraUpdateFactory
                                            .newLatLngZoom(currentLatLng, 12));
                                }
                            }
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
            }
        }
    }
}