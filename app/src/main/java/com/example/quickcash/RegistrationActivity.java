package com.example.quickcash;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseCRUD crud = null;
    private FirebaseDatabase database = null;
    private FirebaseAuth mAuth;
    private boolean validFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registrationLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        this.loadRoleSpinner();
        this.setupRegisterButton();
        this.initializeDatabaseAccess();
    }

    private void initializeDatabaseAccess() {
        database = FirebaseDatabase.getInstance("https://quickcash-8f278-default-rtdb.firebaseio.com/");
        crud = new FirebaseCRUD(database);

    }

    protected void loadRoleSpinner() {
        ArrayList<String> roles = new ArrayList<>();
        roles.add("Employer");
        roles.add("Employee");
        ArrayAdapter<String> roleAdapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                roles);
        roleAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        Spinner roleSpinner = findViewById(R.id.spinnerRole);
        roleSpinner.setAdapter(roleAdapter);
    }

    protected void setupRegisterButton(){
        Button registerButton = findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = getName();
                String email = getEmail();
                String password = getPassword();
                String password2 = getPassword2();
                String role = getRole();

                CredentialsValidator validator = new CredentialsValidator();

                validFlag = true;
                String errorLabel = new String();

                if (validator.isEmptyInput(name)) {
                    validFlag = false;
                    errorLabel = "cannot be empty!";
                } else if (!validator.isValidName(name)) {
                    validFlag = false;
                    errorLabel = "invalid!";
                } else {
                    errorLabel = "valid";
                }
                nameSetStatusMessage(errorLabel);

                if (validator.isEmptyInput(email)) {
                    validFlag = false;
                    errorLabel = "cannot be empty!";
                } else if (!validator.isValidEmail(email)) {
                    validFlag = false;
                    errorLabel = "invalid!";
                } else {
                    errorLabel = "valid";
                }
                emailSetStatusMessage(errorLabel);

                if (validator.isEmptyInput(password)) {
                    validFlag = false;
                    errorLabel = "cannot be empty!";
                } else if (!validator.isValidPassword(password)) {
                    validFlag = false;
                    errorLabel = "invalid!";
                } else if (!password.equals(password2)){
                    validFlag = false;
                    errorLabel = "Passwords do not match!";
                } else {
                    errorLabel = "valid";
                }
                passwordSetStatusMessage(errorLabel);

                if (validator.isEmptyInput(password2)) {
                    validFlag = false;
                    errorLabel = "cannot be empty!";
                } else if (!validator.isValidPassword(password2)) {
                    validFlag = false;
                    errorLabel = "invalid!";
                } else if (!password.equals(password2)){
                    validFlag = false;
                    errorLabel = "Passwords do not match!";
                } else {
                    errorLabel = "valid";
                }

                password2SetStatusMessage(errorLabel);

                if (validFlag) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "User Registered", Toast.LENGTH_LONG).show();

                                //To be added for integration with login activity

                                Intent intent = new Intent(RegistrationActivity.this, FirebaseCRUD.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(RegistrationActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    protected String getName() {
        EditText nameBox = findViewById(R.id.enterName);
        return nameBox.getText().toString().trim();
    }

    protected String getEmail() {
        EditText emailBox = findViewById(R.id.enterEmail);
        return emailBox.getText().toString().trim();
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
        return roleSpinner.getSelectedItem().toString().trim();
    }

    protected void nameSetStatusMessage(String errorLabel) {
        TextView validNameLabel = findViewById(R.id.validName);
        validNameLabel.setText(errorLabel.trim());
    }

    protected void emailSetStatusMessage(String errorLabel) {
        TextView validEmailLabel = findViewById(R.id.validEmail);
        validEmailLabel.setText(errorLabel.trim());
    }

    protected void passwordSetStatusMessage(String errorLabel) {
        TextView validPasswordLabel = findViewById(R.id.validPassword);
        validPasswordLabel.setText(errorLabel.trim());
    }

    protected void password2SetStatusMessage(String errorLabel) {
        TextView validPasswordLabel2 = findViewById(R.id.validPassword2);
        validPasswordLabel2.setText(errorLabel.trim());
    }
}
