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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
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

import com.example.quickcash.model.Job;
import com.google.firebase.database.FirebaseDatabase;

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
    public void testJobResultsAreDisplayed() {
        // Create a test job with all required fields
        Job testJob = new Job();
        testJob.setJobTitle("Software Developer");
        testJob.setCompanyName("Tech Corp");
        testJob.setLocation("Halifax");
        testJob.setSalary(70000);
        testJob.setExpectedDuration("6 months");
        testJob.setJobType("Full-Time");
        testJob.setRequirements("Android Development");

        // Add this job to the Firebase database for testing
        addTestJobToDatabase(testJob);

        onView(ViewMatchers.withId(R.id.welcomeEmployee)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome Employee!")));
        onView(ViewMatchers.withId(R.id.searchJobButton)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        onView(ViewMatchers.withId(R.id.searchJobButton)).perform(ViewActions.click());
        // Perform a search that will result in this job being displayed
        Espresso.onView(withId(R.id.jobTitle)).perform(ViewActions.typeText("Software Developer"), closeSoftKeyboard());
        Espresso.onView(withId(R.id.search_job_parameter)).perform(ViewActions.click());

        // Check that the job title is displayed
        Espresso.onView(withText("Job Title: " + testJob.getJobTitle())).check(matches(isDisplayed()));
        Espresso.onView(withText("Company: " + testJob.getCompanyName())).check(matches(isDisplayed()));
        Espresso.onView(withText("Location: " + testJob.getLocation())).check(matches(isDisplayed()));
        Espresso.onView(withText("Salary: $" + testJob.getSalary())).check(matches(isDisplayed()));
        Espresso.onView(withText("Duration: " + testJob.getExpectedDuration())).check(matches(isDisplayed()));
    }

    private void addTestJobToDatabase(Job job) {
        // Adds job to database
        FirebaseDatabase.getInstance().getReference("Jobs").push().setValue(job);
    }


    @After
    public void tearDown() {
        Intents.release();

    }

}


