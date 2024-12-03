package com.example.quickcash;

/**
 * ApplicationsSubmittedActivity
 *
 * This class represents an Android activity that displays a list of job applications for a specific job.
 * It retrieves the application data from a Firebase Realtime Database and displays it in a RecyclerView.
 * The data includes applicant name, email, cover letter (message), and application status.
 *
 * Key Features:
 * 1. **Firebase Integration**: Fetches applications from the Firebase Realtime Database.
 * 2. **RecyclerView Setup**: Uses RecyclerView to efficiently display the list of applications.
 * 3. **Data Handling**: Extracts and organizes application data for display.
 * 4. **Navigation**: Includes a back button to return to the previous activity.
 *
 * Main Components:
 * - **RecyclerView (applicationsRecyclerView)**: Displays the list of applications in a scrollable list.
 * - **ApplicationsAdapter (applicationsAdapter)**: Custom adapter to manage the data binding for each item in the RecyclerView.
 * - **List<Application> (applicationList)**: Stores the list of applications fetched from Firebase.
 * - **DatabaseReference (databaseReference)**: References the Firebase database node for job applications.
 * - **TextView (pageTitle)**: Displays the title of the page, which includes the job title.
 *
 * Key Methods:
 * - **onCreate()**: Initializes UI components, sets up the RecyclerView, and triggers data fetching from Firebase.
 * - **fetchApplicationsForJob(String jobId)**: Fetches job applications based on the jobId from the Firebase database and updates the RecyclerView.
 *
 * Usage Flow:
 * 1. The activity starts by receiving the `jobId` and `jobTitle` via Intent.
 * 2. Initializes UI components like the RecyclerView and sets up the data adapter.
 * 3. Fetches the list of applications for the specified job from Firebase.
 * 4. Displays the fetched data in the RecyclerView, updating the UI dynamically.
 * 5. Users can navigate back to the previous screen using the back button.
 *
 * Error Handling:
 * - If there is an issue fetching data from Firebase, a Toast message is displayed to inform the user.
 *
 * Logging:
 * - Uses `Log.d()` to log the details of the fetched applications, including the applicant's name, email, and message.
 */


import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.adapter.ApplicationsAdapter;
import com.example.quickcash.model.Application;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays the list of applications submitted for a specific job, fetched from the Firebase database.
 */
public class ApplicationsSubmittedActivity extends AppCompatActivity {
    private final static String TAG = "Applications Page activity";
    private RecyclerView applicationsRecyclerView;
    private ApplicationsAdapter applicationsAdapter;
    private List<Application> applicationList;
    private DatabaseReference databaseReference;
    private String jobId;
    private TextView pageTitle;

    /**
     * Initializes the activity, sets up the RecyclerView, and fetches job applications for the given job.
     *
     * @param savedInstanceState If the activity is being re-initialized, this contains the saved state data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications_submitted);

        // Get jobId and jobTitle passed from the Intent
        jobId = getIntent().getStringExtra("jobId");
        String jobTitle = getIntent().getStringExtra("jobTitle");

        // Initialize UI components
        pageTitle = findViewById(R.id.pageTitle);
        applicationsRecyclerView = findViewById(R.id.applicationsRecyclerView);

        // Set page title with job title
        pageTitle.setText("Applications for " + jobTitle);

        // Set up RecyclerView
        applicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        applicationList = new ArrayList<>();
        applicationsAdapter = new ApplicationsAdapter(applicationList, jobId, this);
        applicationsRecyclerView.setAdapter(applicationsAdapter);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Setup back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v->finish());

        // Fetch applications for the selected job
        fetchApplicationsForJob(jobId);
    }

    /**
     * Fetches applications for a specific job from the Firebase database and updates the RecyclerView with the data.
     *
     * @param jobId The unique identifier of the job for which applications are fetched.
     */
    private void fetchApplicationsForJob(String jobId) {
        Log.d(TAG, "jobID: " + jobId);
        DatabaseReference applicationsRef = databaseReference.child("Jobs").child(jobId).child("applications");

        applicationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            private String email;
            private FirebaseAuth mAuth;

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                applicationList.clear();  // Clear the previous list of applications
                for (DataSnapshot applicationSnapshot : snapshot.getChildren()) {
                    // Extract application data, including applicationId
                    String applicantName = applicationSnapshot.child("Applicant Name").getValue(String.class);
                    Log.d(TAG, "Name: "+applicantName);
                    String applicantEmail = applicationSnapshot.child("Applicant Email").getValue(String.class);
                    Log.d(TAG, "Email: "+applicantEmail);
                    String applicantMessage = applicationSnapshot.child("Cover Letter").getValue(String.class);  // Cover Letter mapped to applicantMessage
                    Log.d(TAG, "Message: "+ applicantMessage);
                    String status = applicationSnapshot.child("Status").getValue(String.class);
                    String applicationId = applicationSnapshot.getKey();  // Firebase automatically assigns the key for each application
                    String employeeID = applicationSnapshot.child("employeeID").getValue(String.class);
                    employeeID = employeeID.replace(".", ",");
                    // Create the application object and set the applicationId
                    Application application = new Application(applicantName, applicantEmail, applicantMessage, status, applicationId, employeeID);

                    // Add the application to the list
                    applicationList.add(application);
                }
                // Notify the adapter to refresh the view
                applicationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ApplicationsSubmittedActivity.this, "Failed to load applications", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
