package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.DownloadManager;

import com.example.quickcash.Firebase.JobCRUD;
import com.example.quickcash.model.Job;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JobSearchDatabaseTest {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mockDatabaseReference;
    private JobCRUD jobCRUD;
    private ArrayList<Job> jobList;

    @Before
    public void setUp() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        jobCRUD = new JobCRUD(firebaseDatabase);
    }

    @Test
    public void testMatchingJobListing(){
        String jobTitle = "Cybersecurity Analyst";

        Query query = firebaseDatabase.getReference("Jobs");
        query = query.orderByChild("jobTitle").equalTo(jobTitle);

        jobCRUD.getJobsByQuery(query).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Job> jobs = task.getResult();
                for (Job j : jobs) {
                    assertEquals(jobTitle, j.getJobTitle());
                }
            }
        });
    }

}
