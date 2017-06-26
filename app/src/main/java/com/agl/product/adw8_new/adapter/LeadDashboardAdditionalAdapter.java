package com.agl.product.adw8_new.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.activity.LeadDashboardActivity1;
import com.agl.product.adw8_new.model.MainLeads;

import java.util.ArrayList;

public class LeadDashboardAdditionalAdapter extends RecyclerView.Adapter<LeadDashboardAdditionalAdapter.MyViewHolder> {

    private LeadDashboardActivity1 activity;
    private ArrayList<MainLeads> additionalLeads;

    public  LeadDashboardAdditionalAdapter(LeadDashboardActivity1 activity, ArrayList<MainLeads> additionalLeads, String fromDateToShow, String toDateToShow, String dateType){
        this.activity = activity ;
        this.additionalLeads = additionalLeads;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lead_dashboard_addtionla_adapter_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MainLeads mainLeads = additionalLeads.get(position);

        holder.textName.setText(mainLeads.getStatus());
        holder.textCount.setText(mainLeads.getCnt());
    }

    @Override
    public int getItemCount() {
        return additionalLeads.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textName, textCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textName = (TextView) itemView.findViewById(R.id.textName);
            textCount = (TextView) itemView.findViewById(R.id.textCount);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (additionalLeads.get(position).isChecked()) return;
            else {
                for (int i = 0; i < additionalLeads.size(); i++) {
                    if (position == i) additionalLeads.get(i).setChecked(true);
                    else additionalLeads.get(i).setChecked(false);
                }
                notifyDataSetChanged();
                activity.openDashBoardListActivity(position, additionalLeads.get(position));
            }
        }
    }
}
