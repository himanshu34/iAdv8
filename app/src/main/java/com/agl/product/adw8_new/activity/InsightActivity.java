package com.agl.product.adw8_new.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.adapter.InsightGroupAdapter;
import com.agl.product.adw8_new.custom_view.SwipeRefreshLayoutBottom;
import com.agl.product.adw8_new.model.InsightDimension;
import com.agl.product.adw8_new.model.InsightGroupType;
import com.agl.product.adw8_new.model.InsightTotal;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Post;
import com.agl.product.adw8_new.service.data.RequestInsightData;
import com.agl.product.adw8_new.service.data.ResponseInsightData;
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

public class InsightActivity extends ActivityBase implements View.OnClickListener, SwipeRefreshLayoutBottom.OnRefreshListener, AdapterView.OnItemSelectedListener {

    private LinearLayout lldefaultSpends;
    private PopupWindow customDatePopup;
    private View customPopupLayout;
    private TextView textYesterday, textLastSevenDays, textLastThirtyDays, textCustom, textSelectedDateRange, textMessage;
    private ImageView editIcon;
    private ConnectionDetector cd;
    private String fromDate, toDate, fromDateToShow, toDateToShow;
    private int offset = 0, rowCount, limit = 10;
    private HorizontalScrollView hrone, hrsecond, hrbottom;
    private DatePickerDialog datePickerDialog;
    Session session;
    HashMap<String, String> userData;
    private String rupeeSymbol, sortingOrder, groupTypeId = "5";
    private ProgressBar progressBar;
    private SwipeRefreshLayoutBottom swipeRefreshLayout;
    private TableLayout tlName, tlValues;
    private ArrayList<InsightDimension> dimensionList;
    private TextView textUsersTotal, textVisitsTotal, textSessTotal, textOrgScrTotal, textNewTotal;
    private Spinner spinner;
    private InsightGroupAdapter insightGroupAdapter;
    private ArrayList<InsightGroupType> insightData;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        cd = new ConnectionDetector(this);
        session = new Session(this);
        rupeeSymbol = getString(R.string.rupee);
        dimensionList = new ArrayList<InsightDimension>();
        insightData = new ArrayList<InsightGroupType>();
        insightGroupAdapter = new InsightGroupAdapter(this, insightData);

