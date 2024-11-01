package com.example.quickcash;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebasePreferredEmployers {

    private String userID;
    private final FirebaseDatabase database;
    private DatabaseReference usersRef;
    private List<String> preferredEmployersList = new ArrayList<>();

    public FirebasePreferredEmployers(String userID) {
        this.userID = userID;
        this.database = FirebaseDatabase.getInstance("https://quickcash-8f278-default-rtdb.firebaseio.com/");
        this.initializeDatabaseRefs();
    }

    protected void initializeDatabaseRefs() {
        this.usersRef = getUsersRef();;
    }

    private DatabaseReference getUsersRef() {
        return this.database.getReference("Users");
    }

    public void getPreferredEmployers(){
        usersRef.child(userID).child("preferredEmployers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int employerIndex = 1;
                    //get first employee
                    String fetchedEmployer = dataSnapshot.child(String.valueOf(employerIndex)).getValue(String.class);
                    if (fetchedEmployer != null) {
                        //loop till no more employees
                        while (fetchedEmployer!=null){
                            //add to arraylist of employers
                            preferredEmployersList.add(fetchedEmployer);
                            //get next value
                            employerIndex++;
                            fetchedEmployer = dataSnapshot.child(String.valueOf(employerIndex)).getValue(String.class);
                        }
                    }
                    else {
                        Log.e("FirebasePreferredEmployers", "no preferred employers found");

                    }
                } else {
                    Log.e("FirebasePreferredEmployers", "no data found for the employer ID");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LoginActivity", "Database error: " + databaseError.getMessage());
            }
        });
    }
}
