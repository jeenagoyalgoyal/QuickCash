package com.example.quickcash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.quickcash.Firebase.OnlinePaymentCRUD;
import com.example.quickcash.model.PaymentTransactionModel;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class OnlinePaymentTest {

        private OnlinePaymentCRUD onlinePaymentCRUD;

        @Before
        public void setUp() {
                onlinePaymentCRUD = new OnlinePaymentCRUD();
        }

        @Test
        public void testFirebaseTransactionPushPull() throws ExecutionException, InterruptedException {
                final String JOB_ID = "test_job_id";
                PaymentTransactionModel transaction = new PaymentTransactionModel();
                transaction.setTransactionID(JOB_ID);
                transaction.setEmployeeId("testingemail@test,db");
                transaction.setEmployerId("test2@gmail,com");
                transaction.setJobId(JOB_ID);
                transaction.setPaymentAmount(400);
                transaction.setTransactionStatus("successful");

                // Act: Push transaction to Firebase
                Task<Boolean> pushTask = onlinePaymentCRUD.pushTransaction(transaction);
                Boolean pushResult = Tasks.await(pushTask);

                // Assert: Ensure the transaction was successfully pushed
                assertTrue("Pushing of payment transaction to Firebase should be successful", pushResult);

                // Act: Retrieve the transaction by job ID
                Task<PaymentTransactionModel> getTask = onlinePaymentCRUD.getTransactionByJobId(JOB_ID);
                PaymentTransactionModel retrievedTransaction = Tasks.await(getTask);

                // Assert: Verify the retrieved transaction matches the original
                assertEquals("Retrieved transaction record should match the one pushed", JOB_ID, retrievedTransaction.getJobId());
                assertEquals(transaction.getEmployeeId(), retrievedTransaction.getEmployeeId());
                assertEquals(transaction.getEmployerId(), retrievedTransaction.getEmployerId());
                assertEquals(transaction.getPaymentAmount(), retrievedTransaction.getPaymentAmount(), 0.001);
                assertEquals(transaction.getTransactionStatus(), retrievedTransaction.getTransactionStatus());

                // Act: Delete the transaction by job ID
                Task<Boolean> deleteTask = onlinePaymentCRUD.deleteTransactionByJobId(JOB_ID);
                Boolean deleteResult = Tasks.await(deleteTask);

                // Assert: Ensure the transaction was successfully deleted
                assertTrue("Deletion of the record after test completion must take place to prevent duplication", deleteResult);

                // Verify: Ensure the record no longer exists in the database
                Task<PaymentTransactionModel> verifyDeleteTask = onlinePaymentCRUD.getTransactionByJobId(JOB_ID);
                PaymentTransactionModel deletedTransaction = Tasks.await(verifyDeleteTask);
                assertEquals("Transaction should no longer exist after deletion", null, deletedTransaction);
        }
}
