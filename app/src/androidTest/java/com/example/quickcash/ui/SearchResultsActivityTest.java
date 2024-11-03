package com.example.quickcash.ui;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.quickcash.R;
import com.example.quickcash.deprecated.SearchActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchResultsActivityTest {

    @Rule
    public ActivityScenarioRule<SearchActivity> activityRule =
            new ActivityScenarioRule<>(SearchActivity.class);

    @Test
    public void testShowMapButtonIsVisible() {
        // Verify that the "Show Map" button is visible on the search results page
        Espresso.onView(ViewMatchers.withId(R.id.showMapButton))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
