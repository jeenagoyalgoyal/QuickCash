package com.example.quickcash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {

    private final FusedLocationProviderClient fusedLocationClient;
    private final Context context;
    private final LocationCallback locationCallback;
    private LocationResultListener locationResultListener;
    private PermissionListener permissionListener;


    public interface LocationResultListener {
        void onLocationRetrieved(double latitude, double longitude, String address, String city);

        void onMapReady(GoogleMap googleMap);
    }

    public interface PermissionListener {
        void onPermissionDenied();
        void onPermissionGranted();
    }

    public LocationHelper(Context context, LocationResultListener listener) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.locationResultListener = listener;

        // Define the LocationCallback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                    double latitude = locationResult.getLastLocation().getLatitude();
                    double longitude = locationResult.getLastLocation().getLongitude();

                    // Use Geocoder to get the address and city
                    String address = getAddressFromCoordinates(latitude, longitude);
                    String city = getCityFromAddress(address);

                    // Pass the location data to the listener
                    if (locationResultListener != null) {
                        locationResultListener.onLocationRetrieved(latitude, longitude, address, city);
                    }

                    // Stop location updates to conserve battery
                    stopLocationUpdates();
                }
            }
        };
    }
    public void setPermissionListener(PermissionListener listener) {
        this.permissionListener = listener;
    }

    public boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        // Check for location permissions
        if (!isLocationPermissionGranted()) {
            // Request permissions if not granted
            if (context instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
            if (permissionListener != null) {
                permissionListener.onPermissionDenied();
            }
            return;
        }

        // Configure location request
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .setWaitForAccurateLocation(true)
                .build();

        // Request location updates
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    // Method to get address from latitude and longitude using Geocoder
    private String getAddressFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0); // Return the first line of the address
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Address not found";
    }
    private String getCityFromAddress(String address) {
        if (address != null && !address.isEmpty()) {
            String[] addressParts = address.split(",");
            if (addressParts.length > 1) {
                return addressParts[1].trim();
            }
        }
        return "Unknown";
    }
}
