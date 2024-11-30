package com.example.quickcash.model;

public class Application {
    private String applicantName;
    private String applicantEmail;
    private String applicantMessage;
    private String status;

    public Application() {
        // Default constructor required for Firebase
    }

    public Application(String applicantName, String applicantEmail, String applicantMessage, String status) {
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.applicantMessage = applicantMessage;
        this.status = status;
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
}
