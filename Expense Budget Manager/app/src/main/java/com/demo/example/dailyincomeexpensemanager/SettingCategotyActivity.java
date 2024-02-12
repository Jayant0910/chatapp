package com.demo.example.dailyincomeexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.demo.example.AdAdmob;
import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.widget.SortListView.DragSortListView;
import com.demo.example.R;

import java.util.List;


public class SettingCategotyActivity extends BaseActivity implements View.OnClickListener {


    private LinearLayout addExpense;
    private LinearLayout addIncome;
    private CategoryBean categoryBean;
    Boolean checkcolumn;
    private SettingCategoryCustomAdapter expenseAdpter;
    private List<CategoryBean> expenseData;
    DragSortListView expenseList;
    private SettingCategoryCustomAdapter incomeAdapter;
    private DragSortListView incomeList;
    private List<CategoryBean> inocmeData;
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int i, int i2) {
            CategoryBean item = SettingCategotyActivity.this.expenseAdpter.getItem(i);
            SettingCategotyActivity.this.expenseData.remove(item);
            SettingCategotyActivity.this.expenseData.add(i2, item);
            if (i < i2 && i != i2) {
                SettingCategotyActivity.this.f60db.addUpdateCategoryByDrod_IdtoOthers(i2, i, 1, item.getAccountRef());
                SettingCategotyActivity.this.f60db.addUpdateCategoryByDrod_Id(i, i2, Integer.parseInt(item.getId()), item, 1, item.getAccountRef());
            } else if (i > i2 && i != i2) {
                SettingCategotyActivity.this.f60db.addUpdateCategoryByDrod_IdtoOthersback(i2, i, 1, item.getAccountRef());
                SettingCategotyActivity.this.f60db.addUpdateCategoryByDrod_Idback(i, i2, Integer.parseInt(item.getId()), item, 1, item.getAccountRef());
            }
            SettingCategotyActivity.this.expenseAdpter.notifyDataSetChanged();
        }
    };
    private DragSortListView.DropListener onDrop1 = new DragSortListView.DropListener() {
        @Override
        public void drop(int i, int i2) {
            CategoryBean item = SettingCategotyActivity.this.incomeAdapter.getItem(i);
            SettingCategotyActivity.this.inocmeData.remove(item);
            SettingCategotyActivity.this.inocmeData.add(i2, item);
            if (i < i2 && i != i2) {
                SettingCategotyActivity.this.f60db.addUpdateCategoryByDrod_IdtoOthers(i2, i, 2, item.getAccountRef());
                SettingCategotyActivity.this.f60db.addUpdateCategoryByDrod_Id(i, i2, Integer.parseInt(item.getId()), item, 2, item.getAccountRef());
            } else if (i > i2 && i != i2) {
                SettingCategotyActivity.this.f60db.addUpdateCategoryByDrod_IdtoOthersback(i2, i, 2, item.getAccountRef());
                SettingCategotyActivity.this.f60db.addUpdateCategoryByDrod_Idback(i, i2, Integer.parseInt(item.getId()), item, 2, item.getAccountRef());
            }
            SettingCategotyActivity.this.incomeAdapter.notifyDataSetChanged();
        }
    };
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int i) {
            SettingCategotyActivity.this.expenseData.remove(SettingCategotyActivity.this.expenseAdpter.getItem(i));
            notifyAll();
        }
    };
    private DragSortListView.RemoveListener onRemove1 = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int i) {
            SettingCategotyActivity.this.inocmeData.remove(SettingCategotyActivity.this.incomeAdapter.getItem(i));
            notifyAll();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_category_list);


        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);
        adAdmob.FullscreenAd(this);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00c853'>Category</font>"));
        this.incomeList = (DragSortListView) findViewById(R.id.cat_list_income);
        this.expenseList = (DragSortListView) findViewById(R.id.cat_list_expense);
        this.addIncome = (LinearLayout) findViewById(R.id.cat_list_income_add);
        this.addExpense = (LinearLayout) findViewById(R.id.cat_list_expense_add);
        this.addIncome.setOnClickListener(this);
        this.addExpense.setOnClickListener(this);
        this.categoryBean = new CategoryBean();
        this.incomeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent intent = new Intent(SettingCategotyActivity.this.context, AddEditCategory.class);
                intent.putExtra(TAG.CATEGORY, 2);
                intent.putExtra(TAG.DATA, (int) j);
                SettingCategotyActivity.this.startActivity(intent);
            }
        });
        this.expenseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent intent = new Intent(SettingCategotyActivity.this.context, AddEditCategory.class);
                intent.putExtra(TAG.CATEGORY, 1);
                intent.putExtra(TAG.DATA, (int) j);
                SettingCategotyActivity.this.startActivity(intent);
            }
        });
        showHomeButton();
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cat_list_expense_add) {
            Intent intent = new Intent(this.context, AddEditCategory.class);
            intent.putExtra(TAG.CATEGORY, 1);
            startActivity(intent);
        } else if (id == R.id.cat_list_income_add) {
            Intent intent2 = new Intent(this.context, AddEditCategory.class);
            intent2.putExtra(TAG.CATEGORY, 2);
            startActivity(intent2);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        this.checkcolumn = this.f60db.existsColumnInTable();
        updateUI();
    }

    private void updateUI() {
        this.expenseList.setDropListener(this.onDrop);
        this.incomeList.setDropListener(this.onDrop1);
        this.expenseList.setRemoveListener(this.onRemove);
        this.incomeList.setRemoveListener(this.onRemove1);
        if (this.checkcolumn.booleanValue()) {
            this.inocmeData = this.f60db.getCategoryList(2);
            this.expenseData = this.f60db.getCategoryList(1);
        } else {
            this.f60db.addcolumn();
            this.inocmeData = this.f60db.getCategoryList(2);
            this.expenseData = this.f60db.getCategoryList(1);
        }
        this.incomeAdapter = new SettingCategoryCustomAdapter(this.context, this.inocmeData);
        this.expenseAdpter = new SettingCategoryCustomAdapter(this.context, this.expenseData);
        this.incomeList.setAdapter((ListAdapter) this.incomeAdapter);
        this.expenseList.setAdapter((ListAdapter) this.expenseAdpter);
        CategoryListHelper.getListViewSize(this.incomeList);
        CategoryListHelper.getListViewSize(this.expenseList);
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
}
