package com.agl.product.adw8_new.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.model.CampaignData;

import java.util.ArrayList;

public class DataCampaignFragment extends Fragment {

    HorizontalScrollView hrHeader, hrData;
    TableLayout tlAddName, tlDataTable;
    private ArrayList<CampaignData> campaignData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.data_campaign_fragment_layout, container, false);
        hrHeader = (HorizontalScrollView) rootView.findViewById(R.id.hrHeader);
        tlAddName = (TableLayout) rootView.findViewById(R.id.tlAdName);
        hrData = (HorizontalScrollView) rootView.findViewById(R.id.hrData);
        tlDataTable = (TableLayout) rootView.findViewById(R.id.data_table_layout);

        if(getArguments().containsKey("campaignList")) {
            campaignData = getArguments().getParcelableArrayList("campaignList");
            createTable(campaignData);
        }

        setListeners();

        return rootView;
    }

    private void setListeners() {
        hrData.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                hrHeader.scrollTo(hrData.getScrollX(), hrData.getScrollY());
            }
        });
    }

    private void createTable(ArrayList<CampaignData> campaignData) {
        if(campaignData != null) {
            for (int i = 0; i < campaignData.size() ; i++) {
                setFirstRow(i, campaignData.get(i));
                setOtherRow(i, campaignData.get(i));
            }
        }
    }

    private void setOtherRow(int pos, CampaignData campaignData) {
        TableRow row = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView1 = (TextView) view.findViewById(R.id.text_view);
        textView1.setText(campaignData.getBudget());
        row.addView(textView1);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView2 = (TextView) view.findViewById(R.id.text_view);
        textView2.setText(campaignData.getClicks());
        row.addView(textView2);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView3 = (TextView) view.findViewById(R.id.text_view);
        textView3.setText(campaignData.getImpressions());
        row.addView(textView3);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView4 = (TextView) view.findViewById(R.id.text_view);
        textView4.setText(campaignData.getAvg_cpc());
        row.addView(textView4);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView5 = (TextView) view.findViewById(R.id.text_view);
        textView5.setText(campaignData.getCost());
        row.addView(textView5);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView6 = (TextView) view.findViewById(R.id.text_view);
        textView6.setText(campaignData.getCtr());
        row.addView(textView6);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView8 = (TextView) view.findViewById(R.id.text_view);
        textView8.setText(campaignData.getConverted_clicks());
        row.addView(textView8);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView9 = (TextView) view.findViewById(R.id.text_view);
        textView9.setText(campaignData.getCpa());
        row.addView(textView9);

        tlDataTable.addView(row, pos, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
    }

    private void setFirstRow(int pos, CampaignData campaignData) {
        TableRow row = new TableRow(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.top_campaign_first_row, row, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        TextView tv = (TextView) v.findViewById(R.id.text_view);
        if(campaignData.getAdvertising_channel().equalsIgnoreCase("display")) {
            imageView.setImageResource(R.drawable.ic_campaign_display);
        } else {
            imageView.setImageResource(R.drawable.ic_campaign_search);
        }
        tv.setText(campaignData.getCampaign());
        tlAddName.addView(v, pos);
    }
}
