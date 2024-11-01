package com.example.quickcash;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.quickcash.adapter.JobSearchAdapter;
import com.example.quickcash.model.Job;

import java.util.ArrayList;
import java.util.List;

public class PreferredJobsJUnitTest {

    // Dummy Jobs
    protected Job job;
    protected Job job2;
    // In-memory list to simulate preferred jobs
    protected List<Job> preferredJobsList;

    @Before
    public void setUp() {
        job = new Job();
        job.setJobTitle("dummy Job Information");
        job.setCompanyName("Dummy Company");
        job.setLocation("Dummy Location");
        job.setSalary(50000);
        job.setExpectedDuration("3 months");

        job2 = new Job();
        job2.setJobTitle("dummy Job Information 2");
        job2.setCompanyName("Dummy Company 2");
        job2.setLocation("Dummy Location 2");
        job2.setSalary(60000);
        job2.setExpectedDuration("6 months");

        // Initialize the preferred jobs list
        preferredJobsList = new ArrayList<>();
    }

    @Test
    public void checkIfPreferredJobIsAdded() {
        // Create a JobSearchAdapter with the in-memory preferred jobs list
        JobSearchAdapter jobSearchAdapter = new JobSearchAdapter(preferredJobsList);

        // Simulate adding the job to the preferred list
        jobSearchAdapter.addJobToPreferredList(job, null); // Pass null for context since we won't use it

        // Check if the job was added
        assertEquals(1, preferredJobsList.size());
        assertEquals(job.getJobTitle(), preferredJobsList.get(0).getJobTitle());
    }

    @Test
    public void checkMultiplePreferredJobsAdded() {
        // Create a JobSearchAdapter with the in-memory preferred jobs list
        JobSearchAdapter jobSearchAdapter = new JobSearchAdapter(preferredJobsList);

        // Simulate adding both jobs to the preferred list
        jobSearchAdapter.addJobToPreferredList(job, null);
        jobSearchAdapter.addJobToPreferredList(job2, null);

        // Check if both jobs were added
        assertEquals(2, preferredJobsList.size());
        assertTrue(preferredJobsList.contains(job));
        assertTrue(preferredJobsList.contains(job2));
    }
}
