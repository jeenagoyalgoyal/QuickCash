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
    public ActivityScenarioRule<RoleActivity> activityRule = new ActivityScenarioRule<>(RoleActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }


    @Test
    public void testJobSearchVisible() {
        // This will launch the employee dashboard
        ActivityScenario.launch(RoleActivity.class);

        // Checks to see if Job Search button is there
        onView(withId(R.id.jobPosting)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.jobPosting)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
    }

    @Test
    public void testNavigateJobSearch() {

        // Employee dashboard with the Job Search button
        onView(ViewMatchers.withId(R.id.jobPosting)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        // When Job search button is clicked
        onView(withId(R.id.jobPosting)).perform(click());

        // Launch the Job Search page
        intended(hasComponent(JobSearchParameterActivity.class.getName()));
    }

    @Test
    public void testFilterOptions() {
        // On employee dashboard, click the Search Job button
        onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employee")));
        onView(ViewMatchers.withId(R.id.jobPosting)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        onView(ViewMatchers.withId(R.id.jobPosting)).perform(ViewActions.click());

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
        onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employee")));
        onView(ViewMatchers.withId(R.id.jobPosting)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        onView(ViewMatchers.withId(R.id.jobPosting)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.jobTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.jobTitle)).perform(ViewActions.click(), ViewActions.typeText("Data Analyst")).check(ViewAssertions.matches(ViewMatchers.withText("Data Analyst")));
        onView(ViewMatchers.withId(R.id.companyName)).perform(ViewActions.click(), ViewActions.typeText("Ubisoft")).check(ViewAssertions.matches(ViewMatchers.withText("Ubisoft")));
        onView(ViewMatchers.withId(R.id.minSalary)).perform(ViewActions.click(), ViewActions.typeText("50000")).check(ViewAssertions.matches(ViewMatchers.withText("50000")));
        onView(ViewMatchers.withId(R.id.maxSalary)).perform(ViewActions.click(), ViewActions.typeText("75000")).check(ViewAssertions.matches(ViewMatchers.withText("75000")));
        onView(ViewMatchers.withId(R.id.duration)).perform(ViewActions.click(), ViewActions.typeText("3")).check(ViewAssertions.matches(ViewMatchers.withText("3")));
        onView(ViewMatchers.withId(R.id.location)).perform(ViewActions.click(), ViewActions.typeText("Halifax")).check(ViewAssertions.matches(ViewMatchers.withText("Halifax")));

        // Will test the submission button to see if the filters worked
        Espresso.closeSoftKeyboard();
        onView(ViewMatchers.withId(R.id.search_job_parameter)).perform(ViewActions.click());
        // Switches to the results
        onView(ViewMatchers.withId(R.id.job_search_screen)).check(matches(isDisplayed()));
        // Checks if our job title shows up
        onView(ViewMatchers.withText("Data Analyst")).check(matches(isDisplayed()));
    }

    @Test
    public void testabc(){
        onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employee")));
        onView(ViewMatchers.withId(R.id.jobPosting)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        onView(ViewMatchers.withId(R.id.jobPosting)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.companyName)).perform(ViewActions.click(), ViewActions.typeText("Microsoft"));
        onView(ViewMatchers.withId(R.id.search_job_parameter)).perform(ViewActions.click());
        // Switches to the results
        onView(ViewMatchers.withId(R.id.job_search_screen)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.companyResult)).check(matches(isDisplayed()));
    }

    @Test
    public void test1(){
        onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employee")));
        onView(ViewMatchers.withId(R.id.jobPosting)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        onView(ViewMatchers.withId(R.id.jobPosting)).perform(ViewActions.click());
        onView(withText("Job Title")).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }

}


