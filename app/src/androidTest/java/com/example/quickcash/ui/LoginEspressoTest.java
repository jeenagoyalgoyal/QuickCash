package com.example.quickcash.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.quickcash.R;
import com.example.quickcash.ui.activities.LoginActivity;

@RunWith(AndroidJUnit4.class)
public class LoginEspressoTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testSuccessfulLogin() {
        enterText(R.id.emailBox, "testingemail@test.db");
        enterText(R.id.passwordBox, "Test_Pass123#");
        clickButton(R.id.loginButton);
        // Verify we get to the dashboard after successful login
        // Need to wait a bit for the login response and redirect
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.welcomeEmployee)).check(matches(isDisplayed())); // Check for employee welcome since this account logs in as employee // Check for employer welcome since this account defaults to employer role
    }

    @Test
    public void testInvalidEmail() {
        enterText(R.id.emailBox, "invalid");
        enterText(R.id.passwordBox, "validPassword123");
        clickButton(R.id.loginButton);
        checkStatusLabel("Invalid email format.");
    }

    @Test
    public void testInvalidPassword() {
        enterText(R.id.emailBox, "valid@example.com");
        enterText(R.id.passwordBox, "wrongPass");
        clickButton(R.id.loginButton);
        checkStatusLabel("Password must be at least 6 characters long and contain at least one letter and one number.");
    }

    @Test
    public void testEmptyPassword() {
        enterText(R.id.emailBox, "valid@example.com");
        clearText(R.id.passwordBox);
        clickButton(R.id.loginButton);
        checkStatusLabel("Email or Password cannot be empty.");
    }

    @Test
    public void testEmptyEmail() {
        clearText(R.id.emailBox);
        enterText(R.id.passwordBox, "validPassword123");
        clickButton(R.id.loginButton);
        checkStatusLabel("Email or Password cannot be empty.");
    }

    @Test
    public void testEmptyEmailAndPassword() {
        clearText(R.id.emailBox);
        clearText(R.id.passwordBox);
        clickButton(R.id.loginButton);
        checkStatusLabel("Email or Password cannot be empty.");
    }

    private void enterText(int viewId, String text) {
        onView(withId(viewId))
                .perform(ViewActions.typeText(text), ViewActions.closeSoftKeyboard());
    }

    private void clearText(int viewId) {
        onView(withId(viewId))
                .perform(ViewActions.clearText(), ViewActions.closeSoftKeyboard());
    }

    private void clickButton(int viewId) {
        onView(withId(viewId)).perform(ViewActions.click());
    }

    private void checkStatusLabel(String expectedText) {
        onView(withId(R.id.statusLabel))
                .check(matches(withText(expectedText)));
    }
}