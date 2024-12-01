package com.example.quickcash.model;

public class Application {
    private String employeeName;
    private String employeeID;
    private String applicantName;
    private String applicantEmail;
    private String applicantMessage;
    private String status;
    private String applicationId;

    public Application() {
        // Default constructor required for Firebase
    }

    public Application(String applicantName, String applicantEmail, String applicantMessage, String status, String applicationId, String employeeID) {
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.applicantMessage = applicantMessage;
        this.status = status;
        this.applicationId = applicationId;
        this.employeeID = employeeID;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public String getApplicantMessage() {
        return applicantMessage;
    }

    public void setApplicantMessage(String applicantMessage) {
        this.applicantMessage = applicantMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public void setEmployeeID(String employeeID){this.employeeID = employeeID;}
    public String getEmployeeID(){return this.employeeID;}
}
