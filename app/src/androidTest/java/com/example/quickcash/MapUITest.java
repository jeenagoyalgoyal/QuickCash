package com.example.quickcash;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MapUITest {
    private static final long LAUNCH_TIMEOUT = 2500;
    private static final String JOB_TITLE = "Software Developer"; // Changed to match existing data
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

    @Test
    public void checkIfLandingPageIsVisible() {
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        assertTrue(emailBox.exists());
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        assertTrue(passwordBox.exists());
    }

    private void logIn() throws UiObjectNotFoundException {
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testingemail@test.db");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Test_Pass123#");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();

        UiObject searchJobButton = device.findObject(new UiSelector().text("Search Job"));
        searchJobButton.clickAndWaitForNewWindow();
    }

    @Test
    public void checkIfJobSearchHasShowMapsButton() throws UiObjectNotFoundException {
        logIn();

        UiObject showMapButton = device.findObject(new UiSelector().text("Show Map"));
        assertTrue("Show Maps Button should exist", showMapButton.exists());
    }

    @Test
    public void checkIfClickingMarkerOpensPopUp() throws UiObjectNotFoundException {
        logIn();

        // Find and fill job title that exists in test data
        UiObject jobTitleBox = device.findObject(new UiSelector().text("Enter Job Title"));
        jobTitleBox.setText(JOB_TITLE);
        UiObject showMapButton = device.findObject(new UiSelector().text("Show Map"));
        showMapButton.click();

        // Wait for permissions dialog if it appears
        try {
            UiObject locationPermissionButton = device.findObject(new UiSelector().text("While using the app"));
            if (locationPermissionButton.exists()) {
                locationPermissionButton.click();
            }
        } catch (UiObjectNotFoundException ignored) {
            // Permission already granted, continue
        }

        // Wait for map to load and markers to appear
        device.wait(Until.hasObject(By.descContains(JOB_TITLE)), 5000);

        // Click the marker
        UiObject marker = device.findObject(new UiSelector().descriptionContains(JOB_TITLE));
        assertTrue("Job marker should be visible on map", marker.exists());
        marker.click();

        // Verify dialog appears
        UiObject dialog = device.findObject(new UiSelector().text("Job"));
        assertTrue("Job details dialog should be visible", dialog.exists());
    }

    @Test
    public void checkIfClickingCloseandBackReturnsToSearchPage() throws UiObjectNotFoundException {
        logIn();

        UiObject jobTitleBox = device.findObject(new UiSelector().text("Enter Job Title"));
        jobTitleBox.setText(JOB_TITLE);
        UiObject showMapButton = device.findObject(new UiSelector().text("Show Map"));
        showMapButton.click();

        // Handle permissions
        try {
            UiObject locationPermissionButton = device.findObject(new UiSelector().text("While using the app"));
            if (locationPermissionButton.exists()) {
                locationPermissionButton.click();
            }
        } catch (UiObjectNotFoundException ignored) {
            // Permission already granted, continue
        }

        // Wait for map and marker to appear
        device.wait(Until.hasObject(By.descContains(JOB_TITLE)), 5000);

        UiObject marker = device.findObject(new UiSelector().descriptionContains(JOB_TITLE));
        assertTrue("Job marker should be visible on map", marker.exists());
        marker.click();

        // Verify and interact with dialog
        UiObject closeButton = device.findObject(new UiSelector().text("Close"));
        assertTrue("Close button should be visible", closeButton.exists());
        closeButton.click();

        UiObject backButton = device.findObject(new UiSelector().resourceId("com.example.quickcash:id/backButton"));
        assertTrue("Back button should be visible", backButton.exists());
        backButton.click();

        // Verify return to search page
        UiObject showMapButton2 = device.findObject(new UiSelector().text("Show Map"));
        assertTrue("Should return to page with Show Map button", showMapButton2.exists());
    }
}