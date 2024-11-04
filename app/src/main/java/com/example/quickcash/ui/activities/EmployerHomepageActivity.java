package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quickcash.R;
import com.example.quickcash.models.UseRole;

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

        Intent intentEmployerDash = getIntent();
        id = intentEmployerDash.getIntExtra("userID", -1);

        String email = intentEmployerDash.getStringExtra("email");

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
                Intent intentSwitchToEmployee = new Intent(EmployerHomepageActivity.this, EmployeeHomepageActivity.class);
                intentSwitchToEmployee.putExtra("email",email);
                startActivity(intentSwitchToEmployee);
            }
        });


        createJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (useRole.getCurrentRole().equals("employer")) {
                    Intent intentJobSub = new Intent(EmployerHomepageActivity.this, JobSubmissionActivity.class);
                    intentJobSub.putExtra("email", email); // Fixed
                    Toast.makeText(EmployerHomepageActivity.this, "Creating a Job!", Toast.LENGTH_SHORT).show();
                    startActivity(intentJobSub);
                }
            }
        });
    }
}
