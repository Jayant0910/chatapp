package com.demo.example.dailyincomeexpensemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;

import com.demo.example.AdAdmob;
import com.demo.example.dailyincomeexpensemanager.bean.AccountBean;
import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.widget.CircleImageView;
import com.demo.example.R;

import java.util.List;


public class AddEditCategory extends BaseActivity implements View.OnClickListener {
    private List<AccountBean> accountBean;


    private CardView cardSpentLimit;
    private CircleImageView catColor;
    private EditText catName;
    private CategoryBean categoryBean;
    private EditText edtLimit;
    private Spinner spinner1;
    Button submit;
    private TextView tvCancel;
    private TextView tvDone;
    int drag_id = 2;
    int check = 0;

    @Override
    public void changeUserAccount() {
    }

    @Override

    public void onDestroy() {
        super.onDestroy();
    }

    @Override

    public void onCreate(Bundle bundle) {
        this.isSpinnerRequired = false;
        super.onCreate(bundle);
        setContentView(R.layout.activity_manage_category);

        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);
        adAdmob.FullscreenAd(this);


        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(Html.fromHtml("<font color='#00c853'>" + getResources().getString(R.string.app_name) + "</font>"));
        this.catColor = (CircleImageView) findViewById(R.id.cat_color);
        this.catName = (EditText) findViewById(R.id.cat_name);
        this.edtLimit = (EditText) findViewById(R.id.edt_limit);
        this.spinner1 = (Spinner) findViewById(R.id.spinner1);
        this.tvDone = (TextView) findViewById(R.id.btn_add_category);
        this.tvCancel = (TextView) findViewById(R.id.btn_cancle);
        this.cardSpentLimit = (CardView) findViewById(R.id.card_spentlimit);

