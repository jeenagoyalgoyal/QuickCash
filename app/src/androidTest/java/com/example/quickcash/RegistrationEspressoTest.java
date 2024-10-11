package com.example.quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;

public class RegistrationEspressoTest {

    private ActivityScenario<RegistrationActivity> activityScenario;

    private final String EMPTY_STRING = "";

    private final String VALID_EMAIL = "example@mail.com";
    private final String VALID_NAME = "John Doe";
    private final String VALID_PASSWORD = "Password123!";

    private final String INVALID_EMAIL = "InvalidEMAIL.EMAIL";
    private final String INVALID_NAME = "Invalid-Name";
    private final String INVALID_PASSWORD = "badpass";

    private final String EMPTY_MESSAGE = "cannot be empty!";
    private final String INVALID_MESSAGE = "invalid!";
    private final String VALID_MESSAGE = "valid";
    private final String UNMATCH_MESSAGE = "passwords do not match!";

    @Before
    public void setup(){
        activityScenario = ActivityScenario.launch(RegistrationActivity.class);
        activityScenario.onActivity(activity -> {
            activity.setupRegisterButton();
        });
    }

    @Test
    public void emptyNameTest() {
        onView(withId(R.id.enterName)).perform(typeText(EMPTY_STRING));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validName)).check(matches(withText(EMPTY_MESSAGE)));
    }

    @Test
    public void emptyEmailTest() {
        onView(withId(R.id.enterEmail)).perform(typeText(EMPTY_STRING));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validEmail)).check(matches(withText(EMPTY_MESSAGE)));
    }

    @Test
    public void emptyPasswordTest() {
        onView(withId(R.id.enterPassword)).perform(typeText(EMPTY_STRING));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validPassword)).check(matches(withText(EMPTY_MESSAGE)));
    }

    @Test
    public void emptyPassword2Test() {
        onView(withId(R.id.enterPassword2)).perform(typeText(EMPTY_STRING));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validPassword2)).check(matches(withText(EMPTY_MESSAGE)));
    }

    @Test
    public void validNameTest() {
        onView(withId(R.id.enterName)).perform(typeText(VALID_NAME));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validName)).check(matches(withText(VALID_MESSAGE)));
    }

    @Test
    public void validEmailTest() {
        onView(withId(R.id.enterEmail)).perform(typeText(VALID_EMAIL));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validEmail)).check(matches(withText(VALID_MESSAGE)));
    }

    @Test
    public void validPasswordTest() {
        onView(withId(R.id.enterPassword)).perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.enterPassword2)).perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validPassword)).check(matches(withText(VALID_MESSAGE)));
    }

    @Test
    public void validRoleTest() {
        onView(withId(R.id.enterPassword)).perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.enterPassword2)).perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validPassword)).check(matches(withText(VALID_MESSAGE)));
    }


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
        onView(withId(R.id.validEmail)).check(matches(withText(INVALID_MESSAGE)));
    }

    @Test
    public void invalidPasswordTest() {
        onView(withId(R.id.enterPassword)).perform(typeText(INVALID_PASSWORD));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validPassword)).check(matches(withText(INVALID_MESSAGE)));
    }

    @Test
    public void invalidPassword2Test() {
        onView(withId(R.id.enterPassword2)).perform(typeText(INVALID_PASSWORD));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validPassword2)).check(matches(withText(INVALID_MESSAGE)));
    }

    @Test
    public void unmatchedPasswordTest() {
        onView(withId(R.id.enterPassword)).perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.enterPassword2)).perform(typeText(VALID_PASSWORD+1));
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.validPassword)).check(matches(withText(UNMATCH_MESSAGE)));
        onView(withId(R.id.validPassword2)).check(matches(withText(UNMATCH_MESSAGE)));

    }
}
