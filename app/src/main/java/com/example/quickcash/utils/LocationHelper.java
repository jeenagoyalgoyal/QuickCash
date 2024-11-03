package com.example.quickcash.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {
    private static final String TAG = "LocationHelper";

    public static class LocationResult {
        public final double latitude;  // Changed to public
        public final double longitude; // Changed to public

        public LocationResult(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public static LocationResult getCoordinates(Context context, String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            Log.e(TAG, "Location name is null or empty");
            return new LocationResult(0, 0);
        }

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                Log.d(TAG, String.format("Found coordinates for %s: (%.6f, %.6f)",
                        locationName, latitude, longitude));

                return new LocationResult(latitude, longitude);
            } else {
                Log.e(TAG, "No coordinates found for location: " + locationName);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error getting coordinates for " + locationName, e);
        }

        return new LocationResult(0, 0);
    }
}