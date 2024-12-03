package com.example.quickcash.Firebase;

import androidx.annotation.NonNull;

import com.example.quickcash.model.Job;
import com.example.quickcash.model.PaymentTransactionModel;
import com.google.android.gms.tasks.Task;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnlinePaymentCRUD {

    public FirebaseDatabase firebaseDatabase;
    private final DatabaseReference databaseReference;

    public OnlinePaymentCRUD(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("PaymentTransaction");
    }

    public Task<Boolean> pushTransaction(PaymentTransactionModel transaction) {
        if(transaction.getTransactionID() != null){
            return databaseReference.child(transaction.getTransactionID()).setValue(transaction)
                    .continueWith(task -> task.isSuccessful());
        }
        return null;
    }

    public Task<PaymentTransactionModel> getTransactionByJobId(String jobId) {
        TaskCompletionSource<PaymentTransactionModel> taskCompletionSource = new TaskCompletionSource<>();

        databaseReference.child(jobId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PaymentTransactionModel  paymentTransactionModel = snapshot.getValue(PaymentTransactionModel.class);
                taskCompletionSource.setResult(paymentTransactionModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });
        return taskCompletionSource.getTask();
    }

    public Task<Boolean> deleteTransactionByJobId(String jobId) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        databaseReference.child(jobId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    databaseReference.child(jobId).removeValue()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    taskCompletionSource.setResult(true);
                                } else {
                                    taskCompletionSource.setResult(false);
                                }
                            });
                } else {
                    taskCompletionSource.setResult(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });

        return taskCompletionSource.getTask();
    }
}
