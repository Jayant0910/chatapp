package com.demo.example.dailyincomeexpensemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.example.Utile.LocaleHelper;
import com.demo.example.dailyincomeexpensemanager.bean.AccountBean;
import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.R;

import java.io.File;
import java.util.List;


public abstract class BaseActivity extends AppCompatActivity {
    private List<AccountBean> accounts;


    protected Context context;
    protected AccountBean currentAccount;


    protected MyDatabaseHandler f60db;
    protected boolean isSpinnerRequired = true;

    private void upadteAccountSpinner() {
    }


    public abstract void changeUserAccount();


    @Override

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        this.context = this;
        this.f60db = new MyDatabaseHandler(this);
        this.accounts = this.f60db.getAccountList();
        updateCurrentAccount();
    }

    private void updateCurrentAccount() {
        this.currentAccount = this.f60db.getAccountModelById(MyUtils.getCurrentAccount(this.context));
    }


    @Override
    public void onResume() {
        super.onResume();
        updateCurrentAccount();
        updateActionBarSpinner();
        this.accounts.size();
    }


    public void updateActionBarSpinner() {
        if (this.isSpinnerRequired) {
            upadteAccountSpinner();
        }
    }

    public void onNavigationItemChanged() {
        if (TextUtils.isEmpty(this.currentAccount.getId())) {
            addNewAccount();
            updateActionBarSpinner();
        } else if (!MyUtils.getCurrentAccount(this.context).equalsIgnoreCase(this.currentAccount.getId())) {
            MyUtils.setAccount(this, this.currentAccount.getId());
            this.f60db = new MyDatabaseHandler(this.context);
            changeUserAccount();
        }
    }

    protected void restartActivity(Activity activity) {
        finish();
        Intent intent = new Intent(this.context, activity.getClass());
        intent.setFlags(603979776);
        startActivity(intent);
    }

    protected void addNewAccount() {
        this.accounts = this.f60db.getAccountList();
        if (this.accounts.size() == 0) {
            startActivity(new Intent(this, AddEditAccount.class));
        }
    }


    @Override

    public void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.onAttach(context));
    }

    protected void openAddEditAccountAlertDialog(String str) {
        final AccountBean accountModelById = this.f60db.getAccountModelById(str);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Account");
        builder.setMessage("Account Name");
        final EditText editText = new EditText(this);
        editText.setSingleLine(true);
        editText.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        editText.setText(accountModelById.getName());
        builder.setView(editText);
        builder.setPositiveButton(getString(R.string.action_done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String obj = editText.getText().toString();
                if (TextUtils.isEmpty(obj)) {
                    Toast.makeText(context, "Enter Some Name for your Account", Toast.LENGTH_SHORT).show();
                    return;
                }
                accountModelById.setName(obj);
                long addUpdateAccountData = BaseActivity.this.f60db.addUpdateAccountData(accountModelById);
                if (addUpdateAccountData > 0) {
                    BaseActivity.this.addDefaultsCategoriesToAccount(String.valueOf(addUpdateAccountData));
                }
                dialogInterface.cancel();
                BaseActivity.this.updateActionBarSpinner();
                BaseActivity.this.changeUserAccount();
            }
        });
        builder.setNegativeButton(getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void deleteAccountAlert(final AccountBean accountBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.warning));
        builder.setMessage("It will delete all the data which are associated this account. Are you sure to delete?");
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BaseActivity.this.f60db.deleteAccountData(accountBean);
                BaseActivity.this.updateActionBarSpinner();
                BaseActivity.this.changeUserAccount();
            }
        });
        builder.setNegativeButton(getString(R.string.action_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void openAddAccountAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.new_account));
        builder.setMessage(getString(R.string.account_name));
        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        editText.setSingleLine(true);
        editText.setLayoutParams(layoutParams);
        builder.setView(editText);
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String obj = editText.getText().toString();
                if (!TextUtils.isEmpty(obj)) {
                    AccountBean accountBean = new AccountBean();
                    accountBean.setName(obj);
                    long addUpdateAccountData = BaseActivity.this.f60db.addUpdateAccountData(accountBean);
                    if (addUpdateAccountData > 0) {
                        BaseActivity.this.addDefaultsCategoriesToAccount(String.valueOf(addUpdateAccountData));
                    }
                    BaseActivity.this.updateActionBarSpinner();
                    BaseActivity.this.changeUserAccount();
                    dialogInterface.cancel();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.action_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    protected void showHomeButton() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void createBackupOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(getString(R.string.backup));
        builder.setMessage(getString(R.string.msg_data_backup));
        builder.setPositiveButton("EXCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                BaseActivity.this.checkForAllowBackupPaid();
            }
        });
        builder.setNegativeButton("XML", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                BaseActivity.this.createXmlBackup();
            }
        });
        builder.show();
    }

    protected void checkForAllowBackupPaid() {
        createExcelBackup();
    }

    private void createExcelBackup() {
        new XLSBackupTask(this).execute(new Void[0]);
    }


    public void createXmlBackup() {
        new BackupTask(this.context).execute(new Void[0]);
    }

    public void alertForSupportv15Restore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(getString(R.string.restore));
        builder.setMessage(getString(R.string.msg_data_restored));
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                BaseActivity.this.restoreSupportv15Data();
            }
        });
        builder.setNegativeButton(getString(R.string.action_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


    public void restoreSupportv15Data() {
        new RestoreTaskv15(this.context).execute(new Void[0]);
    }

    public boolean isFile(String str) {
        return new File(BackupTask.MOBYI_DIRECTORY + "/", str).exists();
    }

    public void shareThisApp() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", "This app is simple and insightful effort of tracking and managing your daily expenses quickly https://play.google.com/store/apps/details?id=" + getPackageName());
        intent.putExtra("android.intent.extra.SUBJECT", "Check it out Daily Income Expense Manager App!");
        startActivity(Intent.createChooser(intent, "Share with.."));
    }

    public void rateThisApp() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
        startActivity(intent);
    }

    protected void addDefaultsCategoriesToAccount(String str) {
        CategoryBean categoryBean = new CategoryBean("Salary", "#66bc29", 2, 0);
        CategoryBean categoryBean2 = new CategoryBean("Other", "#BC8F8F", 2, 1);
        categoryBean.setAccountRef(str);
        categoryBean2.setAccountRef(str);
        this.f60db.addUpdateCategoryData(categoryBean);
        this.f60db.addUpdateCategoryData(categoryBean2);
        CategoryBean categoryBean3 = new CategoryBean("Travel", "#9400D3", 1, 0, "0");
        CategoryBean categoryBean4 = new CategoryBean("Food", "#228B22", 1, 1, "0");
        CategoryBean categoryBean5 = new CategoryBean("Entertainment", "#FF1493", 1, 2, "0");
        CategoryBean categoryBean6 = new CategoryBean("Other", "#696969", 1, 3, "0");
        categoryBean3.setAccountRef(str);
        categoryBean4.setAccountRef(str);
        categoryBean5.setAccountRef(str);
        categoryBean6.setAccountRef(str);
        this.f60db.addUpdateCategoryData(categoryBean3);
        this.f60db.addUpdateCategoryData(categoryBean4);
        this.f60db.addUpdateCategoryData(categoryBean5);
        this.f60db.addUpdateCategoryData(categoryBean6);
    }


    public String getCurrencySymbol() {
        if (this.currentAccount != null) {
            return this.currentAccount.getCurrencySymbol();
        }
        return MyUtils.currencyList[0][0];
    }


}
