package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.model.UseRole;
import com.google.firebase.auth.FirebaseAuth;

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

    public static final String EMAIL = "email";
    private String currentRole = "employer";
    private UseRole useRole;
    private String email;
    FirebaseAuth mAuth;

    public TextView welcomeEmployer;
    public Button createJob;
    public Button payEmployee;
    public Button applicationsButton;
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

        //ID is retrieved
        this.mAuth = FirebaseAuth.getInstance();
        this.email = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
        this.email = this.email.replace(".", ",");

        useRole = UseRole.getInstance();
        useRole.setCurrentRole("Employer");

        welcomeEmployer = findViewById(R.id.welcomeEmployer);
        // Role-specific buttons
        createJob = findViewById(R.id.createJobButton);
        payEmployee = findViewById(R.id.payEmployeeButton);
        applicationsButton = findViewById(R.id.applicationSubmissions);
        employeeSwitch = findViewById(R.id.switchToEmployeeButton);


        // SWITCHES TO EMPLOYEE DASH
        employeeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useRole.switchRole(email);
                Intent intentSwitchToEmployee = new Intent(EmployerHomepageActivity.this, EmployeeHomepageActivity.class);
                intentSwitchToEmployee.putExtra(EMAIL, email);

                startActivity(intentSwitchToEmployee);
            }
        });


        createJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (useRole.getCurrentRole().equals("employer")) {
                    Intent intentJobSub = new Intent(EmployerHomepageActivity.this, JobSubmissionActivity.class);
                    intentJobSub.putExtra(EMAIL, email); // Fixed
                    Toast.makeText(EmployerHomepageActivity.this, "Creating a Job!", Toast.LENGTH_SHORT).show();
                    startActivity(intentJobSub);
                }
            }
        });


        applicationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (useRole.getCurrentRole().equals("employer")) {
                    Intent intentApplications = new Intent(EmployerHomepageActivity.this, EmployerJobsActivity.class);
                    intentApplications.putExtra(EMAIL, email); // Fixed
                    Toast.makeText(EmployerHomepageActivity.this, "Viewing applications!", Toast.LENGTH_SHORT).show();
                    startActivity(intentApplications);
                }
            }
        });

        payEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentApplications = new Intent(EmployerHomepageActivity.this, OnlinePaymentActivity.class);
                intentApplications.putExtra(EMAIL, email);
                startActivity(intentApplications);
            }
        });
    }
}
