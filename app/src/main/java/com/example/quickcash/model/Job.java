package com.example.quickcash.model;

/**
 * Class used for the Job format for database
 */
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
    private JobLocation jobLocation;
    public String status;
    public String employeeName;
    public String employeeId;

    /**
     * Constructor for the job
     */
    public Job() {}

    /**
     * Gets the job company
     * @return company name
     */
    public String getCompanyName(){return companyName;}

    /**
     * Get the employer id
     * @return employer id
     */
    public String getEmployerId() {return employerId;}

    /**
     * Get the expected duration of job
     * @return expected duration
     */
    public String getExpectedDuration() {return expectedDuration;}

    /**
     * Get the job id
     * @return job id
     */
    public String getJobId() {return jobId;}

    /**
     * Get the title of job
     * @return job title
     */
    public String getJobTitle() {return jobTitle;}

    /**
     * Get the location of job
     * @return location
     */
    public String getLocation() {return location;}

    /**
     * Get the type of job
     * @return job type
     */
    public String getJobType() {return jobType;}

    /**
     * Get the requirements of the job
     * @return requirements
     */
    public String getRequirements() {return requirements;}

    /**
     * Get the salary of job
     * @return salary
     */
    public int getSalary() {return salary;}

    /**
     * Get the start date of job
     * @return start date
     */
    public String getStartDate() {return startDate;}

    /**
     * Get the job urgency
     * @return urgency
     */
    public String getUrgency() {return urgency;}

    /**
     * Get the job location
     * @return jobLocation
     */
    public JobLocation getJobLocation() {
        return jobLocation;
    }
    /**
     * Get job status
     * @return status
     */
    public String getStatus() {return status;}

    /**
     * Get name of employee assigned to job
     * @return employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Get id of employee assigned to job
     * @return employeeName
     */
    public String getEmployeeId() {
        return employeeId;
    }


    /**
     * Set the company name
     * @param companyName
     */
    public void setCompanyName(String companyName){this.companyName = companyName;}

    /**
     * Set the employer id
     * @param employerId
     */
    public void setEmployerId(String employerId) {this.employerId = employerId;}

    /**
     * Set the expected duration of job
     * @param expectedDuration
     */
    public void setExpectedDuration(String expectedDuration) {this.expectedDuration = expectedDuration;}

    /**
     * Set the job id for posting
     * @param jobId
     */
    public void setJobId(String jobId) {this.jobId = jobId;}

    /**
     * Sets the title of job
     * @param jobTitle
     */
    public void setJobTitle(String jobTitle) {this.jobTitle = jobTitle;}

    /**
     * Sets the type of job
     * @param jobType
     */
    public void setJobType(String jobType) {this.jobType = jobType;}

    /**
     * Sets location for job
     * @param location
     */
    public void setLocation(String location) {this.location = location;}

    /**
     * Sets requirements for job
     * @param requirements
     */
    public void setRequirements(String requirements) {this.requirements = requirements;}

    /**
     * Sets the salary of posted job
     * @param salary
     */
    public void setSalary(int salary) {this.salary = salary;}

    /**
     * Sets the start date of job
     * @param startDate
     */
    public void setStartDate(String startDate) {this.startDate = startDate;}

    /**
     * Sets job urgency
     * @param urgency
     */
    public void setUrgency(String urgency) {this.urgency = urgency;}

    /**
     * sets job status
     * @param status
     */
    public void setStatus(String status){this.status =status;}

    /**
     * Sets job location
     * @param jobLocation
     */
    public void setJobLocation(JobLocation jobLocation) {
        this.jobLocation = jobLocation;
    }

    /**
     * Set all the required fields for the form
     * @param jobTitle
     * @param companyName
     * @param jobType
     * @param requirements
     * @param salary
     * @param urgency
     * @param location
     * @param expectedDuration
     * @param startDate
     * @param employerId
     * @param jobId
     * @param lat
     * @param lng
     */
    // Method to set all fields at once
    public void setAllField(String jobTitle, String companyName, String jobType,
                            String requirements, int salary, String urgency,
                            String location, String expectedDuration, String startDate,
                            String employerId, String jobId, Double lat, Double lng, String status) {
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
        this.status = status;
    }
}