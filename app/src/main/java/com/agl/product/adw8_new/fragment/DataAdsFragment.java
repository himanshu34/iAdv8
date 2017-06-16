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
    private String rupeeSymbol;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.data_ads_fragment, container, false);

        hrHeader = (HorizontalScrollView) rootView.findViewById(R.id.hrHeader);
        tlAddName = (TableLayout) rootView.findViewById(R.id.tlAdName);
        hrData = (HorizontalScrollView) rootView.findViewById(R.id.hrData);
        tlDataTable = (TableLayout) rootView.findViewById(R.id.data_table_layout);
        rupeeSymbol = getString(R.string.rupee);
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
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView1 = (TextView) view.findViewById(R.id.text_view);
        textView1.setText(adsData.getClicks());
        row.addView(textView1);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView2 = (TextView) view.findViewById(R.id.text_view);
        textView2.setText(adsData.getImpressions());
        row.addView(textView2);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView3 = (TextView) view.findViewById(R.id.text_view);
        textView3.setText(rupeeSymbol+" "+adsData.getAvg_cpc());
        row.addView(textView3);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView4 = (TextView) view.findViewById(R.id.text_view);
        textView4.setText(rupeeSymbol+" "+adsData.getCost());
        row.addView(textView4);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView5 = (TextView) view.findViewById(R.id.text_view);
        textView5.setText(adsData.getCtr());
        row.addView(textView5);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView7 = (TextView) view.findViewById(R.id.text_view);
        textView7.setText(adsData.getConverted_clicks());
        row.addView(textView7);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView8 = (TextView) view.findViewById(R.id.text_view);
        textView8.setText(rupeeSymbol+" "+adsData.getCpa());
        row.addView(textView8);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.top_row_textview,row,false );
        TextView textView9 = (TextView) view.findViewById(R.id.text_view);
        textView9.setText(adsData.getConversion_rate());
        row.addView(textView9);

        tlDataTable.addView(row, pos, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
    }

    private void setFirstRow(int pos, Ads adsData) {
        TableRow row = new TableRow(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.top_first_row, row, false);
        TextView tv = (TextView)v.findViewById(R.id.text_view);
        tv.setText(adsData.getAd());
        tlAddName.addView(v, pos);
    }
}
