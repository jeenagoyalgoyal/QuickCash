package com.example.quickcash.paypal;

import android.os.Bundle;
import android.widget.TextView;

import com.example.quickcash.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class PayPalPaymentProcessor {
    public TextView paymentStatusTV;
    final String CLIENT_ID ="AQ_sPzHwhMPN45cpAi9qL0dSg_l-8ikS9DaZFsChJj0qR4iVeNV3YxvXQcPbRDp3mRXnqX8osc5st0RZ"; //note: QuickCash app ID, not default app ID
    PayPalConfiguration payPalConfiguration;

    public PayPalPaymentProcessor(){
        payPalConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(CLIENT_ID);
    }


    public void handlePaymentConfirmation(PaymentConfirmation confirmation){

    }
}
