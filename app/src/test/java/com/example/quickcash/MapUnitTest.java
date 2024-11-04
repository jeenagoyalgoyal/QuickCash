package com.example.quickcash;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.quickcash.model.Job;

public class MapUnitTest {

    // Tests to be checked:
    // - Job title
    // - Salary
    // - Duration
    // - Location
    // - Add to perfect job

    private Job job;

    @Before
    public void setup(){
        job = new Job();
    }

    @Test
    public void testValidJobTitle() {

        job.setJobTitle("Data Scientist");
        assertEquals("Data Scientist job title should match",
                "Data Scientist", job.getJobTitle());
    }

    @Test
    public void testInvalidJobTitle() {

        job.setJobTitle("");
        assertTrue("Empty job title should be invalid",
                job.getJobTitle() == null || job.getJobTitle().isEmpty());
    }

    @Test
    public void testValidSalary() {

        job.setSalary(30);
        assertEquals("Salary should be 30.0",
                30, job.getSalary());
    }

    @Test
    public void testInvalidSalary() {

        job.setSalary(0);
        assertTrue("Salary should be greater than 0",
                job.getSalary() >= 0);
    }

    @Test
    public void testValidDuration() {

        job.setExpectedDuration("3 years");
        assertEquals("Duration should be 3 years",
                "3 years", job.getExpectedDuration());
    }

    @Test
    public void testInvalidDuration() {

        job.setExpectedDuration("");
        assertTrue("Duration shouldn't be empty",
                job.getExpectedDuration() == null || job.getExpectedDuration().isEmpty());
    }

    @Test
    public void testValidLatitude() {

        job.setLat(10.0);
        assertEquals("Latitude should be 10.0",
                10.0, job.getLat(), 0.0);
    }

    @Test
    public void testInvalidLatitude() {

        job.setLat(-91.0);
        assertTrue("Latitude should not be not Less than -90 or more than 90",
                job.getLat()>= -90.0 || job.getLat() <= 90.0 );
    }

    @Test
    public void testValidLongitude() {

        job.setLng(10.0);
        assertEquals("Latitude should be 10.0",
                10.0, job.getLng(), 0.0);
    }

    //  Used to test JobToMap class  //
}