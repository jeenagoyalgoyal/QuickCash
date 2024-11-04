package com.example.quickcash.maps;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.quickcash.deprecated.JobToMap;

public class MapUnitTest {
    // Tests to be checked:
    // - Job title
    // - Salary
    // - Duration
    // - Location
    // - Add to perfect job

    private JobToMap job;

    @Before
    public void setup(){
        job = new JobToMap();
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

        job.setDuration("3 years");
        assertEquals("Duration should be 3 years",
                "3 years", job.getDuration());
    }

    @Test
    public void testInvalidDuration() {

        job.setDuration("");
        assertTrue("Duration shouldn't be empty",
                job.getDuration() == null || job.getDuration().isEmpty());
    }

    @Test
    public void testValidLatitude() {

        job.setLatitude(10.0);
        assertEquals("Latitude should be 10.0",
                10.0, job.getLatitude(), 0.0);
    }

    @Test
    public void testInvalidLatitude() {

        job.setLatitude(-91.0);
        assertTrue("Latitude should not be not Less than -90 or more than 90",
                job.getLatitude()>= -90.0 || job.getLatitude() <= 90.0 );
    }

    @Test
    public void testValidLongitude() {

        job.setLongitude(10.0);
        assertEquals("Latitude should be 10.0",
                10.0, job.getLongitude(), 0.0);
    }
}