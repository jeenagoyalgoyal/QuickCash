package com.example.quickcash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.adapter.FeedbackAdapter;
import com.example.quickcash.model.FeedbackModel;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    SessionManager sessionManager;
    private RecyclerView feedbackRecyclerView;
    private FeedbackAdapter feedbackAdapter;
    private List<FeedbackModel> feedbackList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        this.onCreateLogoutDialogue();
        sessionManager =new SessionManager(this);

        // Set up RecyclerView
        feedbackRecyclerView = findViewById(R.id.feedbackRecyclerView);
        feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        feedbackList = new ArrayList<>();
        feedbackAdapter = new FeedbackAdapter(feedbackList);
        feedbackRecyclerView.setAdapter(feedbackAdapter);

    }

    //Method to set up the Logout Dialog button
    protected void onCreateLogoutDialogue(){
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutConfirmationDialogue();
            }
        });
    }

    // Method to display a confirmation dialog for logging out
    private void logoutConfirmationDialogue(){
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to Logout")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        performLogout(Boolean.FALSE);
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        performLogout(Boolean.TRUE);
                    }
                })
                .show();
    }



    // Method to perform the logout
    public boolean performLogout(Boolean choice){
        if (choice) {
            sessionManager.logoutUser();
            getSharedPreferences("user_session", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(Profile.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }else{
            return false;
        }
    }
}
