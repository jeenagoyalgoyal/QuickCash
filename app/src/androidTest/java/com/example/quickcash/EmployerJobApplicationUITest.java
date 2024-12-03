package com.example.quickcash;

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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EmployerJobApplicationUITest {

    final String launcherPackageName = "com.example.quickcash";
    private UiDevice device;
    private static final String TAG = "EmployerJobApplicationUITest";
    private static final int LAUNCH_TIMEOUT = 5000;

    protected void loginAsEmployer() throws UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject allowButton = device.findObject(new UiSelector().text("While using the app"));
        if (allowButton.exists()) {
            allowButton.click();
        }
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testemployer@test.com");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Employer123#");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();
        UiObject jobPostingsPageButton = device.findObject(new UiSelector().textContains("Applications"));
        jobPostingsPageButton.clickAndWaitForNewWindow();
        Log.d(TAG, "Clicked Manage Applications button");
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
    public void testApplicationPageLoads() throws InterruptedException, UiObjectNotFoundException {

        loginAsEmployer();

        UiObject manageApplicationsButton = device.findObject(new UiSelector().text("View Applications"));
        manageApplicationsButton.clickAndWaitForNewWindow();

        UiObject jobApplicationsPage = device.findObject(new UiSelector().textContains("Application"));
        assertTrue("Job Applications page should be displayed", jobApplicationsPage.waitForExists(LAUNCH_TIMEOUT));
    }

    @Test
    public void testAcceptRejectButtonsExist() throws InterruptedException, UiObjectNotFoundException {

        loginAsEmployer();
        UiObject manageApplicationsButton = device.findObject(new UiSelector().text("View Applications"));
        manageApplicationsButton.click();
        Log.d(TAG, "Clicked Manage Applications button");

        UiObject acceptButton = device.findObject(new UiSelector().text("Accept"));
        UiObject rejectButton = device.findObject(new UiSelector().text("Reject"));
        assertTrue("Accept button should be visible", acceptButton.exists());
        assertTrue("Reject button should be visible", rejectButton.exists());
    }

    @Test
    public void testRejectButtonFunctionality() throws InterruptedException, UiObjectNotFoundException {

        loginAsEmployer();
        UiObject manageApplicationsButton = device.findObject(new UiSelector().text("View Applications"));
        manageApplicationsButton.click();
        Log.d(TAG, "Clicked Manage Applications button");

        UiObject rejectButton = device.findObject(new UiSelector().text("Reject"));
        assertTrue("Reject button should be visible", rejectButton.exists());
        rejectButton.clickAndWaitForNewWindow();
        Log.d(TAG, "Clicked Reject button");

        UiObject jobApplicationsPage = device.findObject(new UiSelector().textContains("Application"));
        assertTrue("Job Applications page should be displayed", jobApplicationsPage.waitForExists(LAUNCH_TIMEOUT));
    }
}
