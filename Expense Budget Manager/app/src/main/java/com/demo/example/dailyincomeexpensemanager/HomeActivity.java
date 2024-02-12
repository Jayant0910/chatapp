package com.demo.example.dailyincomeexpensemanager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.demo.example.AdAdmob;
import com.demo.example.Utile.LocaleHelper;
import com.demo.example.Utile.Preferences;
import com.demo.example.dailyincomeexpensemanager.bean.AccountBean;
import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.passcode.LoginActivity;
import com.demo.example.passcode.PinEnterActivity;
import com.demo.example.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import com.google.android.material.navigation.NavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static long back_pressed;
    List<CategoryBean> SpentList;
    private List<AccountBean> accounts;
    private Calendar calendar;
    boolean checkcolumn;
    private ArrayList<Integer> colors;
    private Context context;
    protected AccountBean currentAccount;


    MyDatabaseHandler f66db;
    List<CategoryBean> getRecentList;
    List<CategoryBean> getSpendingList;
    List<CategoryBean> incomeCategoryList;
    private LinearLayout layoutExpense;
    private LinearLayout layoutIncome;
    private LinearLayout layoutRecent;
    private ListView lvRecentTran;
    private ListView lvSpending;
    private ConstraintLayout mainLayout;
    private PieChart pieChart;
    List<CategoryBean> recentList;
    private TextView tvSpendingDate;
    private TextView tvSpendingLimitView;
    private TextView tvTodayExpense;
    private TextView tvTodayIncome;
    private TextView tvUserEmail;
    private TextView tvUserName;
    private TextView tvViewTransaction;
    private ArrayList<String> xVals;
    private ArrayList<PieEntry> yvalues;
    private String startTime = "1";
    private String endTime = "9";
    private String totalAmount = "0";
    private String UserName = "";
    private String UserEmail = "";


    public void ShowError(String str) {
    }

    @Override

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);

        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);

        this.context = this;
        this.f66db = new MyDatabaseHandler(this.context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(Html.fromHtml("<font color='#00c853'>" + getResources().getString(R.string.app_name) + "</font>"));
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        View headerView = navigationView.getHeaderView(0);
        this.tvUserEmail = (TextView) headerView.findViewById(R.id.tv_useremail);
        this.tvUserName = (TextView) headerView.findViewById(R.id.tv_username);
        this.tvUserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.this.getAccount();
            }
        });
        firstTimeLaunch();
        this.calendar = Calendar.getInstance();
        this.calendar.set(11, 0);
        this.calendar.clear(12);
        this.calendar.clear(13);
        this.calendar.clear(14);
        this.tvTodayIncome = (TextView) findViewById(R.id.tv_todayincome);
        this.tvTodayExpense = (TextView) findViewById(R.id.tv_todayexpense);
        this.tvSpendingDate = (TextView) findViewById(R.id.tv_spending_date);
        this.tvSpendingLimitView = (TextView) findViewById(R.id.tv_spendinglimit_date);
        this.tvViewTransaction = (TextView) findViewById(R.id.tv_viewTransaction);
        this.pieChart = (PieChart) findViewById(R.id.pie_chart);
        this.lvRecentTran = (ListView) findViewById(R.id.list_recent_tran);
        this.lvSpending = (ListView) findViewById(R.id.list_spending_limit);
        this.layoutIncome = (LinearLayout) findViewById(R.id.layout_main_imcome);
        this.layoutExpense = (LinearLayout) findViewById(R.id.layout_main_expense);
        this.tvSpendingDate.setText(new SimpleDateFormat("MMM yyyy", Locale.US).format(this.calendar.getTime()));
        PrepareDataForChart();
        this.layoutIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, IncomeFragmentActivity.class));
            }
        });
        this.layoutExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, ExpenseFragmentActivity.class));
            }
        });
        this.tvViewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, DetailFragmentActivity.class));
            }
        });
        this.tvSpendingLimitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, SpendingLimitActivity.class));
            }
        });
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(HomeActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();


    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        }
    }

    private void firstTimeLaunch() {
        this.accounts = this.f66db.getAccountList();
        this.currentAccount = new AccountBean();
        this.mainLayout = (ConstraintLayout) findViewById(R.id.layout_main);
        if (Preferences.getInstance().getFirstTimeLaunch()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (this.accounts.size() == 0) {
            startActivity(new Intent(this, AddEditAccount.class));
        } else if (!Preferences.getInstance().getLoginStatus()) {
            Preferences.getInstance().setLoginStatus(true);
            new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.app_name)).setMessage(getString(R.string.dialog_enable_password)).setCancelable(false).setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    HomeActivity.this.startActivity(new Intent(HomeActivity.this, PinEnterActivity.class));
                    System.exit(0);
                }
            }).setNegativeButton(getString(R.string.action_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        }
        if (MyUtils.getWhatNewRequired(this)) {
            startActivity(new Intent(this, WhatsNewActivity.class));
        }
        IntializeMyDataBaseWithDefaultValues();
    }

    private void IntializeMyDataBaseWithDefaultValues() {
        if (this.f66db.getCount(MyDatabaseHandler.Category.TABLE_NAME) == 0) {
            addDefaultsCategoriesToAccount("1");
        }
    }


    public void getAccount() {
        this.accounts = new ArrayList();
        this.accounts = this.f66db.getAccountList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.dialog_account_list, (ViewGroup) null);
        builder.setView(inflate);
        ListView listView = (ListView) inflate.findViewById(R.id.list_account);
        listView.setAdapter((ListAdapter) new ArrayAdapter(this.context, (int) R.layout.listview_txt, this.accounts));
        final AlertDialog create = builder.create();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                HomeActivity.this.currentAccount = (AccountBean) HomeActivity.this.accounts.get(i);
                HomeActivity.this.onNavigationItemChanged();
                create.dismiss();
                HomeActivity.this.setTodayIncomeExpense();
            }
        });
        ((Button) inflate.findViewById(R.id.btn_newAccount)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.this.addNewAccount();
                create.dismiss();
            }
        });
        create.show();
    }


    @Override

    public void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.onAttach(context));
    }


    @Override
    public void changeUserAccount() {
        restartActivity(this);
        setTodayIncomeExpense();
    }


    public void setTodayIncomeExpense() {
        this.currentAccount = this.f66db.getAccountModelById(MyUtils.getCurrentAccount(this.context));
        if (this.currentAccount != null) {
            this.UserName = this.currentAccount.getName();
            this.UserEmail = this.currentAccount.getEmail();
        } else {
            this.UserName = getResources().getString(R.string.app_name);
            this.UserEmail = "example@gmail.com";
        }
        int userSummary = MyUtils.getUserSummary(this);
        PrepareDataForChart();
        Calendar instance = Calendar.getInstance();
        instance.set(11, 0);
        instance.clear(12);
        instance.clear(13);
        instance.clear(14);
        this.tvUserName.setText(this.UserName);
        this.tvUserEmail.setText(this.UserEmail);
        if (userSummary == 1) {
            setSummaryType(getString(R.string.daily));
            Calendar calendar = (Calendar) instance.clone();
            Calendar calendar2 = (Calendar) calendar.clone();
            calendar2.add(6, 1);
            calendar2.add(14, -1);
            setDetails(calendar, calendar2);
        } else if (userSummary == 2) {
            setSummaryType(getString(R.string.weekly));
            setDetails(getWeekStart(instance), getWeekEnd(instance));
        } else if (userSummary == 3) {
            setSummaryType(getString(R.string.monthly));
            setDetails(getMonthStart(instance), getMonthEnd(instance));
        }
    }

    private void setSummaryType(String str) {
        ((TextView) findViewById(R.id.tv_income_type)).setText(str);
        ((TextView) findViewById(R.id.tv_expense_type)).setText(str);
    }

    private void setDetails(Calendar calendar, Calendar calendar2) {
        double d;
        String format = MyUtils.sdfDatabase.format(calendar.getTime());
        String format2 = MyUtils.sdfDatabase.format(calendar2.getTime());
        String totalAmountByTime = this.f66db.getTotalAmountByTime(2, format, format2);
        String totalAmountByTime2 = this.f66db.getTotalAmountByTime(1, format, format2);
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
        setTextValue(this.tvTodayIncome, " %.2f", d2);
        setTextValue(this.tvTodayExpense, "%.2f", d);
    }


    @Override

    public void onResume() {
        super.onResume();
        setTodayIncomeExpense();
    }

    private Calendar getMonthStart(Calendar calendar) {
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.set(5, 1);
        return calendar2;
    }

    private Calendar getMonthEnd(Calendar calendar) {
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.add(2, 1);
        calendar2.add(14, -1);
        return calendar2;
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

    private void setTextValue(TextView textView, String str, double d) {
        if (getCurrencySymbol().equals("¢") || getCurrencySymbol().equals("₣") || getCurrencySymbol().equals("₧") || getCurrencySymbol().equals("﷼") || getCurrencySymbol().equals("₨")) {
            textView.setText(String.format(str, Double.valueOf(d)) + getCurrencySymbol());
            return;
        }
        textView.setText(getCurrencySymbol() + String.format(str, Double.valueOf(d)));
    }

    private void PrepareDataForChart() {
        this.checkcolumn = this.f66db.existsColumnInTable().booleanValue();
        if (this.checkcolumn) {
            this.incomeCategoryList = this.f66db.getCategoryList(2);
            this.getRecentList = this.f66db.getAllCategory();
            this.getSpendingList = this.f66db.getSpeningCategory();
        } else {
            this.f66db.addcolumn();
        }
        for (int i = 0; i < this.incomeCategoryList.size(); i++) {
            CategoryBean categoryBean = this.incomeCategoryList.get(i);
            categoryBean.setCategoryTotal(this.f66db.getCategoryAmount(categoryBean.getCategoryGroup(), categoryBean.getId()));
        }
        this.SpentList = new ArrayList();
        for (int i2 = 0; i2 < this.getSpendingList.size(); i2++) {
            CategoryBean categoryBean2 = this.getSpendingList.get(i2);
            categoryBean2.setCategoryTotal(this.f66db.getCategoryAmount(categoryBean2.getCategoryGroup(), categoryBean2.getId()));
            if (!this.f66db.getCategoryAmount(categoryBean2.getCategoryGroup(), categoryBean2.getId()).equals("0") && this.SpentList.size() < 5) {
                this.SpentList.add(categoryBean2);
            }
        }
        this.recentList = new ArrayList();
        this.recentList.clear();
        for (int i3 = 0; i3 < this.getRecentList.size(); i3++) {
            CategoryBean categoryBean3 = this.getRecentList.get(i3);
            if (!this.f66db.getCategoryAmount(categoryBean3.getId()).equals("0") && this.recentList.size() < 5) {
                this.recentList.add(categoryBean3);
            }
        }
        this.yvalues = new ArrayList<>();
        this.xVals = new ArrayList<>();
        this.colors = new ArrayList<>();
        for (int i4 = 0; i4 < this.incomeCategoryList.size(); i4++) {
            this.yvalues.add(new PieEntry(Float.parseFloat(this.incomeCategoryList.get(i4).getCategoryTotal()), this.incomeCategoryList.get(i4).getName()));
            this.xVals.add(this.incomeCategoryList.get(i4).getName());
            this.colors.add(Integer.valueOf(Color.parseColor(this.incomeCategoryList.get(i4).getColor())));
        }
        CategoryCustomAdapter categoryCustomAdapter = new CategoryCustomAdapter(this.context, this.recentList);
        CategorySpendingLimitAdapter categorySpendingLimitAdapter = new CategorySpendingLimitAdapter(this.context, this.SpentList, false);
        this.lvRecentTran.setAdapter((ListAdapter) categoryCustomAdapter);
        this.lvSpending.setAdapter((ListAdapter) categorySpendingLimitAdapter);
        this.lvRecentTran.setEmptyView(findViewById(R.id.tv_notransaction));
        this.lvSpending.setEmptyView(findViewById(R.id.tv_noSpent));
        this.lvSpending.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        this.lvSpending.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i5, long j) {
                Intent intent = new Intent(HomeActivity.this.context, AddEditCategory.class);
                intent.putExtra(TAG.CATEGORY, 1);
                intent.putExtra(TAG.DATA, (int) j);
                HomeActivity.this.startActivity(intent);
            }
        });
        this.lvRecentTran.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i5, long j) {
                int categoryGroup = HomeActivity.this.recentList.get(i5).getCategoryGroup();
                Intent intent = new Intent(HomeActivity.this, CategoryTimeListFragmentActivity.class);
                intent.putExtra(CategoryTimeFragment.CATEGORY_TYPE, (int) j);
                intent.putExtra("selection_type", categoryGroup);
                intent.putExtra("start_date", HomeActivity.this.startTime);
                intent.putExtra("end_date", HomeActivity.this.endTime);
                intent.putExtra(CategoryTimeFragment.MESSAGE, "");
                HomeActivity.this.startActivity(intent);
            }
        });
        setPieChart();
    }

    private void setPieChart() {
        this.pieChart.setUsePercentValues(true);
        PieDataSet pieDataSet = new PieDataSet(this.yvalues, "");
        pieDataSet.setColors(this.colors);
        pieDataSet.setDrawIcons(false);
        pieDataSet.setSliceSpace(3.0f);
        pieDataSet.setIconsOffset(new MPPointF(0.0f, 40.0f));
        pieDataSet.setSelectionShift(5.0f);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        this.pieChart.setData(pieData);
        this.pieChart.getDescription().setEnabled(false);
        this.pieChart.setDrawHoleEnabled(true);
        this.pieChart.setTransparentCircleRadius(30.0f);
        this.pieChart.setHoleRadius(30.0f);
        this.pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        this.pieChart.setExtraOffsets(0.0f, 5.0f, 0.0f, 5.0f);
        this.pieChart.setDrawSliceText(false);
        Legend legend = this.pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setXEntrySpace(7.0f);
        legend.setYEntrySpace(0.0f);
        legend.setYOffset(10.0f);
        legend.setWordWrapEnabled(true);
        legend.setDrawInside(false);
        legend.getCalculatedLineSizes();
        legend.setEnabled(true);
        this.pieChart.setRotationAngle(0.0f);
        this.pieChart.setRotationEnabled(true);
        this.pieChart.highlightValues(null);
        this.pieChart.setHighlightPerTapEnabled(true);
        this.pieChart.invalidate();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_account) {
            startActivity(new Intent(this, AccountList.class));
            return true;
        } else if (itemId == 16908332) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.action_backup) {
            createBackupOptions();
            return true;
        } else if (itemId == R.id.action_restore) {
            restoreDialog();
            return true;
        } else if (itemId == R.id.action_whats_new) {
            startActivity(new Intent(this, WhatsNewActivity.class));
            return true;
        } else if (itemId == R.id.action_tour) {
            startActivity(new Intent(this, TakeATourActivity.class));
            return true;
        } else {
            if (itemId == R.id.action_analytics) {
                startActivity(new Intent(this, AnalyticsFragmentActivity.class));
            } else if (itemId == R.id.action_settings) {
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            } else if (itemId == R.id.action_details) {
                startActivity(new Intent(this, DetailFragmentActivity.class));
                return true;
            }
            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
            return true;
        }
    }

    private void restoreDialog() {
        if (isFile(BackupTask.FILE_NAME)) {
            new RestoreTask(this.context).execute(new Void[0]);
        } else if (!isFile(RestoreTaskv15.INCOME_CATEGORY) || !isFile(RestoreTaskv15.EXPENSE_CATEGORY) || !isFile(RestoreTaskv15.INCOME_DATA) || !isFile(RestoreTaskv15.EXPENSE_DATA)) {
            Toast.makeText(this, getString(R.string.no_backup), Toast.LENGTH_SHORT).show();

        } else {
            alertForSupportv15Restore();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.option_add) {
            startActivity(new Intent(this, AddExpenceIncomeActivity.class));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (back_pressed + 3000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.back_msg), Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

}
