package com.example.quickcash;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.example.quickcash.Firebase.FirebaseCRUD;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.List;
import java.util.Locale;

/**
 * RegistrationActivity handles user registration, including form validation, database interaction,
 * and location permission management for displaying the user's current location.
 */
public class RegistrationActivity extends AppCompatActivity {

    private FirebaseCRUD crud = null;
    private FirebaseDatabase database = null;
    private FirebaseAuth mAuth;
    private boolean validFlag = true;
    private DatabaseReference userRef;

    public static final int LOCATION_PERMISSION_REQUEST_CODE=1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double testLatitude = 0.0;
    private double testLongitude = 0.0;
    private boolean isLocationReceived = false;
    public LocationCallback locationCallback;
    public static boolean isTesting = false;
    private SessionManager sessionManager;

    /**
     * Initializes the activity, sets up UI components, and handles location permission requests.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge content for the window
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registrationLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (!isTesting) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            requestPermissions();
        }

        mAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);


        this.loadRoleSpinner();
        this.initializeDatabaseAccess();
        this.setupRegisterButton();

        TextView registerTextView = findViewById(R.id.textViewLogin);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Requests location permissions from the user.
     * If permission is granted, starts location updates; otherwise, shows a rationale or dialog.
     */

    void requestPermissions() {
        if (isTesting) return;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                shouldShowRequestPermissionRationale();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            startLocationUpdates();
        }
    }

    /**
     * Shows a rationale for location permission and requests permission again.
     */
    void shouldShowRequestPermissionRationale() {
        new AlertDialog.Builder(this)
                .setTitle("Location Permission Required")
                .setMessage("This app needs access to your location to display your current location.")
                .setPositiveButton("OK", (dialog, which) -> {
                    ActivityCompat.requestPermissions(RegistrationActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    Toast.makeText(this, "Location permission is necessary to proceed", Toast.LENGTH_SHORT).show();
                })
                .create()
                .show();
    }

    /**
     * Handles the result of location permission requests.
     * @param requestCode The request code passed in requestPermissions.
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the requested permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
                showPermissionDialog();
            }
        }
    }

    /**
     * Displays a dialog to the user to enable location permission in settings.
     */
    private void showPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Enable Location Permission")
                .setMessage("Location permission is needed to detect your location. Enable it in settings.")
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }


    /**
     * Starts location updates to fetch the user's current location.
     */
    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000) // 10 seconds interval
                .setMinUpdateIntervalMillis(5000)
                .setWaitForAccurateLocation(true)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                    if(!isLocationReceived){
                        Location location= locationResult.getLastLocation();
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        displayLocationInfo(latitude, longitude);

                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                        isLocationReceived=true;
                    }

                }
            }
        };
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
        } catch (SecurityException e) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Displays location information, including address, latitude, and longitude.
     * @param latitude The latitude of the user's location.
     * @param longitude The longitude of the user's location.
     */
    public void displayLocationInfo(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.CANADA);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressName = address.getAddressLine(0);

                String locationInfo = "Location: " + addressName +
                        "\nLatitude: " + latitude +
                        "\nLongitude: " + longitude;
                Toast.makeText(this, locationInfo, Toast.LENGTH_LONG).show();
                isLocationReceived = true;
                testLatitude = latitude;
                testLongitude = longitude;

            } else {
                Toast.makeText(this, "Unable to find location name", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to get location name", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isLocationReceived() {
        return isLocationReceived;
    }

    public double getTestLatitude() {
        return testLatitude;
    }

    public double getTestLongitude() {
        return testLongitude;
    }
    public void resetLocationFlags() {
        isLocationReceived = false;
        testLatitude = 0.0;
        testLongitude = 0.0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null && locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }



    /**
     * Initializes database access using Firebase.
     */
    public void initializeDatabaseAccess() {
        database = FirebaseDatabase.getInstance("https://quickcash-8f278-default-rtdb.firebaseio.com/");
        crud = new FirebaseCRUD(database);
    }

    /**
     * Loads role options into a Spinner component for user selection.
     */
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

    /**
     * Sets up the Register button with input validation and database interaction logic.
     */
    public void setupRegisterButton(){
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
                String color = new String();

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
                } else if (validator.isValidEmail(email)){
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
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String userId = mAuth.getCurrentUser().getUid();
                                        sessionManager.createSession();
                                        addToDatabase(name, email, password, role);

                                    } else {
                                        Exception exception = task.getException();
                                        if (exception instanceof FirebaseAuthUserCollisionException) {
                                            Toast.makeText(RegistrationActivity.this, "Email already in use by another account.", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(RegistrationActivity.this, "Error: "+exception.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    private void addToDatabase(String name, String email, String password, String role) {
        String validParentNodeName = emailToValidParentNodeName(email);

        userRef = database.getReference("Users");

        userRef = database.getReference("Users").child(validParentNodeName);

        Map<String, String> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("password", password);
        userData.put("role", role);
        userRef.updateChildren((Map) userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Registration Failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    /**
     * Getters and Setters
     */
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