package com.example.quickcash;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.allOf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class UserProfileEspressoTest {

    public ActivityScenario<Profile> profileScenario;

    @Before
    public void setup() {
        profileScenario = ActivityScenario.launch(Profile.class);

    }

    @Test
    public void confirmDialogBox(){
        onView(withId(R.id.logout_button)).perform(click());
        onView(withText("Are you sure you want to Logout")).check(matches(isDisplayed()));
    }

    @Test
    public void checkDialogBoxNoButton(){
        onView(withId(R.id.logout_button)).perform(click());
        onView(withText("No")).perform(click());
        onView(withText("Profile")).check(matches(isDisplayed()));
    }

    @Test
    public void navigateProfileToLogin(){
        onView(withId(R.id.logout_button)).perform(click());
        onView(withText("Yes")).perform(click());
        onView(withText("Login Screen")).check(matches(isDisplayed()));
    }

}
