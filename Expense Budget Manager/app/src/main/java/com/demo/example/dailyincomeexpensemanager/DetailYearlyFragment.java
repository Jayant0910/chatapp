package com.demo.example.dailyincomeexpensemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.demo.example.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DetailYearlyFragment extends DetailFragment {
    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.calendar = Calendar.getInstance();
        this.calendar.set(2, 0);
        this.calendar.set(5, 1);
        this.calendar.set(11, 0);
        this.calendar.clear(12);
        this.calendar.clear(13);
        this.calendar.clear(14);
        getView().findViewById(R.id.nextMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                DetailYearlyFragment.this.calendar.add(1, 1);
                DetailYearlyFragment.this.updateScreen();
            }
        });
        getView().findViewById(R.id.prevMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                DetailYearlyFragment.this.calendar.add(1, -1);
                DetailYearlyFragment.this.updateScreen();
            }
        });
        updateScreen();
    }

    
    public void updateScreen() {
        ((TextView) getView().findViewById(R.id.currentMonth)).setText(new SimpleDateFormat("yyyy", Locale.US).format(this.calendar.getTime()));
        Calendar calendar = (Calendar) this.calendar.clone();
        calendar.add(1, 1);
        this.startTime = MyUtils.sdfDatabase.format(this.calendar.getTime());
        this.endTime = MyUtils.sdfDatabase.format(calendar.getTime());
        updateUI();
    }

    @Override
    public String getMessageText() {
        return new SimpleDateFormat("yyyy", Locale.US).format(this.calendar.getTime());
    }
}
