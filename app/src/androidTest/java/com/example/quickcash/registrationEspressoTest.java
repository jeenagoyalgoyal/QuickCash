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

public class registrationEspressoTest {

    private ActivityScenario<RegistrationActivity> regActivityScenario;

    @Before
    public void setup(){
        regActivityScenario = ActivityScenario.launch(RegistrationActivity.class);;
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

    @Test
    public void invalidNameTest(){

    }
}
