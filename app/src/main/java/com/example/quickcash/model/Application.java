package com.example.quickcash.model;

public class Application {
    private String applicantName;
    private String applicantEmail;
    private String message;

    public Application(String applicantName, String applicantEmail, String message) {
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.message = message;
    }

    // Getters and setters for applicant details
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
