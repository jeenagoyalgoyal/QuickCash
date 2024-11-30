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
    final String CLIENT_ID ="AZbKJXg9hDT9r33XQ7otACaJz2-Fd3qerezqy_XAvRiC__600MnBzorKCAona2aYvFjsdxtLYs5OST-T";
    PayPalConfiguration payPalConfiguration;

    public PayPalPaymentProcessor(){
        payPalConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(CLIENT_ID);
    }


    public void handlePaymentConfirmation(PaymentConfirmation confirmation){

    }
}
