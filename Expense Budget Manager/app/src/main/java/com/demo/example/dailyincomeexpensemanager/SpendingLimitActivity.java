package com.demo.example.dailyincomeexpensemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;

import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.R;

import java.util.ArrayList;
import java.util.List;


public class SpendingLimitActivity extends BaseActivity {
    boolean checkcolumn;
    private Context context;

    
    private MyDatabaseHandler f89db;
    private List<CategoryBean> list;
    private ListView lvSpending;
    private CategorySpendingLimitAdapter spendingLimitAdapter;

    
    @Override
    public void changeUserAccount() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_spending_limit);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(Html.fromHtml("<font color='#00c853'>" + getResources().getString(R.string.app_name) + "</font>"));
        this.context = this;
        this.f89db = new MyDatabaseHandler(this.context);
        this.lvSpending = (ListView) findViewById(R.id.list_spending_limit);
        updateData();
        this.lvSpending.setEmptyView(findViewById(R.id.tv_nodata));
        this.lvSpending.setAdapter((ListAdapter) this.spendingLimitAdapter);
        this.lvSpending.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent intent = new Intent(SpendingLimitActivity.this.context, AddEditCategory.class);
                intent.putExtra(TAG.CATEGORY, 1);
                intent.putExtra(TAG.DATA, (int) j);
                SpendingLimitActivity.this.startActivity(intent);
            }
        });
        showHomeButton();
    }

    private void updateData() {
        this.checkcolumn = this.f89db.existsColumnInTable().booleanValue();
        this.list = new ArrayList();
        if (this.checkcolumn) {
            this.list = this.f89db.getSpeningCategory();
        } else {
            this.f89db.addcolumn();
        }
        for (int i = 0; i < this.list.size(); i++) {
            CategoryBean categoryBean = this.list.get(i);
            categoryBean.setCategoryTotal(this.f89db.getCategoryAmount(categoryBean.getCategoryGroup(), categoryBean.getId()));
        }
        this.spendingLimitAdapter = new CategorySpendingLimitAdapter(this.context, this.list, true);
        this.lvSpending.setAdapter((ListAdapter) this.spendingLimitAdapter);
    }

    
    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }
}
