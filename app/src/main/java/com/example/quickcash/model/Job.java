package com.example.quickcash.model;

public class Job {
    private String companyName;
    private String employerId;
    private String expectedDuration;
    private String jobId;
    private String jobTitle;
    private String jobType;
    private String location;
    private String requirements;
    private int salary;
    private String startDate;
    private String urgency;

    public Job() {}

    public String getCompanyName(){return companyName;}
    public String getEmployerId() {return employerId;}
    public String getExpectedDuration() {return expectedDuration;}
    public String getJobId() {return jobId;}
    public String getJobTitle() {return jobTitle;}
    public String getLocation() {return location;}
    public String getJobType() {return jobType;}
    public String getRequirements() {return requirements;}
    public int getSalary() {return salary;}
    public String getStartDate() {return startDate;}
    public String getUrgency() {return urgency;}

    public void setCompanyName(String companyName){this.companyName = companyName;}
    public void setEmployerId(String employerId) {this.employerId = employerId;}
    public void setExpectedDuration(String expectedDuration) {this.expectedDuration = expectedDuration;}
    public void setJobId(String jobId) {this.jobId = jobId;}
    public void setJobTitle(String jobTitle) {this.jobTitle = jobTitle;}
    public void setJobType(String jobType) {this.jobType = jobType;}
    public void setLocation(String location) {this.location = location;}
    public void setRequirements(String requirements) {this.requirements = requirements;}
    public void setSalary(int salary) {this.salary = salary;}
    public void setStartDate(String startDate) {this.startDate = startDate;}
    public void setUrgency(String urgency) {this.urgency = urgency;}

    public void setAllField(String jobTitle, String companyName, String jobType, String requirements,
                            int salary, String urgency, String location, String expectedDuration,
                            String startDate, String employerId, String jobId) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.jobType = jobType;
        this.requirements = requirements;
        this.salary = salary;
        this.urgency = urgency;
        this.location = location;
        this.expectedDuration = expectedDuration;
        this.startDate = startDate;
        this.employerId = employerId;
        this.jobId = jobId;
    }
}