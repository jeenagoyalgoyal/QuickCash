package com.example.quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.core.app.ActivityScenario;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;

public class registrationEspressoTest {

    private ActivityScenario<RegistrationActivity> activityScenario;

    @Before
    public void setup(){
        activityScenario = ActivityScenario.launch(RegistrationActivity.class);
        activityScenario.onActivity(activity -> {
            activity.setupRegisterButton();
        });
    }

    @Test
    public void validNameTest() {
        onView(withId(R.id.enterName)).perform(typeText("example name"));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validName)).check(matches(withText("Valid Name")));
    }

    @Test
    public void validEmailTest() {
        onView(withId(R.id.enterEmail)).perform(typeText("example@email.com"));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validEmail)).check(matches(withText("Valid Email")));
    }

    @Test
    public void validPasswordTest() {
        onView(withId(R.id.enterPassword)).perform(typeText("example@email.com"));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validPassword)).check(matches(withText("Valid Password")));
    }

    //Setting up Invalid message and inputs
    private final String INVALID_MESSAGE = "Invalid Name";
    private final String INVALID_EMAIL = "Invalid@EMAIL.EMAIL";
    private final String INVALID_NAME = "Invalid_Name";
    private final String INVALID_PASSWORD = "Invalid_Pass";

    @Test
    public void invalidNameTest() {
        onView(withId(R.id.enterName)).perform(typeText(INVALID_NAME));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validName)).check(matches(withText(INVALID_MESSAGE)));
    }

    @Test
    public void invalidEmailTest() {
        onView(withId(R.id.enterEmail)).perform(typeText(INVALID_EMAIL));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validName)).check(matches(withText(INVALID_MESSAGE)));
    }
}
