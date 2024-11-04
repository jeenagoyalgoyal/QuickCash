package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.R;
import com.example.quickcash.model.UseRole;

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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_switch);

        Intent intent = getIntent();
        id = intent.getIntExtra("userID", -1);

        useRole = UseRole.getInstance();
        welcomeText = findViewById(R.id.welcomeText);
        roleSwitch = findViewById(R.id.roleSwitch);

        //Role specific buttons
        jobPosting = findViewById(R.id.jobPosting);
        profileButton = findViewById(R.id.profileButton);
        scheduleButton = findViewById(R.id.scheduleButton);
        taskButton = findViewById(R.id.taskButton);
        performanceButton = findViewById(R.id.performanceButton);
        notificationsButton = findViewById(R.id.notificationsButton);

        // Force employee role for testing
        currentRole = "employee";
        useRole = UseRole.getInstance();
        useRole.setCurrentRole(id, "employee");

        update();

        // Debug toast to show current role
        Toast.makeText(this, "Current role: " + useRole.getCurrentRole(), Toast.LENGTH_SHORT).show();

        roleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useRole.switchRole(id);
                update();
                // Debug toast after role switch
                Toast.makeText(RoleActivity.this,
                        "Switched to: " + useRole.getCurrentRole(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Add click listener for jobPosting button
        jobPosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Debug toast before starting SearchResultsActivity
                Toast.makeText(RoleActivity.this,
                        "Starting job search...",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RoleActivity.this, JobSearchParameterActivity.class);
                intent.putExtra("search_query", ""); // Empty query to show all jobs
                startActivity(intent);
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
            jobPosting.setText("Manage Job Posting");
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