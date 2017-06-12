package com.agl.product.adw8_new.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.model.AdListingData;
import com.agl.product.adw8_new.model.Adgroup;
import com.agl.product.adw8_new.model.CampaignData;
import com.agl.product.adw8_new.model.Keywords;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Post;
import com.agl.product.adw8_new.service.data.RequestDataAdgroup;
import com.agl.product.adw8_new.service.data.RequestDataAds;
import com.agl.product.adw8_new.service.data.RequestDataCampaignDetails;
import com.agl.product.adw8_new.service.data.RequestDataKeywords;
import com.agl.product.adw8_new.service.data.ResponseDataAdgroup;
import com.agl.product.adw8_new.service.data.ResponseDataAds;
import com.agl.product.adw8_new.service.data.ResponseDataCampaignDetails;
import com.agl.product.adw8_new.service.data.ResponseDataKeywords;
import com.agl.product.adw8_new.utils.Session;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampaignActivity extends AppCompatActivity implements View.OnClickListener {

    private TableLayout ll;
    private PopupWindow filterPopup, customDatePopup;
    private View filterLayout, customPopupLayout;
    private LinearLayout llDateLayout;
    Session session;
    HashMap<String, String> userData;
    private String campaignType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        session = new Session(this);

        Intent intent = getIntent();
        if (intent != null)
            campaignType = intent.getStringExtra("type");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setTitle(campaignType.toUpperCase());

        ll = (TableLayout) findViewById(R.id.tableLayout);
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

        llDateLayout.setOnClickListener(this);

        userData = session.getUsuarioDetails();
        requestCampaign();
    }

    private void requestCampaign() {

        switch (campaignType) {
            case "ads":
                getAdsData();
                break;
            case "keywords":
                getKeywordsData();
                break;
            case "campaign":
                getCampaignData();
                break;
            case "adgroup":
                getAdGroupData();
                break;

        }

    }

    private void getAdGroupData() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestDataAdgroup requestKeywords = new RequestDataAdgroup();
        requestKeywords.setAccess_token(userData.get(Session.KEY_ACCESS_TOKEN));
        requestKeywords.setpId("1");
        requestKeywords.setcId(userData.get(Session.KEY_AGENCY_CLIENT_ID));
        requestKeywords.setfDate("2017-06-02");
        requestKeywords.settDate("2017-06-08");
        requestKeywords.setLimit("2");
        requestKeywords.setOrderBy("ASC");
        requestKeywords.setSortBy("clicks");
        requestKeywords.setpId("1");

        Call<ResponseDataAdgroup> adsCall = apiAddClientService.getAdgroupList(requestKeywords);
        adsCall.enqueue(new Callback<ResponseDataAdgroup>() {
            @Override
            public void onResponse(Call<ResponseDataAdgroup> call, Response<ResponseDataAdgroup> response) {

                if ( response != null ){
                    try {
                        ResponseDataAdgroup body = response.body();
                        ArrayList<Adgroup> data = body.getData();
                        if ( data != null && data.size() > 0 ){
                            createAdgroupTable(data);
                        }
                    }catch (Exception e ){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseDataAdgroup> call, Throwable t) {
                if ( t != null ) t.printStackTrace();
            }
        });

    }

    private void createAdgroupTable(ArrayList<Adgroup> data) {
        for (int i = 0; i < data.size(); i++) {
            TableRow row1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row1.setLayoutParams(lp);
            setAdgroupOtherRow(row1, lp, i, data.get(i));
        }

        TableRow row1 = new TableRow(this);
        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp1.span = 1;
        row1.setLayoutParams(lp1);
        setAdgroupHeaderRow(row1, lp1);
    }

    private void setAdgroupHeaderRow(TableRow row, TableRow.LayoutParams lp) {
        TextView textView = new TextView(this);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setText("Name");
        textView.setGravity(Gravity.CENTER);
        row.addView(textView, lp);

        TextView textView1 = new TextView(this);
        textView1.setTextColor(getResources().getColor(R.color.black));
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setText("Impressions");
        textView1.setGravity(Gravity.CENTER);
        row.addView(textView1, lp);

        TextView textView2 = new TextView(this);
        textView2.setTextColor(getResources().getColor(R.color.black));
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        textView2.setText("CTR");
        textView2.setGravity(Gravity.CENTER);
        row.addView(textView2, lp);


        TextView textView3 = new TextView(this);
        textView3.setTextColor(getResources().getColor(R.color.black));
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(lp);
        textView3.setText("Clicks");
        textView3.setGravity(Gravity.CENTER);
        row.addView(textView3, lp);


        TextView textView4 = new TextView(this);
        textView4.setTextColor(getResources().getColor(R.color.black));
        textView4.setPadding(20, 20, 20, 20);
        textView4.setLayoutParams(lp);
        textView4.setText("Cost");
        textView4.setGravity(Gravity.CENTER);
        row.addView(textView4, lp);


        TextView textView5 = new TextView(this);
        textView5.setTextColor(getResources().getColor(R.color.black));
        textView5.setPadding(20, 20, 20, 20);
        textView5.setLayoutParams(lp);
        textView5.setText("Conv.");
        textView5.setGravity(Gravity.CENTER);
        row.addView(textView5, lp);


        ll.addView(row, 0);
    }

    private void setAdgroupOtherRow(TableRow row, TableRow.LayoutParams lp, int i, Adgroup data) {
        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.cell_shape);
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText(data.getAdgroup());
        row.addView(textView, lp);

        TextView textView1 = new TextView(this);
        textView1.setBackgroundResource(R.drawable.cell_shape);
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setGravity(Gravity.CENTER_VERTICAL);
        textView1.setText(data.getImpressions());
        row.addView(textView1, lp);

        TextView textView2 = new TextView(this);
        textView2.setBackgroundResource(R.drawable.cell_shape);
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        textView2.setGravity(Gravity.CENTER_VERTICAL);
        textView2.setText(data.getCtr());
        row.addView(textView2, lp);

        TextView textView3 = new TextView(this);
        textView3.setBackgroundResource(R.drawable.cell_shape);
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(lp);
        textView3.setGravity(Gravity.CENTER_VERTICAL);
        textView3.setText(data.getClicks());
        row.addView(textView3, lp);

        TextView textView4 = new TextView(this);
        textView4.setBackgroundResource(R.drawable.cell_shape);
        textView4.setPadding(20, 20, 20, 20);
        textView4.setLayoutParams(lp);
        textView4.setGravity(Gravity.CENTER_VERTICAL);
        textView4.setText(data.getCost());
        row.addView(textView4, lp);


        TextView textView5 = new TextView(this);
        textView5.setBackgroundResource(R.drawable.cell_shape);
        textView5.setPadding(20, 20, 20, 20);
        textView5.setLayoutParams(lp);
        textView5.setGravity(Gravity.CENTER_VERTICAL);
        textView5.setText(data.getConverted_clicks());
        row.addView(textView5, lp);

        ll.addView(row, i);
    }

    private void getKeywordsData() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestDataKeywords requestKeywords = new RequestDataKeywords();
        requestKeywords.setAccess_token(userData.get(Session.KEY_ACCESS_TOKEN));

        requestKeywords.setpId("1");
        requestKeywords.setcId(userData.get(Session.KEY_AGENCY_CLIENT_ID));
        requestKeywords.setfDate("2017-06-02");
        requestKeywords.settDate("2017-06-08");
        requestKeywords.setLimit("2");
        requestKeywords.setOrderBy("ASC");
        requestKeywords.setSortBy("clicks");
        requestKeywords.setpId("1");
        requestKeywords.setOffset(1);

        Call<ResponseDataKeywords> adsCall = apiAddClientService.getKeywordsList(requestKeywords);
        adsCall.enqueue(new Callback<ResponseDataKeywords>() {
            @Override
            public void onResponse(Call<ResponseDataKeywords> call, Response<ResponseDataKeywords> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseDataKeywords res = response.body();
                            ArrayList<Keywords> keywords = res.getData();
                            if (keywords != null && keywords.size() > 0) {
                                createKeywordsTable(keywords);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                } else {

                }

            }

            @Override
            public void onFailure(Call<ResponseDataKeywords> call, Throwable t) {
                if (t != null) Log.d("TAG", t.getMessage());
            }
        });
    }

    private void createKeywordsTable(ArrayList<Keywords> keywordsData) {
        for (int i = 0; i < keywordsData.size(); i++) {
            TableRow row1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row1.setLayoutParams(lp);
            setKeywordsOtherRow(row1, lp, i, keywordsData.get(i));
        }

        TableRow row1 = new TableRow(this);
        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp1.span = 1;
        row1.setLayoutParams(lp1);
        setKeywordsHeaderRow(row1, lp1);
    }

    private void setKeywordsOtherRow(TableRow row, TableRow.LayoutParams lp, int i, Keywords keyword) {
        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.cell_shape);
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText(keyword.getKeyword_name());
        row.addView(textView, lp);

        TextView textView1 = new TextView(this);
        textView1.setBackgroundResource(R.drawable.cell_shape);
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setGravity(Gravity.CENTER_VERTICAL);
        textView1.setText(keyword.getImpressions());
        row.addView(textView1, lp);


        TextView textView2 = new TextView(this);
        textView2.setBackgroundResource(R.drawable.cell_shape);
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        textView2.setGravity(Gravity.CENTER_VERTICAL);
        textView2.setText(keyword.getClicks());
        row.addView(textView2, lp);


        TextView textView3 = new TextView(this);
        textView3.setBackgroundResource(R.drawable.cell_shape);
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(lp);
        textView3.setGravity(Gravity.CENTER_VERTICAL);
        textView3.setText(keyword.getCost());
        row.addView(textView3, lp);

        TextView textView4 = new TextView(this);
        textView4.setBackgroundResource(R.drawable.cell_shape);
        textView4.setPadding(20, 20, 20, 20);
        textView4.setLayoutParams(lp);
        textView4.setGravity(Gravity.CENTER_VERTICAL);
        textView4.setText(keyword.getConverted_clicks());
        row.addView(textView4, lp);


        TextView textView5 = new TextView(this);
        textView5.setBackgroundResource(R.drawable.cell_shape);
        textView5.setPadding(20, 20, 20, 20);
        textView5.setLayoutParams(lp);
        textView5.setGravity(Gravity.CENTER_VERTICAL);
        textView5.setText(keyword.getAvg_cpc());
        row.addView(textView5, lp);


        TextView textView6 = new TextView(this);
        textView6.setBackgroundResource(R.drawable.cell_shape);
        textView6.setPadding(20, 20, 20, 20);
        textView6.setLayoutParams(lp);
        textView6.setGravity(Gravity.CENTER_VERTICAL);
        textView6.setText(keyword.getCpa());
        row.addView(textView6, lp);


        TextView textView7 = new TextView(this);
        textView7.setBackgroundResource(R.drawable.cell_shape);
        textView7.setPadding(20, 20, 20, 20);
        textView7.setLayoutParams(lp);
        textView7.setGravity(Gravity.CENTER_VERTICAL);
        textView7.setText(keyword.getKeyword_state());
        row.addView(textView7, lp);


        ll.addView(row, i);
    }

    private void setKeywordsHeaderRow(TableRow row, TableRow.LayoutParams lp) {
        TextView textView = new TextView(this);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setText("Name");
        textView.setGravity(Gravity.CENTER);
        row.addView(textView, lp);

        TextView textView1 = new TextView(this);
        textView1.setTextColor(getResources().getColor(R.color.black));
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setText("Impressions");
        textView1.setGravity(Gravity.CENTER);
        row.addView(textView1, lp);


        TextView textView3 = new TextView(this);
        textView3.setTextColor(getResources().getColor(R.color.black));
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(lp);
        textView3.setText("Clicks");
        textView3.setGravity(Gravity.CENTER);
        row.addView(textView3, lp);


        TextView textView4 = new TextView(this);
        textView4.setTextColor(getResources().getColor(R.color.black));
        textView4.setPadding(20, 20, 20, 20);
        textView4.setLayoutParams(lp);
        textView4.setText("Cost");
        textView4.setGravity(Gravity.CENTER);
        row.addView(textView4, lp);


        TextView textView5 = new TextView(this);
        textView5.setTextColor(getResources().getColor(R.color.black));
        textView5.setPadding(20, 20, 20, 20);
        textView5.setLayoutParams(lp);
        textView5.setText("Conv.");
        textView5.setGravity(Gravity.CENTER);
        row.addView(textView5, lp);

        TextView textView6 = new TextView(this);
        textView6.setTextColor(getResources().getColor(R.color.black));
        textView6.setPadding(20, 20, 20, 20);
        textView6.setLayoutParams(lp);
        textView6.setText("Avg. CPC");
        textView6.setGravity(Gravity.CENTER);
        row.addView(textView6, lp);


        TextView textView8 = new TextView(this);
        textView8.setTextColor(getResources().getColor(R.color.black));
        textView8.setPadding(20, 20, 20, 20);
        textView8.setLayoutParams(lp);
        textView8.setText("CPA");
        textView8.setGravity(Gravity.CENTER);
        row.addView(textView8, lp);


        TextView textView9 = new TextView(this);
        textView9.setTextColor(getResources().getColor(R.color.black));
        textView9.setPadding(20, 20, 20, 20);
        textView9.setLayoutParams(lp);
        textView9.setText("Status");
        textView9.setGravity(Gravity.CENTER);
        row.addView(textView9, lp);


        ll.addView(row, 0);
    }

    private void getAdsData() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestDataAds requestDataAds = new RequestDataAds();
        requestDataAds.setAccess_token(userData.get(Session.KEY_ACCESS_TOKEN));

        requestDataAds.setpId("1");
        requestDataAds.setcId(userData.get(Session.KEY_AGENCY_CLIENT_ID));
        requestDataAds.setfDate("2017-06-02");
        requestDataAds.settDate("2017-06-08");
        requestDataAds.setLimit("2");
        requestDataAds.setOrderBy("DESC");
        requestDataAds.setSortBy("clicks");
        requestDataAds.setpId("1");


        Call<ResponseDataAds> adsCall = apiAddClientService.getAdsData(requestDataAds);
        adsCall.enqueue(new Callback<ResponseDataAds>() {
            @Override
            public void onResponse(Call<ResponseDataAds> call, Response<ResponseDataAds> response) {
                try {
                    ResponseDataAds adsData = response.body();
                    ArrayList<AdListingData> adListingData = adsData.getData();
                    if (adListingData != null && adListingData.size() > 0)
                        createAdsTable(adListingData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataAds> call, Throwable t) {

            }
        });


    }

    private void createAdsTable(ArrayList<AdListingData> adListingData) {
        for (int i = 0; i < adListingData.size(); i++) {
            TableRow row1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row1.setLayoutParams(lp);
            setAdsOtherRow(row1, lp, i, adListingData.get(i));
        }

        TableRow row1 = new TableRow(this);
        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp1.span = 1;
        row1.setLayoutParams(lp1);
        setAdsHeaderRow(row1, lp1);

    }

    private void setAdsOtherRow(TableRow row, TableRow.LayoutParams lp, int i, AdListingData adListingData) {

        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.cell_shape);
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText(adListingData.getAd());
        row.addView(textView, lp);

        TextView textView1 = new TextView(this);
        textView1.setBackgroundResource(R.drawable.cell_shape);
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setGravity(Gravity.CENTER_VERTICAL);
        textView1.setText(adListingData.getImpressions());
        row.addView(textView1, lp);

        TextView textView2 = new TextView(this);
        textView2.setBackgroundResource(R.drawable.cell_shape);
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        textView2.setGravity(Gravity.CENTER_VERTICAL);
        textView2.setText(adListingData.getCtr());
        row.addView(textView2, lp);

        TextView textView3 = new TextView(this);
        textView3.setBackgroundResource(R.drawable.cell_shape);
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(lp);
        textView3.setGravity(Gravity.CENTER_VERTICAL);
        textView3.setText(adListingData.getClicks());
        row.addView(textView3, lp);

        TextView textView4 = new TextView(this);
        textView4.setBackgroundResource(R.drawable.cell_shape);
        textView4.setPadding(20, 20, 20, 20);
        textView4.setLayoutParams(lp);
        textView4.setGravity(Gravity.CENTER_VERTICAL);
        textView4.setText(adListingData.getCost());
        row.addView(textView4, lp);


        TextView textView5 = new TextView(this);
        textView5.setBackgroundResource(R.drawable.cell_shape);
        textView5.setPadding(20, 20, 20, 20);
        textView5.setLayoutParams(lp);
        textView5.setGravity(Gravity.CENTER_VERTICAL);
        textView5.setText(adListingData.getConverted_clicks());
        row.addView(textView5, lp);

        ll.addView(row, i);

    }

    private void setAdsHeaderRow(TableRow row, TableRow.LayoutParams lp) {
        TextView textView = new TextView(this);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setText("Name");
        textView.setGravity(Gravity.CENTER);
        row.addView(textView, lp);

        TextView textView1 = new TextView(this);
        textView1.setTextColor(getResources().getColor(R.color.black));
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setText("Impressions");
        textView1.setGravity(Gravity.CENTER);
        row.addView(textView1, lp);

        TextView textView2 = new TextView(this);
        textView2.setTextColor(getResources().getColor(R.color.black));
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        textView2.setText("CTR");
        textView2.setGravity(Gravity.CENTER);
        row.addView(textView2, lp);


        TextView textView3 = new TextView(this);
        textView3.setTextColor(getResources().getColor(R.color.black));
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(lp);
        textView3.setText("Clicks");
        textView3.setGravity(Gravity.CENTER);
        row.addView(textView3, lp);


        TextView textView4 = new TextView(this);
        textView4.setTextColor(getResources().getColor(R.color.black));
        textView4.setPadding(20, 20, 20, 20);
        textView4.setLayoutParams(lp);
        textView4.setText("Cost");
        textView4.setGravity(Gravity.CENTER);
        row.addView(textView4, lp);


        TextView textView5 = new TextView(this);
        textView5.setTextColor(getResources().getColor(R.color.black));
        textView5.setPadding(20, 20, 20, 20);
        textView5.setLayoutParams(lp);
        textView5.setText("Conv.");
        textView5.setGravity(Gravity.CENTER);
        row.addView(textView5, lp);


        ll.addView(row, 0);
    }

    private void getCampaignData() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestDataCampaignDetails requestDataCampaignDetails = new RequestDataCampaignDetails();
        requestDataCampaignDetails.setAccess_token(userData.get(Session.KEY_ACCESS_TOKEN));
        requestDataCampaignDetails.setpId("1");
        requestDataCampaignDetails.setcId(userData.get(Session.KEY_AGENCY_CLIENT_ID));
        requestDataCampaignDetails.setfDate("2017-06-02");
        requestDataCampaignDetails.settDate("2017-06-08");
        requestDataCampaignDetails.setLimit("10");
        requestDataCampaignDetails.setOrderBy("DESC");
        requestDataCampaignDetails.setSortBy("clicks");

        Call<ResponseDataCampaignDetails> campaignCall = apiAddClientService.getCampaignData(requestDataCampaignDetails);
        campaignCall.enqueue(new Callback<ResponseDataCampaignDetails>() {
            @Override
            public void onResponse(Call<ResponseDataCampaignDetails> call, Response<ResponseDataCampaignDetails> response) {
                if (response.isSuccessful()) {
                    ResponseDataCampaignDetails campaignData = response.body();
                    try {
                        ArrayList<CampaignData> data = campaignData.getData();
                        if (data != null && data.size() > 0)
                            createDataTable(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseDataCampaignDetails> call, Throwable t) {
                if (t != null) {
                    Log.d("TAG", t.getMessage());
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.campaign_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_filter:
                displayFilterLayout();
                break;
        }
        return true;
    }


    private void setOtherRow(TableRow row, TableRow.LayoutParams lp, int i, CampaignData campaignData) {
        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.cell_shape);
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setText(campaignData.getCampaign());
        row.addView(textView, lp);

        TextView textView1 = new TextView(this);
        textView1.setBackgroundResource(R.drawable.cell_shape);
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setText(campaignData.getImpressions());
        textView1.setGravity(Gravity.CENTER);
        row.addView(textView1, lp);

        TextView textView2 = new TextView(this);
        textView2.setBackgroundResource(R.drawable.cell_shape);
        textView2.setText(campaignData.getCtr());
        textView2.setPadding(20, 20, 20, 20);
        textView2.setGravity(Gravity.CENTER);
        textView2.setLayoutParams(lp);
        row.addView(textView2, lp);

        TextView textView3 = new TextView(this);
        textView3.setText(campaignData.getClicks());
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(lp);
        textView3.setGravity(Gravity.CENTER);
        textView3.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView3, lp);


        ll.addView(row, i);
    }

    private void setFirstRow(TableRow row, TableRow.LayoutParams lp) {
        TextView textView = new TextView(this);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setText("Name");
        textView.setGravity(Gravity.CENTER);
        row.addView(textView, lp);

        TextView textView1 = new TextView(this);
        textView1.setTextColor(getResources().getColor(R.color.black));
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setText("Impressions");
        textView1.setGravity(Gravity.CENTER);
        row.addView(textView1, lp);

        TextView textView2 = new TextView(this);
        textView2.setTextColor(getResources().getColor(R.color.black));
        textView2.setText("CTR");
        textView2.setGravity(Gravity.CENTER);
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        row.addView(textView2, lp);

        TextView textView3 = new TextView(this);
        textView3.setText("Clicks");
        textView3.setPadding(20, 20, 20, 20);
        textView3.setGravity(Gravity.CENTER);
        textView3.setLayoutParams(lp);
        textView3.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView3, lp);


        ll.addView(row, 0);
    }

    public void displayFilterLayout() {
        filterPopup.showAtLocation(findViewById(R.id.menu_filter), Gravity.RIGHT | Gravity.TOP, 20, getActionBarHeight());
    }

    private int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();

        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llDateLayout:
                customDatePopup.showAsDropDown(llDateLayout, 0, 10);
                break;
        }
    }


    private void createDataTable(ArrayList<CampaignData> campaignDatas) {
        for (int i = 0; i < campaignDatas.size(); i++) {
            TableRow row1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row1.setLayoutParams(lp);
            setOtherRow(row1, lp, i, campaignDatas.get(i));
        }

        TableRow row1 = new TableRow(this);
        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp1.span = 1;
        row1.setLayoutParams(lp1);
        setFirstRow(row1, lp1);
    }


}
