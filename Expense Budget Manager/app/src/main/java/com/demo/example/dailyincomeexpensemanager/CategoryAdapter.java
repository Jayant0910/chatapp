package com.demo.example.dailyincomeexpensemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.demo.example.dailyincomeexpensemanager.bean.CategoryBean;
import com.demo.example.widget.CircleImageView;
import com.demo.example.R;

import java.util.List;


public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryBean> data;
    AlertDialog dialog;
    private LayoutInflater influter;
    private RadioButton mSelectedRB;
    int selectedIndexCategory;
    private TextView tvCategory;

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    public CategoryAdapter(Context context, List<CategoryBean> list, String str, AlertDialog alertDialog, TextView textView) {
        this.selectedIndexCategory = 0;
        this.context = context;
        this.data = list;
        this.dialog = alertDialog;
        this.tvCategory = textView;
        this.influter = (LayoutInflater) this.context.getSystemService("layout_inflater");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equalsIgnoreCase(str)) {
                this.selectedIndexCategory = i;
            }
        }
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = this.influter.inflate(R.layout.list_item_select_category, (ViewGroup) null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.cat_item_name);
            viewHolder.color = (CircleImageView) view.findViewById(R.id.cat_item_color);
            viewHolder.select = (RadioButton) view.findViewById(R.id.cat_item_select);
            viewHolder.item = (LinearLayout) view.findViewById(R.id.cat_list_item);
            view.setTag(viewHolder);
            if (i == this.selectedIndexCategory) {
                this.mSelectedRB = viewHolder.select;
                this.mSelectedRB.setChecked(true);
                this.selectedIndexCategory = i;
            }
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(this.data.get(i).getName());
        viewHolder.color.setCircleBackgroundColor(Color.parseColor(this.data.get(i).getColor()));
        viewHolder.color.setBorderColor(Color.parseColor(this.data.get(i).getColor()));
        viewHolder.color.setColorFilter(Color.parseColor(this.data.get(i).getColor()));
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                RadioButton radioButton = (RadioButton) view2.findViewById(R.id.cat_item_select);
                if (!(i == CategoryAdapter.this.selectedIndexCategory || CategoryAdapter.this.mSelectedRB == null)) {
                    CategoryAdapter.this.mSelectedRB.setChecked(false);
                }
                CategoryAdapter.this.selectedIndexCategory = i;
                CategoryAdapter.this.mSelectedRB = radioButton;
                if (CategoryAdapter.this.selectedIndexCategory != i) {
                    viewHolder.select.setChecked(false);
                } else {
                    viewHolder.select.setChecked(true);
                    if (!(CategoryAdapter.this.mSelectedRB == null || viewHolder.select == CategoryAdapter.this.mSelectedRB)) {
                        CategoryAdapter.this.mSelectedRB = viewHolder.select;
                    }
                }
                CategoryAdapter.this.tvCategory.setText(((CategoryBean) CategoryAdapter.this.data.get(i)).getName());
                CategoryAdapter.this.dialog.dismiss();
            }
        });
        return view;
    }

    public String getSelectedCategoryId() throws ArrayIndexOutOfBoundsException {
        if (this.data.size() == 0) {
            return "0";
        }
        return this.data.get(this.selectedIndexCategory).getId();
    }

    public String getCategoryName() throws ArrayIndexOutOfBoundsException {
        if (this.data.size() == 0) {
            return "Select Category";
        }
        return this.data.get(this.selectedIndexCategory).getName();
    }

    
    private class ViewHolder {
        CircleImageView color;
        LinearLayout item;
        TextView name;
        RadioButton select;

        private ViewHolder() {
        }
    }
}
