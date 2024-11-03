package com.example.quickcash.ui.activities;

import android.content.Intent;
<<<<<<< HEAD
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

=======
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
>>>>>>> c8452335f077f68a71191e31cd517c40ec38c3f0
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.R;
<<<<<<< HEAD
import com.example.quickcash.models.Job;
import com.example.quickcash.ui.utils.Adapter.JobSearchAdapter;
import com.example.quickcash.ui.utils.LocationHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
=======
import com.example.quickcash.ui.models.Job;
import com.example.quickcash.repositories.FirebaseCRUD;
import com.example.quickcash.ui.adapters.JobSearchAdapter;
import com.example.quickcash.ui.utils.LocationHelper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;
>>>>>>> c8452335f077f68a71191e31cd517c40ec38c3f0
import java.util.List;

public class JobSearchParameterActivity extends AppCompatActivity {

    private TextView errorText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private JobSearchAdapter jobSearchAdapter;
    private List<Job> jobList;
    private FirebaseCRUD firebaseCRUD;
    private AutocompleteSupportFragment autocompleteFragment;
    private Place selectedPlace;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_search_parameter);

        // Initialize the Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_api_key));
        }

        initializeViews();
        setupPlacesAutocomplete();
        setupRecyclerView();
        setupSearchButton();
    }

    private void initializeViews() {
        errorText = findViewById(R.id.jspErrorDisplay);
        searchButton = findViewById(R.id.search_job_parameter);
        recyclerView = findViewById(R.id.recyclerView);

        // Initialize FirebaseCRUD using the default constructor
        firebaseCRUD = new FirebaseCRUD();

        jobList = new ArrayList<>();
    }

    private void setupPlacesAutocomplete() {
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return
        autocompleteFragment.setPlaceFields(Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                selectedPlace = place;
                showSuccess("Selected: " + place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {
                showError("Error: " + status.getStatusMessage());
            }
        });

        // Set hint text
        autocompleteFragment.setHint("Enter full address");
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobSearchAdapter = new JobSearchAdapter(jobList, this);
        recyclerView.setAdapter(jobSearchAdapter);
    }

    private void setupSearchButton() {
        searchButton.setOnClickListener(v -> {
            if (validateAddress()) {
                performSearch();
            }
        });
    }

    private boolean validateAddress() {
        if (selectedPlace == null || selectedPlace.getLatLng() == null) {
            showError("Please select an address from the suggestions");
            return false;
        }
        return true;
    }

    private void performSearch() {
        if (selectedPlace == null || selectedPlace.getLatLng() == null) {
            showError("Please select a valid address");
            return;
        }

        String locationName = selectedPlace.getAddress();

        // Use FirebaseCRUD to search for jobs in the selected location
        firebaseCRUD.searchJobs(locationName, new FirebaseCRUD.JobDataCallback() {
            @Override
            public void onCallback(List<Job> jobs) {
                if (jobs.isEmpty()) {
                    showError("No jobs found at the selected location.");
                    return;
                }

                // Prepare data for MapActivity
                Intent mapIntent = new Intent(JobSearchParameterActivity.this, MapActivity.class);

                ArrayList<Double> latitudes = new ArrayList<>();
                ArrayList<Double> longitudes = new ArrayList<>();
                ArrayList<String> titles = new ArrayList<>();
                ArrayList<Integer> salaries = new ArrayList<>();
                ArrayList<String> durations = new ArrayList<>();
                ArrayList<String> companies = new ArrayList<>();

                // Add search location marker
                latitudes.add(selectedPlace.getLatLng().latitude);
                longitudes.add(selectedPlace.getLatLng().longitude);
                titles.add("Search Location: " + selectedPlace.getAddress());
                salaries.add(0);
                durations.add("");
                companies.add("");

                // Add jobs to the lists
                for (Job job : jobs) {
                    // Get coordinates of the job location
                    LatLng jobLatLng = LocationHelper.getCoordinatesFromAddress(JobSearchParameterActivity.this, job.getLocation());
                    if (jobLatLng != null) {
                        latitudes.add(jobLatLng.latitude);
                        longitudes.add(jobLatLng.longitude);
                        titles.add(job.getJobTitle());
                        salaries.add(job.getSalary());
                        durations.add(job.getExpectedDuration()); // Corrected method name
                        companies.add(job.getCompanyName());
                    } else {
                        // Handle cases where geocoding fails for a job location
                        // Optionally, you can log or notify the user
                        System.out.println("Failed to geocode job location: " + job.getLocation());
                    }
                }

                // Add data to intent
                mapIntent.putExtra("latitudes", latitudes);
                mapIntent.putExtra("longitudes", longitudes);
                mapIntent.putStringArrayListExtra("titles", titles);
                mapIntent.putIntegerArrayListExtra("salaries", salaries);
                mapIntent.putStringArrayListExtra("durations", durations);
                mapIntent.putStringArrayListExtra("companies", companies);

                // Add center coordinates
                mapIntent.putExtra("centerLat", selectedPlace.getLatLng().latitude);
                mapIntent.putExtra("centerLng", selectedPlace.getLatLng().longitude);

                startActivity(mapIntent);
            }

            @Override
            public void onError(String error) {
                showError("Failed to retrieve job data: " + error);
            }
        });
    }

    private void showError(String message) {
        errorText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        errorText.setText(message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSuccess(String message) {
        errorText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        errorText.setText(message);
    }
}
