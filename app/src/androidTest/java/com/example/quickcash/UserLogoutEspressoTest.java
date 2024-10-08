package com.example.quickcash;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import androidx.test.core.app.ActivityScenario;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.allOf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class UserLogoutEspressoTest {

    public ActivityScenario<MainActivity> activityScenario;
    public ActivityScenario<Profile> profileScenario;
    public Menu menu;

    @Before
    public void setup() {
        activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity(activity -> {
        });
        profileScenario = ActivityScenario.launch(Profile.class);
    }

    //Test to navigate to ProfileScreen
    @Test
    public void navigateToProfileScreen(){
        onView(withContentDescription("More options")).perform(click());
        onView(withText("Profile")).perform(click());
        onView(withId(R.id.logout_button)).check(matches(isDisplayed()));
    }

    @Test
    public void confirmDialogBox(){
        onView(withId(R.id.logout_button)).perform(click());
        onView(withText("Are you sure you want to Logout")).check(matches(isDisplayed()));
    }

    @Test
    public void navigateProfileToLogin(){
        onView(withId(R.id.logout_button)).perform(click());
        onView(withText("Yes")).perform(click());
        onView(withText("First Fragment")).check(matches(isDisplayed()));
    }



}
