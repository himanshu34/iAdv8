package com.agl.product.adw8_new.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.activity.CampaignActivity;

public class DataActivityGroupAdapter extends RecyclerView.Adapter<DataActivityGroupAdapter.MyViewHolder> {

    private Context context;

    public DataActivityGroupAdapter(Context context){
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_activity_group_adapter_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            context.startActivity(new Intent(context, CampaignActivity.class));
        }
    }
}
