package com.example.quickcash;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MapUITest {
    private static final String TAG = "MapUITest";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String JOB_TITLE = "ABCDMAP"; // Use an existing job title
    final String launcherPackageName = "com.example.quickcash";
    private UiDevice device;

    @Before
    public void setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        Intent launcherIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackageName);
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(launcherIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackageName).depth(0)), LAUNCH_TIMEOUT);
    }

    private void logIn() throws UiObjectNotFoundException {
        // Login
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testingemail@test.db");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Test_Pass123#");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.click();

        // Wait for login to complete and dashboard to load
        device.wait(Until.hasObject(By.text("Search Job")), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkIfClickingMarkerOpensPopUp() throws UiObjectNotFoundException {
        Log.d(TAG, "Starting test: checkIfClickingMarkerOpensPopUp");

        // Login first
        logIn();
        Log.d(TAG, "Successfully logged in");

        // Click Search Job
        UiObject searchJobButton = device.findObject(new UiSelector().text("Search Job"));
        searchJobButton.click();
        Log.d(TAG, "Clicked Search Job button");

        // Wait for search screen and job title input
        UiObject jobTitleInput = device.findObject(new UiSelector().className("android.widget.EditText").instance(0));
        assertTrue("Job title input should be visible", jobTitleInput.waitForExists(LAUNCH_TIMEOUT));

        // Enter job search criteria
        jobTitleInput.setText(JOB_TITLE);
        Log.d(TAG, "Entered job title: " + JOB_TITLE);

        // Click Search button
        UiObject searchButton = device.findObject(new UiSelector().text("Search"));
        searchButton.click();
        Log.d(TAG, "Clicked Search button");

        // Wait for results
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.e(TAG, "Sleep interrupted", e);
        }

        // Click individual job's Show Map button
        UiObject showMapButton = device.findObject(new UiSelector().text("Show Map"));
        assertTrue("Show Map button should be visible", showMapButton.waitForExists(LAUNCH_TIMEOUT));
        showMapButton.click();
        Log.d(TAG, "Clicked Show Map button");

        // Handle permissions if needed
        try {
            UiObject permissionButton = device.findObject(new UiSelector().text("While using the app"));
            if (permissionButton.exists()) {
                permissionButton.click();
                Log.d(TAG, "Handled location permission");
            }
        } catch (UiObjectNotFoundException e) {
            Log.d(TAG, "No permission dialog found");
        }

        // Wait for map to load
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.e(TAG, "Sleep interrupted", e);
        }

        // Try to find and click marker
        boolean markerFound = false;

        // Try finding by job title description
        try {
            UiObject marker = device.findObject(new UiSelector()
                    .className("android.view.View")
                    .descriptionContains(JOB_TITLE));
            if (marker.exists()) {
                Log.d(TAG, "Found marker by job title");
                marker.click();
                markerFound = true;
            }
        } catch (UiObjectNotFoundException e) {
            Log.d(TAG, "Marker not found by job title");
        }

        // Try finding by Google Maps marker
        if (!markerFound) {
            try {
                UiObject marker = device.findObject(new UiSelector()
                        .className("android.view.View")
                        .descriptionContains("Marker"));
                if (marker.exists()) {
                    Log.d(TAG, "Found marker by generic description");
                    marker.click();
                    markerFound = true;
                }
            } catch (UiObjectNotFoundException e) {
                Log.d(TAG, "Marker not found by generic description");
            }
        }

        assertTrue("Job marker should be visible on map", markerFound);

        // Verify job details dialog
        UiObject dialog = device.findObject(new UiSelector().text("Job"));
        assertTrue("Job details dialog should be visible", dialog.exists());
        Log.d(TAG, "Successfully verified dialog");
    }

    @Test
    public void checkIfClickingBackReturnsToSearchPage() throws UiObjectNotFoundException {
        // Login and navigate
        logIn();

        // Click Search Job
        UiObject searchJobButton = device.findObject(new UiSelector().text("Search Job"));
        searchJobButton.click();

        // Wait for search screen
        UiObject jobTitleInput = device.findObject(new UiSelector().className("android.widget.EditText").instance(0));
        assertTrue("Job title input should be visible", jobTitleInput.waitForExists(LAUNCH_TIMEOUT));

        // Enter job criteria and search
        jobTitleInput.setText(JOB_TITLE);

        UiObject searchButton = device.findObject(new UiSelector().text("Search"));
        searchButton.click();

        // Wait for results
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click Show Map
        UiObject showMapButton = device.findObject(new UiSelector().text("Show Map"));
        assertTrue("Show Map button should be visible", showMapButton.waitForExists(LAUNCH_TIMEOUT));
        showMapButton.click();

        // Wait for map
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Try multiple approaches to find the back button
        boolean backButtonFound = false;
        UiObject backButton = null;

        // Try by ID
        backButton = device.findObject(new UiSelector()
                .resourceId("com.example.quickcash:id/backButton"));
        if (backButton.exists()) {
            backButtonFound = true;
        }

        // Try by class and text
        if (!backButtonFound) {
            backButton = device.findObject(new UiSelector()
                    .className("android.widget.Button")
                    .text("Back"));
            if (backButton.exists()) {
                backButtonFound = true;
            }
        }

        // Try by class and index
        if (!backButtonFound) {
            backButton = device.findObject(new UiSelector()
                    .className("android.widget.Button")
                    .instance(0));
            if (backButton.exists()) {
                backButtonFound = true;
            }
        }

        assertTrue("Back button should be visible", backButtonFound);
        backButton.click();

        // Wait for navigation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify return to search page
        UiObject jobTitleField = device.findObject(new UiSelector().text("Job Title"));
        assertTrue("Should return to search page", jobTitleField.waitForExists(LAUNCH_TIMEOUT));
    }
}