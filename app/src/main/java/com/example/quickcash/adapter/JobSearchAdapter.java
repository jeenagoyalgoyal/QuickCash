package com.example.quickcash.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.Firebase.FirebaseCRUD;
import com.example.quickcash.MapActivity;
import com.example.quickcash.model.Job;
import com.example.quickcash.R;
import com.example.quickcash.model.JobLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.example.quickcash.PreferredJobsActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.quickcash.model.PreferredEmployerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Class using adapter for displaying job search results in a RecyclerView
 */
public class JobSearchAdapter extends RecyclerView.Adapter<JobSearchAdapter.JobViewHolder> {
    private List<Job> jobList;
    private DatabaseReference preferredJobsRef;
    private ViewGroup parent;

    /**
     * ViewHolder for holding job item views
     */
    public static class JobViewHolder extends RecyclerView.ViewHolder {
        public TextView jobTypeResult;
        public TextView companyResult;
        public TextView locationResult;
        public TextView salaryResult;
        public TextView durationResult;
        public Button showMapButton;
        public Button addToPreferredButton;
        public Button optionsButton;
        public LinearLayout jobSearchLinearLayout;

        /**
         * Constructor for the view holder of jobs
         *
         * @param itemView
         */
        public JobViewHolder(View itemView) {
            super(itemView);
            jobTypeResult = itemView.findViewById(R.id.jobTypeResult);
            companyResult = itemView.findViewById(R.id.companyResult);
            locationResult = itemView.findViewById(R.id.locationResult);
            salaryResult = itemView.findViewById(R.id.salaryResult);
            durationResult = itemView.findViewById(R.id.durationResult);
            showMapButton = itemView.findViewById(R.id.showMap);
            optionsButton = itemView.findViewById(R.id.optionsButton);
            jobSearchLinearLayout = itemView.findViewById(R.id.jobSearchLinearLayout);
        }
    }

