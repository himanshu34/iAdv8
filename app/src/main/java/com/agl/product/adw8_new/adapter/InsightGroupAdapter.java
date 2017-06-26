package com.agl.product.adw8_new.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.agl.product.adw8_new.model.InsightGroupType;

import java.util.ArrayList;

public class InsightGroupAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context activity;
    private ArrayList<InsightGroupType> asr;

    public InsightGroupAdapter(Context activity, ArrayList<InsightGroupType> asr) {
        this.asr = asr;
        this.activity = activity;
    }

    public int getCount() {
        return asr.size();
    }

    public Object getItem(int i) {
        return asr.get(i);
    }

    public long getItemId(int i) {
        return (long)i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(activity);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(asr.get(position).getGroup_name());
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(activity);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setPadding(8, 8, 8, 8);
        txt.setTextSize(18);
        txt.setMaxLines(1);
        txt.setText(asr.get(i).getGroup_name());
        txt.setTextColor(Color.parseColor("#3B3B3B"));
        return  txt;
    }
}
