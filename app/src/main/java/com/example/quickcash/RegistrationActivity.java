package com.example.quickcash;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseCRUD crud = null;
    private FirebaseDatabase database = null;
    private FirebaseAuth mAuth;
    private boolean validFlag = true;
    private DatabaseReference userRef;

    private static final int LOCATION_PERMISSION_REQUEST_CODE=1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double testLatitude = 0.0;
    private double testLongitude = 0.0;
    private boolean isLocationReceived = false;
    LocationCallback locationCallback;

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

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        requestPermissions();

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            detectAndDisplayLocation();
            startLocationUpdates();

        }
        else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
                shouldShowRequestPermissionRationale();
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }
        }

        mAuth = FirebaseAuth.getInstance();

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

    void requestPermissions() {
        // Request location permission every time the app starts
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
//            detectAndDisplayLocation(); // Permission already granted
            startLocationUpdates();
        }
    }

    void shouldShowRequestPermissionRationale() {
        new AlertDialog.Builder(this)
                .setTitle("Location Permission Required")
                .setMessage("This app needs access to your location to display your current location.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Request permission after showing rationale
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch location
//                detectAndDisplayLocation();
                startLocationUpdates();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
                showPermissionDialog();
            }
        }
    }

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

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(PRIORITY_HIGH_ACCURACY)
                .setInterval(10000) // 10 seconds interval
                .setFastestInterval(5000); // 5 seconds fastest interval

         locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                    double latitude = locationResult.getLastLocation().getLatitude();
                    double longitude = locationResult.getLastLocation().getLongitude();
                    displayLocationInfo(latitude, longitude);
                }
            }
        };

        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
        } catch (SecurityException e) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    void displayLocationInfo(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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

    // Reset method for testing
    public void resetLocationFlags() {
        isLocationReceived = false;
        testLatitude = 0.0;
        testLongitude = 0.0;
    }
//    private void detectAndDisplayLocation() {
//        try {
//            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
//                if (location != null) {
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//
//                    // Convert lat/long to a human-readable address
//                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//                    try {
//                        // Get address from lat/long
//                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//                        if (addresses != null && !addresses.isEmpty()) {
//                            Address address = addresses.get(0);
//                            String addressName = address.getAddressLine(0);
//
//                            // Display latitude, longitude, and address
//                            String locationInfo = "Location: "+addressName+ " Latitude: " + latitude +
//                                    ", Longitude: " + longitude ;
//                            Toast.makeText(RegistrationActivity.this, "Current Location: " + locationInfo, Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(RegistrationActivity.this, "Unable to find location name", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(RegistrationActivity.this, "Failed to get location name", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }).addOnFailureListener(e -> {
//                Toast.makeText(RegistrationActivity.this, "Failed to get location", Toast.LENGTH_SHORT).show();
//            });
//        } catch (SecurityException e) {
//            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
//        }
//    }
@Override
protected void onStop() {
    super.onStop();
    // Stop location updates when the activity is not visible
    if (fusedLocationProviderClient != null && locationCallback != null) {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
    public void setFusedLocationProviderClient(FusedLocationProviderClient fusedLocationProviderClient) {
        this.fusedLocationProviderClient=fusedLocationProviderClient;
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
                String color = new String();

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
