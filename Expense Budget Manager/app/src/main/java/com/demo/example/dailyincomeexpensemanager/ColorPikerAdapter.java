package com.demo.example.dailyincomeexpensemanager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.demo.example.widget.CircleImageView;
import com.demo.example.R;


public class ColorPikerAdapter extends BaseAdapter {
    String[] colorcode;
    private LayoutInflater influter;
    private Context mContext;

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    public ColorPikerAdapter(Context context, String[] strArr) {
        this.mContext = context;
        this.colorcode = strArr;
        this.influter = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
    }

    @Override
    public int getCount() {
        return this.colorcode.length;
    }

    @Override
    public String getItem(int i) {
        return this.colorcode[i];
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.influter.inflate(R.layout.list_item_color, (ViewGroup) null);
        }
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.color_piker_color);
        circleImageView.setColorFilter(Color.parseColor(this.colorcode[i]));
        circleImageView.setBorderColor(Color.parseColor(this.colorcode[i]));
        circleImageView.setCircleBackgroundColor(Color.parseColor(this.colorcode[i]));
        return view;
    }
}
