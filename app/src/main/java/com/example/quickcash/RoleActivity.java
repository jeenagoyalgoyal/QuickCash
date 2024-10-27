package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoleActivity extends AppCompatActivity {

    public TextView welcomeText;
    private String currentRole = "employee";
    private Button roleSwitch;
    private UseRole useRole;
    private int id;

    //Buttons for role specific features
    public Button jobPosting;
    public Button profileButton;
    public Button scheduleButton;
    public Button taskButton;
    public Button performanceButton;
    public Button notificationsButton;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.role_switch);

        Intent intent = getIntent();
        id = intent.getIntExtra("userID", -1);

        useRole = UseRole.getInstance();
        welcomeText = findViewById(R.id.welcomeText);
        roleSwitch = findViewById(R.id.roleSwitch);

        // Role-specific buttons
        jobPosting = findViewById(R.id.jobPosting);
        profileButton = findViewById(R.id.profileButton);
        scheduleButton = findViewById(R.id.scheduleButton);
        taskButton = findViewById(R.id.taskButton);
        performanceButton = findViewById(R.id.performanceButton);
        notificationsButton = findViewById(R.id.notificationsButton);

        // Fetch the current user role and update the UI
        fetchAndSetUserRole();

        roleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useRole.switchRole(id);
                update();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoleActivity.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });

        jobPosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (useRole.getCurrentRole().equals("employer")) {
                    Intent intent = new Intent(RoleActivity.this, JobSubmission.class);
                    startActivity(intent);
                }
            }
        });
    }

    // New method to fetch the correct user role
    private void fetchAndSetUserRole() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        useRole.fetchUserRole(email, new UseRole.OnRoleFetchedListener() {
            @Override
            public void onRoleFetched(String role) {
                if (role != null) {
                    useRole.setCurrentRole(id, role);
                    update();
                } else {
                    welcomeText.setText("Error fetching role.");
                }
            }
        });
    }


    public void update(){
        String role = useRole.getCurrentRole();
        if(role.equals("employee")){
            welcomeText.setText("Welcome, employee");
            roleSwitch.setText("Switch to employer");

            // Update buttons for employee
            jobPosting.setText("Search Job Posting");
            profileButton.setText("My Profile");
            scheduleButton.setText("Work Schedule");
            taskButton.setText("Tasks & Projects");
            performanceButton.setText("Performance Reviews");
            notificationsButton.setText("Notifications & Settings");

            //showing the buttons visibility
            jobPosting.setVisibility(View.VISIBLE);
            profileButton.setVisibility(View.VISIBLE);
            scheduleButton.setVisibility(View.VISIBLE);
            taskButton.setVisibility(View.VISIBLE);
            performanceButton.setVisibility(View.VISIBLE);
            notificationsButton.setVisibility(View.VISIBLE);
        }
        else{
            welcomeText.setText("Welcome, employer");
            roleSwitch.setText("Switch to employee");

            // Update buttons for employer
            jobPosting.setText("Create Job");
            profileButton.setText("Employee Directory");
            scheduleButton.setText("Analytics & Reports");
            taskButton.setText("Tasks & Assignments");
            performanceButton.setText("Schedule & Meetings");
            notificationsButton.setText("Notifications & Settings");

            //showing the buttons visibility
            jobPosting.setVisibility(View.VISIBLE);
            profileButton.setVisibility(View.VISIBLE);
            scheduleButton.setVisibility(View.VISIBLE);
            taskButton.setVisibility(View.VISIBLE);
            performanceButton.setVisibility(View.VISIBLE);
            notificationsButton.setVisibility(View.VISIBLE);
        }
    }
}

