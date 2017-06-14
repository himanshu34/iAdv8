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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
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
import com.agl.product.adw8_new.model.Adgroup;
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
    private LinearLayout llDateLayout,llDataContainer;
    private TableLayout tlName, tlValues;
    private HorizontalScrollView hrone, hrsecond,hrbottom;
    Session session;
    HashMap<String, String> userData;
    private int offset = 0,limit = 50;
    private SwipeRefreshLayoutBottom swipeRefreshLayout;
    private int rowCount;
    private TextView textYesterday,textLastSevenDays,textLastThirtyDays,textCustom,textSelectedDateRange,textMessage;
    private ProgressBar progressBar;
    private ArrayList<Keywords> keywordsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keywords);
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
        llDataContainer = (LinearLayout) findViewById(R.id.llDataContainer);
        tlValues = (TableLayout) findViewById(R.id.tlValues);
        hrone = (HorizontalScrollView) findViewById(R.id.hrone);
        hrsecond = (HorizontalScrollView) findViewById(R.id.hrsecond);
        hrbottom = (HorizontalScrollView) findViewById(R.id.hrbottom);

        tlName = (TableLayout) findViewById(R.id.tlName);
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
        hrsecond.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                hrone.scrollTo(hrsecond.getScrollX(), hrsecond.getScrollY());
                hrbottom.scrollTo(hrsecond.getScrollX(), hrsecond.getScrollY());
            }
        });

        getKeywordsData();

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

    public void displayFilterLayout() {
        filterPopup.showAtLocation(findViewById(R.id.menu_filter), Gravity.RIGHT | Gravity.TOP, 20, getActionBarHeight());
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

    private int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();

        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }


    private void getKeywordsData() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestDataKeywords requestKeywords = new RequestDataKeywords();
        requestKeywords.setAccess_token(userData.get(Session.KEY_ACCESS_TOKEN));

        requestKeywords.setpId("1");
        requestKeywords.setcId(userData.get(Session.KEY_AGENCY_CLIENT_ID));
        requestKeywords.setfDate("2017-06-02");
        requestKeywords.settDate("2017-06-08");
        requestKeywords.setLimit(limit+"");
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
                                    llDataContainer.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    textMessage.setVisibility(View.GONE);
                                    keywordsList.addAll(keywords);
                                    createKeywordsTable(keywords);
                                    offset = offset + limit;
                                }else {
                                    if( offset == 0 ){
                                        llDataContainer.setVisibility(View.INVISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        textMessage.setVisibility(View.VISIBLE);
                                        textMessage.setText("No Keywords Found.");
                                    }
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            if( offset == 0 ){
                                llDataContainer.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.VISIBLE);
                                textMessage.setText("Some error occured.");
                            }else {
                                Toast.makeText(KeywordsActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        if( offset == 0 ){
                            llDataContainer.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.GONE);
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Some error occured.");
                        }else Toast.makeText(KeywordsActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    if( offset == 0 ){
                        llDataContainer.setVisibility(View.INVISIBLE);
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
                    llDataContainer.setVisibility(View.INVISIBLE);
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
        for (int i = 0; i < keywordsData.size(); i++) {
            TableRow row1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row1.setLayoutParams(lp);
            setKeywordsOtherRow(row1, lp, rowCount, keywordsData.get(i));
        }
    }

    private void setKeywordsOtherRow(TableRow row, TableRow.LayoutParams lp, int i, Keywords keyword) {

        setKeywordName( i,keyword);

        LinearLayout.LayoutParams  layoutParams = new LinearLayout.LayoutParams( 20,LinearLayout.LayoutParams.WRAP_CONTENT);

        View view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );

        TextView textView2 = new TextView(this);
        textView2.setBackgroundResource(R.drawable.cell_shape);
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(layoutParams);
        textView2.setGravity(Gravity.CENTER_VERTICAL);

        textView2 = (TextView) view.findViewById(R.id.text_view);
        textView2.setText(keyword.getClicks());
        row.addView(textView2);


        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView1 = (TextView) view.findViewById(R.id.text_view);
        textView1.setText(keyword.getImpressions());
        row.addView(textView1);


        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView5 = (TextView) view.findViewById(R.id.text_view);
        textView5.setText(keyword.getAvg_cpc());
        row.addView(textView5);


        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView3 = (TextView) view.findViewById(R.id.text_view);
        textView3.setText(keyword.getCost());
        row.addView(textView3);

        //CTR
        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView10 = (TextView) view.findViewById(R.id.text_view);
        textView10.setText(keyword.getCtr());
        row.addView(textView10);

        // Avg Pos

        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView8 = (TextView) view.findViewById(R.id.text_view);
        textView8.setText(keyword.getAvg_position());
        row.addView(textView8);


        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView4 = (TextView) view.findViewById(R.id.text_view);
        textView4.setText(keyword.getConverted_clicks());
        row.addView(textView4);



        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView6 = (TextView) view.findViewById(R.id.text_view);
        textView6.setText(keyword.getCpa());
        row.addView(textView6);


        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView7 = (TextView) view.findViewById(R.id.text_view);
        textView7.setText(keyword.getKeyword_state());
        row.addView(textView7);

        tlValues.addView(row, i);
        rowCount++;
    }

    private void setKeywordName(int i, Keywords data) {
        TableRow row = new TableRow(this);
        View v = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        TextView tv = (TextView)v.findViewById(R.id.text_view);
        tv.setText(data.getKeyword_name());
        tlName.addView(v, i);

    }
}
