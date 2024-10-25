package com.example.quickcash;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class UserRoleEspressoTest {

    @Rule
    public ActivityScenarioRule<RoleActivity> activity = new ActivityScenarioRule<>(RoleActivity.class);

    @Test
    public void testRoleSwitching() {

        //Test for initial role as employee
        Espresso.onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employee")));
        //Click on the button to switch
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch)).perform(ViewActions.click());
        //Check role is employer now
        Espresso.onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employer")));
        //Click to switch role again
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch)).perform(ViewActions.click());
        // Check back to employee
        Espresso.onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employee")));
    }


    // Tests for updation on button text based on correct role assigned
    @Test
    public void testBtnTextSwitch() {
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch)).check(ViewAssertions.matches(ViewMatchers.withText("Switch to employee")));
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch)).check(ViewAssertions.matches(ViewMatchers.withText("Switch to employer")));
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch)).check(ViewAssertions.matches(ViewMatchers.withText("Switch to employee")));
    }

    //Test Multiple Switches
    @Test
    public void testMultipleSwitches() {
        Espresso.onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employee")));
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employer")));
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employee")));
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employer")));
    }

    //Test for elements for UI are shown or hidden specific to the role

    //Employee hidden buttons
    @Test
    public void testEmployeeButtons() {
        int id=123;
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employee")));
        Espresso.onView(ViewMatchers.withId(R.id.jobPosting)).check(ViewAssertions.matches(ViewMatchers.withText("Search Job")));
        Espresso.onView(ViewMatchers.withId(R.id.profileButton)).check(ViewAssertions.matches(ViewMatchers.withText("My Profile")));
        Espresso.onView(ViewMatchers.withId(R.id.scheduleButton)).check(ViewAssertions.matches(ViewMatchers.withText("Work Schedule")));
        Espresso.onView(ViewMatchers.withId(R.id.taskButton)).check(ViewAssertions.matches(ViewMatchers.withText("Tasks & Projects")));
        Espresso.onView(ViewMatchers.withId(R.id.performanceButton)).check(ViewAssertions.matches(ViewMatchers.withText("Performance Reviews")));
        Espresso.onView(ViewMatchers.withId(R.id.notificationsButton)).check(ViewAssertions.matches(ViewMatchers.withText("Notifications & Settings")));
    }

    //Employer hidden buttons
    @Test
    public void testEmployerButtons() {
        int id=123;
        Espresso.onView(ViewMatchers.withId(R.id.roleSwitch)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.welcomeText)).check(ViewAssertions.matches(ViewMatchers.withText("Welcome, employer")));
        Espresso.onView(ViewMatchers.withId(R.id.jobPosting)).check(ViewAssertions.matches(ViewMatchers.withText("Manage Job Posting")));
        Espresso.onView(ViewMatchers.withId(R.id.profileButton)).check(ViewAssertions.matches(ViewMatchers.withText("Employee Directory")));
        Espresso.onView(ViewMatchers.withId(R.id.scheduleButton)).check(ViewAssertions.matches(ViewMatchers.withText("Analytics & Reports")));
        Espresso.onView(ViewMatchers.withId(R.id.taskButton)).check(ViewAssertions.matches(ViewMatchers.withText("Tasks & Assignments")));
        Espresso.onView(ViewMatchers.withId(R.id.performanceButton)).check(ViewAssertions.matches(ViewMatchers.withText("Schedule & Meetings")));
        Espresso.onView(ViewMatchers.withId(R.id.notificationsButton)).check(ViewAssertions.matches(ViewMatchers.withText("Notifications & Settings")));
    }

}