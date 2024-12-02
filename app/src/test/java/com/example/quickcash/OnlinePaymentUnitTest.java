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


    private PaymentTransactionModel transaction;

    @Before
    public void setUp() {
        // Initialize a new PaymentTransactionModel instance before each test
        transaction = new PaymentTransactionModel();
    }

    @Test
    public void testSuccessfulPayment() {
        // Arrange: Mock a successful payment response
        String paymentId = "PAYID-SUCCESS123";
        String paymentState = "approved";

        // Act: Set the transaction details
        transaction.setTransactionDetails(
                paymentId,
                "employee@test.com",
                "employer@test.com",
                "test_job_id",
                500.0f,
                paymentState
        );

        // Assert: Verify the transaction details
        assertEquals("PAYID-SUCCESS123", transaction.getTransactionID());
        assertEquals("employee@test.com", transaction.getEmployeeId());
        assertEquals("employer@test.com", transaction.getEmployerId());
        assertEquals("test_job_id", transaction.getJobId());
        assertEquals(500.0f, transaction.getPaymentAmount(), 0.001);
        assertEquals("approved", transaction.getTransactionStatus());
    }

    @Test
    public void testFailedPayment() {
        // Arrange: Mock a failed payment response
        String paymentId = "PAYID-FAILED456";
        String paymentState = "failed";

        // Act: Set the transaction details
        transaction.setTransactionDetails(
                paymentId,
                "employee@test.com",
                "employer@test.com",
                "test_job_id",
                400.0f,
                paymentState
        );

        // Assert: Verify the transaction details
        assertEquals("PAYID-FAILED456", transaction.getTransactionID());
        assertEquals("employee@test.com", transaction.getEmployeeId());
        assertEquals("employer@test.com", transaction.getEmployerId());
        assertEquals("test_job_id", transaction.getJobId());
        assertEquals(400.0f, transaction.getPaymentAmount(), 0.001);
        assertEquals("failed", transaction.getTransactionStatus());
    }

    @Test
    public void testPendingPayment() {
        // Arrange: Mock a pending payment response
        String paymentId = "PAYID-PENDING789";
        String paymentState = "pending";

        // Act: Set the transaction details
        transaction.setTransactionDetails(
                paymentId,
                "employee@test.com",
                "employer@test.com",
                "test_job_id",
                300.0f,
                paymentState
        );

        // Assert: Verify the transaction details
        assertEquals("PAYID-PENDING789", transaction.getTransactionID());
        assertEquals("employee@test.com", transaction.getEmployeeId());
        assertEquals("employer@test.com", transaction.getEmployerId());
        assertEquals("test_job_id", transaction.getJobId());
        assertEquals(300.0f, transaction.getPaymentAmount(), 0.001);
        assertEquals("pending", transaction.getTransactionStatus());
    }
}
