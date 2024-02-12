package com.demo.example.Utile;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.demo.example.dailyincomeexpensemanager.SettingCategotyActivity;
import com.demo.example.R;


public class Notification {
    public static void showNotification(Context context, String str) {
        PendingIntent activity = PendingIntent.getActivity(context, 0, new Intent(context, SettingCategotyActivity.class), 134217728);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(context.getResources().getString(R.string.app_name));
        builder.setContentText(str);
        builder.setContentIntent(activity);
        builder.setSmallIcon(R.drawable.icon200);
        ((NotificationManager) context.getSystemService("notification")).notify(0, builder.build());
    }
}
