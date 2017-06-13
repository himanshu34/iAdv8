package com.agl.product.adw8_new.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.custom_view.SwipeRefreshLayoutBottom;
import com.agl.product.adw8_new.model.Keywords;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Post;
import com.agl.product.adw8_new.service.data.RequestDataKeywords;
import com.agl.product.adw8_new.service.data.ResponseDataKeywords;
import com.agl.product.adw8_new.utils.Session;
import com.agl.product.adw8_new.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeywordsActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayoutBottom.OnRefreshListener {

    private TableLayout ll;
    private PopupWindow filterPopup, customDatePopup;
    private View filterLayout, customPopupLayout;
    private LinearLayout llDateLayout,llContainer;
    Session session;
    HashMap<String, String> userData;
    private int offset = 0;
    private SwipeRefreshLayoutBottom swipeRefreshLayout;
    private int rowCount;
    private TextView textYesterday,textLastSevenDays,textLastThirtyDays,textCustom,textSelectedDateRange,textMessage;
    private ProgressBar progressBar;
    private ArrayList<Keywords> keywordsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        session = new Session(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        keywordsList = new ArrayList<Keywords>();
        ll = (TableLayout) findViewById(R.id.tableLayout);
        swipeRefreshLayout = (SwipeRefreshLayoutBottom) findViewById(R.id.swipeRefresh);
        textSelectedDateRange = (TextView) findViewById(R.id.textSelectedDateRange);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        llContainer = (LinearLayout) findViewById(R.id.llContainer);
        textMessage = (TextView) findViewById(R.id.textMessage);

        llDateLayout = (LinearLayout) findViewById(R.id.llDateLayout);
        filterLayout = getLayoutInflater().inflate(R.layout.custom_filter_layout, null);
        filterPopup = new PopupWindow(this);
        filterPopup.setWidth(500);

        filterPopup.setHeight(ListPopupWindow.WRAP_CONTENT);
        filterPopup.setOutsideTouchable(true);
        filterPopup.setContentView(filterLayout);
        filterPopup.setBackgroundDrawable(new BitmapDrawable());
        filterPopup.setFocusable(true);


        customPopupLayout = getLayoutInflater().inflate(R.layout.date_range_layout, null);

        customDatePopup = new PopupWindow(this);
        customDatePopup.setWidth(400);
        customDatePopup.setHeight(ListPopupWindow.WRAP_CONTENT);
        customDatePopup.setOutsideTouchable(true);
        customDatePopup.setContentView(customPopupLayout);
        customDatePopup.setBackgroundDrawable(new BitmapDrawable());
        customDatePopup.setFocusable(true);

        textYesterday = (TextView) customPopupLayout.findViewById(R.id.textYesterday);
        textLastSevenDays = (TextView) customPopupLayout.findViewById(R.id.textLastSevenDays);
        textLastThirtyDays = (TextView) customPopupLayout.findViewById(R.id.textLastThirtyDays);
        textCustom = (TextView) customPopupLayout.findViewById(R.id.textCustom);

        llDateLayout.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        textYesterday.setOnClickListener(this);
        textLastSevenDays.setOnClickListener(this);
        textLastThirtyDays.setOnClickListener(this);
        textCustom.setOnClickListener(this);

        userData = session.getUsuarioDetails();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llDateLayout:
                customDatePopup.showAsDropDown(llDateLayout, 0, 10);
                break;
            case R.id.textYesterday :
                setYesterday();
                break;
            case R.id.textLastSevenDays :
                setLastSeven();
                break;
            case R.id.textLastThirtyDays :
                setLastThirty();
                break;
            case R.id.textCustom :
                break;

        }
    }


    private void setYesterday() {
        textYesterday.setTextColor(getResources().getColor(R.color.colorPrimary));
        textLastSevenDays.setTextColor(getResources().getColor(R.color.black));
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.black));
        textSelectedDateRange.setText(Utils.getYesterdayDate());
        customDatePopup.dismiss();
    }

    private void setLastSeven() {
        textLastSevenDays.setTextColor(getResources().getColor(R.color.colorPrimary));
        textYesterday.setTextColor(getResources().getColor(R.color.black));
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.black));
        textSelectedDateRange.setText(Utils.getSevenDayBeforeDate()+"-"+Utils.getCurrentDate());
        customDatePopup.dismiss();
    }

    private void setLastThirty() {
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.colorPrimary));
        textLastSevenDays.setTextColor(getResources().getColor(R.color.black));
        textYesterday.setTextColor(getResources().getColor(R.color.black));
        textSelectedDateRange.setText(Utils.getThirtyDayBeforeDate()+"-"+Utils.getCurrentDate());
        customDatePopup.dismiss();
    }


    @Override
    public void onRefresh() {
        getKeywordsData();
    }

    private void getKeywordsData() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestDataKeywords requestKeywords = new RequestDataKeywords();
        requestKeywords.setAccess_token(userData.get(Session.KEY_ACCESS_TOKEN));

        requestKeywords.setpId("1");
        requestKeywords.setcId(userData.get(Session.KEY_AGENCY_CLIENT_ID));
        requestKeywords.setfDate("2017-06-02");
        requestKeywords.settDate("2017-06-08");
        requestKeywords.setLimit("10");
        requestKeywords.setOrderBy("ASC");
        requestKeywords.setSortBy("clicks");
        requestKeywords.setpId("1");
        requestKeywords.setOffset(offset);

        Call<ResponseDataKeywords> adsCall = apiAddClientService.getKeywordsList(requestKeywords);
        adsCall.enqueue(new Callback<ResponseDataKeywords>() {
            @Override
            public void onResponse(Call<ResponseDataKeywords> call, Response<ResponseDataKeywords> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response != null) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseDataKeywords res = response.body();
                            ArrayList<Keywords> keywords = res.getData();
                            if( keywords != null ){
                                if( keywords.size() > 0 ){
                                    llContainer.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    textMessage.setVisibility(View.GONE);
                                    keywordsList.addAll(keywords);
                                    createKeywordsTable(keywords);
                                    offset = offset + 20;
                                }else {
                                    if( offset == 0 ){
                                        llContainer.setVisibility(View.INVISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        textMessage.setVisibility(View.VISIBLE);
                                        textMessage.setText("No Keywords Found.");
                                    }
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            if( offset == 0 ){
                                llContainer.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.VISIBLE);
                                textMessage.setText("Some error occured.");
                            }else {
                                Toast.makeText(KeywordsActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        if( offset == 0 ){
                            llContainer.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.GONE);
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Some error occured.");
                        }else Toast.makeText(KeywordsActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    if( offset == 0 ){
                        llContainer.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        textMessage.setText("Some error occured.");
                    }else {
                        Toast.makeText(KeywordsActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseDataKeywords> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if (t != null) Log.d("TAG", t.getMessage());
                if( offset == 0 ){
                    llContainer.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText("There is some connectivity issue, please try again.");
                }else {
                    Toast.makeText(KeywordsActivity.this, "There is some connectivity issue, please try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void createKeywordsTable(ArrayList<Keywords> keywordsData) {
        if( rowCount != 0 )
            removeKeywordsTotalRows();
        for (int i = 0; i < keywordsData.size(); i++) {
            TableRow row1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row1.setLayoutParams(lp);
            setKeywordsOtherRow(row1, lp, ++rowCount, keywordsData.get(i));
        }
    }
    private void removeKeywordsTotalRows() {
        ll.removeViewAt(ll.getChildCount()-1);
    }

    private void setKeywordsOtherRow(TableRow row, TableRow.LayoutParams lp, int i, Keywords keyword) {
        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.cell_shape);
        LinearLayout.LayoutParams  layoutParams = new LinearLayout.LayoutParams( 20,LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText(keyword.getKeyword_name());
        row.addView(textView, lp);

        TextView textView1 = new TextView(this);
        textView1.setBackgroundResource(R.drawable.cell_shape);
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(layoutParams);
        textView1.setGravity(Gravity.CENTER_VERTICAL);
        textView1.setText(keyword.getImpressions());
        row.addView(textView1, lp);


        TextView textView2 = new TextView(this);
        textView2.setBackgroundResource(R.drawable.cell_shape);
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(layoutParams);
        textView2.setGravity(Gravity.CENTER_VERTICAL);
        textView2.setText(keyword.getClicks());
        row.addView(textView2, lp);


        TextView textView3 = new TextView(this);
        textView3.setBackgroundResource(R.drawable.cell_shape);
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(layoutParams);
        textView3.setGravity(Gravity.CENTER_VERTICAL);
        textView3.setText(keyword.getCost());
        row.addView(textView3, lp);

        TextView textView4 = new TextView(this);
        textView4.setBackgroundResource(R.drawable.cell_shape);
        textView4.setPadding(20, 20, 20, 20);
        textView4.setLayoutParams(layoutParams);
        textView4.setGravity(Gravity.CENTER_VERTICAL);
        textView4.setText(keyword.getConverted_clicks());
        row.addView(textView4, lp);


        TextView textView5 = new TextView(this);
        textView5.setBackgroundResource(R.drawable.cell_shape);
        textView5.setPadding(20, 20, 20, 20);
        textView5.setLayoutParams(layoutParams);
        textView5.setGravity(Gravity.CENTER_VERTICAL);
        textView5.setText(keyword.getAvg_cpc());
        row.addView(textView5, lp);


        TextView textView6 = new TextView(this);
        textView6.setBackgroundResource(R.drawable.cell_shape);
        textView6.setPadding(20, 20, 20, 20);
        textView6.setLayoutParams(layoutParams);
        textView6.setGravity(Gravity.CENTER_VERTICAL);
        textView6.setText(keyword.getCpa());
        row.addView(textView6, lp);


        TextView textView7 = new TextView(this);
        textView7.setBackgroundResource(R.drawable.cell_shape);
        textView7.setPadding(20, 20, 20, 20);
        textView7.setLayoutParams(layoutParams);
        textView7.setGravity(Gravity.CENTER_VERTICAL);
        textView7.setText(keyword.getKeyword_state());
        row.addView(textView7, lp);


        TextView textView8 = new TextView(this);
        textView8.setBackgroundResource(R.drawable.cell_shape);
        textView8.setPadding(20, 20, 20, 20);
        textView8.setLayoutParams(layoutParams);
        textView8.setGravity(Gravity.CENTER_VERTICAL);
        textView8.setText("");
        row.addView(textView8, lp);



        TextView textView9 = new TextView(this);
        textView9.setBackgroundResource(R.drawable.cell_shape);
        textView9.setPadding(20, 20, 20, 20);
        textView9.setLayoutParams(layoutParams);
        textView9.setGravity(Gravity.CENTER_VERTICAL);
        textView9.setText("");
        row.addView(textView9, lp);


        TextView textView10 = new TextView(this);
        textView10.setBackgroundResource(R.drawable.cell_shape);
        textView10.setPadding(20, 20, 20, 20);
        textView10.setLayoutParams(layoutParams);
        textView10.setGravity(Gravity.CENTER_VERTICAL);
        textView10.setText("");
        row.addView(textView10, lp);



        ll.addView(row, i);
    }
}
