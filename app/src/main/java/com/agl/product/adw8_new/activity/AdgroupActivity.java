package com.agl.product.adw8_new.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.Toast;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.custom_view.SwipeRefreshLayoutBottom;
import com.agl.product.adw8_new.model.Adgroup;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Post;
import com.agl.product.adw8_new.service.data.RequestDataAdgroup;
import com.agl.product.adw8_new.service.data.ResponseDataAdgroup;
import com.agl.product.adw8_new.utils.Session;
import com.agl.product.adw8_new.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdgroupActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayoutBottom.OnRefreshListener {

    private TableLayout tlName, tlValues;
    private HorizontalScrollView hrone, hrsecond,hrbottom;
    private ProgressBar progressBar;
    private LinearLayout llDateLayout,llDataContainer;

    private PopupWindow filterPopup, customDatePopup;
    private View filterLayout, customPopupLayout;
    private TextView textYesterday, textLastSevenDays, textLastThirtyDays, textCustom, textSelectedDateRange, textMessage,text;
    Session session;
    private SwipeRefreshLayoutBottom swipeRefreshLayout;
    HashMap<String, String> userData;
    private int rowCount;
    private int offset = 0,limit = 50 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adgroup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        session = new Session(this);
        userData = session.getUsuarioDetails();

        llDateLayout = (LinearLayout) findViewById(R.id.llDateLayout);
        llDataContainer = (LinearLayout) findViewById(R.id.llDataContainer);
        tlValues = (TableLayout) findViewById(R.id.tlValues);
        textMessage = (TextView) findViewById(R.id.textMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textSelectedDateRange = (TextView) findViewById(R.id.textSelectedDateRange);
        swipeRefreshLayout = (SwipeRefreshLayoutBottom) findViewById(R.id.swipeRefresh);
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


        hrone = (HorizontalScrollView) findViewById(R.id.hrone);
        hrsecond = (HorizontalScrollView) findViewById(R.id.hrsecond);
        hrbottom = (HorizontalScrollView) findViewById(R.id.hrbottom);

        tlValues = (TableLayout) findViewById(R.id.tlValues);
        tlName = (TableLayout) findViewById(R.id.tlName);
        text = (TextView) findViewById(R.id.text);

        textYesterday = (TextView) customPopupLayout.findViewById(R.id.textYesterday);
        textLastSevenDays = (TextView) customPopupLayout.findViewById(R.id.textLastSevenDays);
        textLastThirtyDays = (TextView) customPopupLayout.findViewById(R.id.textLastThirtyDays);
        textCustom = (TextView) customPopupLayout.findViewById(R.id.textCustom);

        llDateLayout.setOnClickListener(this);
        textYesterday.setOnClickListener(this);
        textLastSevenDays.setOnClickListener(this);
        textLastThirtyDays.setOnClickListener(this);
        textCustom.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        hrsecond.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                hrone.scrollTo(hrsecond.getScrollX(), hrsecond.getScrollY());
                hrbottom.scrollTo(hrsecond.getScrollX(), hrsecond.getScrollY());
            }
        });

        getAdGroupData();

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llDateLayout:
                customDatePopup.showAsDropDown(llDateLayout, 0, 10);
                break;
            case R.id.textYesterday:
                setYesterday();
                break;
            case R.id.textLastSevenDays:
                setLastSeven();
                break;
            case R.id.textLastThirtyDays:
                setLastThirty();
                break;
            case R.id.textCustom:
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
        textSelectedDateRange.setText(Utils.getSevenDayBeforeDate() + " - " + Utils.getCurrentDate());
        customDatePopup.dismiss();
    }

    private void setLastThirty() {
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.colorPrimary));
        textLastSevenDays.setTextColor(getResources().getColor(R.color.black));
        textYesterday.setTextColor(getResources().getColor(R.color.black));
        textSelectedDateRange.setText(Utils.getThirtyDayBeforeDate() + " - " + Utils.getCurrentDate());
        customDatePopup.dismiss();
    }

    private void getAdGroupData() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestDataAdgroup requestKeywords = new RequestDataAdgroup();
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
                                    llDataContainer.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    textMessage.setVisibility(View.GONE);
                                    createAdgroupTable(data);
                                    offset = offset + limit ;

                                }else {
                                    if( offset == 0 ){
                                        llDataContainer.setVisibility(View.INVISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        textMessage.setVisibility(View.VISIBLE);
                                        textMessage.setText("No Adgroups Found.");
                                    }
                                }
                            }else {
                                if( offset == 0 ){
                                    llDataContainer.setVisibility(View.INVISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    textMessage.setVisibility(View.VISIBLE);
                                    textMessage.setText("Some error occured.");
                                }else
                                    Toast.makeText(AdgroupActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();

                            }

                        }catch (Exception e ){
                            e.printStackTrace();
                            if( offset == 0 ){
                                llDataContainer.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.VISIBLE);
                                textMessage.setText("Some error occured.");
                            }else
                                Toast.makeText(AdgroupActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if( offset == 0 ){
                            llDataContainer.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.GONE);
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Some error occured.");
                        }else
                            Toast.makeText(AdgroupActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();

                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseDataAdgroup> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if ( t != null ) t.printStackTrace();
                if( offset == 0 ){
                    llDataContainer.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText("There is some connectivity issue, please try again.");
                }else
                    Toast.makeText(AdgroupActivity.this, "There is some connectivity issue, please try again.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void createAdgroupTable(ArrayList<Adgroup> data) {
        for (int i = 0; i < data.size(); i++) {
            TableRow row1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row1.setLayoutParams(lp);
            setAdgroupOtherRow(row1, lp, rowCount, data.get(i));
        }
    }

    private void setAdgroupOtherRow(TableRow row, TableRow.LayoutParams lp, int i, Adgroup data) {

        setAdName(i,data);

        View view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );


        TextView textView3 = (TextView) view.findViewById(R.id.text_view);
        textView3.setText(data.getClicks());
        row.addView(textView3);


        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView1 = (TextView) view.findViewById(R.id.text_view);
        textView1.setText(data.getImpressions());
        row.addView(textView1);

        // Avg cpc


        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(data.getAvg_cpc());
        row.addView(textView);

        // Cost

        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView4 = (TextView) view.findViewById(R.id.text_view);
        textView4.setText(data.getCost());
        row.addView(textView4);


        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView2 = (TextView) view.findViewById(R.id.text_view);
        textView2.setText(data.getCtr());
        row.addView(textView2);

        // Avg Pos

        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView6 = (TextView) view.findViewById(R.id.text_view);
        textView6.setText(data.getAvg_position());
        row.addView(textView6);


        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView5 = (TextView) view.findViewById(R.id.text_view);
        textView5.setText(data.getConverted_clicks());
        row.addView(textView5);

        // Cost/ conv

        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView7 = (TextView) view.findViewById(R.id.text_view);
        textView7.setText(data.getCpa());
        row.addView(textView7);


        // Conv Rate

        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView8 = (TextView) view.findViewById(R.id.text_view);
        textView8.setText(data.getConversion_rate());
        row.addView(textView8);

        tlValues.addView(row, i);
        rowCount++;
    }

    private void setAdName( int i, Adgroup data) {
        TableRow row = new TableRow(this);
        View v = LayoutInflater.from(this).inflate(R.layout.first_row, row, false);
        TextView tv = (TextView)v.findViewById(R.id.text_view);
        tv.setText(data.getAdgroup());
        tlName.addView(v, i);
    }

    @Override
    public void onRefresh() {
        getAdGroupData();
    }
}
