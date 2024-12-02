package com.example.quickcash.FirebaseMessaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.quickcash.R;
import com.example.quickcash.UseRole;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessaging";
    private static final String PREFS_NAME = "QuickCashPrefs";
    private static final String TOKEN_KEY = "fcmToken";

    // Called when a new token is generated
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("FCM Token", "Token: " + token);
        saveTokenToPreferences(token);
        // Send the token to your server if needed
    }

    private void saveTokenToPreferences(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear old token if any
        editor.putString(TOKEN_KEY, token);
        editor.apply();
        Log.d(TAG, "Token saved to preferences");
    }

    public static String getTokenFromPreferences(SharedPreferences sharedPreferences) {
        String token = sharedPreferences.getString(TOKEN_KEY, null);
        Log.d(TAG, "Token retrieved from preferences: " + token);
        return token;
    }

    // Called when a message is received
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("message received","received"+remoteMessage);

        // Handle FCM messages here
        if (remoteMessage.getNotification() != null) {
            // Get the notification data
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Log.d("NotificationReceived", "Title: " + title + ", Body: " + body);

            // Display the notification
            showNotification(title, body);
        }
    }

    private void showNotification(String title, String body) {
        // Create an intent to open when the notification is clicked
        Intent intent = new Intent(this, ViewJobNotifications.class); // Replace with your activity
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                10,
                intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        // Create a notification
        String channelId = "payment_notifications";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_foreground) // Replace with your icon
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        builder.setContentIntent(pendingIntent);

        // Get the notification manager
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int id = (int) System.currentTimeMillis();
        if (notificationManager == null) {
            Log.e("NotificationError", "NotificationManager is null.");
            return;
        }

        // Create the notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Payment Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Show the notification
        notificationManager.notify(id, builder.build());
    }
}