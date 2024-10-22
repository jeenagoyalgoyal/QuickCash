package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class UnitTestMaps {

    @InjectMocks
    private MainActivity mainActivity;

    @Mock
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Before
    public void setUp(){
        mainActivity= Mockito.mock(MainActivity.class);
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
    public void testLocationPermissionGranted() {
        // Mock the permission check
        when(ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION))
                .thenReturn(PackageManager.PERMISSION_GRANTED);

        // Call the method to check permission
        mainActivity.onCreate(null);

        // Verify that detectAndDisplayLocation() is called
        verify(fusedLocationProviderClient).getLastLocation();
    }
}
