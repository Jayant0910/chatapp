package com.demo.example.dailyincomeexpensemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.demo.example.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DetailDailyFragment extends DetailFragment {
    Calendar endWeek;
    Calendar startWeek;

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.calendar = Calendar.getInstance();
        this.calendar.set(11, 0);
        this.calendar.clear(12);
        this.calendar.clear(13);
        this.calendar.clear(14);
        getView().findViewById(R.id.nextMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                DetailDailyFragment.this.calendar.add(6, 1);
                DetailDailyFragment.this.updateScreen();
            }
        });
        getView().findViewById(R.id.prevMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                DetailDailyFragment.this.calendar.add(6, -1);
                DetailDailyFragment.this.updateScreen();
            }
        });
        updateScreen();
    }

    
    public void updateScreen() {
        this.startWeek = (Calendar) this.calendar.clone();
        this.endWeek = (Calendar) this.calendar.clone();
        this.endWeek.add(6, 1);
        this.endWeek.add(14, -1);
        ((TextView) getView().findViewById(R.id.currentMonth)).setText(new SimpleDateFormat("dd MMM yyyy", Locale.US).format(this.startWeek.getTime()));
        this.startTime = MyUtils.sdfDatabase.format(this.startWeek.getTime());
        this.endTime = MyUtils.sdfDatabase.format(this.endWeek.getTime());
        updateUI();
    }

    @Override
    public String getMessageText() {
        return new SimpleDateFormat("dd MMM yyyy", Locale.US).format(this.startWeek.getTime());
    }
}
