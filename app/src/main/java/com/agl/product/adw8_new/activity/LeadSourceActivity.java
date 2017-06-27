package com.agl.product.adw8_new.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.custom_view.SwipeRefreshLayoutBottom;
import com.agl.product.adw8_new.model.LeadSource;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Get;
import com.agl.product.adw8_new.service.data.ResponseLeadsSource;
import com.agl.product.adw8_new.utils.Session;
import com.agl.product.adw8_new.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadSourceActivity extends ActivityBase implements SwipeRefreshLayoutBottom.OnRefreshListener{

    Session session;
    HashMap<String, String> userData;
    private String fromDate, toDate, fromDateToShow, toDateToShow;
    int rowCount = 0 ;
    private TableLayout tlValues,tlName;
    private TextView textReject, textClosedLost, textCloseWon, textProposalSent;
    private ProgressDialog pd;
    private SwipeRefreshLayoutBottom swipeRefreshLayout;
    private HorizontalScrollView hrone, hrsecond, hrbottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_source);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Lead Source");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        session = new Session(this);
        userData = session.getUsuarioDetails();
        fromDate = "2017-03-15";
        toDate = Utils.getCurrentDate();
        fromDateToShow = Utils.getDisplaySevenDayBeforeDate();
        toDateToShow = Utils.getDisplayCurrentDate();
        swipeRefreshLayout = (SwipeRefreshLayoutBottom) findViewById(R.id.swipeRefresh);
        tlValues = (TableLayout) findViewById(R.id.tlValues);
        tlName = (TableLayout) findViewById(R.id.tlName);
        textReject = (TextView) findViewById(R.id.textReject);
        textClosedLost = (TextView) findViewById(R.id.textClosedLost);
        textCloseWon = (TextView) findViewById(R.id.textCloseWon);
        textProposalSent = (TextView) findViewById(R.id.textProposalSent);

        hrone = (HorizontalScrollView) findViewById(R.id.hrone);
        hrsecond = (HorizontalScrollView) findViewById(R.id.hrsecond);
        hrbottom = (HorizontalScrollView) findViewById(R.id.hrbottom);

        hrsecond.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                hrone.scrollTo(hrsecond.getScrollX(), hrsecond.getScrollY());
                hrbottom.scrollTo(hrsecond.getScrollX(), hrsecond.getScrollY());
            }
        });

        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait");
        pd.show();
        requestLeadSource();
    }

    private void requestLeadSource() {
        Get apiLeadsService = ApiClient.getClientEarlier().create(Get.class);
        String url = "http://adv8kuber.in/webforms/get-Lead-Utm-Wise/userEmail/" + userData.get(Session.KEY_EMAIL)
                + "/password/" + userData.get(Session.KEY_PASSWORD) + "/sKeys/1r2a3k4s5h6s7i8n9h10/clientId/"
                + userData.get(Session.KEY_AGENCY_CLIENT_ID) + "/groupBy/utm_source,status/fromDate/" + fromDate + "/toDate/" + toDate;

        Call<ResponseLeadsSource> graphCall = apiLeadsService.getLeadSource(url);
        graphCall.enqueue(new Callback<ResponseLeadsSource>() {
            @Override
            public void onResponse(Call<ResponseLeadsSource> call, Response<ResponseLeadsSource> response) {
                pd.dismiss();
                if( response != null ){
                    if( response.isSuccessful() ){
                        ResponseLeadsSource res= response.body();
                        ArrayList<LeadSource> list = res.getData();
                        if( list != null && list.size() > 0  ){
                            createLeadSourceTable(list);
                            setTotalRow(list);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLeadsSource> call, Throwable t) {
                pd.dismiss();
                if ( t != null ) {
                    Log.d("TAG",t.getMessage());
                }
            }
        });
    }

    private void setTotalRow(ArrayList<LeadSource> list) {
        try {
            LeadSource leadSouce = list.get(0);
            textReject.setText("2");
            textClosedLost.setText("0");
            textCloseWon.setText("0");
            textProposalSent.setText("1");
        } catch (Exception e ){
            e.printStackTrace();
        }
    }

    private void createLeadSourceTable(ArrayList<LeadSource> list) {
        for (int i = 0; i < list.size(); i++) {
            LeadSource leadSource = list.get(i );
            TableRow row1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row1.setLayoutParams(lp);
            setFirstColumn(leadSource);
            setAdsOtherRow(leadSource);
        }
    }

    private void setAdsOtherRow(LeadSource insightDimension) {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp.span = 1;
        row.setLayoutParams(lp);

        View view = LayoutInflater.from(this).inflate(R.layout.row_lead_source_tv, row, false);
        TextView textView7 = (TextView) view.findViewById(R.id.text_view);
        textView7.setText(insightDimension.getClosedLost());
        row.addView(textView7);

        view = LayoutInflater.from(this).inflate(R.layout.row_lead_source_tv, row, false);
        TextView textView6 = (TextView) view.findViewById(R.id.text_view);
        textView6.setText(insightDimension.getClosedWon());
        row.addView(textView6);

        view = LayoutInflater.from(this).inflate(R.layout.row_lead_source_tv, row, false);
        TextView textView5 = (TextView) view.findViewById(R.id.text_view);
        textView5.setText(insightDimension.getProsopalSent());
        row.addView(textView5);

        view = LayoutInflater.from(this).inflate(R.layout.row_lead_source_tv, row, false);
        TextView textView8 = (TextView) view.findViewById(R.id.text_view);
        textView8.setText(insightDimension.getRejected());
        row.addView(textView8);

        tlValues.addView(row, rowCount);
        rowCount++;
    }

    private void setFirstColumn(LeadSource lead){
        TableRow row = new TableRow(this);
        View v = LayoutInflater.from(this).inflate(R.layout.first_row, row, false);
        TextView tv = (TextView) v.findViewById(R.id.text_view);
        tv.setText(lead.getUtm_source());
        tlName.addView(v, rowCount);
    }

    @Override
    public void onRefresh() {

    }
}
