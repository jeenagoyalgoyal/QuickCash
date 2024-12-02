package com.example.quickcash;

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

import com.example.quickcash.adapter.JobSearchAdapter;
import com.example.quickcash.model.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity class for managing and displaying preferred employers in the QuickCash application.
 * It interacts with Firebase to retrieve and display the list of preferred employers and
 * their posted jobs by provides UI elements for interacting with the data.
 */
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
    private static final String PREFERRED_EMPLOYER_TEXT = "PreferredEmployers";

    /**
     * Called when the activity is first created.
     * Initializes the UI elements, gets email of current user from FirebaseAuth, and calls methods that fetch preferred employers.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state, if any.
     */
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
        whenEmailRetrievedCorrectly();

    }

    /**
     * Sets up the activity when the email has been retrieved correctly.
     * Initializes database references and calls methods to fetch preferred employees
     * and display them in a listview.
     */
    private void whenEmailRetrievedCorrectly() {
        if (email != null && !email.isEmpty()) {
            this.userID = sanitizeEmail(email);
            this.initializeDatabaseRefs();
            this.setPreferredEmployersListView();
        } else {
            Log.e(PREFERRED_EMPLOYER_TEXT, "no ID initialized! (is the intent working correctly?)");
        }
    }

    /**
     * Initializes the Firebase database references.
     */
    protected void initializeDatabaseRefs() {
        this.database = FirebaseDatabase.getInstance("https://quickcash-8f278-default-rtdb.firebaseio.com/");
        this.preferredEmployersRef = getPreferredEmployersRef();
        this.jobsRef = getJobsRef();
    }

    /**
     * Calls the popup that displays jobs when an employer is clicked.
     */
    private void setupEmployerSelector() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            displayJobsByEmployer(preferredEmployersIdList.get(position));
        });
    }

    /**
     * Sets up the ListView for displaying preferred employers.
     */
    private void setupListView() {
        listView = findViewById(R.id.preferredEmployeesListView);
        preferredEmployersNameList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, preferredEmployersNameList);
        listView.setAdapter(adapter);
    }

    /**
     * Sets up the popup dialog for displaying jobs.
     */
    private void setupPopupDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.preferred_employer_jobs_dialog_box);
        crossButton = dialog.findViewById(R.id.crossButton);
    }

    /**
     * Sets up the RecyclerView for displaying jobs.
     */
    private void setupRecyclerView() {
        recyclerView = dialog.findViewById(R.id.preferredEmployerJobsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext())); // Use dialog context
        jobList = new ArrayList<>();

        jobSearchAdapter = new JobSearchAdapter(dialog.getContext(), jobList);

        recyclerView.setAdapter(jobSearchAdapter);
    }


    /**
     * Displays jobs posted by a specific employer in a dialog.
     *
     * @param employerId The ID of the employer whose jobs are to be displayed.
     */
    private void displayJobsByEmployer(String employerId) {
        jobList.clear();
        Query query = jobsRef;
        query = query.orderByChild("employerId").equalTo(employerId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);
                    jobList.add(job);
                }
                if (jobList.isEmpty()) {
                    Toast.makeText(PreferredEmployersActivity.this, "This employer has no jobs posted!", Toast.LENGTH_LONG).show();
                }
                jobSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PreferredEmployersActivity.this, "Database Connection Error!", Toast.LENGTH_LONG).show();
                Log.e(PREFERRED_EMPLOYER_TEXT, "Error connecting to firebase!");
            }
        });
        dialog.show();
    }

    /**
     * Sets up the cross button for closing the job display dialog.
     */
    private void setupCrossButton() {
        crossButton.setOnClickListener(v -> {
            dialog.cancel();
        });
    }

    /**
     * Retrieves the database reference for preferred employers.
     *
     * @return A DatabaseReference pointing to the user's preferred employers.
     */
    private DatabaseReference getPreferredEmployersRef() {
        return this.database.getReference("Users").child(userID).child("preferredEmployers");
    }

    /**
     * Retrieves the database reference for jobs.
     *
     * @return A DatabaseReference pointing to the jobs database.
     */
    private DatabaseReference getJobsRef() {
        return this.database.getReference("Jobs");
    }


    /**
     * Sets the data in the ListView for preferred employers.
     */
    protected void setPreferredEmployersListView() {
        adapter.clear();
        //add listener to preferredEmployers
        this.preferredEmployersRef.addValueEventListener(new ValueEventListener() {
            PreferredEmployers preferredEmployers = new PreferredEmployers();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //adding preferred employers to preferred employers object
                if (snapshot.hasChildren()) {
                    for (DataSnapshot employerDetails : snapshot.getChildren()) {
                        String id = employerDetails.child("id").getValue(String.class);
                        String name = employerDetails.child("name").getValue(String.class);
                        preferredEmployers.addDetails(id, name);
                    }
                } else {
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
                Log.e(PREFERRED_EMPLOYER_TEXT, "Error connecting to firebase!");
            }
        });
    }

    /**
     * Sanitizes an email address to be used as a Firebase id key.
     * Replaces '.' with ',' to avoid issues with Firebase paths.
     *
     * @param email The email address to be sanitized.
     * @return A sanitized version of the email address that can be used as an id.
     */
    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }
}