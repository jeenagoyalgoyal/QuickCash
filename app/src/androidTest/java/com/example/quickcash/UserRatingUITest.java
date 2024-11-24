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

        // Check if "View Job Details" button is visible in the popup
        UiObject viewJobDetailsButton = device.findObject(new UiSelector().text("Details"));
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

        // Click on the 'View Job Details' button
        UiObject viewJobDetailsButton = device.findObject(new UiSelector().text("Details"));
        viewJobDetailsButton.clickAndWaitForNewWindow();

        // Check if redirected to the 'Job Details' page by checking for title displayed on the page.
        UiObject jobDetailsTitleBox = device.findObject(new UiSelector().text("Job"));
        assertTrue("'Job Details' title should be present on the top of the Job Details page", jobDetailsTitleBox.exists());
    }

    //test checks if star rating component and comment field exists by checking if they are clickable
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

        // Click on the 'View Job Details' button
        UiObject viewJobDetailsButton = device.findObject(new UiSelector().text("Details"));
        viewJobDetailsButton.clickAndWaitForNewWindow();

        // Star rating component and comment field are checked to be clickable to see if they exist
        UiObject2 starRatingComponent = device.findObject(By.res(launcherPackageName,"jobRatingBar")); //id is taken to be starRatingComponent
        assertTrue("star rating component should be clickable", starRatingComponent.isClickable());
        UiObject2 commentField = device.findObject(By.res(launcherPackageName,"commentInput")); //id is taken to be commentField
        assertTrue("comment field should be clickable", commentField.isClickable());
    }

    //test checks that 'Add Comment' button is enabled only if rating and comment is provided, it should be disabled otherwise
    @Test
    public void testAddCommentButtonEnabledCheck() throws UiObjectNotFoundException {
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

        // Click on the 'View Job Details' button
        UiObject viewJobDetailsButton = device.findObject(new UiSelector().text("Details"));
        viewJobDetailsButton.clickAndWaitForNewWindow();

        // 'Add Comment' button should now be disabled
        UiObject addCommentButton = device.findObject(new UiSelector().text("Add Comment"));
        assertFalse("'Add Comment' button should not be enabled with no rating and comment provided", addCommentButton.isEnabled());

        // Star rating component and comment field are given values through interaction
        UiObject2 starRatingComponent = device.findObject(By.res(launcherPackageName, "jobRatingBar"));
        starRatingComponent.click();
        UiObject2 commentField = device.findObject(By.res(launcherPackageName, "commentInput"));
        commentField.setText("test comment");

        // 'Add Comment' button should now be enabled
        assertTrue("'Add Comment' button should be enabled with rating and comment set", addCommentButton.isEnabled());
    }

    //test checks that adding a comment successfully redirects to the Job Search page
    @Test
    public void testRedirectUponCommentSubmit() throws UiObjectNotFoundException {
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

        // Click on the 'View Job Details' button
        UiObject viewJobDetailsButton = device.findObject(new UiSelector().text("Details"));
        viewJobDetailsButton.clickAndWaitForNewWindow();

        // Star rating component and comment field are given values through interaction and comment is submitted
        UiObject2 starRatingComponent = device.findObject(By.res(launcherPackageName, "jobRatingBar")); //id is taken to be starRatingComponent
        starRatingComponent.click();
        UiObject2 commentField = device.findObject(By.res(launcherPackageName, "commentInput")); //id is taken to be commentField
        commentField.setText("test comment");
        UiObject addCommentButton = device.findObject(new UiSelector().text("Add Comment"));
        addCommentButton.click();

        UiObject closeButton = device.findObject(new UiSelector().text("Close"));
        closeButton.click();

        // Check is done to see if redirect to Job Search page was successful
        UiObject searchJobTitleBox = device.findObject(new UiSelector().text("Search Job"));
        assertTrue("'Search Job' title at the top of Job Search page should be present if redirect was successful", searchJobTitleBox.exists());
    }
}
