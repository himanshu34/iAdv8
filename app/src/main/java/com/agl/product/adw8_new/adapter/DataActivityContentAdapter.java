package com.agl.product.adw8_new.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.activity.DataActivity;
import com.agl.product.adw8_new.model.Graph;

import java.util.ArrayList;

public class DataActivityContentAdapter extends RecyclerView.Adapter<DataActivityContentAdapter.MyViewHolder> {

    private DataActivity activity;
    private ArrayList<Graph> list;
    private String rupeeSymbol;

    public DataActivityContentAdapter(DataActivity activity, ArrayList<Graph> list) {
        this.activity = activity;
        this.list = list;
        rupeeSymbol = this.activity.getString(R.string.rupee);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_activity_content_adapter_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Graph graphData = list.get(position);
        if (graphData.is_clicked()) {
            holder.parentView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
            holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.white));
            holder.countTextView.setTextColor(activity.getResources().getColor(R.color.white));
        } else {
            holder.parentView.setBackgroundColor(activity.getResources().getColor(R.color.white));
            holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.black));
            holder.countTextView.setTextColor(Color.parseColor("#9d9d9d"));
        }

        String textShow = "";
        if (graphData.getKey().equalsIgnoreCase("cost") || graphData.getKey().equalsIgnoreCase("cpa")) {
            textShow = rupeeSymbol + " ";
        }
        textShow = textShow + graphData.getTotal();
        holder.titleTextView.setText(graphData.getKey());
        holder.countTextView.setText(textShow);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout parentView;
        private TextView titleTextView, countTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            parentView = (LinearLayout) itemView.findViewById(R.id.parentView);
            titleTextView = (TextView) itemView.findViewById(R.id.textView_title);
            countTextView = (TextView) itemView.findViewById(R.id.textView_count);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (!list.get(position).is_clicked()) {
                for (int i = 0; i < list.size(); i++) {
                    if (position == i) list.get(i).setIs_clicked(true);
                    else list.get(i).setIs_clicked(false);
                }
                notifyDataSetChanged();
            }

        }
    }

    public String getCaseSensitiveWords(String name) {
        try {
            name = name.trim().toLowerCase().replaceAll("\\s+", " ");
            String[] strArray = name.split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : strArray) {
                String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                builder.append(cap + " ");
            }
            name = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name.trim();
    }
}
