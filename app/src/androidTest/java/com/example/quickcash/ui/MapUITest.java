package com.example.quickcash.ui;

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
    private static final long LAUNCH_TIMEOUT = 240;
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

    //Method to make it easy to login
    private void logIn() throws UiObjectNotFoundException{
        // Log in
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testingemail@test.db");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Test_Pass123#");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();

        //Go to job search by parameter page
        UiObject searchJobButton = device.findObject(new UiSelector().text("Search Job"));
        searchJobButton.clickAndWaitForNewWindow();
    }

    @Test
    public void checkIfJobSearchHasShowMapsButton() throws UiObjectNotFoundException {
        logIn();

        //Check if ShowMapButton is Visible
        UiObject showMapButton = device.findObject(new UiSelector().text("Show Map"));
        assertTrue("Show Maps Button should exist", showMapButton.exists());
    }

    @Test
    public void checkIfClickingMarkerOpensPopUp() throws UiObjectNotFoundException{
        logIn();

        //Find Tester job listing and display on map
        UiObject jobTitleBox = device.findObject(new UiSelector().text("Enter Job Title"));
        jobTitleBox.setText("Research Assistant");
        UiObject showMapButton = device.findObject(new UiSelector().text("Show Map"));
        showMapButton.longClick();
        try{
            UiObject locationPermissionButton = device.findObject(new UiSelector().text("While using the app"));
            locationPermissionButton.longClick();
        } catch (UiObjectNotFoundException ignored){
            //Do Nothing if exception is caught since Permission already granted
        }
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Research Assistant"));
        marker.click();
        UiObject dialog = device.findObject(new UiSelector().text("Job"));
        assertTrue("Description dialog should contain the title Job", dialog.exists());
    }

    @Test
    public void checkIfClickingCloseandBackReturnsToSearchPage() throws UiObjectNotFoundException{
        logIn();

        //Find Tester job listing and display on map
        UiObject jobTitleBox = device.findObject(new UiSelector().text("Enter Job Title"));
        jobTitleBox.setText("Research Assistant");
        UiObject showMapButton = device.findObject(new UiSelector().text("Show Map"));
        showMapButton.clickAndWaitForNewWindow();
        try{
            UiObject locationPermissionButton = device.findObject(new UiSelector().text("While using the app"));
            locationPermissionButton.longClick();
        } catch (UiObjectNotFoundException ignored){
            //Do Nothing if exception is caught since Permission already granted
        }
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Research Assistant"));
        marker.click();
        UiObject dialog = device.findObject(new UiSelector().text("Job"));
        assertTrue("Description dialog should contain the title Job", dialog.exists());

        UiObject closeButton = device.findObject(new UiSelector().text("Close"));
        closeButton.longClick();
        UiObject backButton = device.findObject(new UiSelector().text("BACK"));
        backButton.longClick();
        showMapButton = device.findObject(new UiSelector().text("Show Map"));s
        assertTrue("Pressing close on dialog and back on map takes you to page with Show Map button", showMapButton.exists());
    }
}
