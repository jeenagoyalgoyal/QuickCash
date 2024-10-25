package com.example.quickcash.authentication;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.quickcash.ui.utils.CredentialsValidator;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RegistrationJUnitTest {

    protected final String VALID_NAME = "John Doe";
    protected final String INVALID_NAME = "123$123 ";
    protected final String VALID_PASSWORD = "Testin.Pass123";
    protected final String INVALID_PASSWORD = "John# ";
    protected final String EMPTY_STRING = new String();
    protected final String VALID_EMAIL_ADDRESS = "cg443818@dal.ca ";
    protected final String INVALID_EMAIL_ADDRESS = "ad.asd.ads.asd ";

    @Test
    public void checkIfValidName(){
        assertTrue(CredentialsValidator.isValidName(VALID_NAME));
    }

    @Test
    public void checkIfInvalidName(){
        assertFalse(CredentialsValidator.isValidName(INVALID_NAME));
    }
    @Test
    public void checkIfEmptyInput(){
        assertTrue(CredentialsValidator.isEmptyInput(EMPTY_STRING));
    }

    @Test
    public void checkIfValidEmail(){
        assertTrue(CredentialsValidator.isValidEmail(VALID_EMAIL_ADDRESS));
    }

    @Test
    public void checkIfInvalidEmail(){
        assertFalse(CredentialsValidator.isValidEmail(INVALID_EMAIL_ADDRESS));
    }

    @Test
    public void checkIfValidRole(){
        assertTrue(CredentialsValidator.isValidRole("Employer"));
        assertTrue(CredentialsValidator.isValidRole("Employee"));
    }

    @Test
    public void checkIfInvalidRole(){
        assertFalse(CredentialsValidator.isValidRole("ppalsdpal"));
        assertFalse(CredentialsValidator.isValidRole(new String()));
    }

    @Test
    public void checkIfValidPassword(){
        assertTrue(CredentialsValidator.isValidPassword(VALID_PASSWORD));
    }

    @Test
    public void checkIfInvalidPassword(){
        assertFalse(CredentialsValidator.isValidPassword(INVALID_PASSWORD));
    }
}