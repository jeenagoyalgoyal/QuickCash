package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EmployerHomepageActivity extends AppCompatActivity {

    private String currentRole = "employer";
    private UseRole useRole;
    private int id;


    public TextView welcomeEmployer;
    public Button createJob;
    public Button employeeDirectory;
    public Button analyticsReports;
    public Button tasksAssignments;
    public Button scheduleMeetings;
    public Button notificationSettings;
    public Button employeeSwitch;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.employer_dashboard);

        Intent intent = getIntent();
        id = intent.getIntExtra("userID", -1);

        useRole = UseRole.getInstance();

        welcomeEmployer = findViewById(R.id.welcomeEmployer);
        // Role-specific buttons
        createJob = findViewById(R.id.createJobButton);
        employeeDirectory = findViewById(R.id.employeeDirectoryButton);
        analyticsReports = findViewById(R.id.analyticsReportButton);
        tasksAssignments = findViewById(R.id.tasksAssignmentsButton);
        scheduleMeetings = findViewById(R.id.scheduleMeetingsButton);
        notificationSettings = findViewById(R.id.employerNotificationsButton);
        employeeSwitch = findViewById(R.id.switchToEmployeeButton);


        // SWITCHES TO EMPLOYEE DASH
        employeeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useRole.switchRole(id);
                Intent intent = new Intent(EmployerHomepageActivity.this, EmployeeHomepageActivity.class);
                startActivity(intent);
            }
        });


        createJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (useRole.getCurrentRole().equals("employer")) {
                    Intent intent = new Intent(EmployerHomepageActivity.this, JobSubmission.class);
                    startActivity(intent);
                }
            }
        });
    }
}
