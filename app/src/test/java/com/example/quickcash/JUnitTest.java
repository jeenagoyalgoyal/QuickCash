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

    protected final String VALID_BANNER_ID = "B00831332";
    protected final String INVALID_BANNER_ID = "S00831332";
    protected final String EMPTY_STRING = new String();
    protected final String VALID_EMAIL_ADDRESS = "cg443818@dal.ca";
    protected final String INVALID_EMAIL_ADDRESS = "ad.asd.ads.asd";


    @Test
    public void checkIfValidBannerID(){
        assertTrue(CredentialsValidator.isValidBannerID(VALID_BANNER_ID));
    }

    @Test
    public void checkIfInvalidBannerID(){
        assertFalse(CredentialsValidator.isValidBannerID(INVALID_BANNER_ID));
    }

    @Test
    public void checkIfEmptyBannerID(){
        assertTrue(CredentialsValidator.isEmptyBannerID(EMPTY_STRING));
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