package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.location.Location;


import org.junit.Before;
import org.junit.Test;

public class LocationServiceUnitTests {
    private LocationService location;

    @Before
    public void setUp(){
        location= mock(LocationService.class);
    }

    @Test
    public void testCurrentLocation(){
        Location mockLoc= new Location("");
        mockLoc.setLatitude(37.7749);
        mockLoc.setLongitude(-122.4194);

        when(location.getCurrentLocation()).thenReturn(mockLoc);
        Location current = location.getCurrentLocation();

        assertEquals(37.7749, current.getLatitude(), 0.001);
        assertEquals(-122.4194, current.getLongitude(), 0.001);
    }


}
