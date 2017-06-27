package com.agl.product.adw8_new.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.adapter.LeadDashboardAdditionalAdapter;
import com.agl.product.adw8_new.adapter.LeadDashboardHeaderAdapter;
import com.agl.product.adw8_new.adapter.LeadsGraphAdapter;
import com.agl.product.adw8_new.database.IAdv8Database;
import com.agl.product.adw8_new.model.LeadSource;
import com.agl.product.adw8_new.model.LeadsGraphData;
import com.agl.product.adw8_new.model.MainLeads;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Get;
import com.agl.product.adw8_new.service.data.ResponseDataLeads;
import com.agl.product.adw8_new.service.data.ResponseLeadsGraph;
import com.agl.product.adw8_new.service.data.ResponseLeadsSource;
import com.agl.product.adw8_new.utils.ConnectionDetector;
import com.agl.product.adw8_new.utils.Session;
import com.agl.product.adw8_new.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadDashboardActivity1 extends ActivityBase implements View.OnClickListener {

    private RecyclerView rvHeaderData, rvGraph, rvAdditionalDetail;
    HashMap<String, String> userData;
    Session session;
    String campaignIds = "", landingPageIds = "", ownerIds = "", statusIds = "";
    IAdv8Database database = new IAdv8Database(this, "Iadv8.db", null, 1);
    private LinearLayout lldefaultSpends;
    private PopupWindow customDatePopup;
    private View customPopupLayout;
    private TextView textYesterday, textLastSevenDays, textLastThirtyDays, textCustom, textSelectedDateRange, textMessage;
    private DatePickerDialog datePickerDialog;
    private String fromDate, toDate, fromDateToShow, toDateToShow;
    private ConnectionDetector cd;
    private ArrayList<MainLeads> mainLeads;
    private ArrayList<LeadsGraphData> data;
    ArrayList<MainLeads> additionalLeadsTest;
    ArrayList<MainLeads> additionalLeads;
    private ProgressDialog pd;
    private String dateType = "";
    private TextView textReject, textClosedLost, textCloseWon, textProposalSent,viewAll;
    private TableLayout tlName,tlValues;
    private int rowCount = 0 ;
    private HorizontalScrollView hrone, hrsecond, hrbottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_dashboard1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new Session(this);
        cd = new ConnectionDetector(this);
        pd = new ProgressDialog(this);

        tlName = (TableLayout) findViewById(R.id.tlName);
        tlValues = (TableLayout) findViewById(R.id.tlValues);

        textReject = (TextView) findViewById(R.id.textReject);
        textClosedLost = (TextView) findViewById(R.id.textClosedLost);
        textCloseWon = (TextView) findViewById(R.id.textCloseWon);
        textProposalSent = (TextView) findViewById(R.id.textProposalSent);
        textSelectedDateRange = (TextView) findViewById(R.id.textSelectedDateRange);


        lldefaultSpends = (LinearLayout) findViewById(R.id.lldefaultSpends);
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



        viewAll = (TextView) findViewById(R.id.viewAll);
        hrone = (HorizontalScrollView) findViewById(R.id.hrone);
        hrsecond = (HorizontalScrollView) findViewById(R.id.hrsecond);
        hrbottom = (HorizontalScrollView) findViewById(R.id.hrbottom);

        rvHeaderData = (RecyclerView) findViewById(R.id.rvHeaderData);
        rvHeaderData.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvHeaderData.setLayoutManager(mLayoutManager);

        rvGraph = (RecyclerView) findViewById(R.id.rvGraph);
        rvGraph.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGraph.setLayoutManager(mLayoutManager2);

        rvAdditionalDetail = (RecyclerView) findViewById(R.id.rvAdditionalDetail);
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAdditionalDetail.setLayoutManager(mLayoutManager3);
        rvAdditionalDetail.setNestedScrollingEnabled(false);

        mainLeads = new ArrayList<MainLeads>();
        data = new ArrayList<LeadsGraphData>();
        additionalLeads = new ArrayList<MainLeads>();




        fromDate = Utils.getSevenDayBeforeDate();
        toDate = Utils.getCurrentDate();
        fromDateToShow = Utils.getDisplaySevenDayBeforeDate();
        toDateToShow = Utils.getDisplayCurrentDate();

        // Set Seven days before
        textYesterday.setTextColor(getResources().getColor(R.color.black));
        textLastSevenDays.setTextColor(getResources().getColor(R.color.colorPrimary));
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.black));
        textCustom.setTextColor(getResources().getColor(R.color.black));
        dateType = Utils.TYPE_LAST_WEEK;


        hrsecond.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                hrone.scrollTo(hrsecond.getScrollX(), hrsecond.getScrollY());
                hrbottom.scrollTo(hrsecond.getScrollX(), hrsecond.getScrollY());
            }
        });

        lldefaultSpends.setOnClickListener(this);
        textYesterday.setOnClickListener(this);
        textLastSevenDays.setOnClickListener(this);
        textLastThirtyDays.setOnClickListener(this);
        textCustom.setOnClickListener(this);
        viewAll.setOnClickListener(this);


        userData = session.getUsuarioDetails();
        campaignIds = getCampaignCount().toString();
        landingPageIds = getLandingCount().toString();
        ownerIds = getOwnerCount().toString();
        getAllData();
    }

    private void getAllData(){
        if (cd.isConnectedToInternet()) {
            pd.show();
            requestGraphData();
            requestLeadMatrics();
            requestLeadSource();
        } else Toast.makeText(this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
    }

    private void requestLeadSource() {
        Get apiLeadsService = ApiClient.getClientEarlier().create(Get.class);
        String url = "webforms/get-Lead-Utm-Wise/userEmail/" +userData.get(Session.KEY_EMAIL)+"/password/"
                +userData.get(Session.KEY_PASSWORD)+"/sKeys/1r2a3k4s5h6s7i8n9h10/clientId/"+
                userData.get(Session.KEY_AGENCY_CLIENT_ID)+"/groupBy/utm_source,status/fromDate/"
                +fromDate+"/toDate/"+toDate;

        Call<ResponseLeadsSource> graphCall = apiLeadsService.getLeadSource(url);
        graphCall.enqueue(new Callback<ResponseLeadsSource>() {
            @Override
            public void onResponse(Call<ResponseLeadsSource> call, Response<ResponseLeadsSource> response) {
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

    private void requestGraphData() {
        Get apiLeadsService = ApiClient.getClientEarlier().create(Get.class);
        String url = "webforms/get-Lead-Status-Wise-Graph/userEmail/" + userData.get(Session.KEY_EMAIL)
                + "/password/" + userData.get(Session.KEY_PASSWORD) + "/sKeys/1r2a3k4s5h6s7i8n9h10/clientId/"
                + userData.get(Session.KEY_AGENCY_CLIENT_ID) + "/groupBy/status/fromDate/" + fromDate + "/toDate/" + toDate;
        Call<ResponseLeadsGraph> graphCall = apiLeadsService.getLeadGraph(url);
        graphCall.enqueue(new Callback<ResponseLeadsGraph>() {
            @Override
            public void onResponse(Call<ResponseLeadsGraph> call, Response<ResponseLeadsGraph> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getError() == 0) {
                            if (response.body().getData() != null) {
                                if (response.body().getData().size() > 0) {
                                    data = response.body().getData();
                                    rvGraph.setVisibility(View.VISIBLE);
                                    LeadsGraphAdapter leadsGraphAdapter = new LeadsGraphAdapter(LeadDashboardActivity1.this, data);
                                    rvGraph.setAdapter(leadsGraphAdapter);
                                } else {
                                    rvGraph.setVisibility(View.GONE);
                                }
                            } else {
                                rvGraph.setVisibility(View.GONE);
                            }
                        } else {

                        }
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLeadsGraph> call, Throwable t) {

                if (t != null) {
                    Log.d("TAG", t.getMessage());
                }
            }
        });
    }

    private void requestLeadMatrics() {
        Get apiLeadsService = ApiClient.getClientEarlier().create(Get.class);
        String url = "http://adv8kuber.in/webforms/lead-Matrics-New/userEmail/" + userData.get(Session.KEY_EMAIL) + "/password/" + userData.get(Session.KEY_PASSWORD)
                + "/sKeys/1r2a3k4s5h6s7i8n9h10/clientId/" + userData.get(Session.KEY_AGENCY_CLIENT_ID) + "/groupBy/status/fromDate/" + fromDate +
                "/toDate/" + toDate + "/campaignIds/" + campaignIds + "/landingPageIds/" + landingPageIds + "/statusIds/" + statusIds + "/ownerIds/"
                + ownerIds;
        Call<ResponseDataLeads> leadsCall = apiLeadsService.getLeadsData(url);
        leadsCall.enqueue(new Callback<ResponseDataLeads>() {
            @Override
            public void onResponse(Call<ResponseDataLeads> call, Response<ResponseDataLeads> response) {
                pd.dismiss();
                if (response != null) {
                    if (response.isSuccessful()) {
                        ResponseDataLeads responseDataLeads = response.body();
                        try {
                            ArrayList<MainLeads> leads = responseDataLeads.getLeads().getMain();
                            if (leads != null && leads.size() > 0) {
                                mainLeads = leads;
                                MainLeads lead = mainLeads.get(0);
                                lead.setChecked(true);
                                LeadDashboardHeaderAdapter leadDashboardHeaderAdapter = new LeadDashboardHeaderAdapter(LeadDashboardActivity1.this, mainLeads);
                                rvHeaderData.setAdapter(leadDashboardHeaderAdapter);
                            } else {

                            }

                            try {
                                additionalLeadsTest = new ArrayList<MainLeads>();
                                additionalLeads = responseDataLeads.getLeads().getAdditional();
                                if (additionalLeads != null && additionalLeads.size() > 0) {
//                                    for(int i=0; i<4; i++) {
//                                        MainLeads data = additionalLeads.get(i);
//                                        additionalLeadsTest.add(new MainLeads(data.getStatus(), data.getStatus_id(), data.getCnt(), false));
//                                    }
//
//                                    additionalLeadsTest.add(new MainLeads("View More", "", "", false));

                                    LeadDashboardAdditionalAdapter leadDashboardAdditionalAdapter = new LeadDashboardAdditionalAdapter(LeadDashboardActivity1.this, additionalLeads, fromDateToShow, toDateToShow, dateType);
                                    rvAdditionalDetail.setAdapter(leadDashboardAdditionalAdapter);
                                } else {

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDataLeads> call, Throwable t) {
                pd.dismiss();
                if (t != null) Log.d("ERROR", t.getMessage());
            }
        });
    }

    public void openDashBoardListActivity(int position, MainLeads additionalLeads) {
        startActivity(new Intent(LeadDashboardActivity1.this, LeadListDashboardActivity.class)
                .putExtra(Utils.CURRENT_FROM_DATE, fromDate)
                .putExtra(Utils.CURRENT_TO_DATE, toDate)
                .putExtra(Utils.CURRENT_FROM_DATE_TO_SHOW, fromDateToShow)
                .putExtra(Utils.CURRENT_TO_DATE_TO_SHOW, toDateToShow)
                .putExtra(Utils.DATE_TYPE, dateType)
                .putExtra(Utils.ID_TYPE_STATUS, additionalLeads.getStatus_id() + ","));
    }

    public StringBuffer getCampaignCount() {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor curCampaign = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Utils.ID_TYPE_CAMPAIGN_UNITS, "true"}, null, null, null);
        StringBuffer paramCampaignUnitIds = new StringBuffer();
        while (curCampaign.moveToNext()) {
            paramCampaignUnitIds.append(curCampaign.getString(4) + ",");
        }
        return paramCampaignUnitIds;
    }

    public StringBuffer getLandingCount() {
        //Landing********************
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor curLanding = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Utils.ID_TYPE_LANDING_PAGES, "true"}, null, null, null);
        StringBuffer paramLanding = new StringBuffer();
        while (curLanding.moveToNext()) {
            paramLanding.append(curLanding.getString(4) + ",");
        }
        return paramLanding;
    }

    public StringBuffer getOwnerCount() {
        //Owners
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor curOwners = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Utils.ID_TYPE_OWNERS, "true"}, null, null, null);
        StringBuffer paramOwners = new StringBuffer();
        while (curOwners.moveToNext()) {
            paramOwners.append(curOwners.getString(4) + ",");
        }
        return paramOwners;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lldefaultSpends:
                customDatePopup.showAsDropDown(lldefaultSpends, 0, 20);
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
                customDatePopup.dismiss();
                AlertDialog builder = new ShowDateRangeDialog(this, getResources().getString(R.string.instabilidade_servidor));
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                builder.setCanceledOnTouchOutside(false);
                builder.setCancelable(false);
                builder.show();
                break;
            case R.id.viewAll:
                startActivity(new Intent(this,LeadSourceActivity.class));
                break;
        }
    }

    private class ShowDateRangeDialog extends AlertDialog {
        String fromDisplay, toDisplay;
        String fromDay, toDay;

        protected ShowDateRangeDialog(Context context, String message) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogLayout = inflater.inflate(R.layout.custom_date_layout, (ViewGroup) getCurrentFocus());
            setView(dialogLayout);

            LinearLayout llStartDate = (LinearLayout) dialogLayout.findViewById(R.id.llStartDate);
            final TextView textStartDate = (TextView) dialogLayout.findViewById(R.id.textStartDate);

            LinearLayout llEndDate = (LinearLayout) dialogLayout.findViewById(R.id.llEndDate);
            final TextView textEndDate = (TextView) dialogLayout.findViewById(R.id.textEndDate);

            TextView textCancel = (TextView) dialogLayout.findViewById(R.id.textCancel);
            TextView textOk = (TextView) dialogLayout.findViewById(R.id.textOk);

            fromDisplay = fromDateToShow;
            toDisplay = toDateToShow;
            fromDay = fromDate;
            toDay = toDate;
            textStartDate.setText(fromDateToShow);
            textEndDate.setText(toDateToShow);
            llStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    datePickerDialog = new DatePickerDialog(LeadDashboardActivity1.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                            DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                            Date date = new Date(calendar.getTimeInMillis());
                            fromDisplay = dateFormat.format(date);

                            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            Date date1 = new Date(calendar.getTimeInMillis());
                            textStartDate.setText(fromDisplay);
                            fromDay = dateFormat1.format(date1);

                        }


                    }, 2017, 05, 15);

                    datePickerDialog.show();

                }
            });

            llEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    datePickerDialog = new DatePickerDialog(LeadDashboardActivity1.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                            DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                            Date date = new Date(calendar.getTimeInMillis());
                            toDisplay = dateFormat.format(date);

                            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            Date date1 = new Date(calendar.getTimeInMillis());
                            textEndDate.setText(toDisplay);
                            toDay = dateFormat1.format(date1);

                        }


                    }, 2017, 05, 15);

                    datePickerDialog.show();
                }
            });

            textCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            textOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fromDay != null && toDay != null && fromDisplay != null && toDisplay != null) {
                        fromDate = fromDay;
                        toDate = toDay;
                        fromDateToShow = fromDisplay;
                        toDateToShow = toDisplay;
                    }
                    setCustomDay();
                    dismiss();
                }
            });


        }
    }

    private void setYesterday() {
        if (!cd.isConnectedToInternet()) return;
        textYesterday.setTextColor(getResources().getColor(R.color.colorPrimary));
        textLastSevenDays.setTextColor(getResources().getColor(R.color.black));
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.black));
        textCustom.setTextColor(getResources().getColor(R.color.black));

        fromDate = Utils.getYesterdayDate();
        toDate = Utils.getYesterdayDate();
        fromDateToShow = Utils.getDisplayYesterdayDate();
        toDateToShow = Utils.getDisplayYesterdayDate();
        textSelectedDateRange.setText(fromDateToShow);
        customDatePopup.dismiss();
        dateType = Utils.TYPE_YESTERDAY;
        /*offset = 0;
        rowCount = 0;
        requestInsightData();*/
        getAllData();

    }

    private void setLastSeven() {
        if (!cd.isConnectedToInternet()) return;
        textLastSevenDays.setTextColor(getResources().getColor(R.color.colorPrimary));
        textYesterday.setTextColor(getResources().getColor(R.color.black));
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.black));
        textCustom.setTextColor(getResources().getColor(R.color.black));
        fromDate = Utils.getSevenDayBeforeDate();
        toDate = Utils.getCurrentDate();
        fromDateToShow = Utils.getDisplaySevenDayBeforeDate();
        toDateToShow = Utils.getDisplayCurrentDate();
        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);
        customDatePopup.dismiss();
        dateType = Utils.TYPE_LAST_WEEK;
       /* offset = 0;
        rowCount = 0;
        requestInsightData();*/
        getAllData();

    }

    private void setLastThirty() {
        if (!cd.isConnectedToInternet()) return;
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.colorPrimary));
        textLastSevenDays.setTextColor(getResources().getColor(R.color.black));
        textYesterday.setTextColor(getResources().getColor(R.color.black));
        textCustom.setTextColor(getResources().getColor(R.color.black));
        fromDate = Utils.getThirtyDayBeforeDate();
        toDate = Utils.getCurrentDate();
        fromDateToShow = Utils.getDisplayThirtyDayBeforeDate();
        toDateToShow = Utils.getDisplayCurrentDate();
        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);
        customDatePopup.dismiss();
        dateType = Utils.TYPE_LAST_MONTH;
        /*offset = 0;
        rowCount = 0;
        requestInsightData();*/
        getAllData();
    }

    private void setCustomDay() {
        if (!cd.isConnectedToInternet()) return;
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.black));
        textLastSevenDays.setTextColor(getResources().getColor(R.color.black));
        textYesterday.setTextColor(getResources().getColor(R.color.black));
        textCustom.setTextColor(getResources().getColor(R.color.colorPrimary));
        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);
        dateType = Utils.TYPE_CUSTOM_DATE;
       /* offset = 0;
        rowCount = 0;
        requestInsightData();*/
        getAllData();
    }
}
