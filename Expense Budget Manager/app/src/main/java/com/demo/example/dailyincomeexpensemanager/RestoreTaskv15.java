package com.demo.example.dailyincomeexpensemanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.demo.example.dailyincomeexpensemanager.bean.AccountBean;
import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.dailyincomeexpensemanager.bean.DataBean;
import com.demo.example.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class RestoreTaskv15 extends AsyncTask<Void, Void, Void> {
    public static final String AMOUNT = "amount";
    public static final String CAT = "cat";
    public static final String CAT_LIMIT = "amount_limit";
    public static final String COLOR = "color";
    public static final String DATE = "date";
    public static final String DRAGID = "drag_id";
    public static final String EXPENSE_CATEGORY = "expence_cat_xml.xml";
    public static final String EXPENSE_DATA = "expence_data_xml.xml";
    public static final String IMAGE = "image";
    public static final String INCOME_CATEGORY = "income_cat_xml.xml";
    public static final String INCOME_DATA = "income_data_xml.xml";
    public static final String ITEM = "item";
    public static final String METHOD = "method";
    public static final String NAME = "name";
    public static final String NOTE = "note";
    public static final String SUBCAT = "subcat";
    private String accountId;
    private Context context;


    private MyDatabaseHandler f86db;
    private ProgressDialog dialog;

    public RestoreTaskv15(Context context) {
        this.accountId = "1";
        this.context = context;
        this.accountId = MyUtils.getCurrentAccount(context);
    }

    public static String getValue(Node node, String str) {
        return getElementValue(((Element) node).getElementsByTagName(str).item(0));
    }

    public static final String getElementValue(Node node) {
        if (node == null || !node.hasChildNodes()) {
            return "";
        }
        for (Node firstChild = node.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            if (firstChild.getNodeType() == 3) {
                return firstChild.getNodeValue();
            }
        }
        return "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(this.context);
        this.dialog.setTitle(this.context.getString(R.string.restore));
        this.dialog.setMessage(this.context.getString(R.string.msg_please_wait));
        this.dialog.show();
    }


    public void onPostExecute(Void r3) {
        super.onPostExecute(r3);
        Toast.makeText(this.context, this.context.getString(R.string.msg_data_restored_successfully), Toast.LENGTH_SHORT).show();
    }


    public Void doInBackground(Void... voidArr) {
        restoreTaskv15();
        return null;
    }

    private void restoreTaskv15() {
        try {
            readAndParseXml();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e2) {
            e2.printStackTrace();
        } catch (SAXException e3) {
            e3.printStackTrace();
        }
    }

    private AccountBean readAndParseXml() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        List<CategoryBean> categories = getCategories(newDocumentBuilder);
        List<DataBean> managerData = getManagerData(newDocumentBuilder);
        this.f86db = new MyDatabaseHandler(this.context);
        if (categories.size() <= 0 || managerData.size() <= 0) {
            return null;
        }
        this.f86db.clearDatabase();
        for (CategoryBean categoryBean : categories) {
            Log.m953d("Category " + categoryBean.getName() + " Color " + categoryBean.getColor() + " drag_id " + categoryBean.getDrag_id());
            categoryBean.setAccountRef(this.accountId);
            long categoryIdFromData = this.f86db.getCategoryIdFromData(categoryBean);
            if (categoryIdFromData == 0) {
                categoryIdFromData = this.f86db.addUpdateCategoryData(categoryBean);
            }
            categoryBean.setId(String.valueOf(categoryIdFromData));
        }
        for (DataBean dataBean : managerData) {
            Log.m953d("Manager " + dataBean.getAmount() + " Sub Category " + dataBean.getSubCategoryId() + " Note " + dataBean.getNote());
            for (CategoryBean categoryBean2 : categories) {
                if (dataBean.getSubCategoryId().equalsIgnoreCase(categoryBean2.getName())) {
                    dataBean.setSubCategoryId(categoryBean2.getId());
                    dataBean.setAccountRef(this.accountId);
                    dataBean.setId(String.valueOf(this.f86db.addUpdateManagerData(dataBean)));
                }
            }
        }
        return null;
    }

    private List<DataBean> getManagerData(DocumentBuilder documentBuilder) throws SAXException, IOException {
        List<DataBean> dataFromFile = getDataFromFile(documentBuilder, new File(BackupTask.MOBYI_DIRECTORY, INCOME_DATA), 2);
        List<DataBean> dataFromFile2 = getDataFromFile(documentBuilder, new File(BackupTask.MOBYI_DIRECTORY, EXPENSE_DATA), 1);
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(dataFromFile);
        arrayList.addAll(dataFromFile2);
        return arrayList;
    }

    private List<CategoryBean> getCategories(DocumentBuilder documentBuilder) throws SAXException, IOException {
        List<CategoryBean> categoriesFromFile = getCategoriesFromFile(documentBuilder, new File(BackupTask.MOBYI_DIRECTORY, INCOME_CATEGORY), 2);
        List<CategoryBean> categoriesFromFile2 = getCategoriesFromFile(documentBuilder, new File(BackupTask.MOBYI_DIRECTORY, EXPENSE_CATEGORY), 1);
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(categoriesFromFile);
        arrayList.addAll(categoriesFromFile2);
        return arrayList;
    }

    private List<DataBean> getDataFromFile(DocumentBuilder documentBuilder, File file, int i) throws SAXException, IOException {
        NodeList elementsByTagName = documentBuilder.parse(file).getDocumentElement().getElementsByTagName("item");
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < elementsByTagName.getLength(); i2++) {
            Node item = elementsByTagName.item(i2);
            if (item instanceof Element) {
                String str = "Wire Transfer";
                String str2 = "no image";
                String str3 = "0";
                String value = getValue(item, "cat");
                String value2 = getValue(item, "subcat");
                String value3 = getValue(item, "amount");
                String value4 = getValue(item, "date");
                String value5 = getValue(item, "note");
                if (item.getAttributes().getNamedItem("method") != null) {
                    str = getValue(item, "method");
                }
                if (item.getAttributes().getNamedItem("image") != null) {
                    str2 = getValue(item, "image");
                }
                if (item.getAttributes().getNamedItem("amount_limit") != null) {
                    str3 = getValue(item, "amount_limit");
                }
                DataBean dataBean = new DataBean();
                dataBean.setCategoryId(value);
                dataBean.setSubCategoryId(value2);
                dataBean.setAmount(value3);
                dataBean.setDate(value4);
                dataBean.setNote(value5);
                dataBean.setImage(str2);
                dataBean.setMethod(str);
                dataBean.setCategoryLimit(str3);
                arrayList.add(dataBean);
            }
        }
        return arrayList;
    }

    private List<CategoryBean> getCategoriesFromFile(DocumentBuilder documentBuilder, File file, int i) throws SAXException, IOException {
        NodeList elementsByTagName = documentBuilder.parse(file).getDocumentElement().getElementsByTagName("item");
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < elementsByTagName.getLength(); i2++) {
            Node item = elementsByTagName.item(i2);
            if (item instanceof Element) {
                String str = "0";
                String value = getValue(item, "name");
                String value2 = getValue(item, "color");
                String value3 = getValue(item, "drag_id");
                if (item.getAttributes().getNamedItem("amount_limit") != null) {
                    str = getValue(item, "amount_limit");
                }
                CategoryBean categoryBean = new CategoryBean();
                categoryBean.setName(value);
                categoryBean.setColor(value2);
                categoryBean.setCategoryGroup(i);
                categoryBean.setCategoryLimit(str);
                categoryBean.setDrag_id(Integer.parseInt(value3));
                arrayList.add(categoryBean);
            }
        }
        return arrayList;
    }
}
