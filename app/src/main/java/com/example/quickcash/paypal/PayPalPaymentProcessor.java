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


    public void handlePaymentConfirmation(PaymentConfirmation confirmation){

    }
}
