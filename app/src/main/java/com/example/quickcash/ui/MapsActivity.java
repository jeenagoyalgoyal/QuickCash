package com.example.quickcash.ui;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.quickcash.EmployerHomepageActivity;
import com.example.quickcash.EmployeeHomepageActivity;
import com.example.quickcash.R;
import com.example.quickcash.model.UseRole;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.quickcash.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;

/**
 * MapsActivity is responsible for displaying a Google Map with the user's location.
 * The class manages location permission requests, retrieves the user's current or manual location,
 * and navigates to the appropriate dashboard based on the user's role after showing the map.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private double latitude;
    private double longitude;
    private String manualLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionDenied = false;
    private boolean isRoleFetched = false;
    private boolean locationRequested = false; // Flag to prevent repeated requests


    /**
     * Called when the activity is first created. Sets up the UI, initializes location services,
     * and requests location permissions if necessary.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        handleLocationSetup();

    }


    /**
     * Sets up the location by checking if a manual location is provided or if location permissions are granted.
     * If permissions are denied, it requests permissions; otherwise, it initializes the location.
     */
    private void handleLocationSetup() {
        Intent intent = getIntent();
        if (intent.hasExtra("manualLocation")) {
            manualLocation = intent.getStringExtra("manualLocation");
            setupManualLocation();
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionDenied = true;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initializeLocation();
        }
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
        if (manualLocation != null) {
            setupManualLocation();
        } else {
            initializeLocation();
        }

    }

    /**
     * Initializes the location service and retrieves the user's current location.
     * If a manual location is provided through the intent, it uses that instead.
     */
    private void initializeLocation() {
        Intent intent = getIntent();

        // Check if manual location is provided in the format "latitude,longitude"
        if (intent != null && intent.hasExtra("manualLocation")) {
            manualLocation = intent.getStringExtra("manualLocation");

            if (manualLocation != null) {
                String[] latLng = manualLocation.split(",");
                if (latLng.length == 2) {
                    try {
                        latitude = Double.parseDouble(latLng[0].trim());
                        longitude = Double.parseDouble(latLng[1].trim());
                        LatLng manualLatLng = new LatLng(latitude, longitude);

                        // If permission is granted, update the map with manual location
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            updateMapLocation(manualLatLng);
                            fetchUserRoleAndNavigate();
                        } else {
                            // Request permission if not granted, and set map to manual location while waiting
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                            updateMapLocation(manualLatLng);
                            fetchUserRoleAndNavigate();
                        }
                        return;
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid manual location format.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Invalid manual location format. Please use 'latitude,longitude'.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        // If no manual location is provided and permission is granted, fetch the current location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    LatLng userLatLng = new LatLng(latitude, longitude);
                    updateMapLocation(userLatLng);
                    fetchUserRoleAndNavigate();
                } else {
                    Toast.makeText(this, "current location.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to get location. Please try again.", Toast.LENGTH_SHORT).show();
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    /**
     * Fetches the user's role and navigates to the appropriate homepage.
     */
    private void fetchUserRoleAndNavigate() {
        Intent intent = getIntent();

        String email = intent != null ? intent.getStringExtra("email") : null;
        Log.d("Moving to dashboard with Email: ", email);

        if (email != null) {
            UseRole useRole = UseRole.getInstance();
            useRole.fetchUserRole(email, new UseRole.OnRoleFetchedListener() {
                @Override
                public void onRoleFetched(String role) {
                    if (role != null) {
                            navigateToHomepage(role, email);
                    } else {
                        Toast.makeText(MapsActivity.this, "Role not found.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    /**
     * Navigates to the appropriate homepage based on the user's role.
     * @param role The user's role (either "employee" or "employer").
     * @param email The user's email.
     */
    private void navigateToHomepage(String role, String email) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            if (role.equalsIgnoreCase("employee")) {
                intent = new Intent(MapsActivity.this, EmployeeHomepageActivity.class);
            } else if (role.equalsIgnoreCase("employer")) {
                intent = new Intent(MapsActivity.this, EmployerHomepageActivity.class);
            } else {
                Toast.makeText(this, "Role not recognized.", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("email", email);
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

    /**
     * Handles the result of location permission requests.
     * @param requestCode The request code passed in requestPermissions.
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the requested permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the current location if no manual location is set
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        LatLng userLatLng = new LatLng(latitude, longitude);
                        updateMapLocation(userLatLng);
                        fetchUserRoleAndNavigate();
                    } else {
                        Toast.makeText(this, "Unable to get current location.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Permission denied, use manual location if available
                if (manualLocation != null) {
                    String[] latLng = manualLocation.split(",");
                    latitude = Double.parseDouble(latLng[0].trim());
                    longitude = Double.parseDouble(latLng[1].trim());
                    LatLng manualLatLng = new LatLng(latitude, longitude);
                    updateMapLocation(manualLatLng);
                    fetchUserRoleAndNavigate();
                } else {
                    Toast.makeText(this, "Location permission denied. Please provide a manual location.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Sets up the map location based on the manually provided location.
     * If the manual location is a name, Geocoder is used to retrieve coordinates.
     */
    private void setupManualLocation() {
        if (manualLocation != null) {
            // Check if manualLocation contains a comma (indicating "latitude,longitude" format)
            if (manualLocation.contains(",")) {
                // Handle "latitude,longitude" format
                String[] latLng = manualLocation.split(",");
                if (latLng.length == 2) {
                    try {
                        double latitude = Double.parseDouble(latLng[0].trim());
                        double longitude = Double.parseDouble(latLng[1].trim());
                        LatLng manualLatLng = new LatLng(latitude, longitude);
                        updateMapLocation(manualLatLng);
                        fetchUserRoleAndNavigate();
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid manual location format.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // Use Geocoder to convert location name to coordinates
                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addresses = geocoder.getFromLocationName(manualLocation, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        double latitude = address.getLatitude();
                        double longitude = address.getLongitude();
                        LatLng manualLatLng = new LatLng(latitude, longitude);
                        updateMapLocation(manualLatLng);
                        fetchUserRoleAndNavigate();
                    } else {
                        Toast.makeText(this, "Location not found. Please enter a valid location name.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Unable to use Geocoder service. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * Updates the map with the given location, adding a marker and moving the camera.
     * @param location The LatLng object representing the location to display.
     */
    private void updateMapLocation(LatLng location) {
        if (mMap != null) {
            mMap.clear(); // Clear existing markers
            mMap.addMarker(new MarkerOptions().position(location).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15)); // Zoom to user's location
        }
    }
}