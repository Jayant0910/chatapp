package com.demo.example.dailyincomeexpensemanager;

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


public class MyCategoryCustomAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryBean> data;
    private LayoutInflater influter;
    private RadioButton mSelectedRB;
    int selectedIndexCategory;

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    public MyCategoryCustomAdapter(Context context, List<CategoryBean> list, String str) {
        this.selectedIndexCategory = 0;
        this.context = context;
        this.data = list;
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

    
    private class ViewHolder {
        CircleImageView color;
        LinearLayout item;
        TextView name;
        RadioButton select;

        private ViewHolder() {
        }
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
                if (!(i == MyCategoryCustomAdapter.this.selectedIndexCategory || MyCategoryCustomAdapter.this.mSelectedRB == null)) {
                    MyCategoryCustomAdapter.this.mSelectedRB.setChecked(false);
                }
                MyCategoryCustomAdapter.this.selectedIndexCategory = i;
                MyCategoryCustomAdapter.this.mSelectedRB = radioButton;
                if (MyCategoryCustomAdapter.this.selectedIndexCategory != i) {
                    viewHolder.select.setChecked(false);
                    return;
                }
                viewHolder.select.setChecked(true);
                if (MyCategoryCustomAdapter.this.mSelectedRB != null && viewHolder.select != MyCategoryCustomAdapter.this.mSelectedRB) {
                    MyCategoryCustomAdapter.this.mSelectedRB = viewHolder.select;
                }
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
}
