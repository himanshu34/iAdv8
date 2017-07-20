package com.agl.product.adw8_new.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.custom_view.SwipeRefreshLayoutBottom;
import com.agl.product.adw8_new.model.LeadSource;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Get;
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

public class LeadSourceActivity extends ActivityBase implements SwipeRefreshLayoutBottom.OnRefreshListener, View.OnClickListener {

    Session session;
    HashMap<String, String> userData;
    private String fromDate, toDate, fromDateToShow, toDateToShow;
    int rowCount = 0;
    private TableLayout tlValues, tlName;
    private TextView textReject, textClosedLost, textCloseWon, textProposalSent, textMessage;
    private SwipeRefreshLayoutBottom swipeRefreshLayout;
    private HorizontalScrollView hrone, hrsecond, hrbottom;
    private ProgressBar progressBar;
    private LinearLayout llContainer, lldefaultSpends;
    private ConnectionDetector cd;
    private ArrayList<LeadSource> leadSourceList;
    private int offSet = 0, limit = 10;
    private PopupWindow customDatePopup;
    private View customPopupLayout;
    private String dateType = "";
    private DatePickerDialog datePickerDialog;
    private TextView textYesterday, textLastSevenDays, textLastThirtyDays, textCustom, textSelectedDateRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_source);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        cd = new ConnectionDetector(this);
        session = new Session(this);
        userData = session.getUsuarioDetails();
        swipeRefreshLayout = (SwipeRefreshLayoutBottom) findViewById(R.id.swipeRefresh);
        tlValues = (TableLayout) findViewById(R.id.tlValues);
        tlName = (TableLayout) findViewById(R.id.tlName);
        textReject = (TextView) findViewById(R.id.textReject);
        textClosedLost = (TextView) findViewById(R.id.textClosedLost);
        textCloseWon = (TextView) findViewById(R.id.textCloseWon);
        textProposalSent = (TextView) findViewById(R.id.textProposalSent);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textMessage = (TextView) findViewById(R.id.textMessage);
        llContainer = (LinearLayout) findViewById(R.id.llContainer);
        lldefaultSpends = (LinearLayout) findViewById(R.id.lldefaultSpends);
        hrone = (HorizontalScrollView) findViewById(R.id.hrone);
        hrsecond = (HorizontalScrollView) findViewById(R.id.hrsecond);
        hrbottom = (HorizontalScrollView) findViewById(R.id.hrbottom);
        textSelectedDateRange = (TextView) findViewById(R.id.textSelectedDateRange);

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

        dateType = getIntent().getStringExtra(Utils.DATE_TYPE);
        fromDate = getIntent().getStringExtra(Utils.CURRENT_FROM_DATE);
        toDate = getIntent().getStringExtra(Utils.CURRENT_TO_DATE);
        fromDateToShow = getIntent().getStringExtra(Utils.CURRENT_FROM_DATE_TO_SHOW);
        toDateToShow = getIntent().getStringExtra(Utils.CURRENT_TO_DATE_TO_SHOW);
        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);

        showDateLayout(dateType);

        leadSourceList = new ArrayList<LeadSource>();

        lldefaultSpends.setOnClickListener(this);
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

        requestLeadSource();
    }

    private void showDateLayout(String dateType) {
        switch (dateType) {
            case Utils.TYPE_YESTERDAY:
                textYesterday.setTextColor(getResources().getColor(R.color.colorPrimary));
                textLastSevenDays.setTextColor(getResources().getColor(R.color.black));
                textLastThirtyDays.setTextColor(getResources().getColor(R.color.black));
                textCustom.setTextColor(getResources().getColor(R.color.black));
                break;
            case Utils.TYPE_LAST_WEEK:
                textLastSevenDays.setTextColor(getResources().getColor(R.color.colorPrimary));
                textYesterday.setTextColor(getResources().getColor(R.color.black));
                textLastThirtyDays.setTextColor(getResources().getColor(R.color.black));
                textCustom.setTextColor(getResources().getColor(R.color.black));
                break;
            case Utils.TYPE_LAST_MONTH:
                textLastThirtyDays.setTextColor(getResources().getColor(R.color.colorPrimary));
                textLastSevenDays.setTextColor(getResources().getColor(R.color.black));
                textYesterday.setTextColor(getResources().getColor(R.color.black));
                textCustom.setTextColor(getResources().getColor(R.color.black));
                break;
            case Utils.TYPE_CUSTOM_DATE:
                textLastThirtyDays.setTextColor(getResources().getColor(R.color.black));
                textLastSevenDays.setTextColor(getResources().getColor(R.color.black));
                textYesterday.setTextColor(getResources().getColor(R.color.black));
                textCustom.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;

        }
    }

    private void requestLeadSource() {
        if (offSet == 0) {
            progressBar.setVisibility(View.VISIBLE);
            textMessage.setVisibility(View.GONE);
            llContainer.setVisibility(View.GONE);
        }
        Get apiLeadsService = ApiClient.getClientEarlier().create(Get.class);
        String url = "http://adv8kuber.in/webforms/get-Lead-Utm-Wise/userEmail/" + userData.get(Session.KEY_EMAIL)
                + "/password/" + userData.get(Session.KEY_PASSWORD) + "/sKeys/1r2a3k4s5h6s7i8n9h10/clientId/"
                + userData.get(Session.KEY_AGENCY_CLIENT_ID) + "/groupBy/utm_source,status/fromDate/" + fromDate + "/toDate/" + toDate + "/offset/" + offSet + "/limit/" + limit;

        Call<ResponseLeadsSource> graphCall = apiLeadsService.getLeadSource(url);
        graphCall.enqueue(new Callback<ResponseLeadsSource>() {
            @Override
            public void onResponse(Call<ResponseLeadsSource> call, Response<ResponseLeadsSource> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response != null) {
                    if (response.isSuccessful()) {
                        try {
                            ResponseLeadsSource res = response.body();
                            ArrayList<LeadSource> list = res.getData();
                            if (list != null && list.size() > 0) {
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.GONE);
                                llContainer.setVisibility(View.VISIBLE);
                                leadSourceList.addAll(list);
                                createLeadSourceTable(list);
                                setTotalRow(list);
                                offSet = offSet + limit;
                            }

                            if (offSet == 0) {
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.VISIBLE);
                                llContainer.setVisibility(View.GONE);
                                textMessage.setText("NO DATA FOUND");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        if (offSet == 0) {
                            progressBar.setVisibility(View.GONE);
                            textMessage.setVisibility(View.VISIBLE);
                            llContainer.setVisibility(View.GONE);
                            textMessage.setText("Some Error Occured.");
                        } else {
                            Toast.makeText(LeadSourceActivity.this, "Some Error Occured.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLeadsSource> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if (offSet == 0) {
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    llContainer.setVisibility(View.GONE);
                    textMessage.setText("There is some connectivity issue,please try later.");
                } else {
                    Toast.makeText(LeadSourceActivity.this, "There is some connectivity issue,please try later.", Toast.LENGTH_SHORT).show();
                }
                if (t != null) {
                    Log.d("TAG", t.getMessage());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createLeadSourceTable(ArrayList<LeadSource> list) {
        for (int i = 0; i < list.size(); i++) {
            LeadSource leadSource = list.get(i);
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

    private void setFirstColumn(LeadSource lead) {
        TableRow row = new TableRow(this);
        View v = LayoutInflater.from(this).inflate(R.layout.first_row, row, false);
        TextView tv = (TextView) v.findViewById(R.id.text_view);
        tv.setText(lead.getUtm_source());
        tlName.addView(v, rowCount);
    }

    @Override
    public void onRefresh() {
        requestLeadSource();
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
        offSet = 0;
        rowCount = 0;
        if(tlName.getChildCount() > 0 )tlName.removeAllViews();
        if(tlValues.getChildCount() > 0 )tlValues.removeAllViews();
        requestLeadSource();

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
        offSet = 0;
        rowCount = 0;
        if(tlName.getChildCount() > 0 )tlName.removeAllViews();
        if(tlValues.getChildCount() > 0 )tlValues.removeAllViews();
        requestLeadSource();

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
        offSet = 0;
        rowCount = 0;
        if(tlName.getChildCount() > 0 )tlName.removeAllViews();
        if(tlValues.getChildCount() > 0 )tlValues.removeAllViews();
        requestLeadSource();
    }

    private void setCustomDay() {
        if (!cd.isConnectedToInternet()) return;
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.black));
        textLastSevenDays.setTextColor(getResources().getColor(R.color.black));
        textYesterday.setTextColor(getResources().getColor(R.color.black));
        textCustom.setTextColor(getResources().getColor(R.color.colorPrimary));
        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);
        dateType = Utils.TYPE_CUSTOM_DATE;
        offSet = 0;
        rowCount = 0;
        if(tlName.getChildCount() > 0 )tlName.removeAllViews();
        if(tlValues.getChildCount() > 0 )tlValues.removeAllViews();
        requestLeadSource();
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
                    datePickerDialog = new DatePickerDialog(LeadSourceActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
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
                    datePickerDialog = new DatePickerDialog(LeadSourceActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
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
}
