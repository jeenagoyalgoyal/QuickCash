package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PreferredEmployersActivity extends AppCompatActivity {
    private String email;
    private String userID;

    private ListView listView;

    private ArrayList<String> preferredEmployersNameList;
    private ArrayList<String> preferredEmployersIdList;
    private ArrayAdapter<String> adapter;

    private FirebaseDatabase database;
    private DatabaseReference preferredEmployersRef;

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

        //ID and email are gotten from intent
        Intent intentPreferredEmployers = getIntent();
        email = intentPreferredEmployers.getStringExtra("email");

        //listview stuff
        listView = findViewById(R.id.preferredEmployeesListView);
        preferredEmployersNameList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, preferredEmployersNameList);
        listView.setAdapter(adapter);

        //setting of ID and database code only executes if email is retrieved correctly
        if (email!=null && !email.isEmpty()){
            this.userID = email.replace(".", ",");;
            this.database = FirebaseDatabase.getInstance("https://quickcash-8f278-default-rtdb.firebaseio.com/");
            this.initializeDatabaseRefs();
            this.setPreferredEmployersListView();
        }
        else {
            Log.e("PreferredEmployers", "no ID initialized! (is the intent working correctly?)");
        }
        adapter.clear();
    }

    protected void initializeDatabaseRefs() {
        this.preferredEmployersRef = getPreferredEmployersRef();;
    }

    private DatabaseReference getPreferredEmployersRef() {
            return this.database.getReference("Users").child(userID).child("preferredEmployers");
    }

    protected void setPreferredEmployersListView() {
        //add listener to preferredEmployers
        this.preferredEmployersRef.addValueEventListener(new ValueEventListener() {
            PreferredEmployers preferredEmployers = new PreferredEmployers();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //adding preferred employers to preferred employers object
                if (snapshot.hasChildren()){
                    for (DataSnapshot employerDetails : snapshot.getChildren()){
                        String id = employerDetails.child("id").getValue(String.class);
                        String name =  employerDetails.child("name").getValue(String.class);
                        preferredEmployers.addDetails(id, name);
                    }
                }
                else {
                    Toast.makeText(PreferredEmployersActivity.this, "You do not have any preferred employees saved!", Toast.LENGTH_LONG).show();
                }
                //retrieving the list and displaying
                preferredEmployersNameList = preferredEmployers.getNameList();
                //set data in listview
                adapter.addAll(preferredEmployersNameList);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PreferredEmployersActivity.this, "Connection Error!", Toast.LENGTH_LONG).show();
                Log.e("PreferredEmployers", "Error connection to firebase!");
            }
        });
    }
}