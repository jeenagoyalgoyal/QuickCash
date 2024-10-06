package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RoleActivity extends AppCompatActivity {

    private TextView welcomeText;
    private String currentRole = "employee";
    private Button roleSwitch;
    private UseRole useRole;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.role_switch);

        Intent intent = getIntent();
        id = intent.getStringExtra("userID");

        useRole = UseRole.getInstance();
        welcomeText = findViewById(R.id.welcomeText);
        roleSwitch = findViewById(R.id.roleSwitch);

        update();

        roleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useRole.switchRole(id);
                update();
            }
        });

    }

    private void update(){
        String role = useRole.getCurrentRole();
        if(role.equals("employee")){
            welcomeText.setText("Welcome, employee");
            roleSwitch.setText("Switch to employer");
        }
        else{
            welcomeText.setText("Welcome, employer");
            roleSwitch.setText("Switch to employee");
        }
    }
}
