package com.example.quickcash;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PreferredEmployersActivity extends AppCompatActivity {
    private String email;
    private ArrayList<String> preferredEmployersList;
    private ArrayAdapter<String> adapter;
    private String userID;
    private FirebaseDatabase database;
    private DatabaseReference preferredEmployersRef;
    private String tempEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.preferred_employers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.preferredEmployeesLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Intent intentPreferredEmployers = getIntent();
        //email = intentPreferredEmployers.getStringExtra("email");
        preferredEmployersList = new ArrayList<>();
        email = "testingemail@test.db"; //TEMP
        this.userID = email.replace(".", ",");;

        this.database = FirebaseDatabase.getInstance("https://quickcash-8f278-default-rtdb.firebaseio.com/");
        this.initializeDatabaseRefs();
        this.setPreferredEmployersListView();

    }

    protected void initializeDatabaseRefs() {
        this.preferredEmployersRef = getPreferredEmployersRef();;
    }

    private DatabaseReference getPreferredEmployersRef() {
            return this.database.getReference("Users").child(userID).child("preferredEmployers");
    }

    protected void setPreferredEmployersListView() {
        //add listener to preferredEmployers
        this.preferredEmployersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot employerDetails : snapshot.getChildren()){
                String employerName = employerDetails.child("name").getValue(String.class);
                String employerID = employerDetails.child("id").getValue(String.class);
                Log.e("FirebasePreferredEmployers", "new testing! "+employerID+" "+employerName);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
            //do stuff here
        });
    }

    public List<String> getPreferredEmployersList() {
        return this.preferredEmployersList;
    }

    private void addPreferredEmployersList(String fetchedEmployer){
        String fetchedEmployerTemp = fetchedEmployer+" ";
        Log.e("FirebasePreferredEmployers", "checking temp employer"+fetchedEmployerTemp);
        preferredEmployersList.add(fetchedEmployerTemp);
        Log.e("FirebasePreferredEmployers", "checking temp employer in array"+preferredEmployersList.get(0));
    }
}
