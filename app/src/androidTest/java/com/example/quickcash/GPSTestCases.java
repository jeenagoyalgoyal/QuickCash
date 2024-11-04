package com.example.quickcash;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.quickcash.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class GPSTestCases {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void setUp() {
        // Launch the activity before running the tests
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);
    }



    @Test
    public void testLocationPermissionGranted() {
        // Verify that the location permission is granted
        int permissionStatus = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        assertEquals(PackageManager.PERMISSION_GRANTED, permissionStatus);
    }

    @Test
    public void testAutomaticLocationDetection() {
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);

        scenario.onActivity(activity -> {
            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(activity);

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);

            client.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Verify that the detected latitude and longitude are within a valid range
                    assertTrue(latitude >= -90 && latitude <= 90);
                    assertTrue(longitude >= -180 && longitude <= 180);
                }
            });
        });
    }

}
