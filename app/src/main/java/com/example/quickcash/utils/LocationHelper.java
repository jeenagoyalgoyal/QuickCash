package com.example.quickcash.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;

public class LocationHelper {

    public static class LocationResult {
        public final double latitude;
        public final double longitude;
        public final String errorMessage;

        private LocationResult(double latitude, double longitude, String errorMessage) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.errorMessage = errorMessage;
        }
        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public boolean isSuccess() {
            return errorMessage == null;
        }
    }

    public static LocationResult getCoordinates(Context context, String locationString) {
        try {

            Geocoder geocoder = new Geocoder(context);

            List<Address> addresses = geocoder.getFromLocationName(locationString, 5);
            if (addresses != null && !addresses.isEmpty()) {
                for (Address address : addresses) {
                    // Check if this address has valid coordinates
                    if (address.getLatitude() != 0 && address.getLatitude() != 0) {
                        return new LocationResult(
                                address.getLatitude(),
                                address.getLongitude(),
                                null
                        );
                    }
                }
            }

            return new LocationResult(0, 0, "Location not found");

        } catch (IOException e) {
            return new LocationResult(0, 0, "Error getting location");
        }
    }
}