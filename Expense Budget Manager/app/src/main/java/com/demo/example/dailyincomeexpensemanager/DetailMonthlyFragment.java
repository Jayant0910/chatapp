package com.demo.example.dailyincomeexpensemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.demo.example.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DetailMonthlyFragment extends DetailFragment {
    Calendar endMonth;

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.calendar = Calendar.getInstance();
        this.calendar.set(5, 1);
        this.calendar.set(11, 0);
        this.calendar.clear(12);
        this.calendar.clear(13);
        this.calendar.clear(14);
        getView().findViewById(R.id.nextMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                DetailMonthlyFragment.this.calendar.add(2, 1);
                DetailMonthlyFragment.this.updateScreen();
            }
        });
        getView().findViewById(R.id.prevMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                DetailMonthlyFragment.this.calendar.add(2, -1);
                DetailMonthlyFragment.this.updateScreen();
            }
        });
        updateScreen();
    }

    
    public void updateScreen() {
        ((TextView) getView().findViewById(R.id.currentMonth)).setText(new SimpleDateFormat("MMMM yyyy", Locale.US).format(this.calendar.getTime()));
        this.endMonth = (Calendar) this.calendar.clone();
        this.endMonth.add(2, 1);
        this.startTime = MyUtils.sdfDatabase.format(this.calendar.getTime());
        this.endTime = MyUtils.sdfDatabase.format(this.endMonth.getTime());
        updateUI();
    }

    @Override
    public String getMessageText() {
        return new SimpleDateFormat("MMMM yyyy", Locale.US).format(this.calendar.getTime());
    }
}
