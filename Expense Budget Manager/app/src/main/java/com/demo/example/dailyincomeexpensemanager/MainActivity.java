package com.demo.example.dailyincomeexpensemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.example.Utile.Preferences;
import com.demo.example.dailyincomeexpensemanager.bean.AccountBean;
import com.demo.example.passcode.LoginActivity;
import com.demo.example.passcode.PinEnterActivity;
import com.demo.example.R;
import com.github.mikephil.charting.utils.Utils;


import java.util.Calendar;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {


    private List<AccountBean> accounts;
    private Animation animGrowFromBottom;
    private Animation animGrowFromMiddle;
    private Animation animGrowFromTop;
    protected AccountBean currentAccount;
    private Animation growFromMiddleHorizontaly;
    private LinearLayout layoutAnalytics;
    private LinearLayout layoutDetails;
    private LinearLayout layoutExpense;
    private LinearLayout layoutIncome;
    private LinearLayout layoutSettings;
    private Activity mActivity;
    View parent;
    RelativeLayout relativeLayout;
    RelativeLayout relativeLayout2;
    private Animation slideRight;



    @Override

    public void onDestroy() {
        super.onDestroy();
    }

    @Override

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        this.parent = findViewById(R.id.parent_lay);
        this.mActivity = this;
        this.accounts = this.f60db.getAccountList();
        this.currentAccount = new AccountBean();
        if (Preferences.getInstance().getFirstTimeLaunch()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (this.accounts.size() == 0) {
            startActivity(new Intent(this, AddEditAccount.class));
        } else if (!Preferences.getInstance().getLoginStatus()) {
            Preferences.getInstance().setLoginStatus(true);
            new AlertDialog.Builder(this).setTitle(Html.fromHtml("<font color='#000000'>Daily Income Expense Manager</font>")).setMessage("Do you want to enable password?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, PinEnterActivity.class));
                    System.exit(0);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        }
        if (MyUtils.getWhatNewRequired(this)) {
            startActivity(new Intent(this, WhatsNewActivity.class));
        }
        this.accounts.size();
        this.layoutExpense = (LinearLayout) findViewById(R.id.layout_expense);
        this.layoutIncome = (LinearLayout) findViewById(R.id.layout_income);
        this.layoutDetails = (LinearLayout) findViewById(R.id.layout_detail);
        this.layoutSettings = (LinearLayout) findViewById(R.id.layout_settings);
        this.layoutAnalytics = (LinearLayout) findViewById(R.id.layout_analytics);
        animateMylayouts();
        this.layoutExpense.setOnClickListener(this);
        this.layoutAnalytics.setOnClickListener(this);
        this.layoutDetails.setOnClickListener(this);
        this.layoutIncome.setOnClickListener(this);
        this.layoutSettings.setOnClickListener(this);
        IntializeMyDataBaseWithDefaultValues();
    }


    private void updateAmountTextViews() {
        int userSummary = MyUtils.getUserSummary(this);
        Calendar instance = Calendar.getInstance();
        instance.set(11, 0);
        instance.clear(12);
        instance.clear(13);
        instance.clear(14);
        if (userSummary == 1) {
            setSummaryType("Daily");
            Calendar calendar = (Calendar) instance.clone();
            Calendar calendar2 = (Calendar) calendar.clone();
            calendar2.add(6, 1);
            calendar2.add(14, -1);
            setDetails(calendar, calendar2);
        } else if (userSummary == 2) {
            setSummaryType("Weekly");
            setDetails(getWeekStart(instance), getWeekEnd(instance));
        } else if (userSummary == 3) {
            setSummaryType("Monthly");
            setDetails(getMonthStart(instance), getMonthEnd(instance));
        }
    }

    private void setSummaryType(String str) {
        ((TextView) findViewById(R.id.tvIncomeType)).setText(str);
        ((TextView) findViewById(R.id.tvExpenseType)).setText(str);
    }

    private Calendar getMonthEnd(Calendar calendar) {
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.add(2, 1);
        calendar2.add(14, -1);
        return calendar2;
    }

    private Calendar getMonthStart(Calendar calendar) {
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.set(5, 1);
        return calendar2;
    }

    private void setDetails(Calendar calendar, Calendar calendar2) {
        double d;
        String format = MyUtils.sdfDatabase.format(calendar.getTime());
        String format2 = MyUtils.sdfDatabase.format(calendar2.getTime());
        String totalAmountByTime = this.f60db.getTotalAmountByTime(2, format, format2);
        String totalAmountByTime2 = this.f60db.getTotalAmountByTime(1, format, format2);
        String replaceAll = totalAmountByTime.replaceAll(",", "");
        String replaceAll2 = totalAmountByTime2.replaceAll(",", "");
        double d2 = Utils.DOUBLE_EPSILON;
        try {
            double RoundOff = MyUtils.RoundOff(Double.parseDouble(replaceAll));
            d = MyUtils.RoundOff(Double.parseDouble(replaceAll2));
            d2 = RoundOff;
        } catch (NumberFormatException unused) {
            d = 0.0d;
        }
        setTextValue(R.id.tvIncome, "%.2f", d2);
        setTextValue(R.id.tvExpense, "%.2f", d);
        if (d2 >= d) {
            setTextValue(R.id.tvBalance, "%.2f", d2 - d, R.color.detail_income);
        } else {
            setTextValue(R.id.tvBalance, "%.2f", d - d2, R.color.detail_expense);
        }
    }

    private Calendar getWeekEnd(Calendar calendar) {
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.add(3, 1);
        calendar2.add(14, -1);
        return calendar2;
    }

    private Calendar getWeekStart(Calendar calendar) {
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.set(7, calendar.getFirstDayOfWeek());
        return calendar2;
    }

    private void setTextValue(int i, String str, double d) {
        setTextValue(i, str, d, 0);
    }

    private void setTextValue(int i, String str, double d, int i2) {
        TextView textView = (TextView) findViewById(i);
        if (getCurrencySymbol().equals("¢") || getCurrencySymbol().equals("₣") || getCurrencySymbol().equals("₧") || getCurrencySymbol().equals("﷼") || getCurrencySymbol().equals("₨")) {
            textView.setText(String.format(str, Double.valueOf(d)) + getCurrencySymbol());
            if (i2 != 0) {
                textView.setTextColor(getResources().getColor(i2));
                return;
            }
            return;
        }
        textView.setText(getCurrencySymbol() + String.format(str, Double.valueOf(d)));
        if (i2 != 0) {
            textView.setTextColor(getResources().getColor(i2));
        }
    }

    private void IntializeMyDataBaseWithDefaultValues() {
        if (this.f60db.getCount(MyDatabaseHandler.Category.TABLE_NAME) == 0) {
            addDefaultsCategoriesToAccount("1");
        }
    }


    @Override

    public void onResume() {
        super.onResume();
        updateAmountTextViews();
    }


    @Override
    public void changeUserAccount() {
        restartActivity(this);
        updateAmountTextViews();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void animateMylayouts() {
        this.animGrowFromMiddle = AnimationUtils.loadAnimation(this, R.anim.grow_from_middle);
        this.growFromMiddleHorizontaly = AnimationUtils.loadAnimation(this, R.anim.grow_from_middle_horizontaly);
        this.animGrowFromBottom = AnimationUtils.loadAnimation(this, R.anim.grow_from_bottom);
        this.animGrowFromTop = AnimationUtils.loadAnimation(this, R.anim.grow_from_top);
        this.slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_lef_to_right);
        this.layoutExpense.startAnimation(this.slideRight);
        this.layoutAnalytics.startAnimation(this.animGrowFromBottom);
        this.layoutDetails.startAnimation(this.animGrowFromMiddle);
        this.layoutIncome.startAnimation(this.animGrowFromTop);
        this.layoutSettings.startAnimation(this.growFromMiddleHorizontaly);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_analytics:
                intent = new Intent(getBaseContext(), HomeActivity.class);
                break;
            case R.id.layout_detail:
                intent = new Intent(getBaseContext(), DetailFragmentActivity.class);
                break;
            case R.id.layout_expense:
                intent = new Intent(getBaseContext(), ExpenseFragmentActivity.class);
                break;
            case R.id.layout_income:
                intent = new Intent(getBaseContext(), IncomeFragmentActivity.class);
                break;
            case R.id.layout_settings:
                intent = new Intent(getBaseContext(), SettingActivity.class);
                break;
            default:
                intent = null;
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override

    protected void onStart() {
        super.onStart();
    }

    @Override

    protected void onStop() {
        super.onStop();
    }


}
