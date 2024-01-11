package com.example.assignmate;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.*;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Channel_id");
        builder.setSmallIcon(R.drawable.google_play_books);


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(this,1, new Intent[]{intent},PendingIntent.FLAG_IMMUTABLE);
        builder.setContentTitle(remoteMessage.getNotification().getTitle());
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setContentIntent(pendingIntent);

       NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "Channel_id";
        NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable title",NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        builder.setChannelId(channelId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!notificationManager.isNotificationPolicyAccessGranted()) {
                Intent intent2 = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
            }
        }


        notificationManager.notify(100,builder.build());

    }


}
