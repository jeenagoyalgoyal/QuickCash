package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.R;
import com.example.quickcash.models.UseRole;

public class EmployeeHomepageActivity extends AppCompatActivity {

    private String currentRole = "employee";
    private UseRole useRole;
    private int id;


    public TextView welcomeEmployee;
    public Button searchJob;
    public Button myProfile;
    public Button workSchedule;
    public Button tasksProjects;
    public Button performanceReview;
    public Button employeeNotifications;
    public Button employerSwitch;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.employee_dashboard);

        Intent intentEmployeeDash = getIntent();
        id = intentEmployeeDash.getIntExtra("userID", -1);

        String email = intentEmployeeDash.getStringExtra("email");

        useRole = UseRole.getInstance();

        welcomeEmployee = findViewById(R.id.welcomeEmployee);
        // Role-specific buttons
        searchJob = findViewById(R.id.searchJobButton);
        myProfile = findViewById(R.id.myProfileButton);
        workSchedule = findViewById(R.id.workScheduleButton);
        tasksProjects = findViewById(R.id.tasksProjectsButton);
        performanceReview = findViewById(R.id.performanceReviewsButton);
        employeeNotifications = findViewById(R.id.employeeNotifications);
        employerSwitch = findViewById(R.id.switchToEmployerButton);


        // SWITCHES TO EMPLOYEE DASH
        employerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useRole.switchRole(id);
                Intent intentSwitchToEmployer = new Intent(EmployeeHomepageActivity.this, EmployerHomepageActivity.class);
                intentSwitchToEmployer.putExtra("email", email);
                startActivity(intentSwitchToEmployer);
            }
        });

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile = new Intent(EmployeeHomepageActivity.this, Profile.class);
                startActivity(intentProfile);
                finish();
            }
        });

        searchJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile = new Intent(EmployeeHomepageActivity.this, JobSearchParameterActivity.class);
                startActivity(intentProfile);
                finish();
            }
        });




    }
}
