package com.demo.example.dailyincomeexpensemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.example.R;


public class DetailOverallFragment extends DetailFragment {
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.isExpectedRequired = false;
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((TextView) getView().findViewById(R.id.currentMonth)).setText(getString(R.string.all));
        getView().findViewById(R.id.prevMonth).setVisibility(View.GONE);
        getView().findViewById(R.id.nextMonth).setVisibility(View.GONE);
        updateUI();
    }

    @Override
    public String getMessageText() {
        return "Overall";
    }
}
