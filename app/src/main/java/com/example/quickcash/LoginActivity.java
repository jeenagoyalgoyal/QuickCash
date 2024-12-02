package com.example.quickcash;

import static com.example.quickcash.RegistrationActivity.LOCATION_PERMISSION_REQUEST_CODE;


import com.example.quickcash.FirebaseMessaging.MyFirebaseMessagingService;
import com.example.quickcash.model.UseRole;
import com.google.android.gms.location.LocationRequest;
import com.example.quickcash.Firebase.UserCrud;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import com.example.quickcash.Firebase.FirebaseCRUD;
import com.example.quickcash.ui.MapsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.example.quickcash.Firebase.UserCrud;
import com.example.quickcash.FirebaseMessaging.MyFirebaseMessagingService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


import android.Manifest;

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
    private String manualLocation = null;
    private boolean locationPermissionDenied = false;
    private SessionManager sessionManager;
    private UserCrud userCrud;


    // Regex patterns for email and password validation
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[.*)(+@#$%&!?><{}/\\]\\[]).{6,}$"; // At least 6 characters, at least one letter, one number and one special character

    /**
     * Called when the activity is first created. Initializes UI components and sets up event listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intent = new Intent(LoginActivity.this, MapsActivity.class);
        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        statusLabel = findViewById(R.id.statusLabel);
        Button loginButton = findViewById(R.id.loginButton);

        sessionManager = new SessionManager(this);

        userCrud =new UserCrud();
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
        location = findViewById(R.id.Location);
        LocButton = findViewById(R.id.LocButton);
        LocButton.setOnClickListener(e -> handleManualLocationInput());
        requestLocationPermission();
    }

    /**
     * Handles manual location input from the user and updates the intent.
     */
    private void handleManualLocationInput() {
        manualLocation = location.getText().toString().trim();

        if (manualLocation.isEmpty()) {
            Toast.makeText(this, "Location field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(manualLocation, 5);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                // Store latitude and longitude as the manual location
                manualLocation = latitude + "," + longitude;
                manualLocationDetect = true;
                locationPermissionDenied = true;

                Toast.makeText(this, "Manual Location set to: " + manualLocation, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Current location.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Geocoder service not available", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void navigateToMapsActivity() {
        intent.putExtra("manualLocation", manualLocation); // Pass manual location
        startActivity(intent);
    }


    /**
     * Initializes Firebase database access.
     */
    private void initializeDatabaseAccess() {
        database = FirebaseDatabase.getInstance("https://quickcash-8f278-default-rtdb.firebaseio.com/");
        crud = new FirebaseCRUD(database);

    }

    /**
     * Handles the click event for the login button. Validates input and initiates login if valid.
     * @param view The view that was clicked.
     */
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

    public static boolean isEmptyEmailAddress(String email) {
        return email.isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    public static boolean isEmptyPassword(String password) {
        return password.isEmpty();
    }

    public static boolean isValidPassword(String password) {
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }

    /**
     * Logs in the user by checking credentials against Firebase Realtime Database.
     *
     * @param email    The user's email.
     * @param password The user's password.
     */
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

    /**
     * Authenticates the user using Firebase Authentication.
     *
     * @param email    The user's email.
     * @param password The user's password.
     */
    private void authenticateUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        sessionManager.createSession();
                        updateDeviceToken(email);
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
                        // Fetch user role
                        fetchUserRoleAndNavigate(email);
                    } else {
                        handleLoginError(task.getException());
                    }
                });
    }

    private void updateDeviceToken(String email) {    // Get the current FCM token
        MyFirebaseMessagingService.getTokenFromPreferences(getSharedPreferences("QuickCashPrefs", MODE_PRIVATE));
        MyFirebaseMessagingService.getTokenFromPreferences(getSharedPreferences("QuickCashPrefs", MODE_PRIVATE));
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String deviceToken = task.getResult();
                userCrud.setUserDeviceToken(email, deviceToken);
                Log.d("LoginActivity", "Device token updated for user: " + email);
            } else {
                Log.e("LoginActivity", "Failed to retrieve FCM token", task.getException());
            }
        });
    }

    /**
     * Fetches the user's role and navigates to MapsActivity.
     *
     * @param email The user's email.
     */
    private void fetchUserRoleAndNavigate(String email) {
        UseRole useRole = UseRole.getInstance();
        useRole.fetchUserRole(email, role -> {
            if (role != null) {
                navigateToMapsActivity(email, role);
            } else {
                Toast.makeText(LoginActivity.this, "Role not found. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Navigates to MapsActivity with the manual location if set, or the current location if permission is granted.
     */
    private void navigateToMapsActivity(String email, String role) {
        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        Log.d("Logging in with email: ", email);
        intent.putExtra("email", email);
        Log.d("Logging in with role: ", role);
        intent.putExtra("role", role);

        // Pass manual location to MapsActivity if set
        if (manualLocationDetect) {
            intent.putExtra("manualLocation", manualLocation);
        } else {
            getCurrentLocation(intent);
        }

        startActivity(intent);
        finish();
    }


    /**
     * Handles login errors and displays appropriate messages to the user.
     *
     * @param e The exception that occurred during login.
     */
    private void handleLoginError(Exception e) {
        if (e instanceof FirebaseAuthInvalidUserException) {
            statusLabel.setText("No account found with this email.");
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            statusLabel.setText("Invalid password.");
        } else {
            statusLabel.setText("Login failed. Please try again.");
        }
    }

    /**
     * Requests location permission from the user.
     */
    void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation(intent);
        }
    }

    /**
     * Gets the current location of the user and updates the intent.
     */
    void getCurrentLocation(Intent intent) {
        if (manualLocationDetect) {
            return;
        }
        LocationRequest locationRequest = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY) // 10 seconds interval
                    .setMinUpdateIntervalMillis(5000)
                    .build();
        }


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                    latitude = locationResult.getLastLocation().getLatitude();
                    longitude = locationResult.getLastLocation().getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    intent.putExtra("LatLng", latLng);

                }
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
        }
    }

    /**
     * Moves to the next activity after a delay and passes location data via intent.
     *
     * @param manualLocation The manually entered location, if any.
     */
    private void moveToNextWithDelay(String manualLocation) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(LoginActivity.this, EmployerHomepageActivity.class);
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


    /**
     * Handles location permission request results.
     *
     * @param requestCode  The request code passed in requestPermissions.
     * @param permissions  The requested permissions.
     * @param grantResults The grant results for the requested permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation(intent);
            } else {
                locationPermissionDenied = true; // Set flag when permission is denied
                if (manualLocation != null) {
                    // Use manual location if available
                    navigateToMapsActivity();
                } else {
                    Toast.makeText(this, "Location permission is required or provide a manual location.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
