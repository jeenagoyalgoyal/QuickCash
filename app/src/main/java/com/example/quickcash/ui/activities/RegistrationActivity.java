package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickcash.ui.utils.Validators.CredentialsValidator;
import com.example.quickcash.repositories.FirebaseCRUD;
import com.example.quickcash.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseCRUD crud = null;
    private FirebaseDatabase database = null;
    private FirebaseAuth mAuth;
    private boolean validFlag = true;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge content for the window
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        setContentView(R.layout.user_registration);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registrationLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        this.loadRoleSpinner();
        this.initializeDatabaseAccess();
        this.setupRegisterButton();

        TextView registerTextView = findViewById(R.id.textViewLogin);
        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void initializeDatabaseAccess() {
        database = FirebaseDatabase.getInstance("https://quickcash-8f278-default-rtdb.firebaseio.com/");
        crud = new FirebaseCRUD(database);
    }

    protected void loadRoleSpinner() {
        ArrayList<String> roles = new ArrayList<>();
        roles.add("Employer");
        roles.add("Employee");
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                roles);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner roleSpinner = findViewById(R.id.spinnerRole);
        roleSpinner.setAdapter(roleAdapter);
    }

    public void setupRegisterButton(){
        Button registerButton = findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(view -> {
            String name = getName();
            String email = getEmail();
            String password = getPassword();
            String password2 = getPassword2();
            String role = getRole();

            CredentialsValidator validator = new CredentialsValidator();

            validFlag = true;
            String errorLabel;
            String color;

            // Name validation
            if (validator.isEmptyInput(name)) {
                validFlag = false;
                errorLabel = "cannot be empty!";
                color = "#EB0101";
            } else if (!validator.isValidName(name)) {
                validFlag = false;
                errorLabel = "must be longer than 2 characters and cannot contain special characters!";
                color = "#EB0101";
            } else {
                errorLabel = "valid";
                color = "#0DBC00";
            }
            nameSetStatusMessage(errorLabel, color);

            // Email validation
            if (validator.isEmptyInput(email)) {
                validFlag = false;
                errorLabel = "cannot be empty!";
                color = "#EB0101";
            } else if (!validator.isValidEmail(email)) {
                validFlag = false;
                errorLabel = "invalid email, please check format!";
                color = "#EB0101";
            } else {
                errorLabel = "valid";
                color = "#0DBC00";
            }
            emailSetStatusMessage(errorLabel, color);

            // Password validation
            if (validator.isEmptyInput(password)) {
                validFlag = false;
                errorLabel = "cannot be empty!";
                color = "#EB0101";
            } else if (!validator.isValidPassword(password)) {
                validFlag = false;
                errorLabel = "password must be longer than 8 characters and contain at-least 1 special character!";
                color = "#EB0101";
            } else {
                errorLabel = "valid";
                color = "#0DBC00";
            }
            passwordSetStatusMessage(errorLabel, color);

            // Confirm password validation
            if (validator.isEmptyInput(password2)) {
                validFlag = false;
                errorLabel = "cannot be empty!";
                color = "#EB0101";
            } else if (!validator.isValidPassword(password2)) {
                validFlag = false;
                errorLabel = "password must be longer than 8 characters and contain at-least 1 special character!";
                color = "#EB0101";
            } else if (!password.equals(password2)){
                validFlag = false;
                errorLabel = "passwords do not match!";
                color = "#EB0101";
            } else {
                errorLabel = "valid";
                color = "#0DBC00";
            }

            password2SetStatusMessage(errorLabel, color);

            if (validFlag) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                addToDatabase(name, email, password, role);
                            } else {
                                Exception exception = task.getException();
                                if (exception instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(RegistrationActivity.this, "Email already in use by another account.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(RegistrationActivity.this, "Error: "+exception.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    private void addToDatabase(String name, String email, String password, String role) {
        String validParentNodeName = emailToValidParentNodeName(email);
        userRef = database.getReference("Users").child(validParentNodeName);

        Map<String, String> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("password", password);
        userData.put("role", role);

        userRef.updateChildren((Map) userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(RegistrationActivity.this, "Registration Failed!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String emailToValidParentNodeName(String email) {
        return email.replace(".", ",");
    }

    protected String getName() {
        EditText nameBox = findViewById(R.id.enterName);
        return nameBox.getText().toString().toLowerCase().trim();
    }

    protected String getEmail() {
        EditText emailBox = findViewById(R.id.enterEmail);
        return emailBox.getText().toString().toLowerCase().trim();
    }

    protected String getPassword() {
        EditText passwordBox = findViewById(R.id.enterPassword);
        return passwordBox.getText().toString().trim();
    }

    protected String getPassword2() {
        EditText password2Box = findViewById(R.id.enterPassword2);
        return password2Box.getText().toString().trim();
    }

    protected String getRole() {
        Spinner roleSpinner = findViewById(R.id.spinnerRole);
        return roleSpinner.getSelectedItem().toString().toLowerCase().trim();
    }

    protected void nameSetStatusMessage(String errorLabel, String color) {
        TextView validNameLabel = findViewById(R.id.validName);
        validNameLabel.setTextColor(Color.parseColor(color));
        validNameLabel.setText(errorLabel.trim());
    }

    protected void emailSetStatusMessage(String errorLabel, String color) {
        TextView validEmailLabel = findViewById(R.id.validEmail);
        validEmailLabel.setTextColor(Color.parseColor(color));
        validEmailLabel.setText(errorLabel.trim());
    }

    protected void passwordSetStatusMessage(String errorLabel, String color) {
        TextView validPasswordLabel = findViewById(R.id.validPassword);
        validPasswordLabel.setTextColor(Color.parseColor(color));
        validPasswordLabel.setText(errorLabel.trim());
    }

    protected void password2SetStatusMessage(String errorLabel, String color) {
        TextView validPasswordLabel2 = findViewById(R.id.validPassword2);
        validPasswordLabel2.setTextColor(Color.parseColor(color));
        validPasswordLabel2.setText(errorLabel.trim());
    }
}