package com.agl.product.adw8_new.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.activity.DataActivity;
import com.agl.product.adw8_new.model.Graph;
import com.agl.product.adw8_new.model.GraphView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class DataActivityGraphAdapter  extends RecyclerView.Adapter<DataActivityGraphAdapter.MyViewHolder>{

    private DataActivity activity;
    private ArrayList<Graph> list;
    String[] mStringArray;

    public DataActivityGraphAdapter(DataActivity activity, ArrayList<Graph> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_activity_graph_adapter_layout, parent, false);
        return new  MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Graph graphData = list.get(position);
        holder.titleTextView.setText(graphData.getKey().toUpperCase());
        holder.countTextView.setText(graphData.getTotal());

        ArrayList<GraphView> graphViewList = graphData.getGraphViewList();
        ArrayList<String> keys = new ArrayList<String>();
        List<Entry> entries = new ArrayList<Entry>();
        for(int i=0; i<graphViewList.size(); i++) {
            GraphView graphPoints = graphViewList.get(i);
            float pos = i;
            keys.add(graphPoints.getDate());
            entries.add(new Entry(pos, Float.parseFloat(graphPoints.getCount())));
        }

        LineDataSet setComp = new LineDataSet(entries, graphData.getKey().toUpperCase());
        setComp.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp.setColor(activity.getResources().getColor(R.color.colorPrimary));
        setComp.setCircleColor(activity.getResources().getColor(R.color.colorPrimary));
        setComp.setLineWidth(1f);
        setComp.setCircleRadius(3f);
        setComp.setDrawCircleHole(false);
        setComp.setValueTextSize(8f);
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
        xAxis.setGranularity(4f); // minimum axis-step (interval) is 5
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        holder.mLineChart.getAxisRight().setEnabled(false);
        xAxis.setValueFormatter(formatter);

//        // - Y Axis
        YAxis leftAxis = holder.mLineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setDrawGridLines(true);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView, countTextView;
        private LineChart mLineChart;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.textView_title);
            countTextView = (TextView) itemView.findViewById(R.id.textView_count);
            mLineChart = (LineChart) itemView.findViewById(R.id.lineChart);
        }
    }
}
