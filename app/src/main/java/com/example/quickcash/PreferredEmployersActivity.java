package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PreferredEmployersActivity extends AppCompatActivity {
    private String email;
    private List<String> preferredEmployersList = new ArrayList<>();
    private String userID;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private String tempEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.preferred_employers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.preferredEmployeesLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Intent intentPreferredEmployers = getIntent();
        //email = intentPreferredEmployers.getStringExtra("email");
        email = "testingemail@test.db";

        this.userID = email.replace(".", ",");;
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
                Toast.makeText(PreferredEmployersActivity.this, "ToatTest"+snapshot.child(String.valueOf(1)).getValue(String.class), Toast.LENGTH_LONG).show();
                Log.e("FirebasePreferredEmployers", "employee:"+snapshot.child(String.valueOf(1)).getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Log.e("FirebasePreferredEmployers", "checking if employee returns"+tempEmployee);
    }

    public List<String> getPreferredEmployersList() {
        return this.preferredEmployersList;
    }

    private void addPreferredEmployersList(String fetchedEmployer){
        String fetchedEmployerTemp = fetchedEmployer+" ";
        Log.e("FirebasePreferredEmployers", "checking temp employer"+fetchedEmployerTemp);
        preferredEmployersList.add(fetchedEmployerTemp);
        Log.e("FirebasePreferredEmployers", "checking temp employer in array"+preferredEmployersList.get(0));
    }
}
