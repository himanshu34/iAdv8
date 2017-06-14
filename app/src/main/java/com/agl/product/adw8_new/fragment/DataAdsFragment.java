package com.agl.product.adw8_new.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.model.Ads;

import java.util.ArrayList;

public class DataAdsFragment extends Fragment {

    HorizontalScrollView hrHeader, hrData;
    TableLayout tlAddName, tlDataTable;
    private ArrayList<Ads> adData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.data_ads_fragment, container, false);

        hrHeader = (HorizontalScrollView) rootView.findViewById(R.id.hrHeader);
        tlAddName = (TableLayout) rootView.findViewById(R.id.tlAdName);
        hrData = (HorizontalScrollView) rootView.findViewById(R.id.hrData);
        tlDataTable = (TableLayout) rootView.findViewById(R.id.data_table_layout);

        if(getArguments().containsKey("adsList")) {
            adData = getArguments().getParcelableArrayList("adsList");
            createTable(adData);
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

    private void createTable(ArrayList<Ads> adsData) {
        if(adsData != null) {
            for (int i = 0; i < adsData.size() ; i++) {
                setFirstRow(i, adsData.get(i));
                setOtherRow(i, adsData.get(i));
            }
        }
    }

    private void setOtherRow(int pos, Ads adsData) {
        TableRow row = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.span = 1;
        row.setLayoutParams(lp);

        TextView textView1 = new TextView(getActivity());
        textView1.setBackgroundResource(R.drawable.cell_shape);
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setGravity(Gravity.CENTER);
        textView1.setText(adsData.getClicks());
        row.addView(textView1, lp);

        TextView textView2 = new TextView(getActivity());
        textView2.setBackgroundResource(R.drawable.cell_shape);
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        textView2.setGravity(Gravity.CENTER);
        textView2.setText(adsData.getImpressions());
        row.addView(textView2, lp);

        TextView textView3 = new TextView(getActivity());
        textView3.setText(adsData.getAvg_cpc());
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(lp);
        textView3.setGravity(Gravity.CENTER);
        textView3.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView3, lp);

        TextView textView4 = new TextView(getActivity());
        textView4.setText(adsData.getCost());
        textView4.setPadding(20, 20, 20, 20);
        textView4.setLayoutParams(lp);
        textView4.setGravity(Gravity.CENTER);
        textView4.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView4, lp);

        TextView textView5 = new TextView(getActivity());
        textView5.setText(adsData.getCtr());
        textView5.setPadding(20, 20, 20, 20);
        textView5.setLayoutParams(lp);
        textView5.setGravity(Gravity.CENTER);
        textView5.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView5, lp);

        TextView textView6 = new TextView(getActivity());
        textView6.setText("");
        textView6.setPadding(20, 20, 20, 20);
        textView6.setGravity(Gravity.CENTER);
        textView6.setLayoutParams(lp);
        textView6.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView6, lp);

        TextView textView7 = new TextView(getActivity());
        textView7.setText(adsData.getConverted_clicks());
        textView7.setPadding(20, 20, 20, 20);
        textView7.setGravity(Gravity.CENTER);
        textView7.setLayoutParams(lp);
        textView7.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView7, lp);

        TextView textView8 = new TextView(getActivity());
        textView8.setText("");
        textView8.setPadding(20, 20, 20, 20);
        textView8.setGravity(Gravity.CENTER);
        textView8.setLayoutParams(lp);
        textView8.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView8, lp);

        TextView textView9 = new TextView(getActivity());
        textView9.setText("");
        textView9.setPadding(20, 20, 20, 20);
        textView9.setGravity(Gravity.CENTER);
        textView9.setLayoutParams(lp);
        textView9.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView9, lp);

        tlDataTable.addView(row, pos, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
    }

    private void setFirstRow(int pos, Ads adsData) {
        TableRow row = new TableRow(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.first_row, row, false);
        TextView tv = (TextView)v.findViewById(R.id.text_view);
        tv.setText(adsData.getAd());

        tlAddName.addView(v, pos);
    }
}
