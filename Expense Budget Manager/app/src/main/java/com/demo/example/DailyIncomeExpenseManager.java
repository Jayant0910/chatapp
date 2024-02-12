package com.demo.example;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.demo.example.Utile.LocaleHelper;


public class DailyIncomeExpenseManager extends Application {
    public static DailyIncomeExpenseManager INSTANCE;
    static Context mContext;

    public static DailyIncomeExpenseManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = (DailyIncomeExpenseManager) mContext;
        }
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.onAttach(context, "en"));
        MultiDex.install(this);
    }
}
