package com.demo.example.dailyincomeexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import com.demo.example.R;


public class ExpenseFragmentActivity extends BaseActivity {

    

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00c853'>Expense Manager</font>"));
        showHomeButton();
    }

    private void updateUI() {
        IncomeMonthlyFragment incomeMonthlyFragment = new IncomeMonthlyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("selection_type", 1);
        incomeMonthlyFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(16908290, incomeMonthlyFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.option_add) {
            return super.onOptionsItemSelected(menuItem);
        }
        Intent intent = new Intent(this.context, AddExpenceIncomeActivity.class);
        intent.putExtra(TAG.CATEGORY, 1);
        startActivity(intent);
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

    
    @Override
    public void onStart() {
        super.onStart();
    }

    
    @Override
    public void onStop() {
        super.onStop();
    }

    
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Log.m953d("Expense Activity On Saved Instance State");
    }
}
