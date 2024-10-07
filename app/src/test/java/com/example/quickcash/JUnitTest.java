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
    protected final String EMPTY_BANNER_ID = new String();


    @Test
    public void isValidBannerID(){
        assertTrue(CredentialsValidator.isValidBannerID());
    }
}