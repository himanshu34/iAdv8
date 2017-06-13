package com.agl.product.adw8_new.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.custom_view.SwipeRefreshLayoutBottom;
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
import com.agl.product.adw8_new.utils.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CampaignActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayoutBottom.OnRefreshListener {

    private TableLayout ll;
    private PopupWindow filterPopup, customDatePopup;
    private View filterLayout, customPopupLayout;
    private LinearLayout llDateLayout,llContainer;
    Session session;
    HashMap<String, String> userData;
    private String campaignType;
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

        Intent intent = getIntent();
        if (intent != null)
            campaignType = intent.getStringExtra("type");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setTitle(campaignType.toUpperCase());

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
        requestCampaign();
    }

    private void requestCampaign() {
        switch (campaignType) {
            case "ads":
                setAdsHeaderRow();
                getAdsData();
                break;
            case "keywords":
                setKeywordsHeaderRow();
                getKeywordsData();
                break;
            case "campaign":
                createCampaignHeaderRow();
                getCampaignData();
                break;
            case "adgroup":
                setAdgroupHeaderRow();
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
        requestKeywords.setOffset(offset);


        Call<ResponseDataAdgroup> adsCall = apiAddClientService.getAdgroupList(requestKeywords);
        adsCall.enqueue(new Callback<ResponseDataAdgroup>() {
            @Override
            public void onResponse(Call<ResponseDataAdgroup> call, Response<ResponseDataAdgroup> response) {
                    swipeRefreshLayout.setRefreshing(false);
                if ( response != null ){
                    if( response.isSuccessful() ){

                        try {
                            ResponseDataAdgroup body = response.body();
                            ArrayList<Adgroup> data = body.getData();
                            if( data != null ){
                                if ( data.size() > 0 ){
                                    llContainer.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    textMessage.setVisibility(View.GONE);
                                    createAdgroupTable(data);
                                    offset = offset + 20 ;

                                }else {
                                    if( offset == 0 ){
                                        llContainer.setVisibility(View.INVISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        textMessage.setVisibility(View.VISIBLE);
                                        textMessage.setText("No Adgroups Found.");
                                    }
                                }
                            }else {
                                if( offset == 0 ){
                                    llContainer.setVisibility(View.INVISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    textMessage.setVisibility(View.VISIBLE);
                                    textMessage.setText("Some error occured.");
                                }else
                                    Toast.makeText(CampaignActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();

                            }

                        }catch (Exception e ){
                            e.printStackTrace();
                            if( offset == 0 ){
                                llContainer.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.VISIBLE);
                                textMessage.setText("Some error occured.");
                            }else
                                Toast.makeText(CampaignActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if( offset == 0 ){
                            llContainer.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.GONE);
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Some error occured.");
                        }else
                            Toast.makeText(CampaignActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();

                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseDataAdgroup> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if ( t != null ) t.printStackTrace();
                if( offset == 0 ){
                    llContainer.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText("There is some connectivity issue, please try again.");
                }else
                    Toast.makeText(CampaignActivity.this, "There is some connectivity issue, please try again.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void createAdgroupTable(ArrayList<Adgroup> data) {
        for (int i = 0; i < data.size(); i++) {
            TableRow row1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row1.setLayoutParams(lp);
            setAdgroupOtherRow(row1, lp, ++rowCount, data.get(i));
        }

    }

    private void setAdgroupHeaderRow() {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp.span = 1;
        row.setLayoutParams(lp);

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


        TextView textView6 = new TextView(this);
        textView6.setTextColor(getResources().getColor(R.color.black));
        textView6.setPadding(20, 20, 20, 20);
        textView6.setLayoutParams(lp);
        textView6.setText("Avg. CPC");
        textView6.setGravity(Gravity.CENTER);
        row.addView(textView6, lp);


        TextView textView7 = new TextView(this);
        textView7.setTextColor(getResources().getColor(R.color.black));
        textView7.setPadding(20, 20, 20, 20);
        textView7.setLayoutParams(lp);
        textView7.setText("Avg. POS");
        textView7.setGravity(Gravity.CENTER);
        row.addView(textView7, lp);

        TextView textView8 = new TextView(this);
        textView8.setTextColor(getResources().getColor(R.color.black));
        textView8.setPadding(20, 20, 20, 20);
        textView8.setLayoutParams(lp);
        textView8.setText("Cost/Conv.");
        textView8.setGravity(Gravity.CENTER);
        row.addView(textView8, lp);


        TextView textView9 = new TextView(this);
        textView9.setTextColor(getResources().getColor(R.color.black));
        textView9.setPadding(20, 20, 20, 20);
        textView9.setLayoutParams(lp);
        textView9.setText("Conv. Rate");
        textView9.setGravity(Gravity.CENTER);
        row.addView(textView9, lp);


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


        TextView textView6 = new TextView(this);
        textView6.setBackgroundResource(R.drawable.cell_shape);
        textView6.setPadding(20, 20, 20, 20);
        textView6.setLayoutParams(lp);
        textView6.setGravity(Gravity.CENTER_VERTICAL);
        textView6.setText("");
        row.addView(textView6, lp);


        TextView textView7 = new TextView(this);
        textView7.setBackgroundResource(R.drawable.cell_shape);
        textView7.setPadding(20, 20, 20, 20);
        textView7.setLayoutParams(lp);
        textView7.setGravity(Gravity.CENTER_VERTICAL);
        textView7.setText("");
        row.addView(textView7, lp);


        TextView textView8 = new TextView(this);
        textView8.setBackgroundResource(R.drawable.cell_shape);
        textView8.setPadding(20, 20, 20, 20);
        textView8.setLayoutParams(lp);
        textView8.setGravity(Gravity.CENTER_VERTICAL);
        textView8.setText("");
        row.addView(textView8, lp);



        TextView textView9 = new TextView(this);
        textView9.setBackgroundResource(R.drawable.cell_shape);
        textView9.setPadding(20, 20, 20, 20);
        textView9.setLayoutParams(lp);
        textView9.setGravity(Gravity.CENTER_VERTICAL);
        textView9.setText("");
        row.addView(textView9, lp);

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
                                Toast.makeText(CampaignActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        if( offset == 0 ){
                            llContainer.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.GONE);
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Some error occured.");
                        }else Toast.makeText(CampaignActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    if( offset == 0 ){
                        llContainer.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        textMessage.setText("Some error occured.");
                    }else {
                        Toast.makeText(CampaignActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CampaignActivity.this, "There is some connectivity issue, please try again.", Toast.LENGTH_SHORT).show();
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
        setKeywordsTotalRows();
    }

    private void removeKeywordsTotalRows() {
        ll.removeViewAt(ll.getChildCount()-1);
    }

    private void setKeywordsTotalRows() {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp.span = 1;
        row.setLayoutParams(lp);

        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.cell_shape);
        LinearLayout.LayoutParams  layoutParams = new LinearLayout.LayoutParams( 20,LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText("Total");
        row.addView(textView, lp);

        TextView textView1 = new TextView(this);
        textView1.setBackgroundResource(R.drawable.cell_shape);
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(layoutParams);
        textView1.setGravity(Gravity.CENTER_VERTICAL);
        textView1.setText("");
        row.addView(textView1, lp);


        TextView textView2 = new TextView(this);
        textView2.setBackgroundResource(R.drawable.cell_shape);
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(layoutParams);
        textView2.setGravity(Gravity.CENTER_VERTICAL);
        textView2.setText("");
        row.addView(textView2, lp);


        TextView textView3 = new TextView(this);
        textView3.setBackgroundResource(R.drawable.cell_shape);
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(layoutParams);
        textView3.setGravity(Gravity.CENTER_VERTICAL);
        textView3.setText("");
        row.addView(textView3, lp);

        TextView textView4 = new TextView(this);
        textView4.setBackgroundResource(R.drawable.cell_shape);
        textView4.setPadding(20, 20, 20, 20);
        textView4.setLayoutParams(layoutParams);
        textView4.setGravity(Gravity.CENTER_VERTICAL);
        textView4.setText("");
        row.addView(textView4, lp);


        TextView textView5 = new TextView(this);
        textView5.setBackgroundResource(R.drawable.cell_shape);
        textView5.setPadding(20, 20, 20, 20);
        textView5.setLayoutParams(layoutParams);
        textView5.setGravity(Gravity.CENTER_VERTICAL);
        textView5.setText("");
        row.addView(textView5, lp);


        TextView textView6 = new TextView(this);
        textView6.setBackgroundResource(R.drawable.cell_shape);
        textView6.setPadding(20, 20, 20, 20);
        textView6.setLayoutParams(layoutParams);
        textView6.setGravity(Gravity.CENTER_VERTICAL);
        textView6.setText("");
        row.addView(textView6, lp);


        TextView textView7 = new TextView(this);
        textView7.setBackgroundResource(R.drawable.cell_shape);
        textView7.setPadding(20, 20, 20, 20);
        textView7.setLayoutParams(layoutParams);
        textView7.setGravity(Gravity.CENTER_VERTICAL);
        textView7.setText("");
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


        ll.addView(row, ll.getChildCount());
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

    private void setKeywordsHeaderRow() {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp.span = 1;
        row.setLayoutParams(lp);

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

        TextView textView10 = new TextView(this);
        textView10.setTextColor(getResources().getColor(R.color.black));
        textView10.setPadding(20, 20, 20, 20);
        textView10.setLayoutParams(lp);
        textView10.setText("CTR");
        textView10.setGravity(Gravity.CENTER);
        row.addView(textView10, lp);


        TextView textView11 = new TextView(this);
        textView11.setTextColor(getResources().getColor(R.color.black));
        textView11.setPadding(20, 20, 20, 20);
        textView11.setLayoutParams(lp);
        textView11.setText("Avg. Pos");
        textView11.setGravity(Gravity.CENTER);
        row.addView(textView11, lp);

        TextView textView12 = new TextView(this);
        textView12.setTextColor(getResources().getColor(R.color.black));
        textView12.setPadding(20, 20, 20, 20);
        textView12.setLayoutParams(lp);
        textView12.setText("Cost/Conv.");
        textView12.setGravity(Gravity.CENTER);
        row.addView(textView12, lp);


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
        requestDataAds.setOffset(offset);


        Call<ResponseDataAds> adsCall = apiAddClientService.getAdsData(requestDataAds);
        adsCall.enqueue(new Callback<ResponseDataAds>() {
            @Override
            public void onResponse(Call<ResponseDataAds> call, Response<ResponseDataAds> response) {
                swipeRefreshLayout.setRefreshing(false);
                if( response != null ){
                    if( response.isSuccessful() ){
                        try {
                            ResponseDataAds adsData = response.body();
                            ArrayList<AdListingData> adListingData = adsData.getData();
                            if( adListingData != null ){
                                if ( adListingData.size() > 0 ){
                                    llContainer.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    textMessage.setVisibility(View.GONE);
                                    createAdsTable(adListingData);
                                    offset = offset + 20 ;
                                }else {
                                    if( offset == 0 ){
                                        llContainer.setVisibility(View.INVISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        textMessage.setVisibility(View.VISIBLE);
                                        textMessage.setText("No Ads Found.");
                                    }
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            llContainer.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.GONE);
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Some error occured.");
                        }
                    }else {
                        llContainer.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        textMessage.setText("Some error occured.");
                    }
                }else {
                    llContainer.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText("Some error occured.");
                }


            }

            @Override
            public void onFailure(Call<ResponseDataAds> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if( t != null ) Log.d("TAG",t.getMessage()+"");
                llContainer.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText("There is some connectivity issue, please try again.");
            }
        });


    }

    private void createAdsTable(ArrayList<AdListingData> adListingData) {
        for (int i = 0; i < adListingData.size(); i++) {
            TableRow row1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row1.setLayoutParams(lp);
            setAdsOtherRow(row1, lp, ++rowCount, adListingData.get(i));
        }
    }

    private void setAdsOtherRow(TableRow row, TableRow.LayoutParams lp, int i, AdListingData adListingData) {

        TextView textView = new TextView(this);
        ViewGroup.LayoutParams  layoutParams = new ViewGroup.LayoutParams( 200,ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setBackgroundResource(R.drawable.cell_shape);
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(layoutParams);
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


        TextView textView6 = new TextView(this);
        textView6.setBackgroundResource(R.drawable.cell_shape);
        textView6.setPadding(20, 20, 20, 20);
        textView6.setLayoutParams(lp);
        textView6.setGravity(Gravity.CENTER_VERTICAL);
        textView6.setText("");
        row.addView(textView6, lp);

        TextView textView7 = new TextView(this);
        textView7.setBackgroundResource(R.drawable.cell_shape);
        textView7.setPadding(20, 20, 20, 20);
        textView7.setLayoutParams(lp);
        textView7.setGravity(Gravity.CENTER_VERTICAL);
        textView7.setText("");
        row.addView(textView7, lp);



        TextView textView8 = new TextView(this);
        textView8.setBackgroundResource(R.drawable.cell_shape);
        textView8.setPadding(20, 20, 20, 20);
        textView8.setLayoutParams(lp);
        textView8.setGravity(Gravity.CENTER_VERTICAL);
        textView8.setText("");
        row.addView(textView8, lp);


        TextView textView9 = new TextView(this);
        textView9.setBackgroundResource(R.drawable.cell_shape);
        textView9.setPadding(20, 20, 20, 20);
        textView9.setLayoutParams(lp);
        textView9.setGravity(Gravity.CENTER_VERTICAL);
        textView9.setText("");
        row.addView(textView9, lp);



        ll.addView(row, i);

    }

    private void setAdsHeaderRow() {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp.span = 1;
        row.setLayoutParams(lp);

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

        TextView textView6 = new TextView(this);
        textView6.setTextColor(getResources().getColor(R.color.black));
        textView6.setPadding(20, 20, 20, 20);
        textView6.setLayoutParams(lp);
        textView6.setText("Avg. CPC");
        textView6.setGravity(Gravity.CENTER);
        row.addView(textView6, lp);

        TextView textView7 = new TextView(this);
        textView7.setTextColor(getResources().getColor(R.color.black));
        textView7.setPadding(20, 20, 20, 20);
        textView7.setLayoutParams(lp);
        textView7.setText("Avg. Pos");
        textView7.setGravity(Gravity.CENTER);
        row.addView(textView7, lp);

        TextView textView8 = new TextView(this);
        textView8.setTextColor(getResources().getColor(R.color.black));
        textView8.setPadding(20, 20, 20, 20);
        textView8.setLayoutParams(lp);
        textView8.setText("Cost/Conv");
        textView8.setGravity(Gravity.CENTER);
        row.addView(textView8, lp);

        TextView textView9 = new TextView(this);
        textView9.setTextColor(getResources().getColor(R.color.black));
        textView9.setPadding(20, 20, 20, 20);
        textView9.setLayoutParams(lp);
        textView9.setText("Conv. Rate");
        textView9.setGravity(Gravity.CENTER);
        row.addView(textView9, lp);


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
        requestDataCampaignDetails.setLimit("20");
        requestDataCampaignDetails.setOrderBy("DESC");
        requestDataCampaignDetails.setSortBy("clicks");
        requestDataCampaignDetails.setOffset(offset);

        Call<ResponseDataCampaignDetails> campaignCall = apiAddClientService.getCampaignData(requestDataCampaignDetails);
        campaignCall.enqueue(new Callback<ResponseDataCampaignDetails>() {
            @Override
            public void onResponse(Call<ResponseDataCampaignDetails> call, Response<ResponseDataCampaignDetails> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    ResponseDataCampaignDetails campaignData = response.body();

                    try {
                        ArrayList<CampaignData> data = campaignData.getData();
                        if( data != null ){
                            if( data.size() > 0 ){
                                llContainer.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.GONE);
                                createCampaignTable(data);
                                offset = offset + 20;
                            }else {
                                if( offset == 0 ){
                                    llContainer.setVisibility(View.INVISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    textMessage.setVisibility(View.VISIBLE);
                                    textMessage.setText("No Keywords Found.");
                                }
                            }
                        }else {
                            if( offset == 0 ){
                                llContainer.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.VISIBLE);
                                textMessage.setText("Some error occured");
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        if( offset == 0 ){
                            llContainer.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.GONE);
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Some error occured");
                        }
                    }
                } else {
                    if( offset == 0 ){
                        llContainer.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        textMessage.setText("Some error occured");
                    }else Toast.makeText(CampaignActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseDataCampaignDetails> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if (t != null) {
                    Log.d("TAG", t.getMessage());
                }
                if( offset == 0 ){
                    llContainer.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText("There is some connectivity issue.");
                }else Toast.makeText(CampaignActivity.this, "There is some connectivity issue.", Toast.LENGTH_SHORT).show();

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


        TextView textView4 = new TextView(this);
        textView4.setText("");
        textView4.setPadding(20, 20, 20, 20);
        textView4.setLayoutParams(lp);
        textView4.setGravity(Gravity.CENTER);
        textView4.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView4, lp);

        TextView textView5 = new TextView(this);
        textView5.setText("");
        textView5.setPadding(20, 20, 20, 20);
        textView5.setLayoutParams(lp);
        textView5.setGravity(Gravity.CENTER);
        textView5.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView5, lp);

        TextView textView6 = new TextView(this);
        textView6.setText("");
        textView6.setPadding(20, 20, 20, 20);
        textView6.setLayoutParams(lp);
        textView6.setGravity(Gravity.CENTER);
        textView6.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView6, lp);

        TextView textView7 = new TextView(this);
        textView7.setText("");
        textView7.setPadding(20, 20, 20, 20);
        textView7.setLayoutParams(lp);
        textView7.setGravity(Gravity.CENTER);
        textView7.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView7, lp);

        TextView textView8 = new TextView(this);
        textView8.setText("");
        textView8.setPadding(20, 20, 20, 20);
        textView8.setLayoutParams(lp);
        textView8.setGravity(Gravity.CENTER);
        textView8.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView8, lp);

        TextView textView9 = new TextView(this);
        textView9.setText("");
        textView9.setPadding(20, 20, 20, 20);
        textView9.setLayoutParams(lp);
        textView9.setGravity(Gravity.CENTER);
        textView9.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView9, lp);

        ll.addView(row, i);
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


    private void createCampaignHeaderRow(){
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp.span = 1;
        row.setLayoutParams(lp);

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


        TextView textView4 = new TextView(this);
        textView4.setText("Budget");
        textView4.setPadding(20, 20, 20, 20);
        textView4.setGravity(Gravity.CENTER);
        textView4.setLayoutParams(lp);
        textView4.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView4, lp);


        TextView textView5 = new TextView(this);
        textView5.setText("Avg. CPC");
        textView5.setPadding(20, 20, 20, 20);
        textView5.setGravity(Gravity.CENTER);
        textView5.setLayoutParams(lp);
        textView5.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView5, lp);


        TextView textView6 = new TextView(this);
        textView6.setText("Cost");
        textView6.setPadding(20, 20, 20, 20);
        textView6.setGravity(Gravity.CENTER);
        textView6.setLayoutParams(lp);
        textView6.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView6, lp);


        TextView textView7 = new TextView(this);
        textView7.setText("Avg. Pos");
        textView7.setPadding(20, 20, 20, 20);
        textView7.setGravity(Gravity.CENTER);
        textView7.setLayoutParams(lp);
        textView7.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView7, lp);

        TextView textView8 = new TextView(this);
        textView8.setText("Conv.");
        textView8.setPadding(20, 20, 20, 20);
        textView8.setGravity(Gravity.CENTER);
        textView8.setLayoutParams(lp);
        textView8.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView8, lp);


        TextView textView9 = new TextView(this);
        textView9.setText("Cost/Conv.");
        textView9.setPadding(20, 20, 20, 20);
        textView9.setGravity(Gravity.CENTER);
        textView9.setLayoutParams(lp);
        textView9.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView9, lp);



        ll.addView(row, 0);
    }

    private void createCampaignTable(ArrayList<CampaignData> campaignDatas) {
        for (int i = 0; i < campaignDatas.size(); i++) {
            TableRow row1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row1.setLayoutParams(lp);
            setOtherRow(row1, lp, ++rowCount, campaignDatas.get(i));
        }
    }

    @Override
    public void onRefresh() {
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
}
