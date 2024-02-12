package com.demo.example.dailyincomeexpensemanager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.demo.example.Utile.Notification;
import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.R;

import java.util.List;


public class CategorySpendingLimitAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryBean> data;
    private LayoutInflater influter;
    private boolean isNotification;
    int selectedIndexCategory = 0;

    public CategorySpendingLimitAdapter(Context context, List<CategoryBean> list, boolean z) {
        this.isNotification = false;
        this.context = context;
        this.data = list;
        this.isNotification = z;
        this.influter = (LayoutInflater) this.context.getSystemService("layout_inflater");
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public CategoryBean getItem(int i) {
        return this.data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) Integer.parseInt(getItem(i).getId());
    }

    @Override
    @RequiresApi(api = 21)
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        int i2;
        if (view == null) {
            view = this.influter.inflate(R.layout.list_item_spending_limit, (ViewGroup) null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.tv_item_category);
            viewHolder.overSpent = (TextView) view.findViewById(R.id.tv_item_overspent);
            viewHolder.totalAmount = (TextView) view.findViewById(R.id.tv_item_count);
            viewHolder.pbspending = (ProgressBar) view.findViewById(R.id.pb_limit);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CategoryBean item = getItem(i);
        viewHolder.name.setText(item.getName());
        viewHolder.pbspending.setMax(100);
        int parseInt = Integer.parseInt(item.getCategoryTotal());
        if (item.getCategoryLimit().equals("0")) {
            i2 = 1;
        } else {
            i2 = Integer.parseInt(item.getCategoryLimit());
        }
        int i3 = (parseInt * 100) / i2;
        viewHolder.pbspending.setProgress(i3);
        String replaceAll = item.getCategoryTotal().replaceAll(",", "");
        String replaceAll2 = item.getCategoryLimit().replaceAll(",", "");
        if (!TextUtils.isEmpty(replaceAll) && !TextUtils.isEmpty(replaceAll2)) {
            String currencySymbol = ((BaseActivity) this.context).getCurrencySymbol();
            if (currencySymbol.equals("¢") || currencySymbol.equals("₣") || currencySymbol.equals("₧") || currencySymbol.equals("﷼") || currencySymbol.equals("₨")) {
                try {
                    TextView textView = viewHolder.totalAmount;
                    textView.setText("" + ((int) MyUtils.RoundOff(Double.parseDouble(replaceAll))) + currencySymbol + "/" + ((int) MyUtils.RoundOff(Double.parseDouble(replaceAll2))) + currencySymbol);
                } catch (NumberFormatException unused) {
                    TextView textView2 = viewHolder.totalAmount;
                    textView2.setText("0" + currencySymbol + "/" + replaceAll2 + currencySymbol);
                }
            } else {
                try {
                    TextView textView3 = viewHolder.totalAmount;
                    textView3.setText("" + currencySymbol + ((int) MyUtils.RoundOff(Double.parseDouble(replaceAll))) + "/" + currencySymbol + ((int) MyUtils.RoundOff(Double.parseDouble(replaceAll2))));
                } catch (NumberFormatException unused2) {
                    TextView textView4 = viewHolder.totalAmount;
                    textView4.setText(currencySymbol + "0/" + replaceAll2);
                }
            }
        }
        if (90 < i3) {
            viewHolder.name.setTextColor(Color.parseColor("#df151a"));
            viewHolder.overSpent.setVisibility(View.VISIBLE);
            if (this.isNotification) {
                Notification.showNotification(this.context, this.context.getString(R.string.over_spent_msg));
            }
            viewHolder.pbspending.setProgressTintList(ColorStateList.valueOf(this.context.getResources().getColor(R.color.highlight)));
        } else {
            viewHolder.name.setTextColor(Color.parseColor(item.getColor()));
            viewHolder.overSpent.setVisibility(View.GONE);
            viewHolder.pbspending.setProgressTintList(ColorStateList.valueOf(Color.parseColor(item.getColor())));
        }
        return view;
    }

    
    private class ViewHolder {
        TextView name;
        TextView overSpent;
        ProgressBar pbspending;
        TextView totalAmount;

        private ViewHolder() {
        }
    }
}
