package com.demo.example.widget;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;


public class AlertActivity extends Activity {

    
    public Activity mActivity;


    public AlertActivity(Activity activity) {
        this.mActivity = activity;

    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public static boolean isNetworkConnection(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
