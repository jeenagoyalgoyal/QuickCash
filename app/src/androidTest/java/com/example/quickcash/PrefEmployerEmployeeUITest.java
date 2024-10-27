package com.example.quickcash;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
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
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class PrefEmployerEmployeeUITest {
    private static final int LAUNCH_TIMEOUT = 60;
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

    @Test
    public void testAddToPreferredEmployersButton() throws UiObjectNotFoundException {
        // Log in
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testuser@test.com");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("password123");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();

        // Navigate to the search page
        UiObject searchJobButton = device.findObject(new UiSelector().text("Search Jobs"));
        searchJobButton.clickAndWaitForNewWindow();

        // Click on a job listing to open options
        UiObject jobListingOptions = device.findObject(new UiSelector().text("Options"));
        jobListingOptions.click();

        // Check if "Add to Preferred Employers" button is visible in the popup
        UiObject addToPreferredEmployersButton = device.findObject(new UiSelector().text("Add to Preferred Employers"));
        assertTrue("Add to Preferred Employers button should be visible", addToPreferredEmployersButton.exists());
    }

    @Test
    public void testPreferredEmployersButtonOnDashboard() throws UiObjectNotFoundException {
        // Log in
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testuser@test.com");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("password123");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();

        // Navigate to the employee dashboard
        UiObject dashboardButton = device.findObject(new UiSelector().text("Dashboard"));
        dashboardButton.clickAndWaitForNewWindow();

        // Check if "My Preferred Employers" button is visible
        UiObject myPreferredEmployersButton = device.findObject(new UiSelector().text("My Preferred Employers"));
        assertTrue("My Preferred Employers button should be visible on dashboard", myPreferredEmployersButton.exists());

        // Click on "My Preferred Employers" and verify the navigation to the preferred employers list page
        myPreferredEmployersButton.clickAndWaitForNewWindow();
        UiObject preferredEmployersPageTitle = device.findObject(new UiSelector().text("Preferred Employers"));
        assertTrue("User should navigate to Preferred Employers page", preferredEmployersPageTitle.exists());
    }
}
