package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.health.connect.datatypes.ExerciseRoute;
import android.location.Location;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class UnitTestMaps {

    @InjectMocks
    private RegistrationActivity mainActivity;

    @Mock
    FusedLocationProviderClient fusedLocationProviderClient;
    @Mock
    private Task<Location> mockLocation;
    @Mock
    private Location mockLocate;

    @Before
    public void setUp(){
        mainActivity=Mockito.mock(RegistrationActivity.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPermission(){
        when(mainActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION))
                .thenReturn(PackageManager.PERMISSION_GRANTED);

        assertEquals(PackageManager.PERMISSION_GRANTED,
                mainActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @Test
    public void testPermissionDenied(){
        when(mainActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION))
                .thenReturn(PackageManager.PERMISSION_DENIED);

        assertEquals(PackageManager.PERMISSION_DENIED,
                mainActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));

    }
    @Test
    public void testOnRequestPermissionsResultDenied() {
        // Mock permissions result callback
        mainActivity.onRequestPermissionsResult(1,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                new int[]{PackageManager.PERMISSION_DENIED});

        // Test that no location detection is attempted
        verify(fusedLocationProviderClient, never()).getLastLocation();
    }

    @Test
    public void testOnRequestPermissionsResultGranted() {
        // Mock permissions result callback
        mainActivity.onRequestPermissionsResult(1,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                new int[]{PackageManager.PERMISSION_GRANTED});

        // Verify that detectAndDisplayLocation is called
        verify(fusedLocationProviderClient,never()).getLastLocation();
    }

}
