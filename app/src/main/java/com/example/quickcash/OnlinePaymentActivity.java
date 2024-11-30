package com.example.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OnlinePaymentActivity extends AppCompatActivity {

    private Button paymentButton;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_online_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.onlinePaymentLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //setup UI
        setupPaymentButton();
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

    protected void paypalPayment(){
        //does paypal payment stuff
    }
}
