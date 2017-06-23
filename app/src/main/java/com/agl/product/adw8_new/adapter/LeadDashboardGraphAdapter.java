package com.agl.product.adw8_new.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.github.mikephil.charting.charts.LineChart;

public class LeadDashboardGraphAdapter extends RecyclerView.Adapter<LeadDashboardGraphAdapter.MyViewHolder> {

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, hintTextView, countTextView;
        private LineChart mLineChart;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.textView_title);
            hintTextView = (TextView) itemView.findViewById(R.id.hint_textView);
            countTextView = (TextView) itemView.findViewById(R.id.textView_count);
            mLineChart = (LineChart) itemView.findViewById(R.id.lineChart);
        }
    }
}
