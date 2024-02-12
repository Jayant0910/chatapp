package com.demo.example.dailyincomeexpensemanager;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.NotificationCompat;

import com.demo.example.AdAdmob;
import com.demo.example.Utile.LocaleHelper;
import com.demo.example.Utile.Preferences;
import com.demo.example.passcode.PinEnterActivity;
import com.demo.example.R;

import java.util.Calendar;
import java.util.Locale;

import org.achartengine.chart.TimeChart;


public class SettingActivity extends BaseActivity {


    private Switch aSwitchNotification;
    private Switch aSwitchPassword;
    String currentLang;
    String currentLanguage = "en";
    int mHour;
    int mMinute;
    Locale myLocale;
    private TextView tvPassowrd;

    @Override

    public void onDestroy() {
        super.onDestroy();
    }

    @Override

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_settings);


        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);
        adAdmob.FullscreenAd(this);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(Html.fromHtml("<font color='#00c853'>" + getResources().getString(R.string.action_settings) + "</font>"));
        this.tvPassowrd = (TextView) findViewById(R.id.tv_password);
        this.aSwitchPassword = (Switch) findViewById(R.id.switch_password);
        this.aSwitchNotification = (Switch) findViewById(R.id.switch_notification);
        if (Preferences.getInstance().getFirstTimeLaunch()) {
            this.tvPassowrd.setText(getString(R.string.disable_password));
            this.aSwitchPassword.setChecked(true);
        } else {
            this.tvPassowrd.setText(getString(R.string.enable_password));
            this.aSwitchPassword.setChecked(false);
        }
        if (Preferences.getInstance().getNotification()) {
            this.aSwitchNotification.setChecked(true);
        } else {
            this.aSwitchNotification.setChecked(false);
        }
        this.aSwitchPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    SettingActivity.this.startActivity(new Intent(SettingActivity.this, PinEnterActivity.class));
                    SettingActivity.this.finish();
                    return;
                }
                new AlertDialog.Builder(SettingActivity.this).setTitle(SettingActivity.this.getResources().getString(R.string.app_name)).setMessage(SettingActivity.this.getString(R.string.msg_disable_password)).setCancelable(false).setPositiveButton(SettingActivity.this.getString(R.string.action_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Preferences.getInstance().setFirstTimeLaunch(false);
                        SettingActivity.this.aSwitchPassword.setChecked(false);
                        System.exit(0);
                    }
                }).show();
            }
        });
        this.aSwitchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    SettingActivity.this.setTime();
                    return;
                }
                Preferences.getInstance().setNotification(false);
                ((AlarmManager) SettingActivity.this.getSystemService(NotificationCompat.CATEGORY_ALARM)).cancel(PendingIntent.getService(SettingActivity.this, 0, new Intent(SettingActivity.this, NotifyService.class), 0));
                Toast.makeText(SettingActivity.this.getApplicationContext(), "Alarm Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        showHomeButton();
    }


    public void setTime() {
        Calendar instance = Calendar.getInstance();
        this.mHour = instance.get(11);
        this.mMinute = instance.get(12);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i2) {
                Intent intent = new Intent(SettingActivity.this, NotifyService.class);
                AlarmManager alarmManager = (AlarmManager) SettingActivity.this.getSystemService(NotificationCompat.CATEGORY_ALARM);
                PendingIntent service = PendingIntent.getService(SettingActivity.this, 0, intent, 0);
                Calendar instance2 = Calendar.getInstance();
                instance2.set(11, i);
                instance2.set(12, i2);
                instance2.set(13, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, instance2.getTimeInMillis(), TimeChart.DAY, service);
                Preferences.getInstance().setNotification(true);
                Toast.makeText(SettingActivity.this.getApplicationContext(), "Notification Added", Toast.LENGTH_SHORT).show();
            }
        }, this.mHour, this.mMinute, false).show();
    }


    @Override

    public void onResume() {
        super.onResume();
        updateUI();
    }


    public void updateUI() {
        TextView textView = (TextView) findViewById(R.id.tvSummeryLanguage);
        ((TextView) findViewById(R.id.tvSummeryCurrency)).setText(this.currentAccount.getCurrencyName());
        this.currentLanguage = LocaleHelper.getLanguage(this);
        String language = Preferences.getInstance().getLanguage();
        if (language.isEmpty()) {
            textView.setText(getString(R.string.app_language_text));
        } else {
            textView.setText(language);
        }
    }


    @Override

    public void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.onAttach(context));
    }

    public void onSelectItem(View view) {
        selectItem(Integer.parseInt(view.getTag().toString()));
    }

    private void selectItem(int i) {
        switch (i) {
            case 1:
                currencyPickerDialog();
                return;
            case 2:
                startActivity(new Intent(this, SettingCategotyActivity.class));
                return;
            case 3:
                languagePickerDialog();
                return;
            case 4:
                startActivity(new Intent(this, AccountList.class));
                return;
            case 5:
                selectUserSummaryType();
                return;
            case 6:
                shareThisApp();
                return;
            case 7:
                rateThisApp();
                return;
            default:
                return;
        }
    }

    private void languagePickerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_currency_list);
        ListView listView = (ListView) dialog.findViewById(R.id.listView1);
        listView.setAdapter((ListAdapter) new LanguagePickerAdapter(this));
        listView.setPadding(8, 8, 8, 8);
        dialog.setCancelable(true);
        dialog.setTitle(getString(R.string.select_language));
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                SettingActivity.this.setLocale(MyUtils.language[i][0], MyUtils.language[i][1]);
                dialog.dismiss();
            }
        });
    }

    public void setLocale(String str, String str2) {
        if (!str.equals(this.currentLanguage)) {
            Context locale = LocaleHelper.setLocale(this, str);
            this.myLocale = new Locale(str);
            Preferences.getInstance().setLanguage(str2);
            Resources resources = locale.getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = this.myLocale;
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
            resources.updateConfiguration(configuration, displayMetrics);
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(this.currentLang, str);
            startActivity(intent);
            System.exit(1);
            return;
        }
        Toast.makeText(this, "Language already selected!", Toast.LENGTH_SHORT).show();
    }

    @Override

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.myLocale != null) {
            configuration.locale = this.myLocale;
            Locale.setDefault(this.myLocale);
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    private void selectUserSummaryType() {
        String[] strArr = {getString(R.string.daily), getString(R.string.monthly)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(strArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SettingActivity.this.userSummarySelection(i);
            }
        });
        builder.create().show();
    }

    public void userSummarySelection(int i) {
        if (i == 0) {
            i = 1;
        } else if (i == 1) {
            i = 3;
        }
        MyUtils.setUserSummary(this, i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void currencyPickerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_currency_list);
        ListView listView = (ListView) dialog.findViewById(R.id.listView1);
        listView.setAdapter((ListAdapter) new CurrencyPickerAdapter(this));
        dialog.setCancelable(true);
        dialog.setTitle(getString(R.string.select_currency));
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                SettingActivity.this.currentAccount.setCurrency(i);
                SettingActivity.this.f60db.addUpdateAccountData(SettingActivity.this.currentAccount);
                SettingActivity.this.updateUI();
                dialog.dismiss();
            }
        });
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


    public class CurrencyPickerAdapter extends ArrayAdapter<String[]> {
        public CurrencyPickerAdapter(Context context) {
            super(context, (int) R.layout.list_item_currency, MyUtils.currencyList);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = SettingActivity.this.getLayoutInflater().inflate(R.layout.list_item_currency, viewGroup, false);
            String[] item = getItem(i);
            ((TextView) inflate.findViewById(R.id.tvCurrencyName)).setText(item[1]);
            ((TextView) inflate.findViewById(R.id.tvCurrencySymbol)).setText(item[0]);
            return inflate;
        }
    }


    public class LanguagePickerAdapter extends ArrayAdapter<String[]> {
        public LanguagePickerAdapter(Context context) {
            super(context, (int) R.layout.listview_txt, MyUtils.language);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = SettingActivity.this.getLayoutInflater().inflate(R.layout.listview_txt, viewGroup, false);
            ((TextView) inflate.findViewById(R.id.textView)).setText(getItem(i)[1]);
            return inflate;
        }
    }
}
