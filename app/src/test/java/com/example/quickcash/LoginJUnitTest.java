package com.example.quickcash;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class LoginJUnitTest {

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
        assertFalse(validator.isValidPassword("ComplexPassword"));
    }

    @Test
    public void checkIfPasswordIdIsInvalid() {
        assertFalse(validator.isValidPassword("pass")); // Too short
        assertFalse(validator.isValidPassword("123")); // Too short
        assertFalse(validator.isValidPassword("")); // Empty password
    }

    @Test
    public void checkIfEmailIsEmpty() {
        assertTrue(validator.isEmptyEmailAddress(""));
        assertFalse(validator.isEmptyEmailAddress("3130@dal.ca"));
    }

    @Test
    public void checkIfEmailIsValid() {
        assertTrue(validator.isValidEmail("3130@dal.ca"));
        assertTrue(validator.isValidEmail("user.name+tag@domain.com"));
    }

    @Test
    public void checkIfEmailIsInvalid() {
        assertFalse(validator.isValidEmail("3130.dal.ca")); // Missing @
        assertFalse(validator.isValidEmail("user@domain")); // Missing top-level domain
        assertFalse(validator.isValidEmail("user@.com")); // Missing domain name
    }
}
