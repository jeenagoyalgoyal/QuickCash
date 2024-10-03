package com.example.quickcash;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import org.junit.Before;
import androidx.test.espresso.Espresso;

import org.junit.Test;

public class registrationEspressoTest {

    private ActivityScenario<RegistrationActivity> regActivityScenario;

    @Before
    public void setup(){
        regActivityScenario = ActivityScenario.launch(RegistrationActivity.class);;
    }

    @Test
    public void validNameTest() {
        Espresso.onView(withId(R.id.enterName)).perform(typeText("example name"));
        Espresso.onView(withId(R.id.buttonRegister)).perform(click());
        Espresso.onView(withId(R.id.validName)).check(matches(withText("Valid Name")));
    }

    @Test
    public void validEmailTest() {
        Espresso.onView(withId(R.id.enterEmail)).perform(typeText("example@email.com"));
        Espresso.onView(withId(R.id.buttonRegister)).perform(click());
        Espresso.onView(withId(R.id.validEmail)).check(matches(withText("Valid Email")));
    }

    @Test
    public void validPasswordTest() {
        Espresso.onView(withId(R.id.enterPassword)).perform(typeText("example@email.com"));
        Espresso.onView(withId(R.id.buttonRegister)).perform(click());
        Espresso.onView(withId(R.id.validPassword)).check(matches(withText("Valid Password")));
    }

    @Test
    public void invalidNameTest(){

    }
}
