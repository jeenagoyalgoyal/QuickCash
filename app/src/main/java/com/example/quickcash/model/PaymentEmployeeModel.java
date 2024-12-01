package com.example.quickcash.model;

public class PaymentEmployeeModel {
    private String jobTitle;
    private String employeeName;
    private int paymentAmount;

    public PaymentEmployeeModel(String jobTitle, String employeeName, int paymentAmount){
        this.jobTitle = jobTitle;
        this.employeeName = employeeName;
        this.paymentAmount = paymentAmount;
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

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
