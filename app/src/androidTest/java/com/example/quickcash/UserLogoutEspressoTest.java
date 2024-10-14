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

    public ActivityScenario<RoleActivity> activityScenario;

    @Before
    public void setup() {
        activityScenario = ActivityScenario.launch(RoleActivity.class);
    }


    //Test to navigate to ProfileScreen
    @Test
    public void navigateDashboardToProfileScreen(){
        onView(withId(R.id.profileButton)).perform(click());
        onView(withText("Profile")).check(matches(isDisplayed()));
    }

    //Test to navigate from Dashboard to Login Screen
    @Test
    public void navigateDashboardToLoginScreen(){
        onView(withId(R.id.profileButton)).perform(click());
        onView(withText("Profile")).check(matches(isDisplayed()));
        onView(withText("Logout")).perform(click());
        onView(withText("Yes")).perform(click());
        onView(withText("Login")).check(matches(isDisplayed()));
    }

}
