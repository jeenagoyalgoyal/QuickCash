package com.example.quickcash;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.quickcash.ui.activities.JobSearchParameterActivity;

import org.junit.Test;

public class  JobSearchParametersJUnitTest {


    @Test
    public void testValidJobTitle() {
        assertTrue(JobSearchParameterActivity.isValidJobTitle("Data Analyst"));
        assertFalse(JobSearchParameterActivity.isValidJobTitle(""));
        assertFalse(JobSearchParameterActivity.isValidJobTitle(null));
    }

    @Test
    public void testValidSalary() {
        assertTrue(JobSearchParameterActivity.isValidSalary(50000, 75000));
        assertTrue(JobSearchParameterActivity.isValidSalary(0, 0));
        assertFalse(JobSearchParameterActivity.isValidSalary(100000, 50000));
        assertFalse(JobSearchParameterActivity.isValidSalary(-50000, 75000));
        assertFalse(JobSearchParameterActivity.isValidSalary(50000, -75000));
    }

    @Test
    public void testExpectedDuration() {
        assertTrue(JobSearchParameterActivity.isValidDuration("5 years"));
        assertTrue(JobSearchParameterActivity.isValidDuration("7"));
        assertFalse(JobSearchParameterActivity.isValidDuration(""));
        assertFalse(JobSearchParameterActivity.isValidDuration(null));
    }

    @Test
    public void testValidLocation() {
        assertTrue(JobSearchParameterActivity.isValidLocation("Toronto"));
        assertFalse(JobSearchParameterActivity.isValidLocation(""));
        assertFalse(JobSearchParameterActivity.isValidLocation(null));
    }
}
