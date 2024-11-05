package com.example.quickcash;

import android.Manifest;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {

    private final FusedLocationProviderClient fusedLocationClient;
    private final Context context;
    private final LocationCallback locationCallback;
    private LocationResultListener locationResultListener;

    public interface LocationResultListener {
        void onLocationRetrieved(double latitude, double longitude, String address);
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

                    // Use Geocoder to get the address
                    String address = getAddressFromCoordinates(latitude, longitude);

                    // Pass the location data to the listener
                    if (locationResultListener != null) {
                        locationResultListener.onLocationRetrieved(latitude, longitude, address);
                    }

                    // Stop location updates to conserve battery
                    stopLocationUpdates();
                }
            }
        };
    }

    public void getCurrentLocation() {
        // Check for location permissions
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            if (context instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
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
}
