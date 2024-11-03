package com.example.quickcash.ui.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {

    public static class LocationResult {
        public final double latitude;
        public final double longitude;
        public final String errorMessage;

        public LocationResult(double latitude, double longitude, String errorMessage) {
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

    /**
     * Existing method that returns a LocationResult object containing latitude and longitude.
     */
    public static LocationResult getCoordinates(Context context, String locationString) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            List<Address> addresses = geocoder.getFromLocationName(locationString, 5);
            if (addresses != null && !addresses.isEmpty()) {
                for (Address address : addresses) {
                    // Check if this address has valid coordinates
                    if (address.hasLatitude() && address.hasLongitude()) {
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

    /**
     * New method that returns a LatLng object representing the coordinates of the given address.
     */
    public static LatLng getCoordinatesFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (!addresses.isEmpty()) {
                Address addr = addresses.get(0);
                if (addr.hasLatitude() && addr.hasLongitude()) {
                    return new LatLng(addr.getLatitude(), addr.getLongitude());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
