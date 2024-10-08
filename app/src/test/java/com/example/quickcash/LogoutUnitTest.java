package com.example.quickcash;

import androidx.annotation.ContentView;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LogoutUnitTest {

    Profile validator;

    @Before
    public void setup() {
        validator = new Profile();
    }

    @Test
    public void testYesLogoutButton(){
        assertTrue(validator.performLogout(Boolean.TRUE));
    }

    @Test
    public void testNoLogoutButton(){
        assertFalse(validator.performLogout(Boolean.FALSE));
    }
}
