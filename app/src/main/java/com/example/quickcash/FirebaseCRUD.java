package com.example.quickcash;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseCRUD {

    private FirebaseDatabase database;
    private DatabaseReference bannerIDRef = null;
    private DatabaseReference emailRef = null;
    private DatabaseReference roleRef = null;
    private String extractedEmailAddress = null;
    private String extractedRole = null;
    private String extractedBannerID = null;


    public FirebaseCRUD(FirebaseDatabase database) {
        this.database = database;
        this.initializeDatabaseRefs();
        this.initializeDatabaseRefListeners();
    }

    private void initializeDatabaseRefListeners() {
        //Do something
    }

    private void initializeDatabaseRefs() {
        //Do something
    }
}
