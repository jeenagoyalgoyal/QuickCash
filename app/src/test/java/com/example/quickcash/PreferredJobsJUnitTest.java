package com.example.quickcash;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.quickcash.adapter.JobSearchAdapter;
import com.example.quickcash.filter.JobSearchFilter;
import com.example.quickcash.model.Job;

import java.util.ArrayList;
import java.util.List;

public class  PreferredJobsJUnitTest {


    @Test
    public void testValidJobTitle() {
        assertTrue(JobSearchFilter.isValidField("Data Analyst"));
        assertFalse(JobSearchFilter.isValidField(""));
        assertFalse(JobSearchFilter.isValidField(null));
    }

    @Test
    public void testValidSalary() {
        assertTrue(JobSearchFilter.isValidSalary(50000, 75000));
        assertTrue(JobSearchFilter.isValidSalary(0, 0));
        assertFalse(JobSearchFilter.isValidSalary(100000, 50000));
        assertFalse(JobSearchFilter.isValidSalary(-50000, 75000));
        assertFalse(JobSearchFilter.isValidSalary(50000, -75000));
    }

    @Test
    public void testExpectedDuration() {
        assertTrue(JobSearchFilter.isValidField("5 years"));
        assertTrue(JobSearchFilter.isValidField("7"));
        assertFalse(JobSearchFilter.isValidField(""));
        assertFalse(JobSearchFilter.isValidField(null));
    }

    @Test
    public void testValidLocation() {
        assertTrue(JobSearchFilter.isValidField("Toronto"));
        assertFalse(JobSearchFilter.isValidField(""));
        assertFalse(JobSearchFilter.isValidField(null));
    }
}
