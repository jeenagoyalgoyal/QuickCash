package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.List;

public class JobSubmission extends AppCompatActivity {

    private TextView formText;
    private Spinner jobType;
    private Spinner jobUrgency;
    private EditText companyName;
    private EditText requirements;
    private EditText salary;
    private EditText location;
    private EditText expectedDuration;
    private Button startDate;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.job_submission);

        Intent intent = getIntent();

        formText = findViewById(R.id.jobSub);
        jobType = findViewById(R.id.spinnerJobType);
        jobUrgency = findViewById(R.id.spinnerUrgency);
        companyName = findViewById(R.id.companyName);
        requirements = findViewById(R.id.requirementText);
        salary = findViewById(R.id.salaryText);
        location = findViewById(R.id.locationJob);
        expectedDuration = findViewById(R.id.expectedDuration);
        startDate = findViewById(R.id.startDate);
        submitButton = findViewById(R.id.jobSubmissionButton);


        List<String> typeList = new ArrayList<>();
        typeList.add(0, "Select job type");
        typeList.add("Full-time");
        typeList.add("Part-time");
        typeList.add("Internship");

        List <String> urgencyList = new ArrayList<>();
        urgencyList.add(0,"Select urgency");
        urgencyList.add("High");
        urgencyList.add("Medium");
        urgencyList.add("Low");


        // Create ArrayAdapter for each spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobType.setAdapter(typeAdapter);

        ArrayAdapter<String> urgencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, urgencyList);
        urgencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobUrgency.setAdapter(urgencyAdapter);


        // Set up a date picker for the start date
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();

                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                datePicker.addOnPositiveButtonClickListener(selection -> {
                    startDate.setText(datePicker.getHeaderText());
                });
            }
        });

    }
}
