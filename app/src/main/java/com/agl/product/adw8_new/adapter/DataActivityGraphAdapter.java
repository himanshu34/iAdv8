package com.agl.product.adw8_new.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class DataActivityGraphAdapter  extends RecyclerView.Adapter<DataActivityGraphAdapter.MyViewHolder>{

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_activity_graph_adapter_layout, parent, false);
        return new  MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(position == 0) {
            holder.titleTextView.setText("IMPRESSION");
            holder.countTextView.setText("10249");
        } else if(position == 1) {
            holder.titleTextView.setText("CLICKS");
            holder.countTextView.setText("70");
        } else if(position == 2) {
            holder.titleTextView.setText("COST");
            holder.countTextView.setText("2820.29");
        } else if(position == 3) {
            holder.titleTextView.setText("convertedClicks".toUpperCase());
            holder.countTextView.setText("3");
        } else if(position == 4) {
            holder.titleTextView.setText("cpa".toUpperCase());
            holder.countTextView.setText("940.1");
        } else if(position == 5) {
            holder.titleTextView.setText("ctr".toUpperCase());
            holder.countTextView.setText("0.01");
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView, countTextView;
        private LineChart lineChart;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.textView_title);
            countTextView = (TextView) itemView.findViewById(R.id.textView_count);
            lineChart = (LineChart) itemView.findViewById(R.id.lineChart);
            setData();
        }

        private void setData() {
            List<Entry> entries = new ArrayList<>();
            entries.add(new Entry(0f, 30f));
            entries.add(new Entry(1f, 80f));
            entries.add(new Entry(2f, 60f));
            entries.add(new Entry(3f, 50f));
            // gap of 2f
            entries.add(new Entry(5f, 70f));
            entries.add(new Entry(6f, 60f));

            LineDataSet set = new LineDataSet(entries, "Testing");
            set.setAxisDependency(YAxis.AxisDependency.LEFT);

            List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set);

            LineData data = new LineData(dataSets);
            // set x and y axis
            // - X Axis
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setTextSize(12f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextColor(ColorTemplate.getHoloBlue());
            xAxis.setEnabled(true);
            xAxis.disableGridDashedLine();
            xAxis.setSpaceMin(5);
            xAxis.setDrawGridLines(false);
            xAxis.setAvoidFirstLastClipping(true);

            // - Y Axis
            YAxis leftAxis = lineChart.getAxisLeft();
            leftAxis.removeAllLimitLines();
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setTextColor(ColorTemplate.getHoloBlue());
            leftAxis.setAxisMaxValue(1000f);
            leftAxis.setAxisMinValue(0f); // to set minimum yAxis
            leftAxis.setStartAtZero(false);
            leftAxis.enableGridDashedLine(10f, 10f, 0f);
            leftAxis.setDrawLimitLinesBehindData(true);
            leftAxis.setDrawGridLines(true);
            lineChart.getAxisRight().setEnabled(false);

            lineChart.setData(data);
//            lineChart.setFitBars(true); // make the x-axis fit exactly all bars
            lineChart.invalidate(); // refresh
        }

        private ArrayList<String> setXAxisValues(){
            ArrayList<String> xVals = new ArrayList<String>();
            xVals.add("10");
            xVals.add("20");
            xVals.add("30");
            xVals.add("30.5");
            xVals.add("40");
            return xVals;
        }

        private ArrayList<Entry> setYAxisValues(){
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            yVals.add(new Entry(60, 0));
            yVals.add(new Entry(48, 1));
            yVals.add(new Entry(70.5f, 2));
            yVals.add(new Entry(100, 3));
            yVals.add(new Entry(180.9f, 4));
            return yVals;
        }
    }
}
