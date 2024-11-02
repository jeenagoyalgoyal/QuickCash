package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseCRUD crud = null;
    private FirebaseDatabase database = null;
    private FirebaseAuth mAuth;
    private EditText emailBox, passwordBox;
    private TextView statusLabel;
    private DatabaseReference databaseReference;


    // Regex patterns for email and password validation
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[.*)(+@#$%&!?><{}/\\]\\[]).{6,}$"; // At least 6 characters, at least one letter, one number and one special character

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        statusLabel = findViewById(R.id.statusLabel);
        Button loginButton = findViewById(R.id.loginButton);

        this.initializeDatabaseAccess();

        loginButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        // Reference to your Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        TextView registerTextView = findViewById(R.id.textViewRegister);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeDatabaseAccess() {
        database = FirebaseDatabase.getInstance("https://quickcash-8f278-default-rtdb.firebaseio.com/");
        crud = new FirebaseCRUD(database);

    }

    @Override
    public void onClick(View view) {
        String email = emailBox.getText().toString().toLowerCase().trim();
        String password = passwordBox.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Email or Password cannot be empty.");
        } else if (!isValidEmail(email)) {
            statusLabel.setText("Invalid email format.");
        } else if (!isValidPassword(password)) {
            statusLabel.setText("Password must be at least 6 characters long and contain at least one letter and one number.");
        } else {
            loginUser(email, password);
        }
    }
    public static boolean isEmptyEmailAddress(String email){return email.isEmpty();}
    public static boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }
    public static boolean isEmptyPassword(String password){return password.isEmpty();}
    public static boolean isValidPassword(String password) {
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }

    private void loginUser(String email, String password) {
        // Convert the email to Firebase-friendly format by replacing '.' with ','
        String emailKey = email.replace(".", ",");

        // Fetch user details from Firebase Realtime Database
        databaseReference.child(emailKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve user data and authenticate
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);

                    if (storedPassword != null && storedPassword.equals(password)) {
                        // If password matches, proceed with Firebase Authentication
                        authenticateUser(email, password);
                    } else {
                        statusLabel.setText("Invalid password.");
                    }
                } else {
                    statusLabel.setText("No account found with this email.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                statusLabel.setText("Error accessing database, please try again");
                Log.e("LoginActivity", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void authenticateUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
                        moveToWelcomePage();
                    } else {
                        handleLoginError(task.getException());
                    }
                });
    }

    private void handleLoginError(Exception e) {
        if (e instanceof FirebaseAuthInvalidUserException) {
            statusLabel.setText("No account found with this email.");
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            statusLabel.setText("Invalid password.");
        } else {
            statusLabel.setText("Login failed. Please try again.");
        }
    }

    private void moveToWelcomePage() {
        // Intent to move to the welcome page
        Intent intent = new Intent(this, RoleActivity.class);
        startActivity(intent);
    }
}
