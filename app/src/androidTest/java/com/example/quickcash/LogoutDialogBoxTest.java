package com.example.quickcash;

import androidx.test.core.app.ActivityScenario;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LogoutDialogBoxTest {

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
        onView(withText("Login")).check(matches(isDisplayed()));
    }

}
