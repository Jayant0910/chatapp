package com.demo.example.dailyincomeexpensemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.demo.example.Permissions.C0518Permissions;
import com.demo.example.Permissions.PermissionHandler;
import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.dailyincomeexpensemanager.bean.DataBean;
import com.demo.example.dailyincomeexpensemanager.bean.RecurringBean;
import com.demo.example.widget.NumberPicker;
import com.demo.example.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


public class AddExpenceIncomeActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final int DATE_DIALOG_ID = 0;
    private static int selectCategory;
    List<CategoryBean> SubCategory;


    private CategoryAdapter adapter;
    private AlertDialog alert;
    private Calendar calendar;
    List<CategoryBean> categoryItems;
    boolean checkcolumn;
    private DataBean currentDataBean;
    private EditText etAmount;
    private EditText etNote;
    private ImageView imgImage;
    private LinearLayout layoutCamera;
    private LinearLayout layoutCategory;
    private LinearLayout layoutDate;
    private LinearLayout layoutGallery;
    private CoordinatorLayout layoutMain;
    private LinearLayout layoutMethod;
    private LinearLayout layoutRecurring;
    private LinearLayout linearLayout;
    private ListView list;
    private ScaleGestureDetector mScaleGestureDetector;
    private RadioButton rbExpense;
    private RadioButton rbIncome;
    private RadioGroup rgInEX;
    private BottomSheetBehavior sheetBehavior;
    private SwitchCompat tbRecurring;
    private TextView tvCategory;
    private TextView tvDate;
    private TextView tvMethod;
    private TextView tvRecurring;
    int getTotal = 0;
    int getLimit = 0;


    int f55id = 0;
    private int category = 2;
    private int recurringDate = 1;
    private int recurringType = -1;
    private int recurringId = 0;
    private int PICK_IMAGE = 101;
    private String Imagepath = "";
    private String method = "";
    private String subCategory = "";
    private String Image = "";
    private float mScaleFactor = 1.0f;


    @Override
    public void changeUserAccount() {
    }

    public static void verifyStoragePermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }

    @Override

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override

    public void onCreate(Bundle bundle) {
        this.isSpinnerRequired = false;
        super.onCreate(bundle);
        setContentView(R.layout.activity_manage_manager);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(Html.fromHtml("<font color='#00c853'>" + getResources().getString(R.string.app_name) + "</font>"));
        this.rgInEX = (RadioGroup) findViewById(R.id.rg_switch);
        this.rbExpense = (RadioButton) findViewById(R.id.rb_expense);
        this.rbIncome = (RadioButton) findViewById(R.id.rb_income);
        this.tvDate = (TextView) findViewById(R.id.tv_date);
        this.etAmount = (EditText) findViewById(R.id.edt_amount);
        this.rbIncome.setTypeface(null, Typeface.BOLD);
        this.tbRecurring = (SwitchCompat) findViewById(R.id.tbRecurring);
        this.etNote = (EditText) findViewById(R.id.edt_note);
        this.tvCategory = (TextView) findViewById(R.id.tv_category);
        this.tvMethod = (TextView) findViewById(R.id.tv_method);
        this.tvRecurring = (TextView) findViewById(R.id.tv_recurring);
        this.imgImage = (ImageView) findViewById(R.id.img_bill);
        this.linearLayout = (LinearLayout) findViewById(R.id.bottom_sheet);
        this.layoutCategory = (LinearLayout) findViewById(R.id.layout_category);
        this.layoutMethod = (LinearLayout) findViewById(R.id.layout_method);
        this.layoutDate = (LinearLayout) findViewById(R.id.layout_date);
        this.layoutRecurring = (LinearLayout) findViewById(R.id.layout_recurring);
        this.layoutCamera = (LinearLayout) findViewById(R.id.layout_camera);
        this.layoutGallery = (LinearLayout) findViewById(R.id.layout_gallery);
        this.layoutMain = (CoordinatorLayout) findViewById(R.id.layout_main_incomeexpense);
        this.sheetBehavior = BottomSheetBehavior.from(this.linearLayout);
        this.rgInEX.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            @SuppressLint({"ResourceType"})
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (((RadioButton) radioGroup.findViewById(i)) != null && i > -1) {
                    AddExpenceIncomeActivity.this.changeUi();
                }
            }
        });
        this.calendar = Calendar.getInstance();
        this.calendar.set(11, 0);
        this.calendar.clear(12);
        this.calendar.clear(13);
        this.calendar.clear(14);
        showHomeButton();
        this.checkcolumn = this.f60db.existsColumnInTable().booleanValue();
        this.categoryItems = this.f60db.getSpeningCategory();
        for (int i = 0; i < this.categoryItems.size(); i++) {
            CategoryBean categoryBean = this.categoryItems.get(i);
            categoryBean.setCategoryTotal(this.f60db.getCategoryAmount(categoryBean.getCategoryGroup(), categoryBean.getId()));
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.category = extras.getInt(TAG.CATEGORY, 1);
            int i2 = extras.getInt(TAG.DATA, 0);
            this.recurringId = extras.getInt(TAG.RECURRING, 0);
            if (i2 != 0) {
                this.currentDataBean = this.f60db.getManagerModelById(this.category, String.valueOf(i2));
            }
            if (this.recurringId != 0) {
                MyDatabaseHandler myDatabaseHandler = this.f60db;
                int i3 = this.category;
                RecurringBean recurringModelById = myDatabaseHandler.getRecurringModelById(i3, "" + this.recurringId);
                this.currentDataBean = recurringModelById.convertToDataBean();
                this.recurringDate = Integer.parseInt(recurringModelById.getRecurringDate());
                this.recurringType = Integer.parseInt(recurringModelById.getRecurringType());
            }
            if (i2 == 0 && this.recurringId == 0) {
                findViewById(R.id.layout_recurring).setVisibility(View.VISIBLE);
            }
        }
        updateUI();
        if (this.checkcolumn) {
            this.SubCategory = new ArrayList();
            if (this.category == 1) {
                this.SubCategory = this.f60db.getCategoryList(1);
            } else {
                this.SubCategory = this.f60db.getCategoryList(2);
            }
        } else {
            Toast.makeText(this, "columns not found", Toast.LENGTH_SHORT).show();
            this.f60db.addcolumn();
        }
        this.layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddExpenceIncomeActivity.this.showDialog(0);
            }
        });
        this.layoutCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddExpenceIncomeActivity.this.selectCategory();
            }
        });
        this.layoutMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddExpenceIncomeActivity.this.selectMethods();
            }
        });
        this.sheetBehavior.setPeekHeight(0);
        this.imgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenceIncomeActivity.this);
                builder.setTitle(AddExpenceIncomeActivity.this.getString(R.string.receipts));
                builder.setCancelable(true);
                View inflate = AddExpenceIncomeActivity.this.getLayoutInflater().inflate(R.layout.dialog_image, (ViewGroup) null);
                builder.setView(inflate);
                ImageView imageView = (ImageView) inflate.findViewById(R.id.my_image);
                if (AddExpenceIncomeActivity.this.Imagepath.equals("")) {
                    imageView.setImageResource(R.drawable.camera);
                } else if (AddExpenceIncomeActivity.this.Imagepath.equals("no image")) {
                    imageView.setImageResource(R.drawable.camera);
                } else {
                    imageView.setImageURI(Uri.parse(AddExpenceIncomeActivity.this.Imagepath));
                }
                builder.setNegativeButton(AddExpenceIncomeActivity.this.getString(R.string.change), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i4) {
                        if (ActivityCompat.checkSelfPermission(AddExpenceIncomeActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                            AddExpenceIncomeActivity.verifyStoragePermissions(AddExpenceIncomeActivity.this);
                        } else {
                            AddExpenceIncomeActivity.this.sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }
                });
                builder.show();
            }
        });
        this.layoutGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                AddExpenceIncomeActivity.this.startActivityForResult(Intent.createChooser(intent, AddExpenceIncomeActivity.this.getString(R.string.select_picture)), AddExpenceIncomeActivity.this.PICK_IMAGE);
                AddExpenceIncomeActivity.this.sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        this.layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                C0518Permissions.check(AddExpenceIncomeActivity.this, "android.permission.CAMERA", (String) null, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        AddExpenceIncomeActivity.this.startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 111);
                        AddExpenceIncomeActivity.this.sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
            }
        });
    }


    public void selectMethods() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(getString(R.string.select_method));
        builder.setItems(MyUtils.Methods_TYPES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String str = MyUtils.Methods_TYPES[i];
                AddExpenceIncomeActivity.this.tvMethod.setText(str);
                AddExpenceIncomeActivity.this.method = str;
            }
        });
        builder.create().show();
    }

    private void categoryAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.dialog_category_picker, (ViewGroup) null);
        builder.setView(inflate);
        this.SubCategory = new ArrayList();
        this.list = (ListView) inflate.findViewById(R.id.color_list);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.select_category));
        if (this.checkcolumn) {
            this.SubCategory = new ArrayList();
            if (this.category == 1) {
                this.SubCategory = this.f60db.getCategoryList(1);
            } else {
                this.SubCategory = this.f60db.getCategoryList(2);
            }
        } else {
            Toast.makeText(this, "columns not found", Toast.LENGTH_SHORT).show();

            this.f60db.addcolumn();
        }
        this.alert = builder.create();
        this.adapter = new CategoryAdapter(this, this.SubCategory, this.currentDataBean.getSubCategoryId(), this.alert, this.tvCategory);
        this.list.setAdapter((ListAdapter) this.adapter);
        CategoryListHelper.getListViewSize(this.list);
    }


    public void selectCategory() {
        categoryAlert();
        this.alert.show();
    }


    public void changeUi() {
        if (this.rbIncome.isChecked()) {
            this.category = 2;
            this.rbExpense.setTextColor(getResources().getColor(R.color.switch_textcolor));
            this.rbIncome.setTextColor(getResources().getColor(R.color.white));
            this.rbIncome.setTypeface(null, Typeface.BOLD);
            this.rbExpense.setTypeface(null, Typeface.NORMAL);
            return;
        }
        this.category = 1;
        this.rbIncome.setTextColor(getResources().getColor(R.color.switch_textcolor));
        this.rbExpense.setTextColor(getResources().getColor(R.color.white));
        this.rbIncome.setTypeface(null, Typeface.NORMAL);
        this.rbExpense.setTypeface(null, Typeface.BOLD);
    }

    private void updateUI() {
        if (this.currentDataBean != null) {
            String replaceAll = this.currentDataBean.getAmount().replaceAll(",", "");
            try {
                EditText editText = this.etAmount;
                editText.setText("" + ((int) MyUtils.RoundOff(Double.parseDouble(replaceAll))));
            } catch (NumberFormatException unused) {
                this.etAmount.setText("0");
            }
            categoryAlert();
            this.tvMethod.setText(this.currentDataBean.getMethod());
            this.tvCategory.setText(this.currentDataBean.getCategoryModel().getName());
            if (this.category == 2) {
                this.rbIncome.setChecked(true);
            } else {
                this.rbExpense.setChecked(true);
            }
            try {
                this.calendar.setTime(MyUtils.sdfDatabase.parse(this.currentDataBean.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            updateDateView();
            this.etNote.setText(this.currentDataBean.getNote());
            if (!TextUtils.isEmpty(this.currentDataBean.getRefRecurring())) {
                this.tbRecurring.setChecked(true);
            }
        } else {
            this.currentDataBean = new DataBean();
        }
        this.Imagepath = this.currentDataBean.getImage();
        if (this.Imagepath.equals("")) {
            this.imgImage.setImageResource(R.drawable.camera);
        } else if (this.Imagepath.equals("no image")) {
            this.imgImage.setImageResource(R.drawable.camera);
        } else {
            this.imgImage.setImageURI(Uri.parse(this.Imagepath));
        }
        this.tbRecurring.setOnCheckedChangeListener(this);
        updateDateView();
        if (this.category == 1) {
            this.rbExpense.setChecked(true);
        } else {
            this.rbIncome.setChecked(true);
        }
    }

    protected void showNumberPiker() {
        NumberPicker numberPicker = new NumberPicker(this);
        final EditText editText = (EditText) numberPicker.findViewById(R.id.timepicker_input);
        new AlertDialog.Builder(this).setTitle(getString(R.string.recurring_date)).setView(numberPicker).setCancelable(false).setPositiveButton(getString(R.string.action_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String obj = editText.getText().toString();
                AddExpenceIncomeActivity.this.recurringDate = Integer.parseInt(obj.toString());
                AddExpenceIncomeActivity.this.recurringType = 1;
            }
        }).setNegativeButton(getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AddExpenceIncomeActivity.this.recurringType = -1;
                AddExpenceIncomeActivity.this.tbRecurring.setChecked(false);
            }
        }).show();
    }

    @Override
    public void onClick(View view) {
        view.getId();
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int i, Bundle bundle) {
        if (i == 0) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i2, int i3, int i4) {
                    if (AddExpenceIncomeActivity.this.recurringId != 0) {
                        AddExpenceIncomeActivity.this.recurringDate = i4;
                    }
                    AddExpenceIncomeActivity.this.calendar.set(i2, i3, i4);
                    AddExpenceIncomeActivity.this.updateDateView();
                }
            }, this.calendar.get(1), this.calendar.get(2), this.calendar.get(5)).show();
        }
        return super.onCreateDialog(i);
    }

    protected void updateDateView() {
        this.tvDate.setText(MyUtils.sdfUser.format(this.calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_income_expense, menu);
        Drawable wrap = DrawableCompat.wrap(menu.findItem(R.id.option_done).getIcon());
        DrawableCompat.setTint(wrap, ContextCompat.getColor(this, R.color.colorAccent));
        menu.findItem(R.id.option_done).setIcon(wrap);
        Drawable wrap2 = DrawableCompat.wrap(menu.findItem(R.id.option_delete).getIcon());
        DrawableCompat.setTint(wrap2, ContextCompat.getColor(this, R.color.colorAccent));
        menu.findItem(R.id.option_delete).setIcon(wrap2);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.option_delete:
                if (this.recurringId != 0) {
                    deleteAlertForRecurringData();
                } else {
                    deleteAlertForManagerData();
                }
                return true;
            case R.id.option_done:
                if (TextUtils.isEmpty(this.etAmount.getText().toString())) {
                    Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
                } else if (this.method.equals("")) {
                    Toast.makeText(this.context, "Please select method type", Toast.LENGTH_SHORT).show();
                } else if (this.tvCategory.getText().toString().equals("Select Category")) {
                    Toast.makeText(this.context, "Please select category", Toast.LENGTH_SHORT).show();
                } else {
                    int parseInt = Integer.parseInt(this.etAmount.getText().toString());
                    if (this.category == 1) {
                        int i = 0;
                        boolean aaaa = true;

                        while (aaaa) {
                            if (i < this.categoryItems.size()) {
                                if (this.tvCategory.getText().toString().equals(this.categoryItems.get(i).getName())) {
                                    this.getTotal = Integer.parseInt(this.categoryItems.get(i).getCategoryTotal());
                                    this.f55id = Integer.parseInt(this.categoryItems.get(i).getId());
                                    this.getLimit = Integer.parseInt(this.categoryItems.get(i).getCategoryLimit());
                                    aaaa = false;
                                } else {
                                    i++;
                                }
                            }
                        }
                        if (this.getLimit < Math.max(this.getTotal + parseInt, this.getLimit)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle(getString(R.string.over_spent));
                            builder.setMessage(getString(R.string.over_spent_msg));
                            builder.setCancelable(false);
                            builder.setPositiveButton(getString(R.string.change), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i2) {
                                    Intent intent = new Intent(AddExpenceIncomeActivity.this.context, AddEditCategory.class);
                                    intent.putExtra(TAG.CATEGORY, 1);
                                    intent.putExtra(TAG.DATA, AddExpenceIncomeActivity.this.f55id);
                                    AddExpenceIncomeActivity.this.startActivity(intent);
                                }
                            });
                            builder.setNegativeButton(getString(R.string.action_no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i2) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.create().show();
                        } else {
                            doneWithManagerData();
                        }
                    } else {
                        doneWithManagerData();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void updateAlertForRecurringData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener r1 = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    AddExpenceIncomeActivity.this.addUpdateData();
                    AddExpenceIncomeActivity.this.finish();
                }
            }
        };
        builder.setTitle(getString(R.string.be_careful));
        builder.setMessage(getString(R.string.be_careful_msg)).setPositiveButton(getString(R.string.action_yes), r1).setNegativeButton(getString(R.string.action_no), r1);
        builder.show();
    }

    private void doneWithManagerData() {
        String str;
        try {
            String obj = this.etAmount.getText().toString();
            String format = MyUtils.sdfDatabase.format(this.calendar.getTime());
            String obj2 = this.etNote.getText().toString();
            this.method = this.tvMethod.getText().toString();
            this.subCategory = ((CategoryAdapter) this.list.getAdapter()).getSelectedCategoryId();
            try {
                str = "" + Double.parseDouble(obj);
            } catch (NumberFormatException unused) {
                str = "0";
            }
            this.currentDataBean.setCategoryId("" + this.category);
            this.currentDataBean.setAmount(str);
            this.currentDataBean.setNote(obj2);
            this.currentDataBean.setDate(format);
            this.currentDataBean.setSubCategoryId(this.subCategory);
            this.currentDataBean.setMethod(this.method);
            this.currentDataBean.setImage(this.Imagepath);
            this.currentDataBean.setAccountRef(MyUtils.getCurrentAccount(this));
            if (this.recurringId != 0) {
                updateAlertForRecurringData();
                return;
            }
            addUpdateData();
            finish();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            Toast.makeText(this, "There is no category selected.\n Add some category first.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAlertForManagerData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener r1 = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case -2:
                        dialogInterface.dismiss();
                        return;
                    case -1:
                        AddExpenceIncomeActivity.this.f60db.deleteManagerData(AddExpenceIncomeActivity.this.currentDataBean);
                        AddExpenceIncomeActivity.this.finish();
                        return;
                    default:
                        return;
                }
            }
        };
        builder.setMessage(getString(R.string.confirm_delete)).setPositiveButton(getString(R.string.action_yes), r1).setNegativeButton(getString(R.string.action_no), r1);
        builder.show();
    }

    private void deleteAlertForRecurringData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener r1 = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case -2:
                        AddExpenceIncomeActivity.this.finish();
                        return;
                    case -1:
                        MyDatabaseHandler myDatabaseHandler = AddExpenceIncomeActivity.this.f60db;
                        myDatabaseHandler.deleteRecurringDataById("" + AddExpenceIncomeActivity.this.recurringId);
                        AddExpenceIncomeActivity.this.finish();
                        return;
                    default:
                        return;
                }
            }
        };
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(getString(R.string.delete_all_future_recurring_transaction)).setPositiveButton(getString(R.string.action_yes), r1).setNegativeButton(getString(R.string.action_no), r1);
        builder.show();
    }


    public void addUpdateData() {
        if (this.recurringId != 0) {
            RecurringBean convertToRecurringBean = this.currentDataBean.convertToRecurringBean();
            convertToRecurringBean.setId("" + this.recurringId);
            convertToRecurringBean.setRecurringDate("" + this.recurringDate);
            convertToRecurringBean.setRecurringType("" + this.recurringType);
            this.f60db.addUpdateRecurringData(convertToRecurringBean);
            return;
        }
        if (this.recurringType != -1) {
            RecurringBean convertToRecurringBean2 = this.currentDataBean.convertToRecurringBean();
            convertToRecurringBean2.setRecurringDate("" + this.recurringDate);
            convertToRecurringBean2.setRecurringType("" + this.recurringType);
            convertToRecurringBean2.setRecurringLastdate(MyUtils.sdfDatabase.format(MyUtils.getNextUpdateCalender(this.recurringType, this.calendar, this.recurringDate).getTime()));
            this.f60db.addUpdateRecurringData(convertToRecurringBean2);
        }
        this.f60db.addUpdateManagerData(this.currentDataBean);
    }


    @Override

    public void onResume() {
        super.onResume();
        this.categoryItems = new ArrayList();
        this.categoryItems = this.f60db.getSpeningCategory();
        for (int i = 0; i < this.categoryItems.size(); i++) {
            CategoryBean categoryBean = this.categoryItems.get(i);
            categoryBean.setCategoryTotal(this.f60db.getCategoryAmount(categoryBean.getCategoryGroup(), categoryBean.getId()));
        }
    }


    @Override

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == this.PICK_IMAGE && i2 == -1 && intent != null) {
            Uri data = intent.getData();
            try {
                UUID randomUUID = UUID.randomUUID();
                this.Imagepath = getFilesDir() + "/bill-" + randomUUID.toString() + ".png";
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data);
                FileOutputStream fileOutputStream = new FileOutputStream(this.Imagepath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                this.imgImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (i == 111 && i2 == -1 && intent != null) {
            intent.getData();
            try {
                UUID randomUUID2 = UUID.randomUUID();
                this.Imagepath = getFilesDir() + "/bill-" + randomUUID2.toString() + ".png";
                Bitmap createScaledBitmap = Bitmap.createScaledBitmap((Bitmap) intent.getExtras().get("data"), 200, 200, true);
                FileOutputStream fileOutputStream2 = new FileOutputStream(this.Imagepath);
                createScaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream2);
                fileOutputStream2.flush();
                fileOutputStream2.close();
                this.imgImage.setImageBitmap(createScaledBitmap);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1) {
            if (iArr[0] != 0) {
                AlertDialog create = new AlertDialog.Builder(this).setPositiveButton("Let's Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        AddExpenceIncomeActivity.verifyStoragePermissions(AddExpenceIncomeActivity.this);
                    }
                }).create();
                create.setTitle("Notice!");
                create.setMessage("Allowing storage permissions is crucial for the app to work. Please grant the permissions.");
                create.show();
                return;
            }
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction("android.intent.action.GET_CONTENT");
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), this.PICK_IMAGE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (z) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle(getString(R.string.recurring_type));
            builder.setItems(MyDatabaseHandler.RecurringTable.RECURRING_TYPES, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AddExpenceIncomeActivity.this.selectRecurringItem(i);
                }
            });
            builder.create().show();
            return;
        }
        Toast.makeText(this.context, getString(R.string.recurring_delete), Toast.LENGTH_SHORT).show();
        this.recurringType = -1;
    }

    protected void selectRecurringItem(int i) {
        if (i == 0) {
            this.recurringType = 0;
        } else if (i == 1) {
            this.recurringType = 2;
        } else if (i == 2) {
            this.recurringType = 1;
        } else if (i == 3) {
            this.recurringType = 3;
        } else if (i == 4) {
            this.recurringType = 4;
        } else if (i == 5) {
            this.recurringType = 5;
        }

        Toast.makeText(this, ((Object) MyDatabaseHandler.RecurringTable.RECURRING_TYPES[i]) + getString(R.string.RecurringCreated), Toast.LENGTH_SHORT).show();


        if (this.recurringType == 1) {
            showNumberPiker();
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
