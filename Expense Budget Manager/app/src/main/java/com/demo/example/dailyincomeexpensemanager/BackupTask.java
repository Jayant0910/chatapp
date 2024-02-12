package com.demo.example.dailyincomeexpensemanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.demo.example.dailyincomeexpensemanager.bean.AccountBean;
import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.dailyincomeexpensemanager.bean.DataBean;
import com.demo.example.dailyincomeexpensemanager.bean.RecurringBean;
import com.demo.example.dailyincomeexpensemanager.bean.TemplateBean;
import com.demo.example.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;


public class BackupTask extends AsyncTask<Void, Void, Void> {
    public static final String FILE_NAME = "DailyIncomeExpenseManager.xml";
    public static final File MOBYI_DIRECTORY = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Daily Income Expense Manager");


    private Context context;
    private ProgressDialog dialog;

    public BackupTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(this.context);
        this.dialog.setTitle("Xml Backup");
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
        Toast.makeText(context, "We have sent backup at your email address and also store backup file at" + MOBYI_DIRECTORY, Toast.LENGTH_LONG).show();
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

    private File getFile() {
        return new File(MOBYI_DIRECTORY, FILE_NAME);
    }

    
    public Void doInBackground(Void... voidArr) {
        try {
            createBackupFile(prepareXMLFromDatabase(), FILE_NAME);
            return null;
        } catch (IOException e) {
            e.printStackTrace();

            Log.e("sssss", "" + e.getMessage());
            return null;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            Log.e("sssss1", "" + e2.getMessage());

            return null;
        } catch (IllegalStateException e3) {
            e3.printStackTrace();
            Log.e("sssss2", "" + e3.getMessage());

            return null;
        }
    }

    private String createBackupFile(String str, String str2) throws IOException {
        MOBYI_DIRECTORY.mkdirs();
        File file = new File(MOBYI_DIRECTORY, str2);
        Log.e("ddddddddd", "" + file.getPath());
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(str);
        fileWriter.close();
        return file.getAbsolutePath();
    }

    private String prepareXMLFromDatabase() throws IllegalArgumentException, IllegalStateException, IOException {
        StringWriter stringWriter = new StringWriter();
        XmlSerializer defaultXmlSerializer = getDefaultXmlSerializer(stringWriter);
        defaultXmlSerializer.startTag("", "file");
        updateXmlWithAccounts(defaultXmlSerializer, getDataFromDatabase());
        defaultXmlSerializer.endTag("", "file");
        defaultXmlSerializer.endDocument();
        return stringWriter.toString();
    }

    private void updateXmlWithAccounts(XmlSerializer xmlSerializer, List<AccountBean> list) throws IllegalArgumentException, IllegalStateException, IOException {
        for (AccountBean accountBean : list) {
            xmlSerializer.startTag("", MyDatabaseHandler.AccountTable.TABLE_NAME);
            xmlSerializer.attribute("", "name", accountBean.getName());
            xmlSerializer.attribute("", "email", accountBean.getEmail());
            xmlSerializer.attribute("", "currency", "" + accountBean.getCurrency());
            updateXmlWithCategories(xmlSerializer, accountBean.getCategories());
            xmlSerializer.endTag("", MyDatabaseHandler.AccountTable.TABLE_NAME);
        }
    }

    private void updateXmlWithTemplates(XmlSerializer xmlSerializer, List<TemplateBean> list) throws IllegalArgumentException, IllegalStateException, IOException {
        for (TemplateBean templateBean : list) {
            xmlSerializer.startTag("", MyDatabaseHandler.TemplatesTable.TABLE_NAME);
            xmlSerializer.attribute("", MyDatabaseHandler.TemplatesTable.TEMPLATES, templateBean.getTemplate());
            xmlSerializer.endTag("", MyDatabaseHandler.TemplatesTable.TABLE_NAME);
        }
    }

    private XmlSerializer getDefaultXmlSerializer(StringWriter stringWriter) throws IllegalArgumentException, IllegalStateException, IOException {
        XmlSerializer newSerializer = Xml.newSerializer();
        newSerializer.setOutput(stringWriter);
        newSerializer.startDocument("UTF-8", true);
        newSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        return newSerializer;
    }

