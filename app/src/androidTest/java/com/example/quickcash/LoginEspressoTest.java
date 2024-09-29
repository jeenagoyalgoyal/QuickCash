package com.example.quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Espresso tests for UserLogin functionality.
 */
@RunWith(AndroidJUnit4.class)
public class LoginEspressoTest {

    private ActivityScenario<LoginActivity> activityScenario;
    private View decorView;

    @Before
    public void setUp() {
        // Launch the LoginActivity before each test
        activityScenario = ActivityScenario.launch(LoginActivity.class);

        // Get the decor view of the activity
        activityScenario.onActivity(activity -> {
            decorView = activity.getWindow().getDecorView();
        });
    }

    // Assuming these are the IDs for the UI components
    private static final String VALID_EMAIL = "valid@example.com";
    private static final String VALID_PASSWORD = "Valid123";
    private static final String INVALID_EMAIL = "invalid_email";
    private static final String INVALID_PASSWORD = "123";
    private static final String EMPTY_STRING = "";

    private static final String INVALID_EMAIL_MESSAGE = "Please enter a valid email";
    private static final String INVALID_PASSWORD_MESSAGE = "Invalid password";
    private static final String FIELD_EMPTY_MESSAGE = "Field cannot be empty";

    @Test
    public void testSuccessfulLogin() {
        onView(withId(R.id.Sign_In_Email)).perform(typeText(VALID_EMAIL));
        onView(withId(R.id.Sign_In_Password)).perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.Sign_In_Request)).perform(click());

        // Assuming there is a Dashboard activity or similar that the user navigates to
        onView(withId(R.id.dashboard_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidEmail() {
        onView(withId(R.id.Sign_In_Email)).perform(typeText(INVALID_EMAIL));
        onView(withId(R.id.Sign_In_Password)).perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.Sign_In_Request)).perform(click());

        onView(withText(INVALID_EMAIL_MESSAGE))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidPassword() {
        onView(withId(R.id.Sign_In_Email)).perform(typeText(VALID_EMAIL));
        onView(withId(R.id.Sign_In_Password)).perform(typeText(INVALID_PASSWORD));
        onView(withId(R.id.Sign_In_Request)).perform(click());

        onView(withText(INVALID_PASSWORD_MESSAGE))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyPassword() {
        onView(withId(R.id.Sign_In_Email)).perform(typeText(VALID_EMAIL));
        onView(withId(R.id.Sign_In_Password)).perform(typeText(EMPTY_STRING));
        onView(withId(R.id.Sign_In_Request)).perform(click());

        onView(withText(FIELD_EMPTY_MESSAGE))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyEmail() {
        onView(withId(R.id.Sign_In_Email)).perform(typeText(EMPTY_STRING));
        onView(withId(R.id.Sign_In_Password)).perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.Sign_In_Request)).perform(click());

        onView(withText(FIELD_EMPTY_MESSAGE))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyEmailAndPassword() {
        onView(withId(R.id.Sign_In_Email)).perform(typeText(EMPTY_STRING));
        onView(withId(R.id.Sign_In_Password)).perform(typeText(EMPTY_STRING));
        onView(withId(R.id.Sign_In_Request)).perform(click());

        onView(withText(FIELD_EMPTY_MESSAGE))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }
}
