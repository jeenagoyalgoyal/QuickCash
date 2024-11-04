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
    public ActivityScenarioRule<EmployerHomepageActivity> employerActivityRule = new ActivityScenarioRule<>(EmployerHomepageActivity.class);

    @Rule
    public ActivityScenarioRule<EmployeeHomepageActivity> employeeActivityRule = new ActivityScenarioRule<>(EmployeeHomepageActivity.class);

    @Test
    public void testRoleSwitching() {

    }


    // Tests for updating on button text based on correct role assigned
    @Test
    public void testBtnTextSwitch() {
    }

    //Test Multiple Switches
    @Test
    public void testMultipleSwitches() {

    }

    //Test for elements for UI are shown or hidden specific to the role

    //Employee hidden buttons
    @Test
    public void testEmployeeButtons() {
        int id=123;

    }

    //Employer hidden buttons
    @Test
    public void testEmployerButtons() {
        int id=123;

    }

}