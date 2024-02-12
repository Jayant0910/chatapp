package com.demo.example.dailyincomeexpensemanager;

import android.os.Bundle;

import android.text.Html;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.demo.example.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class AnalyticsFragmentActivity extends BaseActivity {

    
    private TabLayout tabLayout;
    private ViewPager viewPager;

    
    @Override
    public void changeUserAccount() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_analytics);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(Html.fromHtml("<font color='#00c853'>" + getResources().getString(R.string.app_name) + "</font>"));
        getSupportActionBar().setElevation(1.0f);

        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(this.viewPager);
        this.tabLayout.setupWithViewPager(this.viewPager);
        showHomeButton();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AnalyticsExpenseFragment(), getString(R.string.expense));
        viewPagerAdapter.addFragment(new AnalyticsIncomeFragment(), getString(R.string.income));
        viewPager.setAdapter(viewPagerAdapter);
    }

    
    @Override
    public void onResume() {
        super.onResume();
    }

    
    
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            return this.mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String str) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(str);
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return this.mFragmentTitleList.get(i);
        }
    }

    
    @Override
    public void onStart() {
        super.onStart();
    }

    
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
