package com.agl.product.adw8_new.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.activity.DataActivity;


public class DataCampaignFragment extends Fragment {

    private DataActivity dataActivity;
    private TableLayout ll;

    public DataCampaignFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_campaign_fragment_layout, container, false);
        dataActivity = (DataActivity) getActivity();
        ll = (TableLayout) view.findViewById(R.id.tableLayout);
        createTable();
        return view;
    }

    private void createTable() {
        for (int i = 0; i < 5; i++) {
            TableRow row = new TableRow(dataActivity);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row.setLayoutParams(lp);

            if (i == 0) {
                setFirstRow(row, lp);
            } else setOtherRow(row, lp ,i);
        }
    }

    private void setOtherRow(TableRow row, TableRow.LayoutParams lp, int i) {
        TextView textView = new TextView(dataActivity);
        textView.setBackgroundResource(R.drawable.cell_shape);
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setText("Campaign"+i);
        row.addView(textView, lp);

        TextView textView1 = new TextView(dataActivity);
        textView1.setBackgroundResource(R.drawable.cell_shape);
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setText("sdfdfdfdffd dsfd sdfsd fsd ");
        row.addView(textView1, lp);

        TextView textView2 = new TextView(dataActivity);
        textView2.setBackgroundResource(R.drawable.cell_shape);
        textView2.setText("sdfdfdfdffd dfdsf sdf ");
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        row.addView(textView2, lp);

        TextView textView3 = new TextView(dataActivity);
        textView3.setText("sdfdfdfdffd dfdsf dsf");
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(lp);
        textView3.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView3, lp);


        ll.addView(row, i);
    }

    private void setFirstRow(TableRow row, TableRow.LayoutParams lp) {
        TextView textView = new TextView(dataActivity);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setText("Name");
        textView.setGravity(Gravity.CENTER);
        row.addView(textView, lp);

        TextView textView1 = new TextView(dataActivity);
        textView1.setTextColor(getResources().getColor(R.color.black));
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setText("Impression");
        textView1.setGravity(Gravity.CENTER);
        row.addView(textView1, lp);

        TextView textView2 = new TextView(dataActivity);
        textView2.setTextColor(getResources().getColor(R.color.black));
        textView2.setText("CTR");
        textView2.setGravity(Gravity.CENTER);
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        row.addView(textView2, lp);

        TextView textView3 = new TextView(dataActivity);
        textView3.setText("Click");
        textView3.setPadding(20, 20, 20, 20);
        textView3.setGravity(Gravity.CENTER);
        textView3.setLayoutParams(lp);
        textView3.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView3, lp);
        ll.addView(row, 0);
    }
}
