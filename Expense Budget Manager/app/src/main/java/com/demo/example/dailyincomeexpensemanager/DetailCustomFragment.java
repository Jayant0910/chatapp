package com.demo.example.dailyincomeexpensemanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.demo.example.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DetailCustomFragment extends DetailFragment {
    Calendar endDate;
    Calendar startDate;
    private TextView tvEndDate;
    private TextView tvStartDate;

    
    public class MyDatePickerListner implements DatePickerDialog.OnDateSetListener {
        private Calendar mCalendar;

        public MyDatePickerListner(Calendar calendar) {
            this.mCalendar = calendar;
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            this.mCalendar.set(i, i2, i3);
            DetailCustomFragment.this.updateScreen();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.calendar = Calendar.getInstance();
        this.calendar.set(11, 0);
        this.calendar.clear(12);
        this.calendar.clear(13);
        this.calendar.clear(14);
        this.startDate = (Calendar) this.calendar.clone();
        this.startDate.add(6, -17);
        this.endDate = (Calendar) this.calendar.clone();
        this.endDate.add(14, -1);
        getView().findViewById(R.id.buttonlayout).setVisibility(View.GONE);
        getView().findViewById(R.id.llDateBar).setVisibility(View.VISIBLE);
        this.tvStartDate = (TextView) getView().findViewById(R.id.tvStartDate);
        this.tvEndDate = (TextView) getView().findViewById(R.id.tvEndDate);
        this.tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                DetailCustomFragment.this.updateDateByUser(DetailCustomFragment.this.startDate);
            }
        });
        this.tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                DetailCustomFragment.this.updateDateByUser(DetailCustomFragment.this.endDate);
            }
        });
        updateScreen();
    }

    
    public void updateDateByUser(Calendar calendar) {
        new DatePickerDialog(getActivity(), new MyDatePickerListner(calendar), calendar.get(1), calendar.get(2), calendar.get(5)).show();
    }

    
    public void updateScreen() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String string = getString(R.string.start_date, simpleDateFormat.format(this.startDate.getTime()));
        String string2 = getString(R.string.end_date, simpleDateFormat.format(this.endDate.getTime()));
        this.tvStartDate.setText(string);
        this.tvEndDate.setText(string2);
        this.startTime = MyUtils.sdfDatabase.format(this.startDate.getTime());
        this.endTime = MyUtils.sdfDatabase.format(this.endDate.getTime());
        updateUI();
    }

    @Override
    public String getMessageText() {
        return new SimpleDateFormat("dd MMM yyyy", Locale.US).format(this.startDate.getTime()) + " to " + new SimpleDateFormat("dd MMM yyyy", Locale.US).format(this.endDate.getTime());
    }
}
