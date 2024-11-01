package com.example.quickcash.ui;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.quickcash.R;
import com.example.quickcash.ui.activities.Profile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LogoutDialogBoxTest {

    private ActivityScenario<Profile> profileScenario;

    @Before
    public void setup() {
        // Get the app context
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Create an explicit intent to launch the Profile activity
        Intent intent = new Intent(appContext, Profile.class);
        // Launch the activity using the intent
        profileScenario = ActivityScenario.launch(intent);
    }

    @Test
    public void confirmDialogBox() {
        onView(withId(R.id.logout_button)).perform(click());
        onView(withText("Are you sure you want to Logout")).check(matches(isDisplayed()));
    }

    @Test
    public void checkDialogBoxNoButton() {
        onView(withId(R.id.logout_button)).perform(click());
        onView(withText("No")).perform(click());
        onView(withText("Profile")).check(matches(isDisplayed()));
    }

    @Test
    public void navigateProfileToLogin() {
        onView(withId(R.id.logout_button)).perform(click());
        onView(withText("Yes")).perform(click());
        onView(withText("Login")).check(matches(isDisplayed()));
    }
}
