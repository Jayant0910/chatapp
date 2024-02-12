package com.demo.example.dailyincomeexpensemanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.demo.example.R;


public class NotifyService extends Service {
    int mId = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((NotificationManager) getSystemService("notification")).notify(this.mId, new Notification.Builder(this).setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, AddExpenceIncomeActivity.class), 134217728)).setSmallIcon(R.drawable.icon200).setContentTitle(getResources().getString(R.string.app_name)).setContentText("Add your Expense or Income Every day").setDefaults(-1).setAutoCancel(true).setNumber(1).build());
    }
}
