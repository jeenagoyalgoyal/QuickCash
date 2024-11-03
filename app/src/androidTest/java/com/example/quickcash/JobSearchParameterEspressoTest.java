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
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import org.junit.After;
import org.junit.Before;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class JobSearchParameterEspressoTest {

    @Rule
    public ActivityScenarioRule<EmployeeHomepageActivity> activityRule = new ActivityScenarioRule<>(EmployeeHomepageActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }


    @Test
    public void testJobSearchVisible() {
        // This will launch the employee dashboard
        ActivityScenario.launch(EmployeeHomepageActivity.class);

        // Checks to see if Job Search button is there
        onView(withId(R.id.searchJobButton)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.searchJobButton)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
    }

    @Test
    public void testNavigateJobSearch() {

        // Employee dashboard with the Job Search button
        onView(ViewMatchers.withId(R.id.searchJobButton)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        // When Job search button is clicked
        onView(withId(R.id.searchJobButton)).perform(click());

        // Launch the Job Search page
        intended(hasComponent(JobSearchParameterActivity.class.getName()));
    }

    @Test
    public void testFilterOptions() {
        // On employee dashboard, click the Search Job button
        onView(ViewMatchers.withId(R.id.welcomeEmployee)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome Employee!")));
        onView(ViewMatchers.withId(R.id.searchJobButton)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        onView(ViewMatchers.withId(R.id.searchJobButton)).perform(ViewActions.click());

        // Checks to see if text boxes are displayed in the job search page
        onView(ViewMatchers.withId(R.id.jobTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.companyName)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.minSalary)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.maxSalary)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.duration)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.location)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testFilterInteraction() {
        // Test the filters by implementing data and seeing if it works
        onView(ViewMatchers.withId(R.id.welcomeEmployee)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome Employee!")));
        onView(ViewMatchers.withId(R.id.searchJobButton)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        onView(ViewMatchers.withId(R.id.searchJobButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.jobTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.jobTitle)).perform(ViewActions.click(), ViewActions.typeText("Software Developer")).check(ViewAssertions.matches(ViewMatchers.withText("Software Developer")));
        onView(ViewMatchers.withId(R.id.companyName)).perform(ViewActions.click(), ViewActions.typeText("Tech Corp")).check(ViewAssertions.matches(ViewMatchers.withText("Tech Corp")));
        onView(ViewMatchers.withId(R.id.minSalary)).perform(ViewActions.click(), ViewActions.typeText("6000")).check(ViewAssertions.matches(ViewMatchers.withText("6000")));
        onView(ViewMatchers.withId(R.id.maxSalary)).perform(ViewActions.click(), ViewActions.typeText("80000")).check(ViewAssertions.matches(ViewMatchers.withText("80000")));
        onView(ViewMatchers.withId(R.id.duration)).perform(ViewActions.click(), ViewActions.typeText("6 months")).check(ViewAssertions.matches(ViewMatchers.withText("6 months")));
        onView(ViewMatchers.withId(R.id.location)).perform(ViewActions.click(), ViewActions.typeText("Halifax")).check(ViewAssertions.matches(ViewMatchers.withText("Halifax")));

        // Will test the submission button to see if the filters worked
        Espresso.closeSoftKeyboard();
        onView(ViewMatchers.withId(R.id.search_job_parameter)).perform(ViewActions.click());
        // Switches to the results
        onView(ViewMatchers.withId(R.id.recyclerView)).check(matches(isDisplayed()));
        // Checks if our job title shows up
        onView(ViewMatchers.withText("Software Developer")).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }

}


