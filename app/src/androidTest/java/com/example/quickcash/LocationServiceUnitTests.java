package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LocationServiceUnitTests {
    private LocationService location;
    private Activity mockActivity;
    private LocationManager mockLocMan;

    @Before
    public void setUp(){
        mockActivity= Mockito.mock(Activity.class);
        mockLocMan= Mockito.mock(LocationManager.class);
        Mockito.when(mockActivity.getSystemService(Context.LOCATION_SERVICE)).thenReturn(mockLocMan);
        location= new LocationService(mockActivity);
    }

    @Test
    public void testIsLocationEnabled_GPSEnabled() {
        Mockito.when(mockLocMan.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true);
        Mockito.when(mockLocMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(false);

        assertTrue(location.isLocationEnabled());
    }

    @Test
    public void testIsLocationEnabled_NetworkEnabled() {
        Mockito.when(mockLocMan.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false);
        Mockito.when(mockLocMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(true);

        assertTrue(location.isLocationEnabled());
    }


    @Test
    public void testIsLocationEnabled_NeitherEnabled() {
        Mockito.when(mockLocMan.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false);
        Mockito.when(mockLocMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(false);

        assertFalse(location.isLocationEnabled());
    }

    @Test
    public void testGetCurrentLocation() {
        // Create a mock Location object
        Location mockLocation = new Location("");
        mockLocation.setLatitude(37.7749);
        mockLocation.setLongitude(-122.4194);

        // Simulate location change
        location.onLocationChanged(mockLocation);

        // Verify that getCurrentLocation returns the correct location
        Location currentLocation = location.getCurrentLocation();
        assertTrue(currentLocation.getLatitude() == 37.7749);
        assertTrue(currentLocation.getLongitude() == -122.4194);
    }
}
