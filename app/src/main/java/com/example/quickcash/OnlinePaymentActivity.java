package com.example.quickcash;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class OnlinePaymentActivity extends AppCompatActivity {

    private Button paymentButton;
    private Button selectJobButton;
    private Dialog dialog;
    private ImageButton crossButton;
    private FirebaseAuth mAuth;
    private String email;
    private String userID;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_online_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.onlinePaymentLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setCurrentUserID();
        //setup UI
        setupPaymentButton();
        setupSelectJobButton();
        setupDialog();
    }
    
    private void setCurrentUserID(){
        this.mAuth = FirebaseAuth.getInstance();
        this.email = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
        this.userID = sanitizeEmail(email);
    }

    private void setupSelectJobButton() {
        selectJobButton = findViewById(R.id.jobSelectButton);
        selectJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchJobSelectDialog();
            }
        });
    }

    protected void setupPaymentButton(){
        paymentButton = findViewById(R.id.paymentButton);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paypalPayment();
            }
        });
    }

    private void setupDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.preferred_employer_jobs_dialog_box);
        crossButton = dialog.findViewById(R.id.crossButton);
        crossButton.setOnClickListener(v -> {
            dialog.cancel();
        });
    }

    private void launchJobSelectDialog() {
        dialog.show();
    }
    
    
    protected void paypalPayment(){
        //does paypal payment stuff
    }

    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }
}
