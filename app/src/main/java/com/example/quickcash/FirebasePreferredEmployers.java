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
    private List<String> preferredEmployersList = new ArrayList<>();
    private String userID;
    private final FirebaseDatabase database;
    private DatabaseReference usersRef;
    private String tempEmployee;


    public FirebasePreferredEmployers(String userID) {
        this.userID = userID;
        this.database = FirebaseDatabase.getInstance("https://quickcash-8f278-default-rtdb.firebaseio.com/");
        this.initializeDatabaseRefs();
        this.setPreferredEmployersListener();
    }

    protected void initializeDatabaseRefs() {
        this.usersRef = getUsersRef();;
    }

    private DatabaseReference getUsersRef() {
        return this.database.getReference("Users");
    }


    protected void setPreferredEmployersListener() {
        this.usersRef.child(userID).child("preferredEmployers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempEmployee=snapshot.child(String.valueOf(1)).getValue(String.class);
                Log.e("FirebasePreferredEmployers", "employee:"+snapshot.child(String.valueOf(1)).getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Log.e("FirebasePreferredEmployers", "checking if employee returns"+tempEmployee);
    }

    public String getPreferredEmployersList() {
        return this.tempEmployee;
    }

    private void addPreferredEmployersList(String fetchedEmployer){
        preferredEmployersList.add(fetchedEmployer);
    }
}
