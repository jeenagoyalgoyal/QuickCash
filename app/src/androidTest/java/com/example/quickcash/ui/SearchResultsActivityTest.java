package com.example.quickcash.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.quickcash.R;
import com.example.quickcash.ui.activities.JobSearchParameterActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchResultsActivityTest {

    @Rule
    public ActivityScenarioRule<JobSearchParameterActivity> activityRule =
            new ActivityScenarioRule<>(JobSearchParameterActivity.class);

    @Test
    public void testShowMapButtonIsVisible() {
        // Verify that the "Show Map" button is visible on the search page
        onView(withId(R.id.showMapButton))
                .check(matches(isDisplayed()));
    }
}