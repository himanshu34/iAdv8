package com.agl.product.adw8_new.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.activity.CampaignActivity;
import com.agl.product.adw8_new.activity.DataActivity;
import com.agl.product.adw8_new.model.Counts;

import java.util.ArrayList;

public class DataActivityGroupAdapter extends RecyclerView.Adapter<DataActivityGroupAdapter.MyViewHolder> {

    private DataActivity activity;
    private ArrayList<Counts> list;

    public DataActivityGroupAdapter(DataActivity activity, ArrayList<Counts> list){
        this.activity = activity;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_activity_group_adapter_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Counts countData = list.get(position);
        holder.nameTextView.setText(countData.getKey());
        holder.countTextView.setText(countData.getValue());
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countData.getKey().equalsIgnoreCase("ads")) {

                } else if(countData.getKey().equalsIgnoreCase("keywords")) {

                } else if(countData.getKey().equalsIgnoreCase("campaign")) {
                    activity.startActivity(new Intent(activity, CampaignActivity.class));
                } else if(countData.getKey().equalsIgnoreCase("adgroup")) {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, countTextView;
        FrameLayout parentView;

        public MyViewHolder(View itemView) {
            super(itemView);
            parentView = (FrameLayout) itemView.findViewById(R.id.parentView);
            nameTextView = (TextView) itemView.findViewById(R.id.textView_name);
            countTextView = (TextView) itemView.findViewById(R.id.textView_count);
        }
    }
}
