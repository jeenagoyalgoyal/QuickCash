package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class JobSubmission extends AppCompatActivity {

    public TextView formText;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.job_submission);

        Intent intent = getIntent();

        formText = findViewById(R.id.jobSub);
    }
}
