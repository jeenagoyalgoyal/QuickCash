package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.quickcash.Firebase.OnlinePaymentCRUD;
import com.example.quickcash.model.PaymentTransactionModel;

import org.junit.Test;

public class OnlinePaymentTest {

    @Test
    public void testFirebaseTransactionPushPull() {
        //setup
        final String JOB_ID = "test_job_id";
        boolean status;
        OnlinePaymentCRUD onlinePaymentCRUD = new OnlinePaymentCRUD();
        PaymentTransactionModel transaction = new PaymentTransactionModel();
        PaymentTransactionModel retrievedTransaction = new PaymentTransactionModel();

        transaction.setEmployeeId("testingemail@test,db");
        transaction.setEmployerId("test2@gmail,com");
        transaction.setJobId("test_job_id");
        transaction.setPaymentAmount(400);
        transaction.setTransactionStatus("successful");
        status = onlinePaymentCRUD.pushTransaction(transaction);
        assertTrue("Pushing of payment pushTransaction record to firebase should be successful",status);
        retrievedTransaction = onlinePaymentCRUD.getTransactionByJobId(JOB_ID);
        assertEquals("Retrieved transaction record should match the one pushed",JOB_ID,retrievedTransaction.getJobId());

        //teardown
        status = false;
        status = onlinePaymentCRUD.deleteTransactionByJobId(JOB_ID);
        assertTrue("Deletion of the record after test completion must take place to prevent duplication",status);
    }
}
