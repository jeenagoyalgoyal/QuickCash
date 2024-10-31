package com.example.quickcash.ui.repositories;

import androidx.annotation.NonNull;

import com.example.quickcash.ui.models.Job;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class FirebaseCRUD {
    private final FirebaseDatabase database;
    private final DatabaseReference jobsRef;

    public FirebaseCRUD(FirebaseDatabase database) {
        this.database = database;
        this.jobsRef = database.getReference("Jobs");
        createDummyJobs();
    }

    private void createDummyJobs() {
        // Remove this line to prevent clearing existing jobs
        // jobsRef.removeValue().addOnCompleteListener(task -> {

        // Halifax jobs
        Job job1 = new Job();
        job1.setJobTitle("Software Developer");
        job1.setSalary(75000);
        job1.setDuration("Full-time");
        job1.setLocation("Halifax, NS");
        job1.setLatitude(44.6488);
        job1.setLongitude(-63.5752);
        jobsRef.child("job1").setValue(job1);

        // Toronto jobs
        Job job2 = new Job();
        job2.setJobTitle("Web Designer");
        job2.setSalary(85000);
        job2.setDuration("Contract");
        job2.setLocation("Toronto, ON");
        job2.setLatitude(43.6532);
        job2.setLongitude(-79.3832);
        jobsRef.child("job2").setValue(job2);

        // Vancouver jobs
        Job job3 = new Job();
        job3.setJobTitle("Data Analyst");
        job3.setSalary(80000);
        job3.setDuration("Part-time");
        job3.setLocation("Vancouver, BC");
        job3.setLatitude(49.2827);
        job3.setLongitude(-123.1207);
        jobsRef.child("job3").setValue(job3);

        // Montreal job
        Job job4 = new Job();
        job4.setJobTitle("UX Designer");
        job4.setSalary(70000);
        job4.setDuration("Full-time");
        job4.setLocation("Montreal, QC");
        job4.setLatitude(45.5017);
        job4.setLongitude(-73.5673);
        jobsRef.child("job4").setValue(job4);

        // Calgary job
        Job job5 = new Job();
        job5.setJobTitle("Project Manager");
        job5.setSalary(90000);
        job5.setDuration("Full-time");
        job5.setLocation("Calgary, AB");
        job5.setLatitude(51.0447);
        job5.setLongitude(-114.0719);
        jobsRef.child("job5").setValue(job5);

        // }); // Remove closing brace for removeValue()
    }

    public void searchJobs(final String query, final JobDataCallback callback) {
        // Wait for dummy data to be added before searching
        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Job> jobList = new ArrayList<>();
                String searchLower = query.toLowerCase().trim();

                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);
                    if (job != null) {
                        String location = job.getLocation().toLowerCase().trim();

                        if (location.contains(searchLower) ||
                                location.equals(searchLower) ||
                                location.replace(", ", ",").equals(searchLower.replace(", ", ","))) {
                            jobList.add(job);
                        }
                    }
                }
                callback.onCallback(jobList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(new ArrayList<>());
            }
        });
    }

    public interface JobDataCallback {
        void onCallback(List<Job> jobList);
    }
}
