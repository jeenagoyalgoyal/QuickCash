package com.example.quickcash.ui.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.R;
import com.example.quickcash.repositories.PreferredEmployers;
import com.example.quickcash.utils.adapter.JobSearchAdapter;
import com.example.quickcash.models.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PreferredEmployersActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String email;
    private String userID;
    private ListView listView;
    private ArrayList<String> preferredEmployersNameList;
    private ArrayList<String> preferredEmployersIdList;
    private ArrayAdapter<String> adapter;
    private FirebaseDatabase database;
    private DatabaseReference preferredEmployersRef;
    private DatabaseReference jobsRef;
    private Dialog dialog;
    private ImageButton crossButton;
    private RecyclerView recyclerView;
    private List<Job> jobList;
    private JobSearchAdapter jobSearchAdapter;

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

        //ID is retrieved
        this.mAuth = FirebaseAuth.getInstance();
        this.email = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;

        setupListView();
        setupPopupDialog();
        setupEmployerSelector();
        setupCrossButton();
        setupRecyclerView();
        //setting of ID and database code only executes if email is retrieved correctly
        WhenEmailRetrievedCorrectly();

    }

    private void WhenEmailRetrievedCorrectly(){
        if (email!=null && !email.isEmpty()){
            this.userID = sanitizeEmail(email);
            this.initializeDatabaseRefs();
            this.setPreferredEmployersListView();
        }
        else {
            Log.e("PreferredEmployers", "no ID initialized! (is the intent working correctly?)");
        }
    }

    protected void initializeDatabaseRefs() {
        this.database = FirebaseDatabase.getInstance("https://quickcash-8f278-default-rtdb.firebaseio.com/");
        this.preferredEmployersRef = getPreferredEmployersRef();;
        this.jobsRef = getJobsRef();
    }


    private void setupEmployerSelector(){
        listView.setOnItemClickListener((parent, view, position, id) -> {
            displayJobsByEmployer(preferredEmployersIdList.get(position));
        });
    }

    private void setupListView(){
        listView = findViewById(R.id.preferredEmployeesListView);
        preferredEmployersNameList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, preferredEmployersNameList);
        listView.setAdapter(adapter);
    }

    private void setupPopupDialog(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.preferred_employer_jobs_dialog_box);
        crossButton = dialog.findViewById(R.id.crossButton);
    }

    private void setupRecyclerView(){
        recyclerView = dialog.findViewById(R.id.preferredEmployerJobsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        jobSearchAdapter = new JobSearchAdapter(jobList);
        recyclerView.setAdapter(jobSearchAdapter);
    }

    private void displayJobsByEmployer(String employerId) {
        jobList.clear();
        Query query = jobsRef;
        query = query.orderByChild("employerId").equalTo(employerId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot jobSnapshot : snapshot.getChildren()){
                    Job job = jobSnapshot.getValue(Job.class);
                    jobList.add(job);
                }
                if (jobList.isEmpty()){
                    Toast.makeText(PreferredEmployersActivity.this, "This employer has no jobs posted!", Toast.LENGTH_LONG).show();
                }
                jobSearchAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PreferredEmployersActivity.this, "Database Connection Error!", Toast.LENGTH_LONG).show();
                Log.e("PreferredEmployers", "Error connecting to firebase!");
            }
        });
        dialog.show();
    }


    private void setupCrossButton(){
        crossButton.setOnClickListener(v -> {
            dialog.cancel();
        });
    }



    private DatabaseReference getPreferredEmployersRef() {
            return this.database.getReference("Users").child(userID).child("preferredEmployers");
    }

    private DatabaseReference getJobsRef() {
        return this.database.getReference("Jobs");
    }


    protected void setPreferredEmployersListView() {
        adapter.clear();
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
                //retrieving the name and id lists
                preferredEmployersNameList = preferredEmployers.getNameList();
                preferredEmployersIdList = preferredEmployers.getIdList();
                //set data in listview
                adapter.addAll(preferredEmployersNameList);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PreferredEmployersActivity.this, "Database Connection Error!", Toast.LENGTH_LONG).show();
                Log.e("PreferredEmployers", "Error connecting to firebase!");
            }
        });
    }

    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }
}