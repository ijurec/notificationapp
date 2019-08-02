package com.task.notificationapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.task.notificationapp.MainActivity;
import com.task.notificationapp.R;

public class NotificationUtil {

    public static final String NOTIFICATION_NUMBER = "notification_number";
    private static final int FRAGMENT_NOTIFICATION_ID = 2005;
    private static  String CHANNEL_ID = "channel_fragment";

    public static void notifyUser(Context context, int notificationNumber) {
        NotificationUtil.createNotificationChannel(context, notificationNumber);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(NOTIFICATION_NUMBER, notificationNumber);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis()
                , intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Resources resources = context.getResources();
        int largeArtResourceId = R.drawable.rounded_button;

        Bitmap largeIcon = BitmapFactory.decodeResource(
                resources,
                largeArtResourceId);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                CHANNEL_ID + notificationNumber)
            .setColor(ContextCompat.getColor(context,R.color.colorAccent))
            .setSmallIcon(R.drawable.blue)
            .setLargeIcon(largeIcon)
            .setContentTitle(context.getString(R.string.notification_title_label))
            .setContentText(context.getString(R.string.notification_text_label) + notificationNumber)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(FRAGMENT_NOTIFICATION_ID + notificationNumber, builder.build());
    }

    private static void createNotificationChannel(Context context, int notificationNumber) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID + notificationNumber, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
