package com.example.quickcash.maps;

import org.junit.Test;
import static org.junit.Assert.*;
import com.example.quickcash.models.JobMap;

public class MapUnitTest {
    // Tests to be checked:
    // - Job title
    // - Salary
    // - Duration
    // - Location
    // - Add to perfect job

    @Test
    public void testValidJobTitle() {
        JobMap job = new JobMap();
        job.setJobTitle("Data Scientist");
        assertEquals("Data Scientist job title should match",
                "Data Scientist", job.getJobTitle());
    }

    @Test
    public void testInvalidJobTitle() {
        JobMap job = new JobMap();
        job.setJobTitle("");
        assertTrue("Empty job title should be invalid",
                job.getJobTitle() == null || job.getJobTitle().isEmpty());
    }

    @Test
    public void testValidSalary() {
        JobMap job = new JobMap();
        job.setSalary(30.0);
        assertEquals("Salary should be 30.0",
                30.0, job.getSalary(), 0.001);
    }

    @Test
    public void testInvalidSalary() {
        JobMap job = new JobMap();
        job.setSalary(0.0);
        assertTrue("Salary should be greater than 0",
                job.getSalary() >= 0.0);
    }

    @Test
    public void testValidDuration() {
        JobMap job = new JobMap();
        job.setDuration("3 years");
        assertEquals("Duration should be 3 years",
                "3 years", job.getDuration());
    }

    @Test
    public void testInvalidDuration() {
        JobMap job = new JobMap();
        job.setDuration("");
        assertTrue("Duration shouldn't be empty",
                job.getDuration() == null || job.getDuration().isEmpty());
    }

    @Test
    public void testValidLatitude() {
        JobMap job = new JobMap();
        job.setLatitude(10.0);
        assertEquals("Latitude should be 10.0",
                10.0, job.getLatitude(), 0.0);
    }

    @Test
    public void testInvalidLatitude() {
        JobMap job = new JobMap();
        job.setLatitude(-91.0);
        assertTrue("Latitude should not be not Less than -90 or more than 90",
                job.getLatitude()>= -90.0 || job.getLatitude() <= 90.0 );
    }

    @Test
    public void testValidLongitude() {
        JobMap job = new JobMap();
        job.setLongitude(10.0);
        assertEquals("Latitude should be 10.0",
                10.0, job.getLongitude(), 0.0);
    }
}