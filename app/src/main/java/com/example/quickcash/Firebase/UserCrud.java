package com.example.quickcash.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.Task;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserCrud {

    public FirebaseDatabase firebaseDatabase;
    private final DatabaseReference databaseReference;

    public UserCrud(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
    }

    public Task<String> getUserDeviceToken(String emailId) {
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();

        // Replace dots in the email with underscores for Firebase compatibility
        String sanitizedEmail = emailId.replace(".", ",");

        databaseReference.child(sanitizedEmail).child("deviceToken").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String deviceToken = snapshot.getValue(String.class);
                    taskCompletionSource.setResult(deviceToken);
                } else {
                    taskCompletionSource.setException(new Exception("Device token not found for user: " + emailId));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    public void setUserDeviceToken(String email, String deviceToken) {
        // Replace dots in the email with underscores for Firebase compatibility
        String sanitizedEmail = email.replace(".", ",");

        // Set the deviceToken for the user
        databaseReference.child(sanitizedEmail).child("deviceToken").setValue(deviceToken)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Successfully saved the device token
                        Log.d("UserCrud", "Device token successfully set for user: " + email);
                    } else {
                        // Failed to save the device token
                        Log.e("UserCrud", "Failed to set device token for user: " + email, task.getException());
                    }
                });
    }

}
