package com.example.quickcash;

import org.mockito.Mockito;

import org.junit.Test;

import static org.junit.Assert.*;

import android.widget.RatingBar;

public class UserRatingJUnitTest {

    //it is assumed that star rating component will be implemented using RatingBar Ui element, if you use another method to implement
    //then change test as needed
    @Test
    public void starRatingComponentTest() {
        //replace this mock with RatingBar UI element.
        //mock start
        RatingBar starRatingComponent = Mockito.mock(RatingBar.class);
        Mockito.when(starRatingComponent.getRating()).thenReturn((float) 0);
        //mock end

        starRatingComponent.setRating(5);
        assertEquals("After rating is set to 5.0, getRating should return 5.0",5,starRatingComponent.getRating(),0);
    }
}
