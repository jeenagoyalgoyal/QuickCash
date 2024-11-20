package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The EmployerHomepageActivity class provides the user interface and functionality
 * for the employer's homepage in the QuickCash application. Employers can:
 * - Create job listings
 * - View employee directory
 * - Access analytics reports and tasks
 * - Schedule meetings and manage notifications
 * - Switch to the employee role
 * This activity manages the employer-specific navigation and role-based actions.
 */
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

    /**
     * Initializes the activity, sets up UI components, and handles employer-specific actions.
     *
     * @param savedInstance The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.employer_dashboard);
        // Retrieve user details from the intent
        Intent intentEmployerDash = getIntent();
        id = intentEmployerDash.getIntExtra("userID", -1);

        String email = intentEmployerDash.getStringExtra("email");
        Log.d("Email recieved at dashboard: ", email);

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
                    Log.d("Passing email to Job Sub: ", email);
                    Toast.makeText(EmployerHomepageActivity.this, "Creating a Job!", Toast.LENGTH_SHORT).show();
                    startActivity(intentJobSub);
                }
            }
        });
    }
}
