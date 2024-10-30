package com.example.quickcash.ui;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.example.quickcash.R;
import com.example.quickcash.ui.activities.RoleActivity;
import org.junit.Rule;
import org.junit.Test;

public class UserRoleEspressoTest {

    @Rule
    public ActivityScenarioRule<RoleActivity> activityRule =
            new ActivityScenarioRule<>(RoleActivity.class);

    @Test
    public void testRoleSwitching() {
        Espresso.onView(ViewMatchers.withId(R.id.welcomeText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employee")));

        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.welcomeText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employer")));
    }

    @Test
    public void testBtnTextSwitch() {
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch))
                .check(ViewAssertions.matches(ViewMatchers.withText("Switch to employer")));

        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch))
                .check(ViewAssertions.matches(ViewMatchers.withText("Switch to employee")));
    }

    @Test
    public void testEmployeeButtons() {
        Espresso.onView(ViewMatchers.withId(R.id.jobPosting))
                .check(ViewAssertions.matches(ViewMatchers.withText("Search Job Posting")));

        Espresso.onView(ViewMatchers.withId(R.id.profileButton))
                .check(ViewAssertions.matches(ViewMatchers.withText("My Profile")));

        Espresso.onView(ViewMatchers.withId(R.id.scheduleButton))
                .check(ViewAssertions.matches(ViewMatchers.withText("Work Schedule")));
    }

    @Test
    public void testEmployerButtons() {
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.jobPosting))
                .check(ViewAssertions.matches(ViewMatchers.withText("Manage Job Posting")));

        Espresso.onView(ViewMatchers.withId(R.id.profileButton))
                .check(ViewAssertions.matches(ViewMatchers.withText("Employee Directory")));
    }
}