package com.example.quickcash.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.quickcash.ui.activities.EmployeeHomepageActivity;
import com.example.quickcash.R;
import com.example.quickcash.databinding.ActivityMainBinding;
import com.example.quickcash.ui.models.UseRole;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Switching Role
        // Switching Role based on current user role
        if (id == R.id.switch_role) {
            UseRole useRole = UseRole.getInstance();
            int userid = 123;
            Intent intent;

            // Check the current role and set the destination activity
            if (useRole.getCurrentRole().equals("employer")) {
                intent = new Intent(MainActivity.this, EmployeeHomepageActivity.class);
                Toast.makeText(this, "Switched to Employee role", Toast.LENGTH_SHORT).show();
            }

            else {
                intent = new Intent(MainActivity.this, EmployerHomepageActivity.class);
                Toast.makeText(this, "Switched to Employer role", Toast.LENGTH_SHORT).show();
            }

            intent.putExtra("userID", userid);
            startActivity(intent);
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        //Navigating to Profile Screen
        if(id == R.id.action_profile) {
            Intent intent = new Intent(MainActivity.this, Profile.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}