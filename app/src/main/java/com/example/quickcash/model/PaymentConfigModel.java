package com.example.quickcash.model;

public class PaymentConfigModel {
    private String provider;
    private String accountEmail;
    private String currency;
    private boolean autoPaymentEnabled;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isAutoPaymentEnabled() {
        return autoPaymentEnabled;
    }

    public void setAutoPaymentEnabled(boolean autoPaymentEnabled) {
        this.autoPaymentEnabled = autoPaymentEnabled;
    }
}
