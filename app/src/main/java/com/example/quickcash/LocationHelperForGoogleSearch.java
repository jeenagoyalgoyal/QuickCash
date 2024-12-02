package com.example.quickcash;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.List;
import java.util.Locale;

public class LocationHelperForGoogleSearch {
    private static final String TAG = "LocationHelperForGoogleSearch";
    private static final long UPDATE_INTERVAL = 10000;  // 10 seconds
    private static final long FASTEST_INTERVAL = 5000;  // 5 seconds

    /**
     * Gets coordinates for any address worldwide
     *
     * @param context      The application context
     * @param locationName The address to geocode
     * @return GeocodingResult containing the coordinates
     */
    public static GeocodingResult getCoordinates(Context context, String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            Log.e(TAG, "Location name is null or empty");
            return null;
        }

        String searchLocation = locationName.trim();
        Log.d(TAG, "Attempting to geocode address: " + searchLocation);

        try {
            // Use the system's default locale for better regional address handling
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                return geocodeAddressModernApi(geocoder, searchLocation);
            } else {
                return geocodeAddressLegacyApi(geocoder, searchLocation);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error geocoding location: " + searchLocation, e);
            return handleGeocodingError(searchLocation);
        }
    }

    /**
     * Geocodes address using modern Android API (Tiramisu and above)
     */
    private static GeocodingResult geocodeAddressModernApi(Geocoder geocoder, String searchLocation) {
        try {
            final GeocodingResult[] result = new GeocodingResult[1];

            geocoder.getFromLocationName(searchLocation, 1, new Geocoder.GeocodeListener() {
                @Override
                public void onGeocode(@NonNull List<Address> addresses) {
                    if (!addresses.isEmpty()) {
                        result[0] = processAddress(addresses.get(0), searchLocation);
                    }
                }
            });

            // Wait briefly for the result
            int attempts = 0;
            while (result[0] == null && attempts < 5) {
                Thread.sleep(FASTEST_INTERVAL);
                attempts++;
            }

            if (result[0] != null) {
                return result[0];
            }

            Log.w(TAG, "Could not geocode address using modern API: " + searchLocation);
            return handleGeocodingError(searchLocation);

        } catch (Exception e) {
            Log.e(TAG, "Error in modern geocoding: " + e.getMessage());
            return handleGeocodingError(searchLocation);
        }
    }

    /**
     * Geocodes address using legacy Android API (pre-Tiramisu)
     */
    private static GeocodingResult geocodeAddressLegacyApi(Geocoder geocoder, String searchLocation) {
        try {
            List<Address> addresses = geocoder.getFromLocationName(searchLocation, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return processAddress(addresses.get(0), searchLocation);
            }

            Log.w(TAG, "Could not geocode address using legacy API: " + searchLocation);
            return handleGeocodingError(searchLocation);

        } catch (Exception e) {
            Log.e(TAG, "Error in legacy geocoding: " + e.getMessage());
            return handleGeocodingError(searchLocation);
        }
    }

    /**
     * Processes a geocoded address and creates a GeocodingResult
     */
    private static GeocodingResult processAddress(Address address, String originalLocation) {
        double latitude = address.getLatitude();
        double longitude = address.getLongitude();

        // Log the full address details for debugging
        StringBuilder addressDetails = new StringBuilder();
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            addressDetails.append(address.getAddressLine(i));
            if (i < address.getMaxAddressLineIndex()) {
                addressDetails.append(", ");
            }
        }

        Log.d(TAG, String.format("Successfully geocoded '%s' to: (%.6f, %.6f)",
                originalLocation, latitude, longitude));
        Log.d(TAG, "Full address details: " + addressDetails.toString());

        return new GeocodingResult(latitude, longitude, addressDetails.toString());
    }

    /**
     * Handles geocoding errors
     */
    private static GeocodingResult handleGeocodingError(String location) {
        Log.e(TAG, "Could not geocode location: " + location);
        return null;
    }

    public static class GeocodingResult {
        private final double latitude;
        private final double longitude;
        private final String formattedAddress;

        public GeocodingResult(double latitude, double longitude, String formattedAddress) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.formattedAddress = formattedAddress;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getFormattedAddress() {
            return formattedAddress;
        }
    }

    /**
     * Validates if coordinates are within valid ranges
     */
    public static boolean areValidCoordinates(double latitude, double longitude) {
        return latitude >= -90 && latitude <= 90 &&
                longitude >= -180 && longitude <= 180;
    }

    public interface CustomLocationCallback {
        void onLocationReceived(double latitude, double longitude);

        void onLocationError(String error);
    }

    /**
     * Gets the current device location
     */
    public static void getCurrentLocation(Context context, CustomLocationCallback callback) {
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            callback.onLocationError("Location permission not granted");
            if (context instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
            return;
        }

        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context);

        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            callback.onLocationError("Location services are disabled");
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL)
                .setMinUpdateIntervalMillis(FASTEST_INTERVAL)
                .build();

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult.getLocations().isEmpty()) {
                    callback.onLocationError("Location result is empty");
                    return;
                }
                android.location.Location location = locationResult.getLocations().get(0);
                callback.onLocationReceived(location.getLatitude(), location.getLongitude());
                fusedLocationClient.removeLocationUpdates(this);
            }
        };

        try {
            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
            );

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            callback.onLocationReceived(
                                    location.getLatitude(),
                                    location.getLongitude()
                            );
                            fusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    });
        } catch (SecurityException e) {
            callback.onLocationError("Security exception: " + e.getMessage());
        }
    }
}