package com.example.quickcash;

import java.util.ArrayList;
import java.util.List;

public class TempJob {
    private static List<Job> jobList;
    private static ArrayList<Job> preferredJobList = new ArrayList<>();

    // Static block to initialize the job list with dummy data
    static {
        jobList = new ArrayList<>();
        jobList.add(new Job(1, "Company A", "Software Engineer"));
        jobList.add(new Job(2, "Company B", "Data Scientist"));
        jobList.add(new Job(3, "Company C", "Product Manager"));
        jobList.add(new Job(4, "Company D", "Graphic Designer"));
        jobList.add(new Job(5, "Company E", "Web Developer"));
        jobList.add(new Job(6, "Company F", "Marketing Specialist"));
        jobList.add(new Job(7, "Company G", "Business Analyst"));
        jobList.add(new Job(8, "Company H", "UX/UI Designer"));
        jobList.add(new Job(9, "Company I", "DevOps Engineer"));
        jobList.add(new Job(10, "Company J", "Systems Administrator"));
    }

    // Method to get the list of dummy jobs
    public static List<Job> getJobList() {
        return jobList;
    }

    public static void addPreferredJob(Job job) {
        if (!preferredJobList.contains(job)) {
            preferredJobList.add(job);
        }
    }

    public static ArrayList<Job> getPreferredJobList() {
        return preferredJobList;
    }


    // Inner Job class to represent a job
    public static class Job {
        private int jobId;
        private String employerName;
        private String jobTitle;

        public Job(int jobId, String employerName, String jobTitle) {
            this.jobId = jobId;
            this.employerName = employerName;
            this.jobTitle = jobTitle;
        }

        public int getJobId() {
            return jobId;
        }

        public String getEmployerName() {
            return employerName;
        }

        public String getJobTitle() {
            return jobTitle;
        }

        // Override toString() method
        @Override
        public String toString() {
            return "Job ID: " + jobId + ", Employer: " + employerName + ", Title: " + jobTitle;
        }
    }
}
