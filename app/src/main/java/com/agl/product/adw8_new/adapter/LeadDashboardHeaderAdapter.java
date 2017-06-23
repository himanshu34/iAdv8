package com.agl.product.adw8_new.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agl.product.adw8_new.R;


public class LeadDashboardHeaderAdapter extends RecyclerView.Adapter<LeadDashboardHeaderAdapter.MyViewHolder> {

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lead_dashboard_header_adapter_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.titleTextView.setText("New");
        holder.countTextView.setText("12");

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parentView;
        private TextView titleTextView, countTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            parentView = (LinearLayout) itemView.findViewById(R.id.parentView);
            titleTextView = (TextView) itemView.findViewById(R.id.textView_title);
            countTextView = (TextView) itemView.findViewById(R.id.textView_count);
        }
    }
}
