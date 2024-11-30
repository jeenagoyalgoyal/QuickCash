package com.example.quickcash;

import com.example.quickcash.model.Job;

public class OnlinePaymentDataDummy {
    Job job;

    public OnlinePaymentDataDummy(){
        job = new Job();
        job.setCompanyName("xyz");
        job.setEmployerId("test@gmail.com");
        job.setExpectedDuration("20");
        job.setJobId("-OBmr8wmfFYluJOywEYP");
        job.setJobTitle("Cybersecurity Analyst");
        job.setJobType("Full-time");
        job.setLocation("Halifax, NS B3H 4P7");
        job.setRequirements("Linux, Java");
        job.setSalary(50);
        job.setStartDate("Nov 20, 2024");
        job.setUrgency("High");
        job.setStatus("In-progress");
    }

    public Job getJob(){
        return job;
    }

}
