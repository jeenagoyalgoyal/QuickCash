package com.example.quickcash.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "user_session";
    private static final String IS_LOGGED_IN = "isLoggedIn";
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

    //Method to check if user is logged in
    public boolean checkIfLoggedIn(){
        return preferences.getBoolean(IS_LOGGED_IN, false);
    }



}
