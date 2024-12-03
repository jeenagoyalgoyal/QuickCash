package com.example.quickcash;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    private static final String PREF_NAME = "user_session";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "user_id";
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context){
        this.context = context;
        preferences =context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    //Create a login Session when the user is logged in
    public void createLoginSession(){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.commit();
    }

    //Method to clear all session data
    public void logoutUser(){
        editor.clear();
        editor.apply();
    }

    public Map<String, String> getUserDetails() {
        Map<String, String> user = new HashMap<>();

        user.put(KEY_USER_ID, preferences.getString(KEY_USER_ID, null));
        return user;
    }
    //Method to check if user is logged in
    public boolean checkIfLoggedIn(){
        return preferences.getBoolean(IS_LOGGED_IN, false);
    }

    // Save user ID during login
    public void createSession() {
        editor.apply();
    }

    // Retrieve user ID
    public String getUserId() {
        return preferences.getString(KEY_USER_ID, null); // Fetch using the correct key
    }


}
