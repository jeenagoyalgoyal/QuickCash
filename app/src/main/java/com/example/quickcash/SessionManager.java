package com.example.quickcash;

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


}
