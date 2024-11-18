package com.example.quickcash;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.example.quickcash.Firebase.OnlinePaymentCRUD;
import com.example.quickcash.model.PaymentTransactionModel;
import com.example.quickcash.paypal.PayPalPaymentProcessor;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OnlinePaymentUnitTest{


    private PayPalPaymentProcessor paymentProcessor;
    @Mock
    private PaymentConfirmation mockPaymentConfirmation;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentProcessor = new PayPalPaymentProcessor();
    }

    @Test
    public void testSuccessfulPayment() throws JSONException {
        JSONObject successResponse = new JSONObject();
        successResponse.put("id", "PAYID-SUCCESS123");
        successResponse.put("state", "approved");

        JSONObject response = new JSONObject();
        response.put("response", successResponse);

        when(mockPaymentConfirmation.toJSONObject()).thenReturn(response);

        paymentProcessor.handlePaymentConfirmation(mockPaymentConfirmation);

        assertEquals("Payment approved\n with payment id is PAYID-SUCCESS123", paymentProcessor.paymentStatusTV.getText().toString());
    }

    @Test
    public void testFailedPayment() throws JSONException {
        JSONObject failedResponse = new JSONObject();
        failedResponse.put("id", "PAYID-FAILED456");
        failedResponse.put("state", "failed");

        JSONObject response = new JSONObject();
        response.put("response", failedResponse);

        when(mockPaymentConfirmation.toJSONObject()).thenReturn(response);

        paymentProcessor.handlePaymentConfirmation(mockPaymentConfirmation);

        assertEquals("Payment failed\n with payment id is PAYID-FAILED456", paymentProcessor.paymentStatusTV.getText().toString());
    }

    @Test
    public void testPendingPayment() throws JSONException {
        JSONObject pendingResponse = new JSONObject();
        pendingResponse.put("id", "PAYID-PENDING789");
        pendingResponse.put("state", "pending");

        JSONObject response = new JSONObject();
        response.put("response", pendingResponse);

        when(mockPaymentConfirmation.toJSONObject()).thenReturn(response);

        paymentProcessor.handlePaymentConfirmation(mockPaymentConfirmation);

        assertEquals("Payment pending\n with payment id is PAYID-PENDING789", paymentProcessor.paymentStatusTV.getText().toString());
    }

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
