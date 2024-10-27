package com.example.quickcash;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PreferredJobsJUnitTest {

    //Dummy Jobs
    protected final TempJob Job = new TempJob("dummy Job Information");
    protected final TempJob Job2 = new TempJob("dummy Job Information 2");
    //ID of account that preferred job adds job to
    protected final String userID = "testingemail@test,db";

    /*
    It is assumed for now that jobs will be retrieved in an array format and that the last added job
    is appended to the end of this array. Refactor this test as necessary depending on your code implementation.
    */


    @Test
    public void checkIfPreferredJobIsAdded(){
       PreferredJobs prefJobs = new PreferredJobs();
       prefJobs.addJob(Job, userID);
       int indexOfAddedJob = prefJobs.getJobs(userID).length-1;
       TempJob returnedJob = prefJobs.getJobs(userID)[indexOfAddedJob];
       prefJobs.removeJob(Job, userID);
       assertEquals(returnedJob, Job);
    }

    @Test
    public void checkMultiplePreferredJobsAdded(){
        PreferredJobs prefJobs = new PreferredJobs();
        prefJobs.addJob(Job, userID);
        prefJobs.addJob(Job2, userID);
        int indexOfAddedJob = prefJobs.getJobs(userID).length-2;
        TempJob returnedJob = prefJobs.getJobs(userID)[indexOfAddedJob];
        prefJobs.removeJob(Job, userID);
        prefJobs.removeJob(Job2, userID);
        assertEquals(returnedJob, Job);
    }
}
