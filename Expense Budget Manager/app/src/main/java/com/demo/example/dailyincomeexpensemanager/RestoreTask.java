package com.demo.example.dailyincomeexpensemanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.demo.example.dailyincomeexpensemanager.bean.AccountBean;
import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.dailyincomeexpensemanager.bean.DataBean;
import com.demo.example.dailyincomeexpensemanager.bean.RecurringBean;
import com.demo.example.dailyincomeexpensemanager.bean.TemplateBean;
import com.demo.example.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class RestoreTask extends AsyncTask<Void, Void, Void> {
    protected List<AccountBean> accounts = new ArrayList();
    private Context context;


    private MyDatabaseHandler f84db;
    private ProgressDialog dialog;

    public RestoreTask(Context context) {
        this.context = context;
        this.f84db = new MyDatabaseHandler(this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(this.context);
        this.dialog.setTitle(this.context.getString(R.string.restore));
        this.dialog.setMessage(this.context.getString(R.string.msg_please_wait));
        this.dialog.setCancelable(false);
        this.dialog.show();
    }


    public void onPostExecute(Void r2) {
        super.onPostExecute(r2);
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
        if (this.accounts == null || this.accounts.size() <= 0 || this.accounts.get(0).getCategories().size() <= 0) {
            alertForClearDatabaseAndUpdate("Seems No data available in your backup file. Are you sure to restore?");
        } else {
            alertForClearDatabaseAndUpdate("It will delete all the current data and update database with your last backup. Are you sure to restore?");
        }
    }

    private void alertForClearDatabaseAndUpdate(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(this.context.getString(R.string.restore));
        builder.setMessage(str);
        builder.setPositiveButton(this.context.getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                RestoreTask.this.userWantUpdate();
            }
        });
        builder.setNegativeButton(this.context.getString(R.string.action_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    protected void userWantUpdate() {
        restoreAccountDataToDatabase(this.accounts);
        alertRestoreComplateRestartApp("App required restart to take affect of new data. Do you want to restart your app?");
    }

    private void alertRestoreComplateRestartApp(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(this.context.getString(R.string.msg_data_restored_successfully));
        builder.setMessage(str);
        builder.setPositiveButton(this.context.getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                MyUtils.restartApp(RestoreTask.this.context);
            }
        });
        builder.setNegativeButton(this.context.getString(R.string.action_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


    public Void doInBackground(Void... voidArr) {
        this.accounts = getAccountsFromBackupFile();
        return null;
    }

    private void restoreAccountDataToDatabase(List<AccountBean> list) {
        this.f84db = new MyDatabaseHandler(this.context);
        this.f84db.clearDatabase();
        for (AccountBean accountBean : list) {
            accountBean.setId(String.valueOf(this.f84db.addUpdateAccountData(accountBean)));
            restoreAccountToDatabase(accountBean);
        }
    }

    private void restoreAccountToDatabase(AccountBean accountBean) {
        for (CategoryBean categoryBean : accountBean.getCategories()) {
            categoryBean.setAccountRef(accountBean.getId());
            categoryBean.setId(String.valueOf(this.f84db.addUpdateCategoryData(categoryBean)));
            for (DataBean dataBean : categoryBean.getManagerData()) {
                dataBean.setAccountRef(accountBean.getId());
                dataBean.setCategoryId(String.valueOf(categoryBean.getCategoryGroup()));
                dataBean.setSubCategoryId(categoryBean.getId());
                dataBean.setId(String.valueOf(this.f84db.addUpdateManagerData(dataBean)));
            }
        }
    }

    private List<AccountBean> getAccountsFromBackupFile() {
        ArrayList arrayList = new ArrayList();
        try {
            return readAndParseXml();
        } catch (IOException e) {
            e.printStackTrace();

            Log.e("readAndParseXml11 erroe", "" + e.getMessage());

            return arrayList;
        } catch (ParserConfigurationException e2) {
            e2.printStackTrace();
            Log.e("readAndParseXml11 erroe", "" + e2.getMessage());

            return arrayList;
        } catch (SAXException e3) {
            e3.printStackTrace();
            Log.e("readAndParseXml11 erroe", "" + e3.getMessage());

            return arrayList;
        }
    }

    private List<AccountBean> readAndParseXml() throws ParserConfigurationException, SAXException, IOException {
        Log.e("readAndParseXml", "readAndParseXml");
        File aa = new File(BackupTask.MOBYI_DIRECTORY, BackupTask.FILE_NAME);
        copy(aa, aa);
        return getListAccounts(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(BackupTask.MOBYI_DIRECTORY, BackupTask.FILE_NAME)).getDocumentElement());
    }

    public static void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (Exception e) {
                Log.e("fffff", "" + e.getMessage());
            }
        } catch (Exception e) {
            Log.e("fffff1", "" + e.getMessage());
        }
    }


    private List<AccountBean> getListAccounts(Element element) {

        Log.e("getListAccounts", "getListAccounts");

        ArrayList arrayList = new ArrayList();
        NodeList elementsByTagName = element.getElementsByTagName(MyDatabaseHandler.AccountTable.TABLE_NAME);
        for (int i = 0; i < elementsByTagName.getLength(); i++) {
            Node item = elementsByTagName.item(i);
            Log.e("1111", "yes");
            if (item instanceof Element) {
                AccountBean accountBean = getAccountBean(item);
                accountBean.setCategories(getListCategories(item));
                arrayList.add(accountBean);
                Log.e("222222", "yes");

            }
        }
        return arrayList;
    }

    private List<CategoryBean> getListCategories(Node node) {
        ArrayList arrayList = new ArrayList();
        NodeList elementsByTagName = ((Element) node).getElementsByTagName(MyDatabaseHandler.Category.TABLE_NAME);
        for (int i = 0; i < elementsByTagName.getLength(); i++) {
            Node item = elementsByTagName.item(i);
            if (item instanceof Element) {
                CategoryBean categoryBean = getCategoryBean(item);
                categoryBean.setManagerData(getListManagers(item));
                arrayList.add(categoryBean);
            }
        }
        return arrayList;
    }

    private List<RecurringBean> getListRecurrings(Node node) {
        ArrayList arrayList = new ArrayList();
        NodeList elementsByTagName = ((Element) node).getElementsByTagName(MyDatabaseHandler.RecurringTable.TABLE_NAME);
        for (int i = 0; i < elementsByTagName.getLength(); i++) {
            Node item = elementsByTagName.item(i);
            if (item instanceof Element) {
                arrayList.add(getRecurringBean(item));
            }
        }
        return arrayList;
    }

    private List<DataBean> getListManagers(Node node) {
        ArrayList arrayList = new ArrayList();
        NodeList elementsByTagName = ((Element) node).getElementsByTagName(MyDatabaseHandler.ManagerTable.TABLE_NAME);
        for (int i = 0; i < elementsByTagName.getLength(); i++) {
            Node item = elementsByTagName.item(i);
            if (item instanceof Element) {
                arrayList.add(getManagerBean(item));
            }
        }
        return arrayList;
    }

    private List<TemplateBean> getListTemplates(Node node) {
        ArrayList arrayList = new ArrayList();
        NodeList elementsByTagName = ((Element) node).getElementsByTagName(MyDatabaseHandler.TemplatesTable.TABLE_NAME);
        for (int i = 0; i < elementsByTagName.getLength(); i++) {
            Node item = elementsByTagName.item(i);
            if (item instanceof Element) {
                arrayList.add(getTemplateBean(item));
            }
        }
        return arrayList;
    }

    private TemplateBean getTemplateBean(Node node) {
        String nodeValue = node.getAttributes().getNamedItem(MyDatabaseHandler.TemplatesTable.TEMPLATES).getNodeValue();
        Log.e("Templates ", " Name " + nodeValue);
        TemplateBean templateBean = new TemplateBean();
        templateBean.setTemplate(nodeValue);
        return templateBean;
    }

    private CategoryBean getCategoryBean(Node node) {
        String nodeValue = node.getAttributes().getNamedItem(MyDatabaseHandler.Category.CAT_NAME).getNodeValue();
        String nodeValue2 = node.getAttributes().getNamedItem(MyDatabaseHandler.Category.CAT_COLOR).getNodeValue();
        String nodeValue3 = node.getAttributes().getNamedItem(MyDatabaseHandler.Category.CAT_TYPE).getNodeValue();
        String nodeValue4 = node.getAttributes().getNamedItem("drag_id").getNodeValue();
        String nodeValue5 = node.getAttributes().getNamedItem("amount_limit") != null ? node.getAttributes().getNamedItem("amount_limit").getNodeValue() : "0";
        Log.e("Category ", " Name " + nodeValue + " Color " + nodeValue2 + " Type " + nodeValue3 + "Spent Limit : " + nodeValue5);
        CategoryBean categoryBean = new CategoryBean();
        categoryBean.setName(nodeValue);
        categoryBean.setColor(nodeValue2);
        categoryBean.setCategoryGroup(Integer.parseInt(nodeValue3));
        categoryBean.setDrag_id(Integer.parseInt(nodeValue4));
        categoryBean.setCategoryLimit(nodeValue5);
        return categoryBean;
    }

    private DataBean getManagerBean(Node node) {
        String str = "Wire Transfer";
        String str2 = "no image";
        String str3 = "0";
        String nodeValue = node.getAttributes().getNamedItem("amount").getNodeValue();
        String nodeValue2 = node.getAttributes().getNamedItem("date").getNodeValue();
        String nodeValue3 = node.getAttributes().getNamedItem("note").getNodeValue();
        if (node.getAttributes().getNamedItem("method") != null) {
            str = node.getAttributes().getNamedItem("method").getNodeValue();
        }
        if (node.getAttributes().getNamedItem("image") != null) {
            str2 = node.getAttributes().getNamedItem("image").getNodeValue();
        }
        if (node.getAttributes().getNamedItem("amount_limit") != null) {
            str3 = node.getAttributes().getNamedItem("amount_limit").getNodeValue();
        }
        Log.e("Manager ", " Amount " + nodeValue + " Date " + nodeValue2 + " Note " + nodeValue3 + " Method " + str + " Image " + str2 + " Spent Limit " + str3);
        DataBean dataBean = new DataBean();
        dataBean.setAmount(nodeValue);
        dataBean.setDate(nodeValue2);
        dataBean.setNote(nodeValue3);
        dataBean.setCategoryLimit(str3);
        dataBean.setMethod(str);
        dataBean.setImage(str2);
        return dataBean;
    }

    private RecurringBean getRecurringBean(Node node) {
        String str = "Wire Transfer";
        String str2 = "no image";
        String str3 = "0";
        String nodeValue = node.getAttributes().getNamedItem("amount").getNodeValue();
        String nodeValue2 = node.getAttributes().getNamedItem(MyDatabaseHandler.RecurringTable.RECURRING_TYPE).getNodeValue();
        String nodeValue3 = node.getAttributes().getNamedItem(MyDatabaseHandler.RecurringTable.RECURRING_LASTDATE).getNodeValue();
        String nodeValue4 = node.getAttributes().getNamedItem("note").getNodeValue();
        String nodeValue5 = node.getAttributes().getNamedItem(MyDatabaseHandler.RecurringTable.RECURRING_DATE).getNodeValue();
        if (node.getAttributes().getNamedItem("method") != null) {
            str = node.getAttributes().getNamedItem("method").getNodeValue();
        }
        if (node.getAttributes().getNamedItem("image") != null) {
            str2 = node.getAttributes().getNamedItem("image").getNodeValue();
        }
        if (node.getAttributes().getNamedItem("amount_limit") != null) {
            str3 = node.getAttributes().getNamedItem("amount_limit").getNodeValue();
        }
        Log.e("Recurring ", " Amount " + nodeValue + " Type " + nodeValue2 + " Last Date " + nodeValue3 + " Note " + nodeValue4 + " Date " + nodeValue5 + " Method " + str + " Image " + str2 + "Spent Limit " + str3);
        RecurringBean recurringBean = new RecurringBean();
        recurringBean.setAmount(nodeValue);
        recurringBean.setRecurringType(nodeValue2);
        recurringBean.setRecurringLastdate(nodeValue3);
        recurringBean.setNote(nodeValue4);
        recurringBean.setRecurringDate(nodeValue5);
        recurringBean.setMethod(str);
        recurringBean.setImage(str2);
        recurringBean.setCategoryLimit(str3);
        return recurringBean;
    }

    private AccountBean getAccountBean(Node node) {
        String nodeValue = node.getAttributes().getNamedItem("name").getNodeValue();
        String nodeValue2 = node.getAttributes().getNamedItem("email").getNodeValue();
        String nodeValue3 = node.getAttributes().getNamedItem("currency").getNodeValue();
        Log.e("Account", " Name " + nodeValue + " Email " + nodeValue2 + " Currency " + nodeValue3);
        AccountBean accountBean = new AccountBean();
        accountBean.setName(nodeValue);
        accountBean.setEmail(nodeValue2);
        accountBean.setCurrency(Integer.parseInt(nodeValue3));
        return accountBean;
    }
}
