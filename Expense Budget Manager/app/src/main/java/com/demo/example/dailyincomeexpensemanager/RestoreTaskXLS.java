package com.demo.example.dailyincomeexpensemanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.demo.example.dailyincomeexpensemanager.bean.AccountBean;

import java.io.File;
import java.io.IOException;

import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class RestoreTaskXLS extends AsyncTask<Void, Void, Void> {
    public static final String AMOUNT = "amount";
    public static final String CAT = "cat";
    public static final String COLOR = "color";
    public static final String DATE = "date";
    public static final String DRAGID = "drag_id";
    public static final String FILE_NAME = "DailyIncomeExpenseManager.xls";
    public static final String ITEM = "item";
    public static final String NAME = "name";
    public static final String NOTE = "note";
    public static final String SUBCAT = "subcat";
    private String accountId;
    private Context context;


    private MyDatabaseHandler f85db;
    private int defaultColumn = 0;
    private ProgressDialog dialog;

    public RestoreTaskXLS(Context context) {
        this.accountId = "1";
        this.context = context;
        this.accountId = MyUtils.getCurrentAccount(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(this.context);
        this.dialog.setTitle("Restore");
        this.dialog.setMessage("Please wait");
        this.dialog.show();
    }


    public void onPostExecute(Void r3) {
        super.onPostExecute(r3);
        Toast.makeText(this.context, "Data Restored Succsessfully", Toast.LENGTH_SHORT).show();
    }


    public Void doInBackground(Void... voidArr) {
        restoreTaskv15();
        return null;
    }

    private void restoreTaskv15() {
        try {
            readAndParsexls();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AccountBean readAndParsexls() throws IOException {
        try {
            Workbook workbook = Workbook.getWorkbook(new File(XLSBackupTask.MOBYI_DIRECTORY, "DailyIncomeExpenseManager.xls"));
            for (int i = 0; i < workbook.getSheets().length; i++) {
                getSheetToAccount(workbook.getSheet(i));
            }
            return null;
        } catch (BiffException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getSheetToAccount(Sheet sheet) {
        prepareSheetHeader(sheet, "Income Data");
    }

    private LabelCell prepareSheetHeader(Sheet sheet, String str) {
        return sheet.findLabelCell(str);
    }
}
