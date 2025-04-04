package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.util.Log;

import com.example.quickcash.Firebase.JobCRUD;
import com.example.quickcash.model.Job;
import com.example.quickcash.model.JobLocation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class JobSubmissionDatabaseTest {

    private FirebaseDatabase firebaseDatabase;

    private JobCRUD jobCRUD;

    @Before
    public void setUp() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        jobCRUD = new JobCRUD(firebaseDatabase);
    }

    @Test
    public void testSubmitJobSendsCorrectDataToBackend() throws ExecutionException, InterruptedException {
        // Arrange
        Job job = new Job();
        job.setJobTitle("Lawn Mowing");
        job.setCompanyName("Mowad");
        job.setExpectedDuration("20");
        job.setEmployerId("test@gmail.com");
        job.setLocation("Halifax, NS B3H 4P7");
        job.setRequirements("Linux, Java");
        job.setSalary(50);
        job.setJobType("Multi day");
        job.setStartDate("Nov 20, 2024");
        job.setUrgency("High");

        // Act
        Task<Boolean> task = jobCRUD.submitJob(job);
        Boolean result = Tasks.await(task);
        Log.d("JobID", job.getJobId());

        // Assert
        assertTrue(result);

        jobCRUD.deleteJobById(job.getJobId());
    }

    @Test
    public void testJobDataMappingToBackendStructure() throws ExecutionException, InterruptedException{
        // Arrange
        Job job = new Job();
        job.setJobTitle("Lawn Mowing");
        job.setCompanyName("Mowad");
        job.setExpectedDuration("20");
        job.setEmployerId("test@gmail.com");
        job.setLocation("Halifax, NS B3H 4P7");
        job.setRequirements("Linux, Java");
        job.setSalary(50);
        job.setJobType("Multi day");
        job.setStartDate("Nov 20, 2024");
        job.setUrgency("High");

        // Act
        Task<Boolean> task = jobCRUD.submitJob(job);

        String expectedTitle = "Lawn Mowing";
        String expectedCompany = "Mowad";
        String expectedType = "Multi day";
        String expectedDuration = "20";
        String expectedEmployerId = "test@gmail.com";
        String expectedLocation = "Halifax, NS B3H 4P7";
        String expectedRequirements = "Linux, Java";
        String expectedStartDate = "Nov 20, 2024";
        String expectedUrgency = "High";
        int expectedSalary = 50;


       Task<Job> job1 = jobCRUD.getJobById(job.getJobId());
       Job job2 = Tasks.await(job1);

        // Act & Assert
        assertEquals(expectedTitle, job2.getJobTitle());
        assertEquals(expectedCompany, job2.getCompanyName());
        assertEquals(expectedType, job2.getJobType());
        assertEquals(expectedSalary, job2.getSalary());
        assertEquals(expectedUrgency, job2.getUrgency());
        assertEquals(expectedDuration, job2.getExpectedDuration());
        assertEquals(expectedEmployerId, job2.getEmployerId());
        assertEquals(expectedLocation, job2.getLocation());
        assertEquals(expectedRequirements, job2.getRequirements());
        assertEquals(expectedStartDate, job2.getStartDate());

        jobCRUD.deleteJobById(job.getJobId());
    }
}
