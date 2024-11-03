package com.example.quickcash.ui;

import static com.example.quickcash.RegistrationActivity.LOCATION_PERMISSION_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.Manifest;
import android.widget.Toast;

import com.example.quickcash.EmployeeHomepageActivity;
import com.example.quickcash.LocationHelper;
import com.example.quickcash.LoginActivity;
import com.example.quickcash.R;
import com.example.quickcash.RoleActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.quickcash.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private double latitude;
    private double longitude;
    private String manualLocation;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions are already granted, proceed with getting the location
            initializeLocation();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
    }

    private void initializeLocation() {
//        if (getIntent() != null && getIntent().hasExtra("latitude") && getIntent().hasExtra("longitude")) {
//            latitude = getIntent().getDoubleExtra("latitude", -34); // Default value: -34
//            longitude = getIntent().getDoubleExtra("longitude", 151); // Default value: 151
//        } else {
//            // Manual location detection using LocationHelper
//            LocationHelper locationHelper = new LocationHelper(this, this::onLocationRetrieved);
//            locationHelper.getCurrentLocation();
//        }
        try {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("manualLocation")) {
                manualLocation = intent.getStringExtra("manualLocation");
                // Set a default location for demo purposes (you may convert this to lat/lng)
                latitude = -34.0; // Example latitude for manual location
                longitude = 151.0; // Example longitude for manual location
                moveToNextWithDelay(manualLocation);
            } else {
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        moveToNextWithDelay(null);
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to get location. Please try again.", Toast.LENGTH_SHORT).show();
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Location permission is required to access your location.", Toast.LENGTH_LONG).show();
        }
    }


    public void onLocationRetrieved(double lat, double lon, String address) {
        // Use FusedLocationProviderClient to get the location
        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    // Update latitude and longitude with the values from FusedLocationProviderClient
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    manualLocation = address;

                    // Update the map with the new location
                    if (mMap != null) {
                        LatLng latLng = new LatLng(latitude, longitude);
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Detected Location").snippet(manualLocation));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }

                    // Move to the RoleActivity after a delay
                    moveToNextWithDelay(manualLocation);
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Location permission is required to access your location", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveToNextWithDelay(String manualLocation) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(MapsActivity.this, EmployeeHomepageActivity.class);
            if (manualLocation != null) {
                intent.putExtra("manualLocation", manualLocation);
            } else {
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
            }
            startActivity(intent);
            finish();
        }, 3000);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, initialize location
                initializeLocation();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Location permission is required to use this feature", Toast.LENGTH_LONG).show();
            }
        }
    }
}
