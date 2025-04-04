package com.example.quickcash;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class JobSearchParameterEspressoTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setup() throws UiObjectNotFoundException {
        Intents.init();
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject allowButton = device.findObject(new UiSelector().text("While using the app"));
        if (allowButton.exists()) {
            allowButton.click();
        }
    }


    @Test
    public void testJobSearchVisible() throws InterruptedException {
        // This will launch the employee dashboard
        // Log in
        onView(ViewMatchers.withId(R.id.emailBox)).perform(ViewActions.click(), ViewActions.typeText("testingemail@test.db"));
        onView(ViewMatchers.withId(R.id.passwordBox)).perform(ViewActions.click(), ViewActions.typeText("Test_Pass123#"));
        onView(ViewMatchers.withId(R.id.loginButton)).perform(click());

        Thread.sleep(8000);

        // Checks to see if Job Search button is there
        onView(withId(R.id.searchJobButton)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.searchJobButton)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
    }

    @Test
    public void testNavigateJobSearch() throws InterruptedException {
        // Log in
        onView(ViewMatchers.withId(R.id.emailBox)).perform(ViewActions.click(), ViewActions.typeText("testingemail@test.db"));
        onView(ViewMatchers.withId(R.id.passwordBox)).perform(ViewActions.click(), ViewActions.typeText("Test_Pass123#"));
        onView(ViewMatchers.withId(R.id.loginButton)).perform(click());

        Thread.sleep(8000);

        // Employee dashboard with the Job Search button
        onView(ViewMatchers.withId(R.id.searchJobButton)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        // When Job search button is clicked
        onView(withId(R.id.searchJobButton)).perform(click());

        // Launch the Job Search page
        intended(hasComponent(JobSearchParameterActivity.class.getName()));
    }

    @Test
    public void testFilterOptions() throws InterruptedException {

        // Log in
        onView(ViewMatchers.withId(R.id.emailBox)).perform(ViewActions.click(), ViewActions.typeText("testingemail@test.db"));
        onView(ViewMatchers.withId(R.id.passwordBox)).perform(ViewActions.click(), ViewActions.typeText("Test_Pass123#"));
        onView(ViewMatchers.withId(R.id.loginButton)).perform(click());

        Thread.sleep(8000);
        // On employee dashboard, click the Search Job button
        onView(ViewMatchers.withId(R.id.welcomeEmployee)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome Employee!")));
        onView(ViewMatchers.withId(R.id.searchJobButton)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        onView(ViewMatchers.withId(R.id.searchJobButton)).perform(ViewActions.click());

        // Checks to see if text boxes are displayed in the job search page
        onView(ViewMatchers.withId(R.id.jobTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.buildingName)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.minSalary)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.maxSalary)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.duration)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.location)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testFilterInteraction() throws InterruptedException {
        // Log in
        onView(ViewMatchers.withId(R.id.emailBox)).perform(ViewActions.click(), ViewActions.typeText("testingemail@test.db"));
        onView(ViewMatchers.withId(R.id.passwordBox)).perform(ViewActions.click(), ViewActions.typeText("Test_Pass123#"));
        onView(ViewMatchers.withId(R.id.loginButton)).perform(click());

        Thread.sleep(8000);

        // Test the filters by implementing data and seeing if it works
        onView(ViewMatchers.withId(R.id.welcomeEmployee)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome Employee!")));
        onView(ViewMatchers.withId(R.id.searchJobButton)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        onView(ViewMatchers.withId(R.id.searchJobButton)).perform(ViewActions.click());

        onView(ViewMatchers.withId(R.id.jobTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.jobTitle)).perform(ViewActions.click(), ViewActions.typeText("Lawn Mowing"));
        onView(ViewMatchers.withId(R.id.buildingName)).perform(ViewActions.click(), ViewActions.typeText("MowMow"));
        onView(ViewMatchers.withId(R.id.minSalary)).perform(ViewActions.click(), ViewActions.typeText("20"));
        onView(ViewMatchers.withId(R.id.maxSalary)).perform(ViewActions.click(), ViewActions.typeText("27"));
        onView(ViewMatchers.withId(R.id.duration)).perform(ViewActions.click(), ViewActions.typeText("20"));

        // Will test the submission button to see if the filters worked
        Espresso.closeSoftKeyboard();
        onView(ViewMatchers.withId(R.id.search_job_parameter)).perform(ViewActions.click());
        // Switches to the results
        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(isDisplayed()));
        // Checks if our job title shows up
        onView(ViewMatchers.withText("Lawn Mowing")).check(matches(isDisplayed()));
    }

    @Test
    public void testNoResultsFound() throws InterruptedException {
        // Log in
        onView(ViewMatchers.withId(R.id.emailBox)).perform(ViewActions.click(), ViewActions.typeText("testingemail@test.db"));
        onView(ViewMatchers.withId(R.id.passwordBox)).perform(ViewActions.click(), ViewActions.typeText("Test_Pass123#"));
        onView(ViewMatchers.withId(R.id.loginButton)).perform(click());

        Thread.sleep(8000);
        // Test the filters by implementing data and seeing if it works
        onView(ViewMatchers.withId(R.id.welcomeEmployee)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome Employee!")));
        onView(ViewMatchers.withId(R.id.searchJobButton)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        onView(ViewMatchers.withId(R.id.searchJobButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.jobTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.jobTitle)).perform(ViewActions.click(), ViewActions.typeText("Software Developer"));
        onView(ViewMatchers.withId(R.id.buildingName)).perform(ViewActions.click(), ViewActions.typeText("Tech Corp"));
        onView(ViewMatchers.withId(R.id.location)).perform(ViewActions.click(), ViewActions.typeText("Mumbai, India"));

        onView(ViewMatchers.withId(R.id.search_job_parameter)).perform(ViewActions.click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //continue
        }
        onView(ViewMatchers.withText("No Results Found")).check(matches(isDisplayed()));

    }

    @After
    public void tearDown() {
        Intents.release();
    }

}


