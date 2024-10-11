package com.example.quickcash;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    CredentialsValidator validator;

    @Before
    public void setup() {
        validator = new CredentialsValidator();
    }

    @Test
    public void checkIfPasswordIsEmpty() {
        assertTrue(validator.isEmptyPassword(""));
        assertFalse(validator.isEmptyPassword("Password123"));
    }

    @Test
    public void checkIfPasswordIdIsValid() {
        assertTrue(validator.isValidPassword("Pass@123"));
    }

    @Test
    public void checkIfPasswordIdIsInvalid() {
        assertFalse(validator.isValidPassword("pass"));
        assertFalse(validator.isValidPassword("pass12345"));
    }

    @Test
    public void checkIfEmailIsEmpty() {
        assertTrue(validator.isEmptyEmailAddress(""));
        assertFalse(validator.isEmptyEmailAddress("3130@dal.ca"));
    }

    @Test
    public void checkIfEmailIsValid() {
        assertTrue(validator.isValidEmail("3130@dal.ca"));
    }

    @Test
    public void checkIfEmailIsInvalid() {
        assertFalse(validator.isValidEmail("3130.dal.ca"));
    }

}