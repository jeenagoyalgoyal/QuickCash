package com.example.quickcash;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SessionTest {

    public SessionManager sessionManager;
    public Context context;
    public SharedPreferences sharedPreferences;

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        sessionManager = new SessionManager(context);
        sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
    }

    @Test
    public void checkLoginSessionCreated(){
        sessionManager.createLoginSession();
        assertTrue(sharedPreferences.getBoolean("isLoggedIn", false));
    }

    @Test
    public void checkIfLoggedIn(){
        sessionManager.createLoginSession();
        assertTrue(sessionManager.checkIfLoggedIn());
    }

    @Test
    public void checkIfLoggedOut(){
        sessionManager.createLoginSession();
        sessionManager.logoutUser();
        assertFalse(sessionManager.checkIfLoggedIn());
    }
}
