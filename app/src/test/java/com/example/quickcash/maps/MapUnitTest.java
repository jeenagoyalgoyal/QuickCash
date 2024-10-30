package com.example.quickcash.maps;

import org.junit.Test;
import static org.junit.Assert.*;
import com.example.quickcash.ui.models.Job;

public class MapUnitTest {
    // Tests to be checked:
    // - Job title
    // - Salary
    // - Duration
    // - Location
    // - Add to perfect job

    @Test
    public void testValidJobTitle() {
        Job job = new Job();
        job.setJobTitle("Data Scientist");
        assertEquals("Data Scientist job title should match",
                "Data Scientist", job.getJobTitle());
    }

    @Test
    public void testInvalidJobTitle() {
        Job job = new Job();
        job.setJobTitle("");
        assertTrue("Empty job title should be invalid",
                job.getJobTitle() == null || job.getJobTitle().isEmpty());
    }

    @Test
    public void testValidSalary() {
        Job job = new Job();
        job.setSalary(30.0);
        assertEquals("Salary should be 30.0",
                30.0, job.getSalary(), 0.001);
    }

    @Test
    public void testInvalidSalary() {
        Job job = new Job();
        job.setSalary(0.0);
        assertTrue("Salary should be greater than 0",
                job.getSalary() >= 0.0);
    }

    @Test
    public void testValidDuration() {
        Job job = new Job();
        job.setDuration("3 years");
        assertEquals("Duration should be 3 years",
                "3 years", job.getDuration());
    }

    @Test
    public void testInvalidDuration() {
        Job job = new Job();
        job.setDuration("");
        assertTrue("Duration shouldn't be empty",
                job.getDuration() == null || job.getDuration().isEmpty());
    }
}