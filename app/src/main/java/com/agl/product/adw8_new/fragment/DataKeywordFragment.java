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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.model.Keywords;

import java.util.ArrayList;

public class DataKeywordFragment extends Fragment {

    HorizontalScrollView hrHeader, hrData;
    TableLayout tlAddName, tlDataTable;
    private ArrayList<Keywords> keywordData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.data_keyword_fragment, container, false);
        hrHeader = (HorizontalScrollView) rootView.findViewById(R.id.hrHeader);
        tlAddName = (TableLayout) rootView.findViewById(R.id.tlAdName);
        hrData = (HorizontalScrollView) rootView.findViewById(R.id.hrData);
        tlDataTable = (TableLayout) rootView.findViewById(R.id.data_table_layout);

        if(getArguments().containsKey("keywordList")) {
            keywordData = getArguments().getParcelableArrayList("keywordList");
            createTable(keywordData);
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

    private void createTable(ArrayList<Keywords> keywordData) {
        if(keywordData != null) {
            for (int i = 0; i < keywordData.size() ; i++) {
                setFirstRow(i, keywordData.get(i));
                setOtherRow(i, keywordData.get(i));
            }
        }
    }

    private void setOtherRow(int pos, Keywords keywordData) {
        TableRow row = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_textview,row,false );
        TextView textView1 = (TextView) view.findViewById(R.id.text_view);
        textView1.setText(keywordData.getClicks());
        row.addView(textView1);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.row_textview,row,false );
        TextView textView2 = (TextView) view.findViewById(R.id.text_view);
        textView2.setText(keywordData.getImpressions());
        row.addView(textView2);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.row_textview,row,false );
        TextView textView3 = (TextView) view.findViewById(R.id.text_view);
        textView3.setText(keywordData.getAvg_cpc());
        row.addView(textView3);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.row_textview,row,false );
        TextView textView4 = (TextView) view.findViewById(R.id.text_view);
        textView4.setText(keywordData.getCost());
        row.addView(textView4);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.row_textview,row,false );
        TextView textView5 = (TextView) view.findViewById(R.id.text_view);
        textView5.setText(keywordData.getCtr());
        row.addView(textView5);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.row_textview,row,false );
        TextView textView6 = (TextView) view.findViewById(R.id.text_view);
        textView6.setText(keywordData.getAvg_position());
        row.addView(textView6);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.row_textview,row,false );
        TextView textView7 = (TextView) view.findViewById(R.id.text_view);
        textView7.setText(keywordData.getConverted_clicks());
        row.addView(textView7);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.row_textview,row,false );
        TextView textView8 = (TextView) view.findViewById(R.id.text_view);
        textView8.setText(keywordData.getCost_per_conversion());
        row.addView(textView8);

        view = LayoutInflater.from(getActivity()).inflate(R.layout.row_textview,row,false );
        TextView textView9 = (TextView) view.findViewById(R.id.text_view);
        textView9.setText(keywordData.getKeyword_state());
        row.addView(textView9);

        tlDataTable.addView(row, pos, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
    }

    private void setFirstRow(int pos, Keywords keywordData) {
        TableRow row = new TableRow(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.first_row, row, false);
        TextView tv = (TextView)v.findViewById(R.id.text_view);
        tv.setText(keywordData.getKeyword_name());
        tlAddName.addView(v, pos);
    }
}
