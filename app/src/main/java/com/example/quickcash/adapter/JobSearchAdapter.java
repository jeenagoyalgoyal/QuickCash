package com.example.quickcash.adapter;

import android.content.Context;
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

import com.example.quickcash.model.Job;
import com.example.quickcash.R;
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

public class JobSearchAdapter extends RecyclerView.Adapter<JobSearchAdapter.JobViewHolder> {
    private List<Job> jobList;
    private ViewGroup parent;

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        public TextView jobTypeResult;
        public TextView companyResult;
        public TextView locationResult;
        public TextView salaryResult;
        public TextView durationResult;
        public Button showMapButton;
        public Button optionsButton;
        public LinearLayout jobSearchLinearLayout;

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

    public JobSearchAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent=parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_search_result_view, parent, false);
        JobViewHolder vh = new JobViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.jobTypeResult.setText("Job Title: " + job.getJobTitle());
        holder.companyResult.setText("Company: " + job.getCompanyName());
        holder.locationResult.setText("Location: " + job.getLocation());
        holder.salaryResult.setText("Salary: $" + job.getSalary());
        holder.durationResult.setText("Duration: " + job.getExpectedDuration());

        holder.showMapButton.setOnClickListener(view -> {
            // Implement map functionality here
        });

        holder.optionsButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(parent.getContext(),holder.optionsButton);
            popupMenu.getMenuInflater().inflate(R.menu.job_search_options,popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //set conditions for when different options are pressed
                    if (item.getTitle().equals("Add to Preferred Employers")){
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;

                        addToPreferredEmployersList(userId,job.getEmployerId(),holder.itemView.getContext());
                        Toast.makeText(parent.getContext(), "preferred employee added! ", Toast.LENGTH_LONG).show();
                    }
                    return false;
                }
            });
        });
    }

    public void addToPreferredEmployersList(String userId, String employerId, Context context){
        if (userId == null || userId.isEmpty()){
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

                    if (employerName!=null){
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

    public void addPreferredEmployerToDB(Context context,DatabaseReference userPreferredEmployersRef,String employerId, String employerName){
        //check if preferredEmployers node exists
        userPreferredEmployersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    userPreferredEmployersRef.setValue(new ArrayList<String>());
                }
                PreferredEmployerModel preferredEmployer = new PreferredEmployerModel();
                preferredEmployer.setId(employerId);
                preferredEmployer.setName(employerName); //TEMP

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PreferredEmployers", "Error with database connection! (is the URL correct?)");
            }
        });
    }

    private DatabaseReference getUserPreferredEmployersRef(String userId){
        return FirebaseDatabase.getInstance().getReference("Users").child(userId).child("preferredEmployers");
    }

    private DatabaseReference getPreferredEmployersNameRef(String userId){
        return FirebaseDatabase.getInstance().getReference("Users").child(userId);
    }

    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }


    @Override
    public int getItemCount() {
        return jobList.size();
    }
}
