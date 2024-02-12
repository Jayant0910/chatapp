package com.demo.example.dailyincomeexpensemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;


public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    protected String getCurrencySymbol() {
        return getBaseActivity().getCurrencySymbol();
    }

    private BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
