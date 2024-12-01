package com.example.quickcash;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcash.adapter.PaymentJobAdapter;
import com.example.quickcash.model.PaymentEmployeeModel;
import com.example.quickcash.paypal.PayPalPaymentProcessor;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class OnlinePaymentActivity extends AppCompatActivity {

    private TextView jobTitleText;
    private TextView employeeNameText;
    private TextView paymentAmountText;
    private Button paymentButton;
    private Button selectJobButton;
    private Dialog dialog;
    private ImageButton crossButton;
    private FirebaseAuth mAuth;
    private String email;
    private String userID;
    private PayPalPaymentProcessor payPalPaymentProcessor;

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
        setupTextViews();
        setupPaymentButton();
        setupSelectJobButton();
        setupDialog();
    }

    private void setCurrentUserID(){
        this.mAuth = FirebaseAuth.getInstance();
        this.email = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
        this.userID = sanitizeEmail(email);
    }

    private void setupTextViews() {
        this.jobTitleText = findViewById(R.id.paymentWindowJobTitleText);
        this.employeeNameText = findViewById(R.id.paymentWindowEmployeeNameText);
        this.paymentAmountText = findViewById(R.id.paymentWindowAmountText);
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
        dialog.setContentView(R.layout.payment_jobs_select_dialog);
        crossButton = dialog.findViewById(R.id.crossButton);
        crossButton.setOnClickListener(v -> dialog.dismiss());
    }

    private void launchJobSelectDialog() {
        // Initialize sample data
        List<PaymentEmployeeModel> employeeList = new ArrayList<>();
        employeeList.add(new PaymentEmployeeModel("Software Developer", "Alice", 5000));
        employeeList.add(new PaymentEmployeeModel("UI/UX Designer", "Bob", 4500));
        employeeList.add(new PaymentEmployeeModel("Project Manager", "Charlie", 7000));

        // Set up RecyclerView in dialog
        RecyclerView recyclerView = dialog.findViewById(R.id.paymentJobsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set the adapter with callback
        PaymentJobAdapter adapter = new PaymentJobAdapter(employeeList, selectedItem -> {
            dialog.dismiss(); // Close the dialog
            handleSelectedItem(selectedItem); // Handle the selected item in parent activity
        });

        recyclerView.setAdapter(adapter);
        dialog.show(); // Show the dialog
    }

    private void handleSelectedItem(PaymentEmployeeModel selectedItem) {
        // Update the UI or perform actions based on the selected item
        this.jobTitleText.setText(selectedItem.getJobTitle());
        this.employeeNameText.setText(selectedItem.getEmployeeName());
        this.paymentAmountText.setText(String.valueOf(selectedItem.getPaymentAmount()));

        Toast.makeText(this, "Selected: " + selectedItem.getJobTitle(), Toast.LENGTH_SHORT).show();
    }


    protected void paypalPayment(){
        //does paypal payment stuff
        String jobTitle = (String) this.jobTitleText.getText();
        String employeeName = (String) this.employeeNameText.getText();
        int paymentAmount = Integer.parseInt((String) this.paymentAmountText.getText());
        //payPalPaymentProcessor.handlePayment(employeeName, paymentAmount);
    }

    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }
}
