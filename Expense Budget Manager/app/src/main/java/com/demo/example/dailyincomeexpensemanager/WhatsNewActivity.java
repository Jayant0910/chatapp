package com.demo.example.dailyincomeexpensemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.demo.example.R;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;


public class WhatsNewActivity extends IntroActivity {
    private static boolean isPermission = false;

    
    MyDatabaseHandler f90db;

    public static void verifyStoragePermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 1);
        }
    }

    @Override
    @SuppressLint({"WrongThread"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setFullscreen(true);
        setButtonBackVisible(false);
        addSlide(new SimpleSlide.Builder().image(R.drawable.screen1).background(R.color.background).backgroundDark(R.color.colorPrimaryDark).scrollable(false).build());
        addSlide(new SimpleSlide.Builder().image(R.drawable.screen2).background(R.color.background).backgroundDark(R.color.colorPrimaryDark).scrollable(false).build());
        addSlide(new SimpleSlide.Builder().image(R.drawable.screen3).background(R.color.background).backgroundDark(R.color.colorPrimaryDark).scrollable(false).build());
        addSlide(new SimpleSlide.Builder().image(R.drawable.screen4).background(R.color.background).backgroundDark(R.color.colorPrimaryDark).scrollable(false).build());
        addSlide(new SimpleSlide.Builder().image(R.drawable.screen5).background(R.color.background).backgroundDark(R.color.colorPrimaryDark).scrollable(false).build());
        if (ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            addSlide(new SimpleSlide.Builder().background(R.color.background).scrollable(false).title("Storage and SMS Permission").description("In order for the app to work correctly, Please allow the Storage write and SMS read permissions.").image(R.drawable.storage_sms_permission).buttonCtaLabel("Grant Permissions").backgroundDark(R.color.colorPrimaryDark).buttonCtaClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WhatsNewActivity.verifyStoragePermissions(WhatsNewActivity.this);
                }
            }).build());
        }
        addSlide(new SimpleSlide.Builder().title("Setup Completed!").description("Start tracking your income and daily spending as the first step towards using a monthly budget and finance planning.").background(R.color.colorPrimary).image(R.drawable.icon200).backgroundDark(R.color.colorPrimaryDark).scrollable(false).build());
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                int size = WhatsNewActivity.this.getSlides().size();
                WhatsNewActivity.this.setNavigation(true, true);
                if (i == size - 1) {
                    MyUtils.setNotNeededWhatNew(WhatsNewActivity.this);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 4113 && checkIfBatteryOptimizationIgnored()) {
            setNavigation(true, false);
            nextSlide();
        }
    }

    private boolean checkIfBatteryOptimizationIgnored() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return ((PowerManager) getSystemService("power")).isIgnoringBatteryOptimizations(getPackageName());
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

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1) {
            if (iArr[0] != 0) {
                AlertDialog create = new AlertDialog.Builder(this).setPositiveButton("Let's Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        WhatsNewActivity.verifyStoragePermissions(WhatsNewActivity.this);
                    }
                }).create();
                create.setTitle("Notice!");
                create.setMessage("Allowing storage permissions is crucial for the app to work. Please grant the permissions.");
                create.show();
                return;
            }
            setNavigation(true, false);
            nextSlide();
        }
    }
}
