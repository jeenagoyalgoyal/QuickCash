package com.example.quickcash.maps;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.quickcash.ui.activities.MapActivity;

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
        assertEquals("John has posted Barber as a job listing","Data Scientist", MapActivity.getJobTitle("John"));
    }

    @Test
    public void testInvalidJobTitle(){
        //Similiar instruction to above unit test
        assertEquals("Should specify job asked isn't present","InValid", MapActivity.getJobTitle("John"));
    }

    @Test
    public void testValidSalary(){
        // Assuming getSalary takes name and job listed as input
        // and returns salary for job posted by given name
        // Can be changed depending on how we are getting job title for pin on map

        assertTrue ("salary is $30/hour for Data Scientist job listed by John",30 == MapActivity.getSalary("John", "Data Scientist"));
    }

    @Test
    public void testInvalidSalary(){
        //Similiar instruction to above unit test
        assertFalse("Salary is always more than 0",0 < MapActivity.getSalary("John", "Data Scientist"));
        assertEquals("Salary for Data Scientist job by John is $30/hour", 30, MapActivity.getSalary("John", "Data Scientist"));
    }
}
