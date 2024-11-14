package com.example.quickcash;

import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

import android.widget.*;

public class UserRatingJUnitTest {

    //currently class is mocked using an interface and Mockito
    @Mock
    UserRatingHelper userRatingHelper;

    //initializes mocks
    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    //it is assumed that a helper class is used to handle RatingBar and EditText on the comment page.
    @Test
    public void starRatingComponentTest() {
        //mocked method returns
        Mockito.when(userRatingHelper.getRating()).thenReturn((float) 0);

        userRatingHelper.setRating(5F);

        assertEquals("After rating is set to 5.0, getRating should return 5.0",5F,userRatingHelper.getRating(),0);
    }

    @Test
    public void commentFieldTest() {
        //mocked method returns
        Mockito.when(userRatingHelper.getComment()).thenReturn("");

        userRatingHelper.setComment("testing");

        assertEquals("After comment is set, getComment should return the set comment","testing",userRatingHelper.getComment());
    }
}
