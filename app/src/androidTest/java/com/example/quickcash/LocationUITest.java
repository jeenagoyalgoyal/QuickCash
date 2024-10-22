package com.example.quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import static org.hamcrest.CoreMatchers.containsString;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Test;

public class LocationUITest {

    public void grantLocationPermission() throws UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Look for the permission dialog and click "Allow"
        UiObject allowButton = device.findObject(new UiSelector().text("Allow"));
        if (allowButton.exists() && allowButton.isEnabled()) {
            allowButton.click();
        }
    }
    public void denyLocationPermission() throws UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Look for the permission dialog and click "Deny"
        UiObject denyButton = device.findObject(new UiSelector().text("Deny"));
        if (denyButton.exists() && denyButton.isEnabled()) {
            denyButton.click();
        }
    }


    @Test
    public void testLocationPermission(){
        ActivityScenario<MainActivity> scenario= ActivityScenario.launch(MainActivity.class);

        onView(withText("Allow QuickCash to access this device location?")).check(matches(isDisplayed()));
    }

    @Test
    public void testLocationDisplayedAfterPermissionGranted() throws UiObjectNotFoundException {
        // Assume permissions are granted
        grantLocationPermission();

        // Check that the location is displayed
        onView(withId(R.id.location_detect))
                .check(matches(withText(containsString("Lat:"))));
    }

    @Test
    public void testManualLocationInputWhenPermissionDenied() throws UiObjectNotFoundException {
        // Simulate permission denial
        denyLocationPermission();

        // Check if manual location input dialog is displayed
        onView(withText("Enter Your Location"))
                .check(matches(ViewMatchers.isDisplayed()));
    }

}
