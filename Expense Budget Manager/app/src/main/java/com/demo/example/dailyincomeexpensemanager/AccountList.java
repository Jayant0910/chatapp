package com.demo.example.dailyincomeexpensemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.demo.example.dailyincomeexpensemanager.bean.AccountBean;
import com.demo.example.R;

import java.util.List;


public class AccountList extends BaseActivity {


    private ListView list;

    @Override
    public void onCreate(Bundle bundle) {
        this.isSpinnerRequired = false;
        super.onCreate(bundle);
        setContentView(R.layout.activity_account_list);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(Html.fromHtml("<font color='#00c853'>" + context.getResources().getString(R.string.action_account) + "</font>"));
        this.list = (ListView) findViewById(16908298);
        this.list.setEmptyView(findViewById(16908292));
        showHomeButton();
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        this.list.setAdapter((ListAdapter) new AccountListAdapter(this, this.f60db.getAccountList()));
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                AccountList.this.openAddEditAccountAlertDialog(((AccountBean) adapterView.getItemAtPosition(i)).getId());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.option_add) {
            addNewAccount();
        }
        return super.onOptionsItemSelected(menuItem);
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
        super.onBackPressed();
    }


    public class AccountListAdapter extends ArrayAdapter<AccountBean> {
        public AccountListAdapter(Context context, List<AccountBean> list) {
            super(context, (int) R.layout.list_item_account, list);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = AccountList.this.getLayoutInflater().inflate(R.layout.list_item_account, viewGroup, false);
            final AccountBean item = getItem(i);
            ((TextView) inflate.findViewById(R.id.tvAccount)).setText(item.getName());
            inflate.findViewById(R.id.ivAccountSelect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    MyUtils.setAccount(AccountList.this.context, item.getId());
                    AccountList.this.startActivity(new Intent(AccountList.this.context, HomeActivity.class));
                }
            });
            return inflate;
        }
    }
}
