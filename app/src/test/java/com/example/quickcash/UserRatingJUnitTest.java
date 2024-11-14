package com.example.quickcash;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class UserRatingJUnitTest {

    protected final float RATING = 5F;
    protected final String COMMENT = "test comment";
    protected final String COMMENT_ID = "testcommentid";

    //tests are made with surface level knowledge of UserRating feature, rework tests as needed to fit your implementation

    //it is assumed that a helper class delegate will be used to handle the backend logic of the rating page
    @Mock
    UserRatingSubmissionHelper userRatingSubmissionHelper;

    //it us assumed that a helper class delegate will be used to handle the logic of retrieving and displaying ratings on employer profile
    @Mock
    UserRatingRetrievalHelper userRatingRetrievalHelper;


    //initializes mocks
    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void starRatingComponentTest() {
        //mocked method returns
        Mockito.when(userRatingSubmissionHelper.getRating()).thenReturn((float) 0);

        userRatingSubmissionHelper.setRating(RATING);
        assertEquals("After rating is set to 5.0, getRating should return 5.0",RATING, userRatingSubmissionHelper.getRating(),0);
    }

    @Test
    public void commentFieldTest() {
        //mocked method returns
        Mockito.when(userRatingSubmissionHelper.getComment()).thenReturn("");

        userRatingSubmissionHelper.setComment(COMMENT);
        assertEquals("After comment is set, getComment should return the set comment",COMMENT, userRatingSubmissionHelper.getComment());
    }

    @Test
    public void addCommentButtonDisabledTest() {
        //mocked method returns
        Mockito.when(userRatingSubmissionHelper.addCommentButtonIsEnabled()).thenReturn(true);

        assertEquals("Before rating and comment are set, button should be disabled",false, userRatingSubmissionHelper.addCommentButtonIsEnabled());
        userRatingSubmissionHelper.setRating(RATING);
        userRatingSubmissionHelper.setComment(COMMENT);
        assertEquals("After rating and comment are set, button should be enabled",true, userRatingSubmissionHelper.addCommentButtonIsEnabled());
    }

    @Test
    public void userRatingFormatCheck() {
        //mocked method returns
        Mockito.when(userRatingSubmissionHelper.formatIsValid()).thenReturn(false);

        userRatingSubmissionHelper.setRating(RATING);
        userRatingSubmissionHelper.setComment(COMMENT);
        userRatingSubmissionHelper.formatForFirebase();
        assertEquals("The rating and comment should have been formatted correctly for firebase",true, userRatingSubmissionHelper.formatIsValid());
    }

    //it is assumed that a helper class is used when retrieving ratings to show on the employer profile page
    @Test
    public void getComment() {
        //mocked method returns
        Mockito.when(userRatingRetrievalHelper.commentExists(COMMENT_ID)).thenReturn(false);

        assertEquals("test comment should exists when searched by ID (check database)",true, userRatingRetrievalHelper.commentExists(COMMENT_ID));
    }
}
