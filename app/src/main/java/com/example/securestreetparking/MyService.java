package com.example.securestreetparking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import androidx.core.app.NotificationCompat;


public class MyService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        String notificationTitle = null, notificationBody = null;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        sendNotification(notificationTitle, notificationBody);
    }


    private void sendNotification(String notificationTitle, String notificationBody) {
        Log.d("NOOtify", "Message Notification Body: "+notificationBody+"--"+notificationTitle);
        {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "ChannelName";
                String description = "ChannelDescription";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("1", name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }

        Intent intent = new Intent(this, DataActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notificationBuilder =  new NotificationCompat.Builder(this,"1")
//                .setAutoCancel(true)   //Automatically delete the notification
                .setSmallIcon(R.drawable.main_icon) //Notification icon
                .setContentIntent(pendingIntent)
                .setContentTitle(notificationTitle)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setContentText(notificationBody);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}