package com.example.quickcash;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class ExampleUnitTest {

    CredentialValidator validator;

    @Before
    public void setup() {
        validator = new CredentialValidator();
    }

    @Test
    public void checkIfPasswordIsEmpty() {
        assertTrue(validator.isEmptyPassword(""));
        assertFalse(validator.isEmptyPassword("Password123"));
    }

    @Test
    public void checkIfPasswordIdIsValid() {
        assertTrue(validator.isValidPassword("Pass123"));
        assertTrue(validator.isValidPassword("ComplexPassword"));
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
        assertTrue(validator.isValidEmailAddress("3130@dal.ca"));
        assertTrue(validator.isValidEmailAddress("user.name+tag@domain.com"));
    }

    @Test
    public void checkIfEmailIsInvalid() {
        assertFalse(validator.isValidEmailAddress("3130.dal.ca")); // Missing @
        assertFalse(validator.isValidEmailAddress("user@domain")); // Missing top-level domain
        assertFalse(validator.isValidEmailAddress("user@.com")); // Missing domain name
    }
}