    /**
     * Constructor for job search adapter
     *
     * @param jobList
     */
    public JobSearchAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    /**
     * Creating the view holder
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_search_result_view, parent, false);
        JobViewHolder vh = new JobViewHolder(v);
        return vh;
    }

    /**
     * Shows results of job search
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        Job job = jobList.get(position);

        // Set the job details
        holder.jobTypeResult.setText("Job Title: " + job.getJobTitle());
        holder.companyResult.setText("Company: " + job.getCompanyName());

        // Get location using the helper method from Job class
        JobLocation jobLocation = job.getJobLocation();
        if (jobLocation != null) {
            holder.locationResult.setText("Location: " + jobLocation.getAddress());
        } else {
            holder.locationResult.setText("Location: Not specified");
        }

        holder.salaryResult.setText("Salary: $" + String.format("%,d", job.getSalary()));
        holder.durationResult.setText("Duration: " + job.getExpectedDuration());

        // Set up map button click handler

        holder.showMapButton.setOnClickListener(view -> {
            JobLocation location = job.getJobLocation();
            if (location == null) {
                Log.e("JobSearchAdapter", "No location data available for job: " + job.getJobTitle());
                Toast.makeText(parent.getContext(), "No location data available for this job", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("JobSearchAdapter", "Creating map intent for job: " + job.getJobTitle());
            Log.d("JobSearchAdapter", "Location data - Lat: " + location.getLat() +
                    ", Lng: " + location.getLng() +
                    ", Address: " + location.getAddress());

            Intent mapIntent = new Intent(parent.getContext(), MapActivity.class);

            // Create lists for map data
            ArrayList<Double> latitudes = new ArrayList<>();
            ArrayList<Double> longitudes = new ArrayList<>();
            ArrayList<String> locations = new ArrayList<>();  // Add this
            ArrayList<String> titles = new ArrayList<>();
            ArrayList<Integer> salaries = new ArrayList<>();
            ArrayList<String> durations = new ArrayList<>();
            ArrayList<String> companies = new ArrayList<>();
            ArrayList<String> jobTypes = new ArrayList<>();

            // Add job data to lists
            latitudes.add(location.getLat());
            longitudes.add(location.getLng());
            locations.add(location.getAddress());  // Add this
            titles.add(job.getJobTitle());
            salaries.add(job.getSalary());
            durations.add(job.getExpectedDuration());
            companies.add(job.getCompanyName());
            jobTypes.add(job.getJobType());

            // Pass all data in the intent
            mapIntent.putExtra("latitudes", latitudes);
            mapIntent.putExtra("longitudes", longitudes);
            mapIntent.putStringArrayListExtra("locations", locations);  // Add this
            mapIntent.putStringArrayListExtra("titles", titles);
            mapIntent.putIntegerArrayListExtra("salaries", salaries);
            mapIntent.putStringArrayListExtra("durations", durations);
            mapIntent.putStringArrayListExtra("companies", companies);
            mapIntent.putStringArrayListExtra("jobTypes", jobTypes);

            Log.d("JobSearchAdapter", "Sending to MapActivity - " +
                    "Latitudes: " + latitudes + ", " +
                    "Longitudes: " + longitudes + ", " +
                    "Locations: " + locations + ", " +
                    "Titles: " + titles);

            parent.getContext().startActivity(mapIntent);
        });

        holder.optionsButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(parent.getContext(), holder.optionsButton);
            popupMenu.getMenuInflater().inflate(R.menu.job_search_options, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getTitle().equals("Add to Preferred Employers")) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
                        addToPreferredEmployersList(userId, job.getEmployerId(), holder.itemView.getContext());
                    } else if (item.getTitle().equals("Add to Preferred Jobs")) {
                        //Do preferred Job calls here!
                        addJobToPreferredList(job, holder.itemView.getContext());
                    }
                    return true;
                }
            });
        });
    }

    /**
     * Adds an employer to the preferred employers list in the database.
     *
     * @param userId     The user's ID.
     * @param employerId The ID of the employer to be added.
     * @param context    The context for database operations and displaying messages.
     */
    public void addToPreferredEmployersList(String userId, String employerId, Context context) {
        if (userId == null || userId.isEmpty()) {
            Log.e("PreferredEmployers", "Error with userId (is it retrieved correctly?)");
            return;
        }
        userId = sanitizeEmail(userId);
        DatabaseReference userPreferredEmployersRef = getUserPreferredEmployersRef(userId);
        DatabaseReference preferredEmployersNameRef = getPreferredEmployersNameRef(employerId);

        preferredEmployersNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String employerName = snapshot.child("name").getValue(String.class);

                    if (employerName != null) {
                        addPreferredEmployerToDB(context, userPreferredEmployersRef, employerId, employerName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PreferredEmployers", "Error with database connection! (is the URL correct?)");
            }
        });

    }

    /**
     * Adds a preferred employer to the user's preferred employers list in the database.
     * This method first checks if the preferred employers node exists. If it doesn't exist,
     * it initializes it. Then it checks if the employer is already present in the list, if not,
     * it creates a new entry for the employer with the provided ID and name.
     * A success or failure message is displayed based on the outcome of the database operation in the for of a toast.
     *
     * @param context                   The context from which this method is called, used for displaying messages
     *                                  (e.g., Toast) and for accessing application-specific resources.
     * @param userPreferredEmployersRef A reference to the user's preferred employers list in the database.
     * @param employerId                The ID of the employer to be added to the preferred list.
     * @param employerName              The name of the employer to be added to the preferred list.
     */
    public void addPreferredEmployerToDB(Context context, DatabaseReference userPreferredEmployersRef, String employerId, String employerName) {
        //check if preferredEmployers node exists
        userPreferredEmployersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean employerNotPresent = true;
                if (snapshot.hasChildren()) {
                    for (DataSnapshot preferredEmployer : snapshot.getChildren()) {
                        String presentEmployerId = preferredEmployer.child("id").getValue(String.class);
                        if (employerId.equals(presentEmployerId)) {
                            employerNotPresent = false;
                        }
                    }
                }
                if (!snapshot.exists()) {
                    userPreferredEmployersRef.setValue(new ArrayList<String>());
                }
                if (employerNotPresent) {
                    PreferredEmployerModel preferredEmployer = new PreferredEmployerModel();
                    preferredEmployer.setId(employerId);
                    preferredEmployer.setName(employerName);

                    userPreferredEmployersRef.push().setValue(preferredEmployer).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Preferred employee added successfully!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Failed to add preferred employee!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(context, "Employer already in preferred list!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PreferredEmployers", "Error with database connection! (is the URL correct?)");
            }
        });
    }

    /**
     * Retrieves a DatabaseReference to the user's preferred employers list.
     *
     * @param userId The user's ID.
     * @return A DatabaseReference to the preferred employers list.
     */
    private DatabaseReference getUserPreferredEmployersRef(String userId) {
        return FirebaseDatabase.getInstance().getReference("Users").child(userId).child("preferredEmployers");
    }

    /**
     * Retrieves a DatabaseReference to the employer's name node.
     *
     * @param userId The ID of the employer.
     * @return A DatabaseReference to the employer's name.
     */
    private DatabaseReference getPreferredEmployersNameRef(String userId) {
        return FirebaseDatabase.getInstance().getReference("Users").child(userId);
    }


    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public void addJobToPreferredList(Job job, Context context) {
        // Ensure the userId is correctly initialized from FirebaseAuth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;

        if (userId == null || userId.isEmpty()) {
            Log.e("AddJobError", "User ID is null or empty");
            return;
        }

        userId = sanitizeEmail(userId); // Sanitize user ID if it's an email

        DatabaseReference preferredJobsRef = FirebaseDatabase.getInstance()
                .getReference("Users").child(userId).child("preferredJobs");

        // Check if preferred jobs already exist
        preferredJobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // If the preferred jobs node doesn't exist, create an empty list
                    preferredJobsRef.setValue(new ArrayList<Job>());
                }

                // Now, push the job to the preferred job list
                preferredJobsRef.push().setValue(job)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("AddJobSuccess", "Job added successfully");
                            Toast.makeText(context, "Job added to preferred list!", Toast.LENGTH_SHORT).show(); // Display toast
                        })
                        .addOnFailureListener(e -> Log.e("AddJobError", "Failed to add job", e));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("AddJobError", "Database error: " + databaseError.getMessage());
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
    // Utility function to sanitize email for Firebase path
    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }

}
