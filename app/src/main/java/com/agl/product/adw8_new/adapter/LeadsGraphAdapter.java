package com.agl.product.adw8_new.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.activity.LeadDashboardActivity1;
import com.agl.product.adw8_new.decoration.ActionItem;
import com.agl.product.adw8_new.decoration.QuickAction;
import com.agl.product.adw8_new.model.LeadsGraphData;
import com.agl.product.adw8_new.model.LeadsStatus;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class LeadsGraphAdapter extends RecyclerView.Adapter<LeadsGraphAdapter.MyViewHolder> {

    private LeadDashboardActivity1 activity;
    private ArrayList<LeadsGraphData> data;
    String[] mStringArray;

    public LeadsGraphAdapter(LeadDashboardActivity1 activity, ArrayList<LeadsGraphData> data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_activity_graph_adapter_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final LeadsGraphData dataGraph = data.get(position);
        try {
            holder.titleTextView.setText(dataGraph.getKey().toUpperCase());
            holder.countTextView.setText(dataGraph.getTotal());
            holder.hintTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showQuickAction(holder.hintTextView, activity, dataGraph.getHint());
                }
            });

            ArrayList<LeadsStatus> graphViewList = dataGraph.getStatusData();
            ArrayList<String> keys = new ArrayList<String>();
            List<Entry> entries = new ArrayList<Entry>();
            for(int i=0; i<graphViewList.size(); i++) {
                LeadsStatus graphPoints = graphViewList.get(i);
                float pos = i;
                keys.add(graphPoints.getDate());
                entries.add(new Entry(pos, Float.parseFloat(graphPoints.getCount())));
            }

            LineDataSet setComp = new LineDataSet(entries, dataGraph.getKey().toUpperCase());
            setComp.setAxisDependency(YAxis.AxisDependency.LEFT);
            setComp.setColor(activity.getResources().getColor(R.color.colorPrimary));
            setComp.setCircleColor(activity.getResources().getColor(R.color.colorPrimary));
            setComp.setLineWidth(1f);
            setComp.setCircleRadius(3f);
            setComp.setDrawCircleHole(false);
            setComp.setValueTextSize(10f);
//        setComp.setDrawFilled(true);

            List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(setComp);

            LineData data = new LineData(dataSets);
            holder.mLineChart.setData(data);
            holder.mLineChart.invalidate(); // refresh

            mStringArray = new String[keys.size()];
            mStringArray = keys.toArray(mStringArray);
            // the labels that should be drawn on the XAxis
            IAxisValueFormatter formatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return mStringArray[(int) value];
                }
            };

            holder.mLineChart.getDescription().setText("");

            XAxis xAxis = holder.mLineChart.getXAxis();
            if(entries.size() >= 1) {
                xAxis.setGranularity(4f); // minimum axis-step (interval) is 4
            } if(entries.size() >= 7) {
                xAxis.setGranularity(5f);
            } if(entries.size() >= 20) {
                xAxis.setGranularity(10f);
            } if(entries.size() >= 40) {
                xAxis.setGranularity(20f);
            }
            xAxis.setTextSize(12f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            holder.mLineChart.getAxisRight().setEnabled(false);
            xAxis.setValueFormatter(formatter);

//        // - Y Axis
            YAxis leftAxis = holder.mLineChart.getAxisLeft();
            leftAxis.removeAllLimitLines();
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setStartAtZero(false);
            leftAxis.setTextSize(12f);
            leftAxis.setDrawLimitLinesBehindData(true);
            leftAxis.setDrawGridLines(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showQuickAction(final TextView textView, final Activity activity, String hint) {
        final QuickAction quickAction = new QuickAction(activity, QuickAction.VERTICAL);
        ActionItem actionItem = new ActionItem(hint);
        quickAction.addActionItem(actionItem);
        quickAction.show(textView);
        quickAction.setAnimStyle(QuickAction.ANIM_REFLECT);
    }

    @Override
    public int getItemCount() {
        return data.size();
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
