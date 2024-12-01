package com.example.quickcash.model;

public class PaymentEmployeeModel {
    private String jobId;
    private String jobTitle;
    private String employeeName;
    private String employeeId;
    private String employerId;
    private int paymentAmount;

    public PaymentEmployeeModel(String jobId, String jobTitle, String employeeName, String employeeId, String employerId, int paymentAmount){
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.employeeName = employeeName;
        this.employeeId = employeeId;
        this.employerId = employerId;
        this.paymentAmount = paymentAmount;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