        lldefaultSpends = (LinearLayout) findViewById(R.id.lldefaultSpends);
        textSelectedDateRange = (TextView) findViewById(R.id.textSelectedDateRange);
        editIcon = (ImageView) findViewById(R.id.editIcon);
        hrone = (HorizontalScrollView) findViewById(R.id.hrone);
        hrsecond = (HorizontalScrollView) findViewById(R.id.hrsecond);
        hrbottom = (HorizontalScrollView) findViewById(R.id.hrbottom);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayoutBottom) findViewById(R.id.swipeRefresh);
        tlName = (TableLayout) findViewById(R.id.tlName);
        tlValues = (TableLayout) findViewById(R.id.tlValues);
        textMessage = (TextView) findViewById(R.id.textMessage);
        spinner = (Spinner) findViewById(R.id.spinner);

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

        textUsersTotal = (TextView) findViewById(R.id.textUsersTotal);
        textVisitsTotal = (TextView) findViewById(R.id.textVisitsTotal);
        textSessTotal = (TextView) findViewById(R.id.textSessTotal);
        textOrgScrTotal = (TextView) findViewById(R.id.textOrgScrTotal);
        textNewTotal = (TextView) findViewById(R.id.textNewTotal);

        spinner.setAdapter(insightGroupAdapter);
        spinner.setOnItemSelectedListener(this);
        userData = session.getUsuarioDetails();

        fromDate = "2016-06-01";
        toDate = Utils.getCurrentDate();
        fromDateToShow = Utils.getDisplaySevenDayBeforeDate();
        toDateToShow = Utils.getDisplayCurrentDate();

        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);
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
        swipeRefreshLayout.setOnRefreshListener(this);

        requestInsightData();
    }

    private void requestInsightData() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestInsightData requestInsight = new RequestInsightData();
        requestInsight.setAccess_token(userData.get(Session.KEY_ACCESS_TOKEN));
        requestInsight.setProfile_id(userData.get(Session.KEY_PROFILE_ID));
        requestInsight.settDate(toDate);
        requestInsight.setfDate(fromDate);
        requestInsight.setOffset(offset + "");
        requestInsight.setRow_count(limit + "");
        requestInsight.setOrder(sortingOrder);
        requestInsight.setCuId("0");
        requestInsight.setcId("196");
        requestInsight.setGroup_type_id(groupTypeId);
        if (offset == 0) {
            swipeRefreshLayout.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            textMessage.setVisibility(View.GONE);
        }

        String url = "http://agldashboard.adv8.co/analytics/insights/get_insights";
        Call<ResponseInsightData> adsCall = apiAddClientService.getInsightData(url, requestInsight);
        adsCall.enqueue(new Callback<ResponseInsightData>() {
            @Override
            public void onResponse(Call<ResponseInsightData> call, Response<ResponseInsightData> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response != null) {
                    if (response.isSuccessful()) {
                        ResponseInsightData responseInsightData = response.body();
                        try {
                            ArrayList<InsightDimension> dimensions = responseInsightData.getData().getDimensions();
                            ArrayList<InsightTotal> insightTotal = responseInsightData.getData().getTotal();
                            ArrayList<InsightGroupType> insightGroupList = responseInsightData.getData().getGroup_type();
                            if (insightGroupList != null && insightGroupList.size() > 0) {
                                if (insightData.size() == 0) {
                                    insightData = insightGroupList;
                                    insightGroupAdapter = new InsightGroupAdapter(InsightActivity.this, insightData);
                                    spinner.setAdapter(insightGroupAdapter);
                                }
                            }

                            if (dimensions.size() > 0) {
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.GONE);
                                dimensionList.addAll(dimensions);
                                offset = offset + limit;
                                setTable(dimensions);
                                if (insightTotal != null && insightTotal.size() > 0)
                                    setTotalRow(insightTotal);
                            } else {
                                if (offset == 0) {
                                    swipeRefreshLayout.setVisibility(View.INVISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    textMessage.setVisibility(View.VISIBLE);
                                    textMessage.setText("No Data Found.");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (offset == 0) {
                                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.VISIBLE);
                                textMessage.setText("Some error occured.");
                            } else {
                                Toast.makeText(InsightActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } else {
                        if (offset == 0) {
                            swipeRefreshLayout.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.GONE);
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Some error occured.");
                        } else
                            Toast.makeText(InsightActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseInsightData> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if (t != null) {
                    Log.d("ERRROR", t.getMessage());
                }
                if (offset == 0) {
                    swipeRefreshLayout.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText("There is some connectivity issue, please try again.");
                } else {
                    Toast.makeText(InsightActivity.this, "There is some connectivity issue, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setTotalRow(ArrayList<InsightTotal> insightTotal) {
        InsightTotal insight = insightTotal.get(0);
        textUsersTotal.setText(insight.getUsers());
        textVisitsTotal.setText(insight.getVisits());
        textSessTotal.setText(insight.getSess());
        textOrgScrTotal.setText(insight.getOrg_src());
        textNewTotal.setText(insight.getNewData());
    }

    private void setTable(ArrayList<InsightDimension> dimensions) {
        for (int i = 0; i < dimensions.size(); i++) {
            InsightDimension insightDimension = dimensions.get(i);
            String dimenstionName = insightDimension.getDimension();
            setDimentionValue(dimenstionName, rowCount);
            setValuesRow(insightDimension, rowCount);
        }
    }

    private void setDimentionValue(String dimenstionName, int rowCount) {
        TableRow row = new TableRow(this);
        View v = LayoutInflater.from(this).inflate(R.layout.first_row, row, false);
        TextView tv = (TextView) v.findViewById(R.id.text_view);
        tv.setText(dimenstionName);
        tlName.addView(v, rowCount);
    }

    private void setValuesRow(InsightDimension insightDimension, int rowCount) {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp.span = 1;
        row.setLayoutParams(lp);

        String users = insightDimension.getUsers();
        String visits = insightDimension.getVisits();
        String sess = insightDimension.getSess();
        String org_src = insightDimension.getOrg_src();
        String newData = insightDimension.getNewData();

        View view = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        TextView textView7 = (TextView) view.findViewById(R.id.text_view);
        textView7.setText(users);
        row.addView(textView7);

        view = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        TextView textView6 = (TextView) view.findViewById(R.id.text_view);
        textView6.setText(visits);
        row.addView(textView6);

        view = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        TextView textView5 = (TextView) view.findViewById(R.id.text_view);
        textView5.setText(sess);
        row.addView(textView5);

        view = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        TextView textView8 = (TextView) view.findViewById(R.id.text_view);
        textView8.setText(org_src);
        row.addView(textView8);

        view = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        TextView textView9 = (TextView) view.findViewById(R.id.text_view);
        textView9.setText(newData);
        row.addView(textView9);

        tlValues.addView(row, rowCount);
        rowCount++;
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
                AlertDialog builder = new ShowDateRangeDialog(InsightActivity.this);
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
        fromDate = Utils.getYesterdayDate();
        toDate = Utils.getYesterdayDate();
        fromDateToShow = Utils.getDisplayYesterdayDate();
        toDateToShow = Utils.getDisplayYesterdayDate();
        textSelectedDateRange.setText(fromDateToShow);
        customDatePopup.dismiss();
        offset = 0;
        rowCount = 0;
        requestInsightData();
    }

    private void setLastSeven() {
        if (!cd.isConnectedToInternet()) return;
        textLastSevenDays.setTextColor(getResources().getColor(R.color.colorPrimary));
        textYesterday.setTextColor(getResources().getColor(R.color.black));
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.black));
        fromDate = Utils.getSevenDayBeforeDate();
        toDate = Utils.getCurrentDate();
        fromDateToShow = Utils.getDisplaySevenDayBeforeDate();
        toDateToShow = Utils.getDisplayCurrentDate();
        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);
        customDatePopup.dismiss();
        offset = 0;
        rowCount = 0;
        requestInsightData();

    }

    private void setLastThirty() {
        if (!cd.isConnectedToInternet()) return;
        textLastThirtyDays.setTextColor(getResources().getColor(R.color.colorPrimary));
        textLastSevenDays.setTextColor(getResources().getColor(R.color.black));
        textYesterday.setTextColor(getResources().getColor(R.color.black));
        fromDate = Utils.getThirtyDayBeforeDate();
        toDate = Utils.getCurrentDate();
        fromDateToShow = Utils.getDisplayThirtyDayBeforeDate();
        toDateToShow = Utils.getDisplayCurrentDate();
        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);
        customDatePopup.dismiss();
        offset = 0;
        rowCount = 0;
        requestInsightData();
    }

    private void setCustomDay() {
        if (!cd.isConnectedToInternet()) return;
        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);
        offset = 0;
        rowCount = 0;
        requestInsightData();
    }

    @Override
    public void onRefresh() {
        requestInsightData();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (++check > 1) {
            InsightGroupType item = (InsightGroupType) spinner.getSelectedItem();
            groupTypeId = item.getId();
            offset = 0;
            rowCount = 0;
            requestInsightData();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private class ShowDateRangeDialog extends AlertDialog {
        String fromDisplay, toDisplay;
        String fromDay, toDay;

        protected ShowDateRangeDialog(Context context) {
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
                    datePickerDialog = new DatePickerDialog(InsightActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
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
                    datePickerDialog = new DatePickerDialog(InsightActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
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
