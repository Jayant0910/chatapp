package com.demo.example.dailyincomeexpensemanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.demo.example.dailyincomeexpensemanager.bean.AccountBean;
import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.dailyincomeexpensemanager.bean.DataBean;
import com.demo.example.R;
import com.github.mikephil.charting.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class XLSBackupTask extends AsyncTask<Void, Void, Void> {
    public static final String FILE_NAME = "DailyIncomeExpenseManager.xls";
    public static final File MOBYI_DIRECTORY = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Daily Income Expense Manager");


    private Context context;
    private ProgressDialog dialog;
    private int defaultColumn = 0;
    private int row = 0;
    private int column = this.defaultColumn;

    public XLSBackupTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(this.context);
        this.dialog.setTitle("Excel Backup");
        this.dialog.setMessage(this.context.getString(R.string.msg_please_wait));
        this.dialog.setCancelable(false);
        this.dialog.show();
    }


    public void onPostExecute(Void r3) {
        super.onPostExecute(r3);
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
        Context context = this.context;
        Toast.makeText(context, "We have sent backup at your email address and also store backup file at" + BackupTask.MOBYI_DIRECTORY, Toast.LENGTH_LONG).show();
        maildata();
    }

    private void maildata() {
        try {
            Uri data = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", getFile());
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.STREAM", data);
            intent.putExtra("android.intent.extra.SUBJECT", "Daily Income Expense Manager Backup File");
            intent.setPackage("com.google.android.gm");
            this.context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Void doInBackground(Void... voidArr) {
        try {
            createBackupFile("DailyIncomeExpenseManager.xls");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalStateException e3) {
            e3.printStackTrace();
            return null;
        } catch (WriteException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    private void prepareXLSWorkSheet(WritableWorkbook writableWorkbook) throws RowsExceededException, WriteException {
        List<AccountBean> dataFromDatabase = getDataFromDatabase();
        for (int i = 0; i < dataFromDatabase.size(); i++) {
            this.defaultColumn = 0;
            initVar(0, this.defaultColumn);
            AccountBean accountBean = dataFromDatabase.get(i);
            addAccountToSheet(writableWorkbook.createSheet(accountBean.getName(), i), accountBean);
        }
    }

    private void addAccountToSheet(WritableSheet writableSheet, AccountBean accountBean) throws RowsExceededException, WriteException {
        prepareSheetHeader(writableSheet, "Income Data");
        addHeaderRow(writableSheet);
        addCateogryDataToSheet(writableSheet, accountBean.getIncomeCategory());
        this.defaultColumn = 7;
        initVar(0, this.defaultColumn);
        prepareSheetHeader(writableSheet, "Expense Data");
        addHeaderRow(writableSheet);
        addCateogryDataToSheet(writableSheet, accountBean.getExpenseCategory());
    }

    private void initVar(int i, int i2) {
        this.row = i;
        this.column = i2;
    }

    private void prepareSheetHeader(WritableSheet writableSheet, String str) throws RowsExceededException, WriteException {
        writableSheet.addCell(new Label(this.defaultColumn + 1, this.row, str));
        this.row++;
    }

    private void addCateogryDataToSheet(WritableSheet writableSheet, List<CategoryBean> list) throws RowsExceededException, WriteException {
        for (int i = 0; i < list.size(); i++) {
            addCategoryToSheet(writableSheet, list.get(i));
        }
    }

    private void addCategoryToSheet(WritableSheet writableSheet, CategoryBean categoryBean) throws RowsExceededException, WriteException {
        for (int i = 0; i < categoryBean.getManagerData().size(); i++) {
            DataBean dataBean = categoryBean.getManagerData().get(i);
            addRow(writableSheet, dataBean.getDate(), dataBean.getAmount(), categoryBean.getName(), dataBean.getNote(), dataBean.getMethod(), dataBean.getImage(), dataBean.getCategoryLimit());
            this.row++;
        }
    }

    private void addRow(WritableSheet writableSheet, String str, String str2, String str3, String str4, String str5, String str6, String str7) throws RowsExceededException, WriteException {
        writableSheet.addCell(getDateCell(str));
        writableSheet.addCell(getNumberCell(str2));
        writableSheet.addCell(getLabelCell(str3));
        writableSheet.addCell(getLabelCell(str4));
        writableSheet.addCell(getLabelCell(str5));
        writableSheet.addCell(getLabelCell(str6));
        writableSheet.addCell(getLabelCell(str7));
        this.column = this.defaultColumn;
    }

    private void addHeaderRow(WritableSheet writableSheet) throws RowsExceededException, WriteException {
        writableSheet.addCell(getLabelCell("Date"));
        writableSheet.addCell(getLabelCell("Amount"));
        writableSheet.addCell(getLabelCell("Category"));
        writableSheet.addCell(getLabelCell("Note"));
        writableSheet.addCell(getLabelCell("Method"));
        writableSheet.addCell(getLabelCell("Image"));
        writableSheet.addCell(getLabelCell("SpentLimit"));
        this.column = this.defaultColumn;
        this.row++;
    }

    private WritableCell getDateCell(String str) {
        Date date;
        Date date2 = new Date();
        try {
            date = MyUtils.sdfDatabase.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            date = date2;
        }
        DateTime dateTime = new DateTime(this.column, this.row, date, new WritableCellFormat(new DateFormat("d/M/yyyy")));
        this.column++;
        return dateTime;
    }

    private WritableCell getNumberCell(String str) {
        double d;
        try {
            d = Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
            d = Utils.DOUBLE_EPSILON;
        }
        Number number = new Number(this.column, this.row, d);
        this.column++;
        return number;
    }

    private WritableCell getLabelCell(String str) {
        Label label = new Label(this.column, this.row, str);
        this.column++;
        return label;
    }

    private File getFile() {
        return new File(MOBYI_DIRECTORY, "DailyIncomeExpenseManager.xls");
    }

    private String createBackupFile(String str) throws IOException, WriteException {
        MOBYI_DIRECTORY.mkdirs();
        File file = new File(MOBYI_DIRECTORY, str);
        WritableWorkbook createWorkbook = Workbook.createWorkbook(file);
        prepareXLSWorkSheet(createWorkbook);
        createWorkbook.write();
        createWorkbook.close();
        return file.getAbsolutePath();
    }

    private List<AccountBean> getDataFromDatabase() {
        MyDatabaseHandler myDatabaseHandler = new MyDatabaseHandler(this.context);
        List<AccountBean> accountList = myDatabaseHandler.getAccountList();
        for (AccountBean accountBean : accountList) {
            myDatabaseHandler.setAccount(accountBean.getId());
            List<CategoryBean> categoryData = getCategoryData(myDatabaseHandler, 2);
            List<CategoryBean> categoryData2 = getCategoryData(myDatabaseHandler, 1);
            accountBean.setIncomeCategory(categoryData);
            accountBean.setExpenseCategory(categoryData2);
            for (CategoryBean categoryBean : categoryData) {
                categoryBean.setManagerData(getManagerData(myDatabaseHandler, categoryBean.getId()));
            }
            for (CategoryBean categoryBean2 : categoryData2) {
                categoryBean2.setManagerData(getManagerData(myDatabaseHandler, categoryBean2.getId()));
            }
        }
        return accountList;
    }

    private List<CategoryBean> getCategoryData(MyDatabaseHandler myDatabaseHandler, int i) {
        return myDatabaseHandler.getCategoryListToBackup(i);
    }

    private List<DataBean> getManagerData(MyDatabaseHandler myDatabaseHandler, String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(myDatabaseHandler.getManagerDataByCategoryByTime(1, str, "1", "9"));
        arrayList.addAll(myDatabaseHandler.getManagerDataByCategoryByTime(2, str, "1", "9"));
        return arrayList;
    }
}
