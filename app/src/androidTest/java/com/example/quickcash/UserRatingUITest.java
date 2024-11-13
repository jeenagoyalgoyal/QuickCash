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
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserRatingUITest {
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

    //test to make sure UIAutomator runs correctly and app starts up correctly
    @Test
    public void checkIfLandingPageIsVisible() {
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        assertTrue(emailBox.exists());
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        assertTrue(passwordBox.exists());
    }

    //test has been made to include 'View Job Details' in the popup menu instead of directly on the job
    //as the current plan is to change the current 'show map' button to the 'apply' button and include
    //'show map' button and any other options instead of apply in the options popup menu instead to decrease
    //clutter. Please contact if any issue is there!
    @Test
    public void testViewJobDetailsButtonIsVisible() throws UiObjectNotFoundException {
        // Log in to employee account
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testingemail@test.db");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Test_Pass123#");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();

        // Navigate to the search page
        UiObject searchJobButton = device.findObject(new UiSelector().text("Search Job"));
        searchJobButton.clickAndWaitForNewWindow();

        // Enter job details
        UiObject jobTitleBox = device.findObject(new UiSelector().text("Enter Job Title"));
        jobTitleBox.setText("Software Developer");
        UiObject searchButton = device.findObject(new UiSelector().text("Search"));
        searchButton.longClick();

        // Click on a job listing to open options
        UiObject jobListingOptions = device.findObject(new UiSelector().text("Options"));
        jobListingOptions.click();

        // Check if "View Job Details" button is visible in the popup
        UiObject viewJobDetailsButton = device.findObject(new UiSelector().text("View Job Details"));
        assertTrue("View Job Details button should be visible in the popup", viewJobDetailsButton.exists());
    }

    //test checks for title that is assumed to be present at the top of the Job Details page
    @Test
    public void testRedirectToJobDetailsPage() throws UiObjectNotFoundException {
        // Log in to employee account
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testingemail@test.db");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Test_Pass123#");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();

        // Navigate to the search page
        UiObject searchJobButton = device.findObject(new UiSelector().text("Search Job"));
        searchJobButton.clickAndWaitForNewWindow();

        // Enter job details
        UiObject jobTitleBox = device.findObject(new UiSelector().text("Enter Job Title"));
        jobTitleBox.setText("Software Developer");
        UiObject searchButton = device.findObject(new UiSelector().text("Search"));
        searchButton.longClick();

        // Click on a job listing to open options
        UiObject jobListingOptions = device.findObject(new UiSelector().text("Options"));
        jobListingOptions.click();

        // Click on the 'View Job Details' button
        UiObject viewJobDetailsButton = device.findObject(new UiSelector().text("View Job Details"));
        viewJobDetailsButton.clickAndWaitForNewWindow();

        // Check if redirected to the 'Job Details' page by checking for title displayed on the page.
        UiObject jobDetailsTitleBox = device.findObject(new UiSelector().text("Job Details"));
        assertTrue("'Job Details' title should be present on the top of the Job Details page", jobDetailsTitleBox.exists());
    }

    //test checks for labels of star rating component and comment field
    //instead of star rating component and comment field directly to give freedom
    //in how the component and field are implemented
    @Test
    public void testRatingAndCommentAreVisible() throws UiObjectNotFoundException {
        // Log in to employee account
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        emailBox.setText("testingemail@test.db");
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        passwordBox.setText("Test_Pass123#");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();

        // Navigate to the search page
        UiObject searchJobButton = device.findObject(new UiSelector().text("Search Job"));
        searchJobButton.clickAndWaitForNewWindow();

        // Enter job details
        UiObject jobTitleBox = device.findObject(new UiSelector().text("Enter Job Title"));
        jobTitleBox.setText("Software Developer");
        UiObject searchButton = device.findObject(new UiSelector().text("Search"));
        searchButton.longClick();

        // Click on a job listing to open options
        UiObject jobListingOptions = device.findObject(new UiSelector().text("Options"));
        jobListingOptions.click();

        // Click on the 'View Job Details' button
        UiObject viewJobDetailsButton = device.findObject(new UiSelector().text("View Job Details"));
        viewJobDetailsButton.clickAndWaitForNewWindow();

        // Check if label of star rating component contains 'Rating:' and comment input field label is visible
        UiObject ratingLabel = device.findObject(new UiSelector().text("Rating:"));
        assertTrue("label of the star rating component should include 'Rating:'", ratingLabel.exists());
        UiObject commentFieldLabel = device.findObject(new UiSelector().text("Comment:"));
        assertTrue("label of the star comment field should include 'Comment:'", commentFieldLabel.exists());
    }
}
