package com.example.quickcash;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.runner.lifecycle.Stage.RESUMED;
import static org.junit.Assert.assertEquals;

import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import android.Manifest;

public class MapsUnitTests {


    @Before
    public void setup() {

        //launching the login activity to check for test case

        launch(LoginActivity.class);

    }

    //test case for permission on UI granted

    @Test
    public void testLocationPermissionNotGranted() {
        int permissionStatus = getApplicationContext()
                .checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        // Check if permission is denied (-1)
        assertEquals(PackageManager.PERMISSION_DENIED, permissionStatus);
    }



}