        String currentAccount = MyUtils.getCurrentAccount(this.context);
        this.accountBean = this.f60db.getAccountList();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, this.accountBean);
        arrayAdapter.setDropDownViewResource(17367049);
        this.spinner1.setAdapter((SpinnerAdapter) arrayAdapter);
        this.spinner1.setSelection(Integer.parseInt(currentAccount) - 1);
        this.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.catColor.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int i = extras.getInt(TAG.CATEGORY, 1);
            int i2 = extras.getInt(TAG.DATA, 0);
            if (i2 != 0) {
                MyDatabaseHandler myDatabaseHandler = this.f60db;
                this.categoryBean = myDatabaseHandler.getCategoryModelById(i, "" + i2);
                this.categoryBean.setDrag_id(this.f60db.getDragIdfromCategoty(i2, this.categoryBean.getAccountRef()));
            } else {
                this.categoryBean = new CategoryBean();
                String str = MyUtils.colorcode[(int) (((double) MyUtils.colorcode.length) * Math.random())];
                this.categoryBean.setCategoryGroup(i);
                this.categoryBean.setColor(str);
                this.categoryBean.setDrag_id(this.f60db.getCategoryCount(MyDatabaseHandler.Category.TABLE_NAME, i, MyUtils.getCurrentAccount(this.context)));
            }
            if (i == 1) {
                this.cardSpentLimit.setVisibility(View.VISIBLE);
            }
        }
        this.catColor.setColorFilter(Color.parseColor(this.categoryBean.getColor()));
        this.catColor.setBorderColor(Color.parseColor(this.categoryBean.getColor()));
        this.catColor.setCircleBackgroundColor(Color.parseColor(this.categoryBean.getColor()));
        this.catName.setText(this.categoryBean.getName());
        this.edtLimit.setText(this.categoryBean.getCategoryLimit());
        showHomeButton();
        this.tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String obj = AddEditCategory.this.catName.getText().toString();
                String obj2 = AddEditCategory.this.edtLimit.getText().toString();
                if (TextUtils.isEmpty(obj)) {
                    Toast.makeText(AddEditCategory.this.context, AddEditCategory.this.getString(R.string.msg_please_enter_category_name), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (obj2.isEmpty()) {
                    obj2 = "0";
                }
                AddEditCategory.this.categoryBean.setName(obj);
                AddEditCategory.this.categoryBean.setCategoryLimit(obj2);
                AddEditCategory.this.categoryBean.setAccountRef(AddEditCategory.this.currentAccount.getId());
                AddEditCategory.this.f60db.addUpdateCategoryData(AddEditCategory.this.categoryBean);
                String currentAccount2 = MyUtils.getCurrentAccount(AddEditCategory.this.context);
                String obj3 = AddEditCategory.this.spinner1.getSelectedItem().toString();
                AccountBean accountModelByName = AddEditCategory.this.f60db.getAccountModelByName(obj3);
                if (!AddEditCategory.this.f60db.getAccountModelById(currentAccount2).getName().equals(obj3) && accountModelByName.getName().equals(obj3)) {
                    Bundle extras2 = AddEditCategory.this.getIntent().getExtras();
                    int i3 = extras2.getInt(TAG.CATEGORY, 1);
                    int categoryCount = AddEditCategory.this.f60db.getCategoryCount(MyDatabaseHandler.Category.TABLE_NAME, i3, accountModelByName.getId());
                    int i4 = extras2.getInt(TAG.DATA, 0);
                    AddEditCategory.this.f60db.addUpdateCategoryByDrod_IdtoOthersMove(AddEditCategory.this.categoryBean.getDrag_id(), i3, currentAccount2);
                    AddEditCategory.this.f60db.UpdateMoveCategory(String.valueOf(i4), accountModelByName.getId());
                    AddEditCategory.this.f60db.UpdateManagerAccount_id(i4, accountModelByName.getId());
                    AddEditCategory.this.f60db.UpdateRecirringAccount_id(i4, accountModelByName.getId());
                    AddEditCategory.this.f60db.UpdateDragIdMoveCategory(String.valueOf(i4), categoryCount);
                }
                AddEditCategory.this.finish();
            }
        });
        this.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEditCategory.this.deleteCategory();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cat_color) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View inflate = getLayoutInflater().inflate(R.layout.dialog_color_piker, (ViewGroup) null);
            builder.setView(inflate);
            builder.setCancelable(true);
            builder.setTitle("Select Color");
            ColorPikerAdapter colorPikerAdapter = new ColorPikerAdapter(this.context, MyUtils.colorcode);
            GridView gridView = (GridView) inflate.findViewById(R.id.color_list);
            final AlertDialog create = builder.create();
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                    AddEditCategory.this.catColor.setColorFilter(Color.parseColor(MyUtils.colorcode[i]));
                    AddEditCategory.this.catColor.setBorderColor(Color.parseColor(MyUtils.colorcode[i]));
                    AddEditCategory.this.catColor.setCircleBackgroundColor(Color.parseColor(MyUtils.colorcode[i]));
                    create.dismiss();
                    AddEditCategory.this.categoryBean.setColor(MyUtils.colorcode[i]);
                }
            });
            gridView.setAdapter((ListAdapter) colorPikerAdapter);
            create.show();
        }
    }


    public void deleteCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener
                r1 = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case -2:
                        AddEditCategory.this.finish();
                        return;
                    case -1:
                        int drag_id = AddEditCategory.this.categoryBean.getDrag_id();
                        int categoryGroup = AddEditCategory.this.categoryBean.getCategoryGroup();
                        String accountRef = AddEditCategory.this.categoryBean.getAccountRef();
                        AddEditCategory.this.f60db.deleteCategoryData(AddEditCategory.this.categoryBean);
                        AddEditCategory.this.f60db.UpdatedeleteCategoryData(drag_id, categoryGroup, accountRef);
                        AddEditCategory.this.finish();
                        return;
                    default:
                        return;
                }
            }
        };
        builder.setMessage("It will delete all relevant data\nAre you sure to delete?").setPositiveButton(getString(R.string.action_yes), r1).setNegativeButton(getString(R.string.action_no), r1);
        builder.show();
    }

    @Override

    public void onResume() {
        super.onResume();
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
