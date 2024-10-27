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
        Espresso.onView(withId(R.id.jobPosting)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.jobPosting)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
    }

    @Test
    public void testNavigateJobSearch() {

        // Employee dashboard with the Job Search button
        Espresso.onView(ViewMatchers.withId(R.id.jobPosting)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        // When Job search button is clicked
        Espresso.onView(withId(R.id.jobPosting)).perform(click());

        // Launch the Job Search page
        intended(hasComponent(JobSearchParameterActivity.class.getName()));
    }

    @Test
    public void testFilterOptions() {
        // On employee dashboard, click the Search Job button
        Espresso.onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employee")));
        Espresso.onView(ViewMatchers.withId(R.id.jobPosting)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        Espresso.onView(ViewMatchers.withId(R.id.jobPosting)).perform(ViewActions.click());

        // Checks to see if text boxes are displayed in the job search page
        Espresso.onView(ViewMatchers.withId(R.id.jobTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.companyName)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.salary)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.duration)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.location)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testFilterInteraction() {
        // Test the filters by implementing data and seeing if it works
        Espresso.onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employee")));
        Espresso.onView(ViewMatchers.withId(R.id.jobPosting)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        Espresso.onView(ViewMatchers.withId(R.id.jobPosting)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.jobTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.jobTitle)).perform(ViewActions.click(), ViewActions.typeText("Data Analyst")).check(ViewAssertions.matches(ViewMatchers.withText("Data Analyst")));
        Espresso.onView(ViewMatchers.withId(R.id.companyName)).perform(ViewActions.click(), ViewActions.typeText("Ubisoft")).check(ViewAssertions.matches(ViewMatchers.withText("Ubisoft")));
        Espresso.onView(ViewMatchers.withId(R.id.salary)).perform(ViewActions.click(), ViewActions.typeText("75000")).check(ViewAssertions.matches(ViewMatchers.withText("75000")));
        Espresso.onView(ViewMatchers.withId(R.id.duration)).perform(ViewActions.click(), ViewActions.typeText("35")).check(ViewAssertions.matches(ViewMatchers.withText("35")));
        Espresso.onView(ViewMatchers.withId(R.id.location)).perform(ViewActions.click(), ViewActions.typeText("Halifax")).check(ViewAssertions.matches(ViewMatchers.withText("Halifax")));
    }

    @After
    public void tearDown() {
        Intents.release();
    }

}


