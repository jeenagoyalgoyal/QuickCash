package com.example.quickcash.model;

public class PaymentTransactionModel {
    private String transactionId;
    private String employeeId;
    private String employerId;
    private String jobId;
    private float paymentAmount;
    private String transactionStatus;

    public void setTransactionID(String transactionID) {this.transactionId = transactionID;}

    public String getTransactionID(){return transactionId;}

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
