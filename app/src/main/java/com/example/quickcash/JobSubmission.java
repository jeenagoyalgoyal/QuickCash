package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class JobSubmission extends AppCompatActivity {

    private TextView formText;
    private Spinner jobType;
    private Spinner jobUrgency;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.job_submission);

        Intent intent = getIntent();

        formText = findViewById(R.id.jobSub);
        jobType = findViewById(R.id.spinnerJobType);
        jobUrgency = findViewById(R.id.spinnerUrgency);

        List<String> typeList = new ArrayList<>();
        typeList.add(0, "Select job type");

        List <String> urgencyList = new ArrayList<>();
        urgencyList.add(0,"Select urgency");


        // Create ArrayAdapter for each spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobType.setAdapter(typeAdapter);

        ArrayAdapter<String> urgencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, urgencyList);
        urgencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobUrgency.setAdapter(urgencyAdapter);

    }
}
