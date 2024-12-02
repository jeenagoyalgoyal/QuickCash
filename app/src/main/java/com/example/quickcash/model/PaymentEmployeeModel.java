package com.example.quickcash.model;

/**
 * Represents an employee associated with a job and their payment details.
 * This model is used to encapsulate information about the employee, the job they are assigned to,
 * and the payment amount due for their work.
 */
public class PaymentEmployeeModel {
    private String jobId;
    private String jobTitle;
    private String employeeName;
    private String employeeId;
    private String employerId;
    private int paymentAmount;

    /**
     * Constructs a new PaymentEmployeeModel with the specified details.
     *
     * @param jobId         the unique identifier of the job used in Firebase
     * @param jobTitle      the title of the job
     * @param employeeName  the name of the employee assigned to the job
     * @param employeeId    the Id of the employee
     * @param employerId    the Id of the employer
     * @param paymentAmount the payment amount (CAD) due for the job completion
     */
    public PaymentEmployeeModel(String jobId, String jobTitle, String employeeName, String employeeId, String employerId, int paymentAmount){
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.employeeName = employeeName;
        this.employeeId = employeeId;
        this.employerId = employerId;
        this.paymentAmount = paymentAmount;
    }

    /**
     * Gets the unique identifier of the job as used in Firebase.
     *
     * @return the job ID
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Sets the unique identifier of the job.
     *
     * @param jobId the job ID to set
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * Gets the title the job.
     *
     * @return the job title
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets the title of the job.
     *
     * @param jobTitle the job title to set
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Gets the name of the employee assigned to the job.
     *
     * @return the employee name
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Sets the name of the employee assigned to the job.
     *
     * @param employeeName the employee name to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * Gets the unique identifier of the employee.
     *
     * @return the employee ID
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the unique identifier of the employee.
     *
     * @param employeeId the employee ID to set
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Gets the unique identifier of the employer.
     *
     * @return the employer ID
     */
    public String getEmployerId() {
        return employerId;
    }

    /**
     * Sets the unique identifier of the employer.
     *
     * @param employerId the employer ID to set
     */
    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    /**
     * Gets the payment amount (in CAD) for the job.
     *
     * @return the payment amount
     */
    public int getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Sets the payment amount (in CAD) for the job.
     *
     * @param paymentAmount the payment amount to set
     */
    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
