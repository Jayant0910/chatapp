package com.demo.example.dailyincomeexpensemanager;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.demo.example.R;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;


public class TakeATourActivity extends IntroActivity {
    int[] image = {R.drawable.screen1, R.drawable.screen2, R.drawable.screen3, R.drawable.screen4, R.drawable.screen5};

    @Override
    @SuppressLint({"ResourceType"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setFullscreen(true);
        setButtonBackVisible(false);
        for (int i = 0; i < this.image.length; i++) {
            addSlide(new SimpleSlide.Builder().image(this.image[i]).background(R.color.background).backgroundDark(R.color.colorPrimaryDark).scrollable(false).build());
        }
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i2) {
            }

            @Override
            public void onPageScrolled(int i2, float f, int i3) {
            }

            @Override
            public void onPageSelected(int i2) {
                TakeATourActivity.this.setNavigation(true, false);
            }
        });
    }

    
    public void setNavigation(final boolean z, final boolean z2) {
        setNavigationPolicy(new NavigationPolicy() {
            @Override
            public boolean canGoForward(int i) {
                return z;
            }

            @Override
            public boolean canGoBackward(int i) {
                return z2;
            }
        });
    }
}
