package com.example.quickcash;

// Import statements

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.espresso.intent.Intents;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.quickcash.R;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class JobSubmissionUITest {

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void testFormSubmitsSuccessfully() {
        // Launch LoginActivity explicitly
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);

        // Login steps
        onView(withId(R.id.emailBox))
                .check(matches(isDisplayed()))
                .perform(typeText("test2@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.passwordBox))
                .check(matches(isDisplayed()))
                .perform(typeText("TestingPassword!1"), closeSoftKeyboard());

        onView(withId(R.id.loginButton))
                .check(matches(isDisplayed()))
                .perform(click());

        // Wait for employer homepage to load
        try {
            Thread.sleep(2000); // Adjust the sleep time as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify we're on employer homepage and click create job
        onView(withId(R.id.createJobButton))
                .check(matches(isDisplayed()))
                .perform(click());

        // Fill in job details
        onView(withId(R.id.jobTitle))
                .check(matches(isDisplayed()))
                .perform(typeText("Software Developer"), closeSoftKeyboard());

        onView(withId(R.id.companyName))
                .check(matches(isDisplayed()))
                .perform(typeText("Tech Company"), closeSoftKeyboard());

        // Select job type from spinner
        onView(withId(R.id.spinnerJobType))
                .check(matches(isDisplayed()))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Full-time"))).perform(click());

        // Fill remaining fields
        onView(withId(R.id.requirementText))
                .check(matches(isDisplayed()))
                .perform(typeText("Java, Python experience"), closeSoftKeyboard());

        onView(withId(R.id.salaryText))
                .check(matches(isDisplayed()))
                .perform(typeText("25"), closeSoftKeyboard());

        // Select urgency from spinner
        onView(withId(R.id.spinnerUrgency))
                .check(matches(isDisplayed()))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("High"))).perform(click());

        onView(withId(R.id.locationJob))
                .check(matches(isDisplayed()))
                .perform(typeText("Halifax, NS"), closeSoftKeyboard());

        onView(withId(R.id.expectedDuration))
                .check(matches(isDisplayed()))
                .perform(typeText("20"), closeSoftKeyboard());

        // Handle the Material Date Picker
        onView(withId(R.id.startDate))
                .perform(click());

        // Navigate to a closer month (e.g., next month)
        // Assuming the target month is November 2024
        int monthsToGoBack = calculateMonthsToGoBack(2024, 11); // Year: 2024, Month: November
        for (int i = 0; i < monthsToGoBack; i++) {
            onView(anyOf(
                    withContentDescription("Previous month"),
                    withContentDescription("Change to previous month")
            ))
                    .perform(click());
        }

        // Select the date "15" in the closer month
        onView(allOf(withText("15"), isDisplayed()))
                .perform(click());

        // Confirm the date selection by clicking "OK"
        onView(withText("OK"))
                .perform(click());

        // Submit the form
        onView(withId(R.id.jobSubmissionButton))
                .check(matches(isDisplayed()))
                .perform(click());

        // Wait for submission and navigation
        try {
            Thread.sleep(2000); // Adjust the sleep time as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify we're back on employer homepage
        onView(withId(R.id.welcomeEmployer))
                .check(matches(isDisplayed()))
                .check(matches(withText("Welcome Employer!")));
    }

    // Helper method to calculate the number of months to go back
    private int calculateMonthsToGoBack(int targetYear, int targetMonth) {
        java.util.Calendar current = java.util.Calendar.getInstance();
        int currentYear = current.get(java.util.Calendar.YEAR);
        int currentMonth = current.get(java.util.Calendar.MONTH) + 1; // Calendar.MONTH is zero-based

        int yearsDifference = currentYear - targetYear;
        int monthsDifference = currentMonth - targetMonth;

        return yearsDifference * 12 + monthsDifference;
    }

    @Test
    public void testPreventsSubmissionIfMissingFields() {
        ActivityScenario<JobSubmissionActivity> scenario =
                ActivityScenario.launch(JobSubmissionActivity.class);

        // Fill only partial information
        onView(withId(R.id.jobTitle))
                .perform(typeText("Software Developer"), closeSoftKeyboard());

        onView(withId(R.id.companyName))
                .perform(typeText("Tech Company"), closeSoftKeyboard());

        // Select job type
        onView(withId(R.id.spinnerJobType)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Full-time"))).perform(click());

        // Try to submit incomplete form
        onView(withId(R.id.jobSubmissionButton)).perform(click());

        // Verify form is still displayed (submission prevented)
        onView(withId(R.id.jobSubmissionButton)).check(matches(isDisplayed()));
        onView(withId(R.id.jobSub)).check(matches(isDisplayed()));

        // Verify required fields are still empty
        onView(withId(R.id.salaryText)).check(matches(withText("")));
        onView(withId(R.id.locationJob)).check(matches(withText("")));
        onView(withId(R.id.expectedDuration)).check(matches(withText("")));
    }

    @Test
    public void checkCreateJobButtonPresent() {
        ActivityScenario.launch(EmployerHomepageActivity.class);
        onView(withText("Create Job")).check(matches(isDisplayed()));
    }

    @Test
    public void checkJobSubmissionForm() {
        ActivityScenario.launch(JobSubmissionActivity.class);
        onView(withId(R.id.jobSub)).check(matches(isDisplayed()));
    }

    @Test
    public void checkRequiredFields() {
        ActivityScenario.launch(JobSubmissionActivity.class);

        // Verify all required fields are present
        onView(withId(R.id.jobTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.companyName)).check(matches(isDisplayed()));
        onView(withId(R.id.spinnerJobType)).check(matches(isDisplayed()));
        onView(withId(R.id.requirementText)).check(matches(isDisplayed()));
        onView(withId(R.id.salaryText)).check(matches(isDisplayed()));
        onView(withId(R.id.spinnerUrgency)).check(matches(isDisplayed()));
        onView(withId(R.id.locationJob)).check(matches(isDisplayed()));
        onView(withId(R.id.expectedDuration)).check(matches(isDisplayed()));
        onView(withId(R.id.startDate)).check(matches(isDisplayed()));
    }
}
