package com.demo.example.dailyincomeexpensemanager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.widget.CircleImageView;
import com.demo.example.R;

import java.util.List;


public class SettingCategoryCustomAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryBean> data;
    private LayoutInflater influter;
    int selectedIndexCategory = 0;

    public SettingCategoryCustomAdapter(Context context, List<CategoryBean> list) {
        this.context = context;
        this.data = list;
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


    private class ViewHolder {
        CircleImageView color;
        TextView name;
        TextView totalAmount;

        private ViewHolder() {
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = this.influter.inflate(R.layout.list_item_category_setting, (ViewGroup) null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.cat_item_name);
            viewHolder.totalAmount = (TextView) view.findViewById(R.id.tv_spending_limit);
            viewHolder.color = (CircleImageView) view.findViewById(R.id.cat_item_color);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CategoryBean item = getItem(i);
        viewHolder.name.setText(item.getName());
        viewHolder.color.setBorderColor(Color.parseColor(item.getColor()));
        viewHolder.color.setColorFilter(Color.parseColor(item.getColor()));
        viewHolder.color.setCircleBackgroundColor(Color.parseColor(item.getColor()));
        if (!TextUtils.isEmpty(item.getCategoryLimit())) {
            if (item.getCategoryGroup() == 2) {
                view.findViewById(R.id.layout_limit).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.layout_limit).setVisibility(View.VISIBLE);
            }
            String currencySymbol = ((BaseActivity) this.context).getCurrencySymbol();
            if (currencySymbol.equals("¢") || currencySymbol.equals("₣") || currencySymbol.equals("₧") || currencySymbol.equals("﷼") || currencySymbol.equals("₨")) {
                TextView textView = viewHolder.totalAmount;
                textView.setText(item.getCategoryLimit() + currencySymbol);
            } else {
                TextView textView2 = viewHolder.totalAmount;
                textView2.setText(currencySymbol + item.getCategoryLimit());
            }
        }
        return view;
    }
}
