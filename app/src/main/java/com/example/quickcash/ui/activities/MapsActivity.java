    package com.example.quickcash.ui.activities;


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

    import com.example.quickcash.R;
    import com.google.android.gms.location.FusedLocationProviderClient;
    import com.google.android.gms.location.LocationServices;
    import com.google.android.gms.maps.CameraUpdateFactory;
    import com.google.android.gms.maps.GoogleMap;
    import com.google.android.gms.maps.OnMapReadyCallback;
    import com.google.android.gms.maps.SupportMapFragment;
    import com.google.android.gms.maps.model.LatLng;
    import com.google.android.gms.maps.model.MarkerOptions;
    import com.example.quickcash.databinding.ActivityMapsBinding;


    /**
     * MapsActivity handles displaying the Google Map, obtaining the user's current location,
     * and navigating to the EmployeeHomepageActivity after a short delay.
     */
    public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

        private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
        private GoogleMap mMap;
        private ActivityMapsBinding binding;
        private double latitude;
        private double longitude;
        private String manualLocation;
        private FusedLocationProviderClient fusedLocationClient;


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


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                initializeLocation();
            }


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

            LatLng sydney = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
        }

        /**
         * Initializes the location service and retrieves the user's current location.
         * If a manual location is provided through the intent, it uses that instead.
         */
        private void initializeLocation() {
            try {
                Intent intent = getIntent();
                if (intent != null && intent.hasExtra("manualLocation")) {
                    manualLocation = intent.getStringExtra("manualLocation");
                    latitude = -34.0;
                    longitude = 151.0;
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

        /**
         * Navigates to the EmployeeHomepageActivity after a short delay.
         * Passes either the manual location or the latitude and longitude as extras in the intent.
         * @param manualLocation The manually entered location, if any.
         */
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
                    initializeLocation();
                } else {
                    Toast.makeText(this, "Location permission is required to use this feature", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
