package com.example.quickcash.model;

/**
 * Represents a payment transaction made between an employer and an employee.
 * This model is used to store the details of the transaction, including the
 * transaction ID, employee and employer details, job information, payment amount,
 * and the status of the transaction resulting from PayPal payment.
 */
public class PaymentTransactionModel {
    private String transactionId;
    private String employeeId;
    private String employerId;
    private String jobId;
    private float paymentAmount;
    private String transactionStatus;

    /**
     * Sets the details of the transaction.
     *
     * @param transactionId   the unique identifier for the transaction
     * @param employeeId      the unique identifier of the employee receiving the payment
     * @param employerId      the unique identifier of the employer making the payment
     * @param jobId           the unique identifier of the job for which the payment is being made
     * @param paymentAmount   the amount of the payment made
     * @param transactionStatus the status of the transaction (e.g., "In-progress", "Pending")
     */
    public void setTransactionDetails(String transactionId, String employeeId, String employerId, String jobId, float paymentAmount, String transactionStatus){
        this.transactionId = transactionId;
        this.employeeId = employeeId;
        this.employerId = employerId;
        this.jobId = jobId;
        this.paymentAmount = paymentAmount;
        this.transactionStatus = transactionStatus;
    }

    /**
     * Sets the unique identifier for the transaction.
     *
     * @param transactionID the transaction ID to set
     */
    public void setTransactionID(String transactionID) {this.transactionId = transactionID;}

    /**
     * Gets the unique identifier for the transaction.
     *
     * @return the transaction ID
     */
    public String getTransactionID(){return transactionId;}

    /**
     * Gets the unique identifier of the employee receiving the payment.
     *
     * @return the employee ID
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the unique identifier of the employee receiving the payment.
     *
     * @param employeeId the employee ID to set
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Gets the unique identifier of the employer making the payment.
     *
     * @return the employer ID
     */
    public String getEmployerId() {
        return employerId;
    }

    /**
     * Sets the unique identifier of the employer making the payment.
     *
     * @param employerId the employer ID to set
     */
    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    /**
     * Gets the unique identifier of the job for which the payment is made.
     *
     * @return the job ID
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Sets the unique identifier of the job for which the payment is made.
     *
     * @param jobId the job ID to set
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * Gets the amount of the payment made in the transaction.
     *
     * @return the payment amount
     */
    public float getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Sets the amount of the payment made in the transaction.
     *
     * @param paymentAmount the payment amount to set
     */
    public void setPaymentAmount(float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * Gets the status of the transaction (e.g., "Completed", "Pending").
     *
     * @return the transaction status
     */
    public String getTransactionStatus() {
        return transactionStatus;
    }

    /**
     * Sets the status of the transaction (e.g., "In-progress", "Pending").
     *
     * @param transactionStatus the transaction status to set
     */
    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