    private void updateXmlWithCategories(XmlSerializer xmlSerializer, List<CategoryBean> list) throws IllegalArgumentException, IllegalStateException, IOException {
        for (CategoryBean categoryBean : list) {
            xmlSerializer.startTag("", MyDatabaseHandler.Category.TABLE_NAME);
            xmlSerializer.attribute("", MyDatabaseHandler.Category.CAT_NAME, categoryBean.getName());
            xmlSerializer.attribute("", MyDatabaseHandler.Category.CAT_COLOR, categoryBean.getColor());
            xmlSerializer.attribute("", MyDatabaseHandler.Category.CAT_TYPE, "" + categoryBean.getCategoryGroup());
            xmlSerializer.attribute("", "drag_id", "" + categoryBean.getDrag_id());
            xmlSerializer.attribute("", "amount_limit", "" + categoryBean.getCategoryLimit());
            for (DataBean dataBean : categoryBean.getManagerData()) {
                updateXmlWithManager(xmlSerializer, dataBean);
            }
            xmlSerializer.endTag("", MyDatabaseHandler.Category.TABLE_NAME);
        }
    }

    private void updateXmlWithManager(XmlSerializer xmlSerializer, DataBean dataBean) throws IllegalArgumentException, IllegalStateException, IOException {
        xmlSerializer.startTag("", MyDatabaseHandler.ManagerTable.TABLE_NAME);
        xmlSerializer.attribute("", "amount", dataBean.getAmount());
        xmlSerializer.attribute("", "date", dataBean.getDate());
        xmlSerializer.attribute("", "note", dataBean.getNote());
        xmlSerializer.attribute("", "amount_limit", dataBean.getCategoryLimit());
        xmlSerializer.attribute("", "method", dataBean.getMethod());
        xmlSerializer.attribute("", "image", dataBean.getImage());
        xmlSerializer.endTag("", MyDatabaseHandler.ManagerTable.TABLE_NAME);
    }

    private void updateXmlWithRecurring(XmlSerializer xmlSerializer, RecurringBean recurringBean) throws IllegalArgumentException, IllegalStateException, IOException {
        xmlSerializer.startTag("", MyDatabaseHandler.RecurringTable.TABLE_NAME);
        xmlSerializer.attribute("", "amount", recurringBean.getAmount());
        xmlSerializer.attribute("", MyDatabaseHandler.RecurringTable.RECURRING_TYPE, recurringBean.getRecurringType());
        xmlSerializer.attribute("", MyDatabaseHandler.RecurringTable.RECURRING_LASTDATE, recurringBean.getRecurringLastdate());
        xmlSerializer.attribute("", "note", recurringBean.getNote());
        xmlSerializer.attribute("", "amount_limit", recurringBean.getCategoryLimit());
        xmlSerializer.attribute("", "image", recurringBean.getImage());
        xmlSerializer.attribute("", "method", recurringBean.getMethod());
        xmlSerializer.attribute("", MyDatabaseHandler.RecurringTable.RECURRING_DATE, recurringBean.getRecurringDate());
        xmlSerializer.endTag("", MyDatabaseHandler.RecurringTable.TABLE_NAME);
    }

    private List<AccountBean> getDataFromDatabase() {
        MyDatabaseHandler myDatabaseHandler = new MyDatabaseHandler(this.context);
        List<AccountBean> accountList = myDatabaseHandler.getAccountList();
        for (AccountBean accountBean : accountList) {
            myDatabaseHandler.setAccount(accountBean.getId());
            accountBean.setTemplates(myDatabaseHandler.getTemplateListByAccount());
            List<CategoryBean> categoryData = getCategoryData(myDatabaseHandler);
            accountBean.setCategories(categoryData);
            for (CategoryBean categoryBean : categoryData) {
                categoryBean.setManagerData(getManagerData(myDatabaseHandler, categoryBean.getId()));
                categoryBean.setRecurringData(getRecurringData(myDatabaseHandler, categoryBean.getId()));
            }
        }
        return accountList;
    }

    private List<CategoryBean> getCategoryData(MyDatabaseHandler myDatabaseHandler) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(myDatabaseHandler.getCategoryListToBackup(1));
        arrayList.addAll(myDatabaseHandler.getCategoryListToBackup(2));
        return arrayList;
    }

    private List<DataBean> getManagerData(MyDatabaseHandler myDatabaseHandler, String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(myDatabaseHandler.getManagerDataByCategoryByTime(1, str, "1", "9"));
        arrayList.addAll(myDatabaseHandler.getManagerDataByCategoryByTime(2, str, "1", "9"));
        return arrayList;
    }

    private List<RecurringBean> getRecurringData(MyDatabaseHandler myDatabaseHandler, String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(myDatabaseHandler.getRecurringModelList(1, str));
        arrayList.addAll(myDatabaseHandler.getRecurringModelList(2, str));
        return arrayList;
    }
}
