package com.demo.example.dailyincomeexpensemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.demo.example.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DetailWeeklyFragment extends DetailFragment {
    Calendar endWeek;
    Calendar startWeek;

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.calendar = Calendar.getInstance();
        this.calendar.set(11, 0);
        this.calendar.clear(12);
        this.calendar.clear(13);
        this.calendar.clear(14);
        this.calendar.set(7, this.calendar.getFirstDayOfWeek());
        getView().findViewById(R.id.nextMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                DetailWeeklyFragment.this.calendar.add(3, 1);
                DetailWeeklyFragment.this.updateScreen();
            }
        });
        getView().findViewById(R.id.prevMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                DetailWeeklyFragment.this.calendar.add(3, -1);
                DetailWeeklyFragment.this.updateScreen();
            }
        });
        updateScreen();
    }

    
    public void updateScreen() {
        this.startWeek = (Calendar) this.calendar.clone();
        this.endWeek = (Calendar) this.calendar.clone();
        this.endWeek.add(3, 1);
        this.endWeek.add(14, -1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        ((TextView) getView().findViewById(R.id.currentMonth)).setText(simpleDateFormat.format(this.startWeek.getTime()) + " to " + simpleDateFormat.format(this.endWeek.getTime()));
        this.startTime = MyUtils.sdfDatabase.format(this.startWeek.getTime());
        this.endTime = MyUtils.sdfDatabase.format(this.endWeek.getTime());
        updateUI();
    }

    @Override
    public String getMessageText() {
        return new SimpleDateFormat("dd MMM yyyy", Locale.US).format(this.startWeek.getTime()) + " to " + new SimpleDateFormat("dd MMM yyyy", Locale.US).format(this.endWeek.getTime());
    }
}
