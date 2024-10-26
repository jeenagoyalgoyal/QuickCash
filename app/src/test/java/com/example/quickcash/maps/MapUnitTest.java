package com.example.quickcash.maps;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.quickcash.ui.activities.MapActivity;

public class MapUnitTest {
    // Tests to be checked:
    // - Job title
    // - Salary
    // - Duration
    // - Location
    // - Add to perfect job

    @Test
    public void testValidJobTitle() {
        // Assuming getJobTitle takes name as input
        // and returns job title for job posted by given name
        // Can be changed depending on how we are getting job title for pin on map

        // Also change activity to correct getter activity if changed
        assertEquals("John has posted Data Scientist as a job listing",
                "Data Scientist", MapActivity.getJobTitle("John"));
    }

    @Test
    public void testInvalidJobTitle() {
        // Using a name that doesn't have a job posting
        assertEquals("Should specify job asked isn't present",
                "Invalid", MapActivity.getJobTitle("UnknownUser"));
    }

    @Test
    public void testValidSalary() {
        // Assuming getSalary takes name and job listed as input
        // and returns salary for job posted by given name
        // Can be changed depending on how we are getting job title for pin on map

        assertTrue("Salary is $30/hour for Data Scientist job listed by John",
                30 == MapActivity.getSalary("John", "Data Scientist"));
    }

    @Test
    public void testInvalidSalary() {
        // Corrected assertion logic: Salary should be greater than 0
        assertTrue("Salary is always more than 0",
                0 < MapActivity.getSalary("John", "Data Scientist"));
    }

    @Test
    public void testValidDuration() {
        assertArrayEquals("3 years is valid duration for contract",
                new String[]{"3", "years"},
                MapActivity.getDuration("John", "Data Scientist"));
    }

    @Test
    public void testInvalidDuration() {
        // Corrected assertion logic: Duration should be greater than 0
        assertTrue("Duration can't be less than 0",
                0 < Integer.parseInt(MapActivity.getDuration("John", "Data Scientist")[0]));
    }
}
