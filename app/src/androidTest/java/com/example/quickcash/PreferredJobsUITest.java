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
    public void checkForPreferredJobsButton() throws UiObjectNotFoundException {
        //logging in
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testingemail@test.db");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Test_Pass123#");
        UiObject registerButton = device.findObject(new UiSelector().text("Login"));
        registerButton.clickAndWaitForNewWindow();

        //looking for Search Jobs button on homepage
        UiObject searchJobButton = device.findObject(new UiSelector().text("Search Jobs"));
        searchJobButton.clickAndWaitForNewWindow();

        //looking for options button on a single job listing
        UiObject optionsButton = device.findObject(new UiSelector().text("options"));
        optionsButton.click();

        //popup with text should show up
        UiObject addToPreferredJobsButton = device.findObject(new UiSelector().text("Add to Preferred Jobs"));
        assertTrue(addToPreferredJobsButton.exists());
    }
}
