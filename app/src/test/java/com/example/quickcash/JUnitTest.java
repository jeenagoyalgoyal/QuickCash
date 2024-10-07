package com.example.quickcash;

import org.junit.Test;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JUnitTest {

    protected final String VALID_NAME = "Dhruv Sharma";
    protected final String INVALID_NAME = "@123 123@";
    protected final String EMPTY_STRING = new String();
    protected final String VALID_EMAIL_ADDRESS = "cg443818@dal.ca";
    protected final String INVALID_EMAIL_ADDRESS = "ad.asd.ads.asd";


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
    public void checkIfValidEmailAddress(){
        assertTrue(CredentialsValidator.isValidEmailAddress(VALID_EMAIL_ADDRESS));
    }

    @Test
    public void checkIfInvalidEmailAddress(){
        assertFalse(CredentialsValidator.isValidEmailAddress(INVALID_EMAIL_ADDRESS));
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

}