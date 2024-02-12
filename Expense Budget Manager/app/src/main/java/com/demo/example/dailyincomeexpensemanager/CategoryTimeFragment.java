package com.demo.example.dailyincomeexpensemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.example.dailyincomeexpensemanager.bean.DataBean;
import com.demo.example.R;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


public class CategoryTimeFragment extends BaseFragment {
    public static final String ARG_SECTION_NUMBER = "selection_type";
    public static final String CATEGORY_TYPE = "category_type";
    public static final String END_DATE = "end_date";
    public static final String MESSAGE = "message";
    public static final String START_DATE = "start_date";
    IncomeExpenceListAdapter adapter;
    private int categoryId;
    private Context context;


    MyDatabaseHandler f61db;
    private String endTime;
    private String message;
    private int selection;
    private String startTime;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_manager_list, viewGroup, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.selection = arguments.getInt("selection_type", 1);
            this.categoryId = arguments.getInt(CATEGORY_TYPE, 1);
            this.startTime = arguments.getString("start_date");
            this.endTime = arguments.getString("end_date");
            this.message = arguments.getString(MESSAGE);
            PrintStream printStream = System.out;
            printStream.println("Selection=" + this.selection + " CategoryId=" + this.categoryId + " StartTime=" + this.startTime + " EndTime=" + this.endTime);
        }
        this.context = getActivity();
        this.f61db = new MyDatabaseHandler(getActivity());
        ListView listView = (ListView) inflate.findViewById(R.id.lvDistribution);
        listView.setEmptyView(inflate.findViewById(R.id.tvNoDataFound));
        inflate.findViewById(R.id.buttonlayout).setVisibility(View.GONE);
        if (!TextUtils.isEmpty(this.message)) {
            TextView textView = (TextView) inflate.findViewById(R.id.tvHeader);
            textView.setVisibility(View.VISIBLE);
            textView.setText(this.message);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (j != 0) {
                    Intent intent = new Intent(CategoryTimeFragment.this.context, AddExpenceIncomeActivity.class);
                    intent.putExtra(TAG.CATEGORY, CategoryTimeFragment.this.selection);
                    intent.putExtra(TAG.DATA, (int) j);
                    CategoryTimeFragment.this.startActivity(intent);
                    return;
                }
                String refRecurring = CategoryTimeFragment.this.adapter.getItem(i).getRefRecurring();
                Intent intent2 = new Intent(CategoryTimeFragment.this.context, AddExpenceIncomeActivity.class);
                intent2.putExtra(TAG.CATEGORY, CategoryTimeFragment.this.selection);
                intent2.putExtra(TAG.RECURRING, Integer.parseInt(refRecurring));
                CategoryTimeFragment.this.startActivity(intent2);
            }
        });
        return inflate;
    }

    private void updateUI() {
        List<DataBean> managerDataByTimeAndCategory = this.f61db.getManagerDataByTimeAndCategory(this.selection, this.categoryId, this.startTime, this.endTime);
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(managerDataByTimeAndCategory);
        this.adapter = new IncomeExpenceListAdapter(this.context, arrayList);
        ((ListView) getView().findViewById(R.id.lvDistribution)).setAdapter((ListAdapter) this.adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }
}
