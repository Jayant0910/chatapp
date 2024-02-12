package com.demo.example.dailyincomeexpensemanager;

import android.os.Bundle;

import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.demo.example.AdAdmob;
import com.demo.example.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class DetailFragmentActivity extends BaseActivity {

    
    private View lastView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private void updateUI() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_details);


        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);
        adAdmob.FullscreenAd(this);

        getSupportActionBar().setElevation(1.0f);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(Html.fromHtml("<font color='#00c853'>" + getResources().getString(R.string.app_name) + "</font>"));

        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(this.viewPager);
        this.tabLayout.setupWithViewPager(this.viewPager);
        showHomeButton();
        updateUI();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new DetailOverallFragment(), getString(R.string.all));
        viewPagerAdapter.addFragment(new DetailDailyFragment(), getString(R.string.daily));
        viewPagerAdapter.addFragment(new DetailWeeklyFragment(), getString(R.string.weekly));
        viewPagerAdapter.addFragment(new DetailMonthlyFragment(), getString(R.string.monthly));
        viewPagerAdapter.addFragment(new DetailYearlyFragment(), getString(R.string.yearly));
        viewPagerAdapter.addFragment(new DetailCustomFragment(), getString(R.string.custom));
        viewPager.setAdapter(viewPagerAdapter);
    }



    
    @Override
    public void onResume() {
        super.onResume();
    }

    
    @Override
    public void changeUserAccount() {
        updateUI();
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
        finish();
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
}
