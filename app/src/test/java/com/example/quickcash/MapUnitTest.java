package com.example.quickcash;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapUnitTest {

    @Test
    public void TestValidJobTitle(){
        // Assuming getJobTitle takes name as input
        // and returns job title for job posted by given name
        // Can be changed depending on how we are getting job title for pin on map

        //Also change activity to correct getter activity if changed
        assertEquals(MapActivity.getJobTitle("John"), "Barber");
    }

    @Test
    public void TestInvalidJobTitle(){
        assertNotEquals(MapActivity.getJobTitle("John"), "null");
    }

}
