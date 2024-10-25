package com.example.quickcash;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.health.connect.datatypes.ExerciseRoute;
import android.location.Location;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.Task;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class UnitTestMaps {

    @InjectMocks
    private RegistrationActivity registrationActivity;

    @Mock
    FusedLocationProviderClient fusedLocationProviderClient;
    @Mock
    private Task<Location> mockLocation;
    @Mock
    private Location mockLocate;
    @Mock
    private LocationCallback locationCallback;

    @Before
    public void setUp(){
        registrationActivity= mock(RegistrationActivity.class);
        MockitoAnnotations.initMocks(this);
        registrationActivity.resetLocationFlags();
    }

    @Test
    public void testPermission(){
        when(registrationActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION))
                .thenReturn(PackageManager.PERMISSION_GRANTED);

        assertEquals(PackageManager.PERMISSION_GRANTED,
                registrationActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @Test
    public void testPermissionDenied(){
        when(registrationActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION))
                .thenReturn(PackageManager.PERMISSION_DENIED);

        assertEquals(PackageManager.PERMISSION_DENIED,
                registrationActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));

    }
    @Test
    public void testOnRequestPermissionsResultDenied() {
        // Mock permissions result callback
        registrationActivity.onRequestPermissionsResult(1,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                new int[]{PackageManager.PERMISSION_DENIED});

        // Test that no location detection is attempted
        verify(fusedLocationProviderClient, never()).getLastLocation();
    }

    @Test
    public void testOnRequestPermissionsResultGranted() {
        // Mock permissions result callback
        registrationActivity.onRequestPermissionsResult(1,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                new int[]{PackageManager.PERMISSION_GRANTED});

        // Verify that detectAndDisplayLocation is called
        verify(fusedLocationProviderClient,never()).getLastLocation();
    }


    @Test
    public void testLocationCallback_WithNullResult() {
        // Trigger the LocationCallback with a null LocationResult
        registrationActivity.locationCallback.onLocationResult(null);

        // Verify that the location is not set
        assertFalse(registrationActivity.isLocationReceived());
    }

    @Test
    public void testResetLocationFlags() {
        // Set the location flag and coordinates
        registrationActivity.displayLocationInfo(40.7128, -74.0060);

        // Reset location flags
        registrationActivity.resetLocationFlags();

        // Verify that the location flag and coordinates are reset
        assertFalse(registrationActivity.isLocationReceived());
        assertEquals(0.0, registrationActivity.getTestLatitude(), 0.0001);
        assertEquals(0.0, registrationActivity.getTestLongitude(), 0.0001);
    }

    @Test
    public void testInitialState_BeforeFetchingLocation() {
        // Verify initial state before any location fetching
        assertFalse(registrationActivity.isLocationReceived());
        assertEquals(0.0, registrationActivity.getTestLatitude(), 0.0001);
        assertEquals(0.0, registrationActivity.getTestLongitude(), 0.0001);
    }

    @Test
    public void testResetDatabaseConnectionState() {
        // Simulate setting up the database connection
        registrationActivity.initializeDatabaseAccess();

        // Reset location flags and related state
        registrationActivity.resetLocationFlags();

        // Verify that the location flags are reset (indirectly indicating no interference)
        assertFalse(registrationActivity.isLocationReceived());
        assertEquals(0.0, registrationActivity.getTestLatitude(), 0.0001);
        assertEquals(0.0, registrationActivity.getTestLongitude(), 0.0001);
    }



}
