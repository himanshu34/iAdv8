package com.agl.product.adw8_new.activity;

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
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.custom_view.SwipeRefreshLayoutBottom;
import com.agl.product.adw8_new.model.AdListingData;
import com.agl.product.adw8_new.model.Keywords;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Post;
import com.agl.product.adw8_new.service.data.RequestDataAds;
import com.agl.product.adw8_new.service.data.ResponseDataAds;
import com.agl.product.adw8_new.utils.ConnectionDetector;
import com.agl.product.adw8_new.utils.Session;
import com.agl.product.adw8_new.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdsActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayoutBottom.OnRefreshListener {

    private PopupWindow filterPopup, customDatePopup;
    private View filterLayout, customPopupLayout;
    private LinearLayout llDateLayout,llDataContainer;
    private TableLayout tlName, tlValues;
    private HorizontalScrollView hrone, hrsecond,hrbottom;
    Session session;
    HashMap<String, String> userData;
    private int offset = 0,limit = 50 ;
    private SwipeRefreshLayoutBottom swipeRefreshLayout;
    private int rowCount;
    private TextView textYesterday,textLastSevenDays,textLastThirtyDays,textCustom,textSelectedDateRange,textMessage;
    private ProgressBar progressBar;
    private ArrayList<Keywords> keywordsList;
    private String fromDate,toDate;
    private ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        session = new Session(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        keywordsList = new ArrayList<Keywords>();
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

        cd = new ConnectionDetector(this );

        userData = session.getUsuarioDetails();
        hrsecond.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                hrone.scrollTo(hrsecond.getScrollX(), hrsecond.getScrollY());
                hrbottom.scrollTo(hrsecond.getScrollX(), hrsecond.getScrollY());
            }
        });

        fromDate = Utils.getSevenDayBeforeDate();
        toDate = Utils.getCurrentDate();
        textSelectedDateRange.setText(Utils.getDisplaySevenDayBeforeDate()+" - "+Utils.getDisplayCurrentDate());
        getAdsData();
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

    private void getAdsData() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestDataAds requestDataAds = new RequestDataAds();
        requestDataAds.setAccess_token(userData.get(Session.KEY_ACCESS_TOKEN));

        requestDataAds.setpId("1");
        requestDataAds.setcId(userData.get(Session.KEY_AGENCY_CLIENT_ID));
        requestDataAds.setfDate(fromDate);
        requestDataAds.settDate(toDate);
        requestDataAds.setLimit(limit+"");
        requestDataAds.setOrderBy("DESC");
        requestDataAds.setSortBy("clicks");
        requestDataAds.setpId("1");
        requestDataAds.setOffset(offset);

        if( offset == 0 ){
            // Show loading on first time
            llDataContainer.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            textMessage.setVisibility(View.GONE);
        }

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
                                    llDataContainer.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    textMessage.setVisibility(View.GONE);
                                    createAdsTable(adListingData);
                                    offset = offset + limit ;
                                }else {
                                    if( offset == 0 ){
                                        llDataContainer.setVisibility(View.INVISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        textMessage.setVisibility(View.VISIBLE);
                                        textMessage.setText("No Ads Found.");
                                    }
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            llDataContainer.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.GONE);
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Some error occured.");
                        }
                    }else {
                        llDataContainer.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        textMessage.setText("Some error occured.");
                    }
                }else {
                    llDataContainer.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText("Some error occured.");
                }

            }

            @Override
            public void onFailure(Call<ResponseDataAds> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if( t != null ) Log.d("TAG",t.getMessage()+"");
                llDataContainer.setVisibility(View.INVISIBLE);
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
            setAdsOtherRow(row1, lp, rowCount, adListingData.get(i));
        }
    }

    private void setAdsOtherRow(TableRow row, TableRow.LayoutParams lp, int i, AdListingData adListingData) {

        setAdsName(i,adListingData);

        View view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );


        TextView textView3 = (TextView) view.findViewById(R.id.text_view);
        textView3.setText(adListingData.getClicks());
        row.addView(textView3);


        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView1 = (TextView) view.findViewById(R.id.text_view);
        textView1.setText(adListingData.getImpressions());
        row.addView(textView1);

        // Avg CPC

        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView6 = (TextView) view.findViewById(R.id.text_view);
        textView6.setText(adListingData.getAvg_cpc());
        row.addView(textView6);



        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView4 = (TextView) view.findViewById(R.id.text_view);
        textView4.setText(adListingData.getCost());
        row.addView(textView4);



        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView2 = (TextView) view.findViewById(R.id.text_view);
        textView2.setText(adListingData.getCtr());
        row.addView(textView2);


        // Avg Pos
        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView7 = (TextView) view.findViewById(R.id.text_view);
        textView7.setText(adListingData.getAvg_position());
        row.addView(textView7);

        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView5 = (TextView) view.findViewById(R.id.text_view);
        textView5.setText(adListingData.getConverted_clicks());
        row.addView(textView5);

        // Cost / conv

        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView8 = (TextView) view.findViewById(R.id.text_view);
        textView8.setText(adListingData.getCpa());
        row.addView(textView8);

        // Conv rate

        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView9 = (TextView) view.findViewById(R.id.text_view);
        textView9.setText(adListingData.getConversion_rate());
        row.addView(textView9);

        tlValues.addView(row, i);
        rowCount++;

    }

    private void setAdsName(int i, AdListingData data) {
        TableRow row = new TableRow(this);
        View v = LayoutInflater.from(this).inflate(R.layout.first_row, row, false);
        TextView tv = (TextView)v.findViewById(R.id.text_view);
        tv.setText(data.getAd());
        tlName.addView(v, i);
    }

    private void setYesterday() {
        if( !cd.isConnectedToInternet() ) return;
        textYesterday.setTextColor(getResources().getColor(R.color.colorPrimary));
        textLastSevenDays.setTextColor(getResources().getColor(R.color.black));
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.black));
        fromDate = Utils.getYesterdayDate();
        toDate = Utils.getYesterdayDate();
        textSelectedDateRange.setText(Utils.getDisplayYesterdayDate());
        customDatePopup.dismiss();
        offset = 0;
        rowCount = 0;
        getAdsData();
    }

    private void setLastSeven() {
        if( !cd.isConnectedToInternet() ) return;
        textLastSevenDays.setTextColor(getResources().getColor(R.color.colorPrimary));
        textYesterday.setTextColor(getResources().getColor(R.color.black));
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.black));
        fromDate = Utils.getSevenDayBeforeDate();
        toDate = Utils.getCurrentDate();
        textSelectedDateRange.setText(Utils.getDisplaySevenDayBeforeDate()+" - "+Utils.getDisplayCurrentDate());
        customDatePopup.dismiss();
        offset = 0;
        rowCount = 0;
        getAdsData();
    }

    private void setLastThirty() {
        if( !cd.isConnectedToInternet() ) return;
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.colorPrimary));
        textLastSevenDays.setTextColor(getResources().getColor(R.color.black));
        textYesterday.setTextColor(getResources().getColor(R.color.black));
        fromDate = Utils.getThirtyDayBeforeDate();
        toDate = Utils.getCurrentDate();
        textSelectedDateRange.setText(Utils.getDisplayThirtyDayBeforeDate()+" - "+Utils.getDisplayCurrentDate());
        customDatePopup.dismiss();
        offset = 0;
        rowCount = 0;
        getAdsData();
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
    public void onRefresh() {
        getAdsData();
    }
}
