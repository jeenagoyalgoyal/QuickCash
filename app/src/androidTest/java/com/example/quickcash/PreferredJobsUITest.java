/**
 * PreferredJobsUITest is a UI test class for the QuickCash application that verifies the functionality of the
 * Preferred Jobs feature. It uses the UI Automator framework to interact with the app's user interface and
 * perform automated tests.
 *
 * <p>This class includes tests for the following functionalities:</p>
 * <ul>
 *     <li>Checking the presence of the "My Preferred Jobs" button on the employee dashboard.</li>
 *     <li>Verifying the ability to view job details from the preferred jobs list.</li>
 *     <li>Testing the addition of jobs to the preferred jobs list from the job search results.</li>
 * </ul>
 *
 * <p>Key components of this test class include:</p>
 * <ul>
 *     <li>{@link UiDevice} - Used to interact with the device's UI elements.</li>
 *     <li>{@link UiObject} - Represents a UI element that can be interacted with.</li>
 *     <li>{@link UiSelector} - Used to find UI elements based on their properties.</li>
 * </ul>
 *
 * <p>Test Lifecycle:</p>
 * <ul>
 *     <li>{@link #setup()} - Initializes the test environment by launching the QuickCash application.</li>
 *     <li>{@link #checkForPreferredJobsButton()} - Tests the presence and functionality of the "My Preferred Jobs" button.</li>
 *     <li>{@link #AddToPreferredJobs()} - Tests the process of adding a job to the preferred jobs list.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>This class should be run as part of the application's UI testing suite to ensure that the Preferred Jobs
 * functionality works as expected.</p>
 */

package com.example.quickcash;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.provider.Contacts;

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

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class PreferredJobsUITest {
    private static final int LAUNCH_TIMEOUT = 120;
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
    public void checkForPreferredJobsButton() throws UiObjectNotFoundException {
        //logging in
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testingemail@test.db");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Test_Pass123#");
        UiObject registerButton = device.findObject(new UiSelector().text("Login"));
        registerButton.clickAndWaitForNewWindow();
        //looking for 'Search Jobs' button on homepage
        UiObject searchJobButton = device.findObject(new UiSelector().text("My Preferred Jobs"));
        assertTrue("My preferred jobs button should be present on employee dashboard", searchJobButton.exists());
        searchJobButton.clickAndWaitForNewWindow();

        //looking for 'View Job' button on a single job listing
        UiObject optionsButton = device.findObject(new UiSelector().text("View Job"));
        optionsButton.click();

        //looking for 'Close' button on a single job listing
        UiObject closeButton = device.findObject(new UiSelector().text("Close"));
        closeButton.click();
    }

    @Test
    public void AddToPreferredJobs() throws UiObjectNotFoundException {
        //logging in
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testingemail@test.db");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Test_Pass123#");
        UiObject registerButton = device.findObject(new UiSelector().text("Login"));
        registerButton.clickAndWaitForNewWindow();

        //looking for 'Search Jobs' button on homepage
        UiObject searchJobButton = device.findObject(new UiSelector().text("Search Job"));
        searchJobButton.clickAndWaitForNewWindow();

        UiObject jobTitle = device.findObject(new UiSelector().text("Enter Job Title"));
        jobTitle.setText("Software Developer");

        //Press Search
        UiObject searchButton = device.findObject(new UiSelector().text("Search"));
        searchButton.longClick();

        //looking for 'options' button on a single job listing
        UiObject optionsButton = device.findObject(new UiSelector().text("Options"));
        optionsButton.longClick();

        UiObject addToPreferredJobsButton = device.findObject(new UiSelector().text("Add to Preferred Jobs"));
        assertTrue("Add to preferred jobs must show up in the popup menu when options is clicked on a job listing", addToPreferredJobsButton.exists());
    }
}
