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
import com.agl.product.adw8_new.model.CampaignData;

import java.util.ArrayList;

public class DataAdsFragment extends Fragment {

    private TableLayout ll;
    private ArrayList<CampaignData> adData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_campaign_fragment_layout, container, false);
        ll = (TableLayout) view.findViewById(R.id.tableLayout);

        if(getArguments().containsKey("adsList")) {
            adData = getArguments().getParcelableArrayList("adsList");
            createTable(adData);
        }

        return view;
    }

    private void createTable(ArrayList<CampaignData> campaignData) {
        if(campaignData != null) {
            for (int i = 0; i < campaignData.size() ; i++) {
                TableRow row1 = new TableRow(getActivity());
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                lp1.span = 1;
                row1.setLayoutParams(lp1);
                setOtherRow(row1, lp1 ,i,campaignData.get(i));
            }
        }

        TableRow row = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp.span = 1;
        row.setLayoutParams(lp);
        setFirstRow(row, lp);
    }

    private void setOtherRow(TableRow row, TableRow.LayoutParams lp, int i, CampaignData campaignData) {
        TextView textView = new TextView(getActivity());
        textView.setBackgroundResource(R.drawable.cell_shape);
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText(campaignData.getCampaign());
        row.addView(textView, lp);

        TextView textView1 = new TextView(getActivity());
        textView1.setBackgroundResource(R.drawable.cell_shape);
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setGravity(Gravity.CENTER);
        textView1.setText(campaignData.getImpressions());
        row.addView(textView1, lp);

        TextView textView2 = new TextView(getActivity());
        textView2.setBackgroundResource(R.drawable.cell_shape);
        textView2.setText(campaignData.getCtr());
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        textView2.setGravity(Gravity.CENTER);
        row.addView(textView2, lp);

        TextView textView3 = new TextView(getActivity());
        textView3.setText(campaignData.getClicks());
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(lp);
        textView3.setGravity(Gravity.CENTER);
        textView3.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView3, lp);

        ll.addView(row, i);
    }

    private void setFirstRow(TableRow row, TableRow.LayoutParams lp) {
        TextView textView = new TextView(getActivity());
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setText("Name");
        textView.setGravity(Gravity.CENTER);
        row.addView(textView, lp);

        TextView textView1 = new TextView(getActivity());
        textView1.setTextColor(getResources().getColor(R.color.black));
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setText("Impression");
        textView1.setGravity(Gravity.CENTER);
        row.addView(textView1, lp);

        TextView textView2 = new TextView(getActivity());
        textView2.setTextColor(getResources().getColor(R.color.black));
        textView2.setText("CTR");
        textView2.setGravity(Gravity.CENTER);
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        row.addView(textView2, lp);

        TextView textView3 = new TextView(getActivity());
        textView3.setText("Click");
        textView3.setPadding(20, 20, 20, 20);
        textView3.setGravity(Gravity.CENTER);
        textView3.setLayoutParams(lp);
        textView3.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView3, lp);
        ll.addView(row, 0);
    }
}
