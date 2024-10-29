package com.example.quickcash;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class JobSearchParameterActivity extends AppCompatActivity{

    private EditText jobTitle;
    private EditText minSalary;
    private EditText maxSalary;
    private EditText duration;
    private EditText location;
    private TextView errorText;
    private Button searchButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_search_parameter);

        jobTitle = findViewById(R.id.jobTitle);
        minSalary = findViewById(R.id.minSalary);
        maxSalary = findViewById(R.id.maxSalary);
        duration = findViewById(R.id.duration);
        location = findViewById(R.id.location);
        errorText = findViewById(R.id.jspErrorDisplay);
        searchButton = findViewById(R.id.search_job_parameter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allEmptyFields()){
                    errorText.setText("All Fields are empty");
                }else if(salaryField(minSalary.getText().toString(), maxSalary.getText().toString())){
                    errorText.setText("Enter Valid Salary Range");
                }else{

                }
            }
        });

    }



    // Tests the job title (can be empty)
    public static boolean isValidJobTitle(String title) {
        return title != null;
    }

    // Tests salary is within boundary
    public static boolean isValidSalary(int minSalary, int maxSalary) {
        return minSalary >= 0 && maxSalary >= 0 && minSalary <= maxSalary;
    }

    // Tests valid duration
    public static boolean isValidDuration(String duration) {
        return duration != null && !duration.trim().isEmpty();
    }

    // Tests valid location
    public static boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty() && !location.isEmpty();
    }

    public boolean allEmptyFields(){
        return TextUtils.isEmpty(jobTitle.getText().toString().trim()) &&
                TextUtils.isEmpty(minSalary.getText().toString().trim()) &&
                TextUtils.isEmpty(maxSalary.getText().toString().trim()) &&
                TextUtils.isEmpty(duration.getText().toString().trim()) &&
                TextUtils.isEmpty(location.getText().toString().trim());
    }

    public boolean salaryField(String minSalary, String maxSalary){
        if(minSalary.isEmpty() && maxSalary.isEmpty()){
            return true;
        }else if(minSalary.isEmpty()){
            return true;
        } else if (maxSalary.isEmpty()) {
            return true;
        }else{
            if(isValidSalary(Integer.parseInt(minSalary), Integer.parseInt(maxSalary))){
                return true;
            }
        }
        return false;
    }
}
