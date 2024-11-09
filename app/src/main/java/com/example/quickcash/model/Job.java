package com.example.quickcash.model;

public class Job {
    private String jobTitle;
    private String companyName;
    private String jobType;
    private String requirements;
    private int salary;
    private String urgency;
    private String location;
    private String expectedDuration;
    private String startDate;
    private String employerId;
    private String jobId;
    private JobLocation jobLocation;

    // Default constructor required for Firebase
    public Job() {
    }

    // Method to set all fields at once
    public void setAllField(String jobTitle, String companyName, String jobType,
                            String requirements, int salary, String urgency,
                            String location, String expectedDuration, String startDate,
                            String employerId, String jobId, Double lat, Double lng) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.jobType = jobType;
        this.requirements = requirements != null ? requirements : ""; // Handle null requirements
        this.salary = salary;
        this.urgency = urgency;
        this.location = location;
        this.expectedDuration = expectedDuration;
        this.startDate = startDate;
        this.employerId = employerId;
        this.jobId = jobId;
        this.jobLocation = new JobLocation(lat, lng, location);
    }

    // Add these methods for JobLocation
    public void setJobLocation(JobLocation jobLocation) {
        this.jobLocation = jobLocation;
    }

    public JobLocation getJobLocation() {
        return jobLocation;
    }

    // Existing getters and setters
    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExpectedDuration() {
        return expectedDuration;
    }

    public void setExpectedDuration(String expectedDuration) {
        this.expectedDuration = expectedDuration;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}