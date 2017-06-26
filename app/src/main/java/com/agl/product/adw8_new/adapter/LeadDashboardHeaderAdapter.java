package com.agl.product.adw8_new.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.activity.LeadDashBoardActivity;
import com.agl.product.adw8_new.activity.LeadDashboardActivity1;
import com.agl.product.adw8_new.activity.LeadListDashboardActivity;
import com.agl.product.adw8_new.model.MainLeads;
import com.agl.product.adw8_new.utils.Utils;

import java.util.ArrayList;


public class LeadDashboardHeaderAdapter extends RecyclerView.Adapter<LeadDashboardHeaderAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<MainLeads> mainLeads;
    private String fromDateToShow, toDateToShow,dateType;

    public LeadDashboardHeaderAdapter(Context context, ArrayList<MainLeads> mainLeads, String fromDateToShow, String toDateToShow,String dateType) {
        this.context = context;
        this.mainLeads = mainLeads;
        this.fromDateToShow = fromDateToShow;
        this.toDateToShow = toDateToShow;
        this.dateType = dateType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lead_dashboard_header_adapter_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MainLeads mainLead = this.mainLeads.get(position);

        if (mainLead.isChecked()) {
            holder.parentView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.titleTextView.setTextColor(context.getResources().getColor(R.color.white));
            holder.countTextView.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.parentView.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.titleTextView.setTextColor(context.getResources().getColor(R.color.black));
            holder.countTextView.setTextColor(Color.parseColor("#9d9d9d"));
        }

        holder.countTextView.setText(mainLead.getCnt());
        holder.titleTextView.setText(mainLead.getStatus());

    }


    @Override
    public int getItemCount() {
        return mainLeads.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout parentView;
        private TextView titleTextView, countTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            parentView = (LinearLayout) itemView.findViewById(R.id.parentView);
            titleTextView = (TextView) itemView.findViewById(R.id.textView_title);
            countTextView = (TextView) itemView.findViewById(R.id.textView_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (!mainLeads.get(position).isChecked()) {
                for (int i = 0; i < mainLeads.size(); i++) {
                    if (position == i) mainLeads.get(i).setChecked(true);
                    else mainLeads.get(i).setChecked(false);
                }
                notifyDataSetChanged();
            }

            context.startActivity(new Intent(context, LeadListDashboardActivity.class)
                    .putExtra(Utils.CURRENT__FROM_DATE, fromDateToShow)
                    .putExtra(Utils.CURRENT_TO_DATE, toDateToShow)
                    .putExtra(Utils.DATE_TYPE, dateType)
                    .putExtra(Utils.ID_TYPE_STATUS, mainLeads.get(position).getStatus_id()+","));
        }
    }
}
