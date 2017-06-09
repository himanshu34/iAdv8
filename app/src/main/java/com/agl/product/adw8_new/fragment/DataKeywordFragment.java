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
import com.agl.product.adw8_new.model.Keywords;

import java.util.ArrayList;

public class DataKeywordFragment extends Fragment {

    private TableLayout ll;
    private ArrayList<Keywords> keywordData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_keyword_fragment, container, false);
        ll = (TableLayout) view.findViewById(R.id.tableLayout);

        if(getArguments().containsKey("keywordList")) {
            keywordData = getArguments().getParcelableArrayList("keywordList");
            createTable(keywordData);
        }

        return view;
    }

    private void createTable(ArrayList<Keywords> keywordData) {
        if(keywordData != null) {
            for (int i = 0; i < keywordData.size() ; i++) {
                TableRow row1 = new TableRow(getActivity());
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                lp1.span = 1;
                row1.setLayoutParams(lp1);
                setOtherRow(row1, lp1 , i, keywordData.get(i));
            }
        }

        TableRow row = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp.span = 1;
        row.setLayoutParams(lp);
        setFirstRow(row, lp);
    }

    private void setOtherRow(TableRow row, TableRow.LayoutParams lp, int i, Keywords keywordData) {
        TextView textView = new TextView(getActivity());
        textView.setBackgroundResource(R.drawable.cell_shape);
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText(keywordData.getKeyword_name());
        row.addView(textView, lp);

        TextView textView1 = new TextView(getActivity());
        textView1.setBackgroundResource(R.drawable.cell_shape);
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setGravity(Gravity.CENTER);
        textView1.setText(keywordData.getImpressions());
        row.addView(textView1, lp);

        TextView textView2 = new TextView(getActivity());
        textView2.setBackgroundResource(R.drawable.cell_shape);
        textView2.setText(keywordData.getCtr());
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        textView2.setGravity(Gravity.CENTER);
        row.addView(textView2, lp);

        TextView textView3 = new TextView(getActivity());
        textView3.setText(keywordData.getClicks());
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(lp);
        textView3.setGravity(Gravity.CENTER);
        textView3.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView3, lp);


        TextView textView4 = new TextView(getActivity());
        textView4.setText(keywordData.getAvg_cpc());
        textView4.setPadding(20, 20, 20, 20);
        textView4.setLayoutParams(lp);
        textView4.setGravity(Gravity.CENTER);
        textView4.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView4, lp);


        TextView textView6 = new TextView(getActivity());
        textView6.setText(keywordData.getCost());
        textView6.setPadding(20, 20, 20, 20);
        textView6.setLayoutParams(lp);
        textView6.setGravity(Gravity.CENTER);
        textView6.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView6, lp);


        /*TextView textView7 = new TextView(getActivity());
        textView7.setText(keywordData.get());
        textView7.setPadding(20, 20, 20, 20);
        textView7.setLayoutParams(lp);
        textView7.setGravity(Gravity.CENTER);
        textView7.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView7, lp);*/


        TextView textView8 = new TextView(getActivity());
        textView8.setText(keywordData.getConverted_clicks());
        textView8.setPadding(20, 20, 20, 20);
        textView8.setLayoutParams(lp);
        textView8.setGravity(Gravity.CENTER);
        textView8.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView8, lp);

        TextView textView9 = new TextView(getActivity());
        textView9.setText(keywordData.getKeyword_state());
        textView9.setPadding(20, 20, 20, 20);
        textView9.setLayoutParams(lp);
        textView9.setGravity(Gravity.CENTER);
        textView9.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView9, lp);



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



        TextView textView5 = new TextView(getActivity());
        textView5.setText("Avg. CPC");
        textView5.setPadding(20, 20, 20, 20);
        textView5.setGravity(Gravity.CENTER);
        textView5.setLayoutParams(lp);
        textView5.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView5, lp);

        TextView textView7 = new TextView(getActivity());
        textView7.setText("Cost");
        textView7.setPadding(20, 20, 20, 20);
        textView7.setGravity(Gravity.CENTER);
        textView7.setLayoutParams(lp);
        textView7.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView7, lp);

       /* TextView textView8 = new TextView(getActivity());
        textView8.setText("Avg Pos");
        textView8.setPadding(20, 20, 20, 20);
        textView8.setGravity(Gravity.CENTER);
        textView8.setLayoutParams(lp);
        textView8.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView8, lp);*/


        TextView textView9 = new TextView(getActivity());
        textView9.setText("Conv.");
        textView9.setPadding(20, 20, 20, 20);
        textView9.setGravity(Gravity.CENTER);
        textView9.setLayoutParams(lp);
        textView9.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView9, lp);

        TextView textView10 = new TextView(getActivity());
        textView10.setText("Status");
        textView10.setPadding(20, 20, 20, 20);
        textView10.setGravity(Gravity.CENTER);
        textView10.setLayoutParams(lp);
        textView10.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView10, lp);

        ll.addView(row, 0);



    }
}
