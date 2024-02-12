package com.demo.example.dailyincomeexpensemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.demo.example.dailyincomeexpensemanager.bean.RecurringBean;
import java.text.ParseException;
import java.util.Calendar;


public class AlarmReceiver extends BroadcastReceiver {

    
    MyDatabaseHandler f56db;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.f56db = new MyDatabaseHandler(context);
        for (RecurringBean recurringBean : this.f56db.getTodayRecurringData()) {
            this.f56db.addUpdateManagerData(recurringBean.convertToDataBean());
            updateRecurringDataForNext(recurringBean);
        }
    }

    private void updateRecurringDataForNext(RecurringBean recurringBean) {
        Calendar instance = Calendar.getInstance();
        try {
            instance.setTime(MyUtils.sdfDatabase.parse(recurringBean.getRecurringLastdate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        recurringBean.setRecurringLastdate(MyUtils.sdfDatabase.format(MyUtils.getNextUpdateCalender(Integer.parseInt(recurringBean.getRecurringType()), instance, Integer.parseInt(recurringBean.getRecurringDate())).getTime()));
        this.f56db.addUpdateRecurringData(recurringBean);
    }
}
