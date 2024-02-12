package com.demo.example.dailyincomeexpensemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.demo.example.dailyincomeexpensemanager.bean.DataBean;
import com.demo.example.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class IncomeMonthlyFragment extends BaseFragment {
    public static final String ARG_SECTION_NUMBER = "selection_type";
    IncomeExpenceListAdapter adapter;
    private Calendar calendar;
    private Context context;

    
    MyDatabaseHandler f68db;
    private int selection = 1;
    private TextView title;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_manager_list, viewGroup, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.selection = arguments.getInt("selection_type", 1);
        }
        this.context = getActivity();
        this.f68db = new MyDatabaseHandler(getActivity());
        this.calendar = Calendar.getInstance();
        this.calendar.set(5, 1);
        this.calendar.set(11, 0);
        this.calendar.clear(12);
        this.calendar.clear(13);
        this.calendar.clear(14);
        this.title = (TextView) inflate.findViewById(R.id.currentMonth);
        ListView listView = (ListView) inflate.findViewById(R.id.lvDistribution);
        listView.setEmptyView(inflate.findViewById(R.id.tvNoDataFound));
        inflate.findViewById(R.id.nextMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IncomeMonthlyFragment.this.calendar.add(2, 1);
                IncomeMonthlyFragment.this.updateUI();
            }
        });
        inflate.findViewById(R.id.prevMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IncomeMonthlyFragment.this.calendar.add(2, -1);
                IncomeMonthlyFragment.this.updateUI();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (j != 0) {
                    Intent intent = new Intent(IncomeMonthlyFragment.this.context, AddExpenceIncomeActivity.class);
                    intent.putExtra(TAG.CATEGORY, IncomeMonthlyFragment.this.selection);
                    intent.putExtra(TAG.DATA, (int) j);
                    IncomeMonthlyFragment.this.startActivity(intent);
                    return;
                }
                String refRecurring = IncomeMonthlyFragment.this.adapter.getItem(i).getRefRecurring();
                Intent intent2 = new Intent(IncomeMonthlyFragment.this.context, AddExpenceIncomeActivity.class);
                intent2.putExtra(TAG.CATEGORY, IncomeMonthlyFragment.this.selection);
                intent2.putExtra(TAG.RECURRING, Integer.parseInt(refRecurring));
                IncomeMonthlyFragment.this.startActivity(intent2);
            }
        });
        return inflate;
    }

    protected void showUneditableAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sorry");
        builder.setMessage("You cann't edit the transcations which are recurring.");
        builder.setNeutralButton("OK", (DialogInterface.OnClickListener) null);
        builder.create().show();
    }

    
    public void updateUI() {
        this.title.setText(new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(this.calendar.getTime()));
        String format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(this.calendar.getTime());
        Calendar calendar = (Calendar) this.calendar.clone();
        calendar.add(2, 1);
        List<DataBean> managerDataByTime = this.f68db.getManagerDataByTime(this.selection, format, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime()));
        List<DataBean> futureRecurringDataByTime = MyUtils.getFutureRecurringDataByTime(this.f68db, this.selection, (Calendar) this.calendar.clone(), (Calendar) calendar.clone());
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(managerDataByTime);
        arrayList.addAll(futureRecurringDataByTime);
        this.adapter = new IncomeExpenceListAdapter(this.context, arrayList);
        ((ListView) getView().findViewById(R.id.lvDistribution)).setAdapter((ListAdapter) this.adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }
}
