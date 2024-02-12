package com.demo.example.dailyincomeexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.dailyincomeexpensemanager.bean.DataBean;
import com.demo.example.R;
import com.github.mikephil.charting.utils.Utils;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;


public abstract class DetailFragment extends BaseFragment {
    protected Calendar calendar;
    boolean checkcolumn;

    
    protected MyDatabaseHandler f62db;
    List<CategoryBean> expenseData;
    List<CategoryBean> inocmeData;
    protected String startTime = "1";
    protected String endTime = "9";
    protected boolean isExpectedRequired = true;

    public abstract String getMessageText();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.f62db = new MyDatabaseHandler(getActivity());
        return layoutInflater.inflate(R.layout.fragment_details, viewGroup, false);
    }

    protected void updateUI() {
        double d;
        String totalAmountByTime = this.f62db.getTotalAmountByTime(2, this.startTime, this.endTime);
        String totalAmountByTime2 = this.f62db.getTotalAmountByTime(1, this.startTime, this.endTime);
        String replaceAll = totalAmountByTime.replaceAll(",", "");
        String replaceAll2 = totalAmountByTime2.replaceAll(",", "");
        double d2 = Utils.DOUBLE_EPSILON;
        try {
            double RoundOff = MyUtils.RoundOff(Double.parseDouble(replaceAll));
            d = MyUtils.RoundOff(Double.parseDouble(replaceAll2));
            d2 = RoundOff;
        } catch (NumberFormatException unused) {
            d = 0.0d;
        }
        setTextValue(R.id.tvTotalIncome, " %.2f", d2);
        setTextValue(R.id.tvTotalExpense, "%.2f", d);
        if (d2 >= d) {
            setTextValue(R.id.tvTotalNet, "%.2f", d2 - d, R.color.detail_income);
        } else {
            setTextValue(R.id.tvTotalNet, "%.2f", d - d2, R.color.detail_expense);
        }
        updateListofCategories();
        if (this.isExpectedRequired) {
            try {
                updateExpectedUI();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            getView().findViewById(R.id.layout_reminder).setVisibility(View.GONE);
        }
    }

    private void updateListofCategories() {
        ListView listView = (ListView) getView().findViewById(R.id.cat_list_income);
        ListView listView2 = (ListView) getView().findViewById(R.id.cat_list_expense);
        this.checkcolumn = this.f62db.existsColumnInTable().booleanValue();
        if (this.checkcolumn) {
            this.inocmeData = this.f62db.getCategoryList(2);
            this.expenseData = this.f62db.getCategoryList(1);
        } else {
            this.f62db.addcolumn();
        }
        for (CategoryBean categoryBean : this.inocmeData) {
            categoryBean.setCategoryTotal(this.f62db.getCategoryAmountByTime(categoryBean.getCategoryGroup(), categoryBean.getId(), this.startTime, this.endTime));
        }
        for (CategoryBean categoryBean2 : this.expenseData) {
            categoryBean2.setCategoryTotal(this.f62db.getCategoryAmountByTime(categoryBean2.getCategoryGroup(), categoryBean2.getId(), this.startTime, this.endTime));
        }
        CategoryCustomAdapter categoryCustomAdapter = new CategoryCustomAdapter(getActivity(), this.inocmeData);
        CategoryCustomAdapter categoryCustomAdapter2 = new CategoryCustomAdapter(getActivity(), this.expenseData);
        listView.setAdapter((ListAdapter) categoryCustomAdapter);
        listView2.setAdapter((ListAdapter) categoryCustomAdapter2);
        CategoryListHelper.getListViewSize(listView);
        CategoryListHelper.getListViewSize(listView2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                DetailFragment.this.openDetailsFor(2, (int) j);
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                DetailFragment.this.openDetailsFor(1, (int) j);
            }
        });
    }

    
    public void openDetailsFor(int i, int i2) {
        Intent intent = new Intent(getActivity(), CategoryTimeListFragmentActivity.class);
        intent.putExtra("selection_type", i);
        intent.putExtra(CategoryTimeFragment.CATEGORY_TYPE, i2);
        intent.putExtra("start_date", this.startTime);
        intent.putExtra("end_date", this.endTime);
        intent.putExtra(CategoryTimeFragment.MESSAGE, getMessageText());
        startActivity(intent);
    }

    private void setTextValue(int i, String str, double d) {
        setTextValue(i, str, d, 0);
    }

    private void setTextValue(int i, String str, double d, int i2) {
        TextView textView = (TextView) getView().findViewById(i);
        if (getCurrencySymbol().equals("¢") || getCurrencySymbol().equals("₣") || getCurrencySymbol().equals("₧") || getCurrencySymbol().equals("﷼") || getCurrencySymbol().equals("₨")) {
            textView.setText(String.format(str, Double.valueOf(d)) + getCurrencySymbol());
            if (i2 != 0) {
                textView.setTextColor(getResources().getColor(i2));
                return;
            }
            return;
        }
        textView.setText(getCurrencySymbol() + String.format(str, Double.valueOf(d)));
        if (i2 != 0) {
            textView.setTextColor(getResources().getColor(i2));
        }
    }

    private void updateExpectedUI() throws ParseException {
        Calendar instance = Calendar.getInstance();
        instance.setTime(MyUtils.sdfDatabase.parse(this.startTime));
        Calendar instance2 = Calendar.getInstance();
        instance2.setTime(MyUtils.sdfDatabase.parse(this.endTime));
        double expectedTotal = getExpectedTotal(this.f62db, 2, instance, instance2);
        double expectedTotal2 = getExpectedTotal(this.f62db, 1, instance, instance2);
        setTextValue(R.id.tvTotalIncomeExpected, "%.2f", expectedTotal);
        setTextValue(R.id.tvTotalExpenseExpected, "%.2f", expectedTotal2);
    }

    private double getExpectedTotal(MyDatabaseHandler myDatabaseHandler, int i, Calendar calendar, Calendar calendar2) throws NumberFormatException {
        double d = Utils.DOUBLE_EPSILON;
        for (DataBean dataBean : MyUtils.getFutureRecurringDataByTime(myDatabaseHandler, i, (Calendar) calendar.clone(), (Calendar) calendar2.clone())) {
            d += Double.parseDouble(dataBean.getAmount());
        }
        return d;
    }
}
