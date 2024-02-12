package com.demo.example.dailyincomeexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;

import com.demo.example.R;


public class CategoryTimeListFragmentActivity extends BaseActivity {
    private Bundle mBundle;

    @Override

    public void onCreate(Bundle bundle) {
        this.isSpinnerRequired = false;
        super.onCreate(bundle);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(Html.fromHtml("<font color='#00c853'>" + getResources().getString(R.string.app_name) + "</font>"));
        showHomeButton();
        this.mBundle = getIntent().getExtras();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.option_add) {
            return super.onOptionsItemSelected(menuItem);
        }
        Intent intent = new Intent(this.context, AddExpenceIncomeActivity.class);
        intent.putExtra(TAG.CATEGORY, 2);
        startActivity(intent);
        ((BaseActivity) getParent()).changeUserAccount();
        return true;
    }

    @Override

    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void changeUserAccount() {
        updateUI();
    }

    private void updateUI() {
        CategoryTimeFragment categoryTimeFragment = new CategoryTimeFragment();
        categoryTimeFragment.setArguments(this.mBundle);
        getSupportFragmentManager().beginTransaction().replace(16908290, categoryTimeFragment).commit();
    }
}
