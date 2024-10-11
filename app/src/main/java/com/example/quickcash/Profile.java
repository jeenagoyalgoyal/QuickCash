package com.example.quickcash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        this.onCreateLogoutDialogue();
        sessionManager =new SessionManager(this);
    }

    protected void onCreateLogoutDialogue(){
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutConfirmationDialogue();
            }
        });
    }

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

    public boolean performLogout(Boolean choice){
        if (choice) {
            sessionManager.logoutUser();
            getSharedPreferences("user_session", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(Profile.this, Login.class);
            startActivity(intent);
            finish();
            return true;
        }else{
            return false;
        }
    }
}
