package com.example.quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EmployeeJobApplicationUITest {

    final String launcherPackageName = "com.example.quickcash";
    private UiDevice device;
    private static final String TAG = "EmployeeJobApplicationUITest";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String JOB_TITLE = "Software Developer"; // Use an existing job

    protected void login() throws UiObjectNotFoundException, InterruptedException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject allowButton = device.findObject(new UiSelector().text("While using the app"));
        if (allowButton.exists()) {
            allowButton.click();
        }
        //logging in
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testingemail@test.db");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Test_Pass123#");
        UiObject registerButton = device.findObject(new UiSelector().text("Login"));
        registerButton.clickAndWaitForNewWindow();
        Thread.sleep(4000);
    }

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
    public void testApplyButtonExists() throws InterruptedException, UiObjectNotFoundException {
        login();
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

        UiObject applyButton = device.findObject(new UiSelector().textContains("Apply"));
        assertTrue("Apply button should be visible on search by parameter page", applyButton.exists());
    }

    @Test
    public void testApplyButtonLaunchesApplicationPage() throws InterruptedException, UiObjectNotFoundException {
        //Log in
        login();
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

        UiObject applyButton = device.findObject(new UiSelector().textContains("Apply"));
        assertTrue("Apply button should be visible on search by parameter page", applyButton.exists());
        applyButton.clickAndWaitForNewWindow();

        UiObject nameField = device.findObject(new UiSelector().resourceId(launcherPackageName + ":id/editTextName"));
        UiObject emailField = device.findObject(new UiSelector().resourceId(launcherPackageName + ":id/editTextEmail"));
        UiObject cvField = device.findObject(new UiSelector().resourceId(launcherPackageName + ":id/editTextMessage"));
        applyButton = device.findObject(new UiSelector().resourceId(launcherPackageName + ":id/buttonApply"));
        assertTrue("Page should have name field", nameField.exists());
        assertTrue("Page should have email field", emailField.exists());
        assertTrue("Page should have CV field", cvField.exists());
        assertTrue("Page should have an apply button", applyButton.exists());
    }

    @Test
    public void testApplyingToJobWorks() throws InterruptedException, UiObjectNotFoundException {
        //Log in
        login();
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

        UiObject applyButton = device.findObject(new UiSelector().textContains("Apply"));
        assertTrue("Apply button should be visible on search by parameter page", applyButton.exists());
        applyButton.clickAndWaitForNewWindow();

        UiObject nameField = device.findObject(new UiSelector().resourceId(launcherPackageName + ":id/editTextName"));
        nameField.setText("Test_Name");
        UiObject emailField = device.findObject(new UiSelector().resourceId(launcherPackageName + ":id/editTextEmail"));
        emailField.setText("Test_Email@test.db");
        UiObject cvField = device.findObject(new UiSelector().resourceId(launcherPackageName + ":id/editTextMessage"));
        cvField.setText("Job wanted by me");
        applyButton = device.findObject(new UiSelector().resourceId(launcherPackageName + ":id/buttonApply"));
        applyButton.clickAndWaitForNewWindow();

        onView(withText("Welcome Employee!")).check(matches(isDisplayed()));
    }
}
