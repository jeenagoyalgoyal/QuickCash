package com.example.quickcash;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginJUnitTest {

    @Test
    public void checkIfPasswordIsEmpty() {
        assertTrue(LoginActivity.isEmptyPassword(""));
        assertFalse(LoginActivity.isEmptyPassword("Password123"));
    }

    @Test
    public void checkIfPasswordIdIsValid() {
        assertTrue(LoginActivity.isValidPassword("Pass@123"));
        assertFalse(LoginActivity.isValidPassword("ComplexPassword"));
    }

    @Test
    public void checkIfPasswordIdIsInvalid() {
        assertFalse(LoginActivity.isValidPassword("pass")); // Too short
        assertFalse(LoginActivity.isValidPassword("123")); // Too short
        assertFalse(LoginActivity.isValidPassword("")); // Empty password
    }

    @Test
    public void checkIfEmailIsEmpty() {
        assertTrue(LoginActivity.isEmptyEmailAddress(""));
        assertFalse(LoginActivity.isEmptyEmailAddress("3130@dal.ca"));
    }

    @Test
    public void checkIfEmailIsValid() {
        assertTrue(LoginActivity.isValidEmail("3130@dal.ca"));
        assertTrue(LoginActivity.isValidEmail("user.name+tag@domain.com"));
    }

    @Test
    public void checkIfEmailIsInvalid() {
        assertFalse(LoginActivity.isValidEmail("3130.dal.ca")); // Missing @
        assertFalse(LoginActivity.isValidEmail("user@domain")); // Missing top-level domain
        assertFalse(LoginActivity.isValidEmail("user@.com")); // Missing domain name
    }
}
