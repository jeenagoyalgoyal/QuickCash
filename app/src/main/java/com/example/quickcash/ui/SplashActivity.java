package com.example.quickcash.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quickcash.R;

/**
 * The {@code SplashActivity} class represents the splash screen of the application.
 * It initializes the UI and ensures that the app uses edge-to-edge display modes.
 */
public class SplashActivity extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     * Sets up the splash screen layout, enables edge-to-edge display,
     * and configures padding to account for system bars (status and navigation bars).
     *
     * @param savedInstanceState a {@link Bundle} object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}