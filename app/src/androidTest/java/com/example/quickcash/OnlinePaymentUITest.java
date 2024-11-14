package com.example.quickcash;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static android.support.test.uiautomator;


import android.graphics.Color;
import android.os.SystemClock;
import android.widget.TextView;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.TypeTextAction;
import androidx.test.espresso.assertion.ViewAssertions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Root;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnit4.class)
public class OnlinePaymentUITest {

    public ActivityScenario<EmployerHomepageActivity> employerActivityScenario;
    public ActivityScenario<OnlinePaymentActivity> onlinePaymentActivityScenario;

    public void setupEmployer() {
        employerActivityScenario = ActivityScenario.launch(EmployerHomepageActivity.class);
    }

    public void setupOnlinePayment() {
        onlinePaymentActivityScenario = ActivityScenario.launch(OnlinePaymentActivity.class);
    }

    @Test
    public void checkPayEmployeeButton() {
        setupEmployer();
        onView(withText("Pay Employee")).check(matches(isDisplayed()));
    }

    @Test
    public void checkFields() {
        setupEmployer();
        onView(withText("Pay Employee")).perform(click());
        onView(withText("Enter Amount")).check(matches(isDisplayed()));
        onView(withText("Select Employee")).check(matches(isDisplayed()));
        onView(withText("Pay")).check(matches(isDisplayed()));
        onView(withText("Cancel")).check(matches(isDisplayed()));
    }
}
