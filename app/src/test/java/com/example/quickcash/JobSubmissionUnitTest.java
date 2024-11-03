package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.example.quickcash.Firebase.JobCRUD;
import com.example.quickcash.model.Job;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutionException;

public class JobSubmissionUnitTest {

    @Mock
    private FirebaseDatabase mockFirebaseDatabase;
    @Mock
    private DatabaseReference mockDatabaseReference;

    private JobCRUD jobCRUD;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock FirebaseDatabase
        when(mockFirebaseDatabase.getReference(anyString())).thenReturn(mockDatabaseReference);

        // Inject the mock into your JobCRUD or provide a way to set it
        jobCRUD = new JobCRUD(mockFirebaseDatabase);
    }

    @Test
    public void testSubmitJobSendsCorrectDataToBackend() throws ExecutionException, InterruptedException {
        // Arrange
        Job job = new Job();
        job.setJobTitle("Cybersecurity Analyst");
        job.setCompanyName("Tech Company");
        job.setExpectedDuration("20");
        job.setEmployerId("test@gmail.com");
        job.setLocation("Montreal");
        job.setRequirements("Linux, Java");
        job.setSalary(50000);
        job.setJobType("Full-time");
        job.setStartDate("Nov 20, 2024");
        job.setUrgency("High");

        when(mockDatabaseReference.push()).thenReturn(mockDatabaseReference);
        when(mockDatabaseReference.getKey()).thenReturn("job123");
        when(mockDatabaseReference.setValue(any(Job.class))).thenReturn(Tasks.forResult(null));

        // Act
        Task<Boolean> task = jobCRUD.submitJob(job);
        Boolean result = Tasks.await(task);

        // Assert
        assertTrue(result);
        verify(mockDatabaseReference, times(1)).push();
        verify(mockDatabaseReference, times(1)).setValue(job);

        // Act
        /*boolean result = jobCRUD.submitJob(job).getResult();

        // Assert
        assertTrue(result);

        // Retrieve the job data to verify it was stored correctly
        DatabaseReference jobRef = databaseReference.child(job.getJobId());
        Task<DataSnapshot> getTask = jobRef.get();
        DataSnapshot snapshot = Tasks.await(getTask);

        if (snapshot.exists()) {
            Job retrievedJob = snapshot.getValue(Job.class);
            assertEquals("Cybersecurity Analyst", retrievedJob.getJobTitle());
            assertEquals("Tech Company", retrievedJob.getCompanyName());
            assertEquals(50000, retrievedJob.getSalary());
        }*/
    }

    @Test
    public void testJobDataMappingToBackendStructure() {
        // Arrange
        String expectedTitle = "Software Developer";
        String expectedCompany = "Tech Company";
        String expectedType = "Full-time";
        int expectedSalary = 50000;
        String expectedUrgency = "High";

        Job job = new Job();
        job.setJobTitle(expectedTitle);
        job.setCompanyName(expectedCompany);
        job.setJobType(expectedType);
        job.setSalary(expectedSalary);
        job.setUrgency(expectedUrgency);

        // Act & Assert
        assertEquals(expectedTitle, job.getJobTitle());
        assertEquals(expectedCompany, job.getCompanyName());
        assertEquals(expectedType, job.getJobType());
        assertEquals(expectedSalary, job.getSalary());
        assertEquals(expectedUrgency, job.getUrgency());
    }
}