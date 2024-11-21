package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PrefEmployerUITest {
    private static final int LAUNCH_TIMEOUT = 60;
    final String launcherPackageName = "com.example.quickcash";
    private UiDevice device;

    @Before
    public void setup() throws UiObjectNotFoundException {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        Intent launcherIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackageName);
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(launcherIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackageName).depth(0)), LAUNCH_TIMEOUT);
        UiObject allowButton = device.findObject(new UiSelector().text("While using the app"));
        if (allowButton.exists()) {
            allowButton.click();
        }
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
        emailBox.setText("testingemail@test.db");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Test_Pass123#");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();

        // Navigate to the search page
        UiObject searchJobButton = device.findObject(new UiSelector().text("Search Job"));
        searchJobButton.clickAndWaitForNewWindow();

        //enter job details
        UiObject jobTitleBox = device.findObject(new UiSelector().text("Enter Job Title"));
        jobTitleBox.setText("Software Developer");
        UiObject searchButton = device.findObject(new UiSelector().text("Search"));
        searchButton.longClick();

        // Click on a job listing to open options
        UiObject jobListingOptions = device.findObject(new UiSelector().text("Options"));
        jobListingOptions.click();

        // Check if "Add to Preferred Employers" button is visible in the popup
        UiObject addToPreferredEmployersButton = device.findObject(new UiSelector().text("Add to Preferred Employers"));
        assertTrue("Add to Preferred Employers button should be visible", addToPreferredEmployersButton.exists());
    }

    @Test
    public void testPreferredEmployersButtonOnDashboard() throws UiObjectNotFoundException, InterruptedException {
        UiObject allowButton = device.findObject(new UiSelector().text("While using the app"));
        if (allowButton.exists()) {
            allowButton.click();
        }

        // Log in
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testingemail@test.db");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Test_Pass123#");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();
        Thread.sleep(4000);

        // Check if "My Preferred Employers" button is visible
        UiObject preferredEmployersButton = device.findObject(new UiSelector().text("Preferred Employers"));
        assertTrue("My Preferred Employers button should be visible on dashboard", preferredEmployersButton.exists());

        // Click on "My Preferred Employers" and verify the navigation to the preferred employers list page
        preferredEmployersButton.clickAndWaitForNewWindow();
        UiObject preferredEmployersPageTitle = device.findObject(new UiSelector().text("Your Preferred Employers:"));
        assertTrue("User should navigate to Preferred Employers page", preferredEmployersPageTitle.exists());
    }

    @Test
    public void testMyPreferredEmployersJobsPopup() throws UiObjectNotFoundException {
        // Log in
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testingemail@test.db");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Test_Pass123#");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();

        // Click on "My Preferred Employers"
        UiObject preferredEmployersButton = device.findObject(new UiSelector().text("Preferred Employers"));
        preferredEmployersButton.clickAndWaitForNewWindow();

        UiObject preferredEmployerTesting2 = device.findObject(new UiSelector().text("testing2"));
        preferredEmployerTesting2.click();

        UiObject titleTextOfDialog = device.findObject(new UiSelector().textContains("Jobs Posted By This Employer"));
        assertTrue("The popup containing the jobs posted by the employer should appear", titleTextOfDialog.exists());

        UiObject crossButton = device.findObject(new UiSelector().clickable(true));
        crossButton.click();
        assertFalse("The popup containing the jobs posted by the employer should close", titleTextOfDialog.exists());
    }
}
