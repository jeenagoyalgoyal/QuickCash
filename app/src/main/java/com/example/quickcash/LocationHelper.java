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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LocationHelper {
    private static final String TAG = "LocationHelper";
    private static final long UPDATE_INTERVAL = 10000;  // 10 seconds
    private static final long FASTEST_INTERVAL = 5000;  // 5 seconds

    // Cache for common city coordinates
    private static final Map<String, GeocodingResult> CITY_COORDINATES = new HashMap<>();

    static {
        CITY_COORDINATES.put("halifax", new GeocodingResult(44.6488, -63.5752));
        CITY_COORDINATES.put("montreal", new GeocodingResult(45.5017, -73.5673));
        CITY_COORDINATES.put("toronto", new GeocodingResult(43.6532, -79.3832));
        CITY_COORDINATES.put("vancouver", new GeocodingResult(49.2827, -123.1207));
        CITY_COORDINATES.put("ottawa", new GeocodingResult(45.4215, -75.6972));
        CITY_COORDINATES.put("calgary", new GeocodingResult(51.0447, -114.0719));
        CITY_COORDINATES.put("edmonton", new GeocodingResult(53.5461, -113.4938));
    }

    public static GeocodingResult getCoordinates(Context context, String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            Log.e(TAG, "Location name is null or empty");
            return CITY_COORDINATES.get("halifax"); // Default to Halifax
        }

        String normalizedLocation = locationName.trim().toLowerCase();

        // First check our cached city coordinates with partial matching
        for (Map.Entry<String, GeocodingResult> entry : CITY_COORDINATES.entrySet()) {
            if (normalizedLocation.contains(entry.getKey()) ||
                    entry.getKey().contains(normalizedLocation)) {
                Log.d(TAG, "Found cached coordinates for " + locationName);
                return entry.getValue();
            }
        }

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                final GeocodingResult[] result = new GeocodingResult[1];

                geocoder.getFromLocationName(locationName, 1, -90, -180, 90, 180,
                        new Geocoder.GeocodeListener() {
                            @Override
                            public void onGeocode(@NonNull List<Address> addresses) {
                                if (!addresses.isEmpty()) {
                                    Address address = addresses.get(0);
                                    double latitude = address.getLatitude();
                                    double longitude = address.getLongitude();

                                    // Cache all components of the address
                                    if (address.getLocality() != null) {
                                        CITY_COORDINATES.put(address.getLocality().toLowerCase(),
                                                new GeocodingResult(latitude, longitude));
                                    }
                                    if (address.getAdminArea() != null) {
                                        CITY_COORDINATES.put(address.getAdminArea().toLowerCase(),
                                                new GeocodingResult(latitude, longitude));
                                    }

                                    result[0] = new GeocodingResult(latitude, longitude);
                                    Log.d(TAG, String.format("Geocoding result: (%.6f, %.6f)",
                                            latitude, longitude));
                                }
                            }
                        });

                if (result[0] != null) {
                    return result[0];
                }
            } else {
                addresses = geocoder.getFromLocationName(locationName, 1, -90, -180, 90, 180);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    double latitude = address.getLatitude();
                    double longitude = address.getLongitude();

                    // Cache all components of the address
                    if (address.getLocality() != null) {
                        CITY_COORDINATES.put(address.getLocality().toLowerCase(),
                                new GeocodingResult(latitude, longitude));
                    }
                    if (address.getAdminArea() != null) {
                        CITY_COORDINATES.put(address.getAdminArea().toLowerCase(),
                                new GeocodingResult(latitude, longitude));
                    }

                    Log.d(TAG, String.format("Found coordinates for %s: (%.6f, %.6f)",
                            locationName, latitude, longitude));

                    return new GeocodingResult(latitude, longitude);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error getting coordinates for " + locationName, e);
        }

        // Default to Halifax coordinates if everything fails
        Log.w(TAG, "Defaulting to Halifax coordinates for: " + locationName);
        return CITY_COORDINATES.get("halifax");
    }

    public interface CustomLocationCallback {
        void onLocationReceived(double latitude, double longitude);
        void onLocationError(String error);
    }

    public static class GeocodingResult {
        private final double latitude;
        private final double longitude;

        public GeocodingResult(double latitude, double longitude) {
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