package com.example.quickcash;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapUnitTest {
    //To be checked:
    //Job title
    //Salary
    //Duration
    //Location
    //Add to perfect job

    @Test
    public void testValidJobTitle(){
        // Assuming getJobTitle takes name as input
        // and returns job title for job posted by given name
        // Can be changed depending on how we are getting job title for pin on map

        //Also change activity to correct getter activity if changed
        assertEquals("Barber", MapActivity.getJobTitle("John"));
    }

    @Test
    public void testInvalidJobTitle(){
        assertNotEquals("null", MapActivity.getJobTitle("John"));
    }


}
