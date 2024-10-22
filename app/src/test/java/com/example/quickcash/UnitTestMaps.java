package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import android.Manifest;
import android.content.pm.PackageManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UnitTestMaps {

    private MainActivity mainActivity;

    @Before
    public void setUp(){
        mainActivity= Mockito.mock(MainActivity.class);
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
}
