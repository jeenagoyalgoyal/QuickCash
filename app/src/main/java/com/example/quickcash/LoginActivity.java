package com.example.quickcash;

import static com.example.quickcash.RegistrationActivity.LOCATION_PERMISSION_REQUEST_CODE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.quickcash.ui.MapsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
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

    Intent intent;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;
    private boolean manualLocationDetect = false;
    private EditText location;
    private Button LocButton;
    private String manualLocation=null;

    // Regex patterns for email and password validation
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[.*)(+@#$%&!?><{}/\\]\\[]).{6,}$"; // At least 6 characters, at least one letter, one number and one special character

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intent = new Intent(LoginActivity.this, MapsActivity.class);
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        location= findViewById(R.id.Location);
        LocButton=findViewById(R.id.LocButton);
        LocButton.setOnClickListener(e -> handleManualLocationInput());
        requestLocationPermission();

    }

    private void handleManualLocationInput() {
        manualLocation = location.getText().toString().trim();

        if (manualLocation.isEmpty()) {
            Toast.makeText(this, "Location field cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            manualLocationDetect = true;
            Toast.makeText(this, "Manual Location set to: " + manualLocation, Toast.LENGTH_SHORT).show();
            intent.putExtra("manualLocation", manualLocation);
//            new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
//                intent.putExtra("manualLocation", manualLocation);
//                startActivity(intent);
//                moveToNextWithDelay(null);
//            }, 3000);
        }
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
                        // Fetch user role
                        navigateActivity();
                    } else {
                        handleLoginError(task.getException());
                    }
                });
    }

    private void navigateActivity() {
        if (manualLocation != null ) {
            Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
            intent.putExtra("manualLocation", manualLocation);
            startActivity(intent);
            finish();
        } else {
            getCurrentLocation();
            startActivity(intent);
        }
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
    void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    void getCurrentLocation() {
        if(manualLocationDetect){
            return;
        }
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000) // 10 seconds interval
                .setMinUpdateIntervalMillis(5000)
                .setWaitForAccurateLocation(true)// 5 seconds fastest interval
                .build();


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                    latitude = locationResult.getLastLocation().getLatitude();
                    longitude = locationResult.getLastLocation().getLongitude();
                    LatLng latLng= new LatLng(latitude,longitude);
                    intent.putExtra("LatLng",latLng);

                }
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
        }
    }
    private void moveToNextWithDelay( String manualLocation) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(LoginActivity.this, RoleActivity.class);
            if (manualLocation != null) {
                intent.putExtra("manualLocation", manualLocation);
            } else {
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
            }
            startActivity(intent);
            finish();
        }, 3000);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void fetchUserRole(String email) {

        UseRole useRole = UseRole.getInstance();
        useRole.fetchUserRole(email, new UseRole.OnRoleFetchedListener() {
            @Override
            public void onRoleFetched(String role) {
                if (role != null) {
                    // Set the current role in UseRole
                    useRole.setCurrentRole(role);
                    // Navigate to the appropriate homepage
                    if (role.equalsIgnoreCase("employee")) {
                        navigateToEmployeeHomepage(email);
                    } else if (role.equalsIgnoreCase("employer")) {
                        navigateToEmployerHomepage(email);
                    } else {
                        statusLabel.setText("Role not recognized.");
                    }
                } else {
                    statusLabel.setText("Role not found.");
                }
            }
        });
    }

    private void navigateToEmployeeHomepage(String email) {
        Intent intentEmployee = new Intent(this, EmployeeHomepageActivity.class);
        intentEmployee.putExtra("email", email);
        startActivity(intentEmployee);
    }

    private void navigateToEmployerHomepage(String email) {
        Intent intentEmployer = new Intent(this, EmployerHomepageActivity.class);
        intentEmployer.putExtra("email", email);
        startActivity(intentEmployer);
    }
}
