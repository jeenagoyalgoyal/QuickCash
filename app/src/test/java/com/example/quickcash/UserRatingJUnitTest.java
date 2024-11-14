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

    protected final float RATING = 5F;
    protected final String COMMENT = "test comment";

    //tests are made with surface level knowledge of UserRating feature, rework tests as needed to fit your implementation
    //it is assumed that a helper class delegate will be used to handle the backend logic of the rating page
    @Mock
    UserRatingHelper userRatingHelper;

    //initializes mocks
    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void starRatingComponentTest() {
        //mocked method returns
        Mockito.when(userRatingHelper.getRating()).thenReturn((float) 0);

        userRatingHelper.setRating(RATING);

        assertEquals("After rating is set to 5.0, getRating should return 5.0",RATING,userRatingHelper.getRating(),0);
    }

    @Test
    public void commentFieldTest() {
        //mocked method returns
        Mockito.when(userRatingHelper.getComment()).thenReturn("");

        userRatingHelper.setComment(COMMENT);

        assertEquals("After comment is set, getComment should return the set comment",COMMENT,userRatingHelper.getComment());
    }

    @Test
    public void addCommentButtonDisabledTest() {
        //mocked method returns
        Mockito.when(userRatingHelper.addCommentButtonIsEnabled()).thenReturn(true);

        assertEquals("Before rating and comment are set, button should be disabled",false,userRatingHelper.addCommentButtonIsEnabled());

        userRatingHelper.setRating(RATING);
        userRatingHelper.setComment(COMMENT);

        assertEquals("After rating and comment are set, button should be enabled",true,userRatingHelper.addCommentButtonIsEnabled());
    }

}
