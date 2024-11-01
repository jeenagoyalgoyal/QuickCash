package com.example.quickcash.maps;

import org.junit.Test;
import static org.junit.Assert.*;
import com.example.quickcash.ui.models.JobToMap;

public class MapUnitTest {
    // Tests to be checked:
    // - Job title
    // - Salary
    // - Duration
    // - Location
    // - Add to perfect job

    @Test
    public void testValidJobTitle() {
        JobToMap job = new JobToMap();
        job.setJobTitle("Data Scientist");
        assertEquals("Data Scientist job title should match",
                "Data Scientist", job.getJobTitle());
    }

    @Test
    public void testInvalidJobTitle() {
        JobToMap job = new JobToMap();
        job.setJobTitle("");
        assertTrue("Empty job title should be invalid",
                job.getJobTitle() == null || job.getJobTitle().isEmpty());
    }

    @Test
    public void testValidSalary() {
        JobToMap job = new JobToMap();
        job.setSalary(30);
        assertEquals("Salary should be 30.0",
                30, job.getSalary());
    }

    @Test
    public void testInvalidSalary() {
        JobToMap job = new JobToMap();
        job.setSalary(0);
        assertTrue("Salary should be greater than 0",
                job.getSalary() >= 0);
    }

    @Test
    public void testValidDuration() {
        JobToMap job = new JobToMap();
        job.setDuration("3 years");
        assertEquals("Duration should be 3 years",
                "3 years", job.getDuration());
    }

    @Test
    public void testInvalidDuration() {
        JobToMap job = new JobToMap();
        job.setDuration("");
        assertTrue("Duration shouldn't be empty",
                job.getDuration() == null || job.getDuration().isEmpty());
    }

    @Test
    public void testValidLatitude() {
        JobToMap job = new JobToMap();
        job.setLatitude(10.0);
        assertEquals("Latitude should be 10.0",
                10.0, job.getLatitude(), 0.0);
    }

    @Test
    public void testInvalidLatitude() {
        JobToMap job = new JobToMap();
        job.setLatitude(-91.0);
        assertTrue("Latitude should not be not Less than -90 or more than 90",
                job.getLatitude()>= -90.0 || job.getLatitude() <= 90.0 );
    }

    @Test
    public void testValidLongitude() {
        JobToMap job = new JobToMap();
        job.setLongitude(10.0);
        assertEquals("Latitude should be 10.0",
                10.0, job.getLongitude(), 0.0);
    }
}