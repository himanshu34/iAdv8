package com.agl.product.adw8_new.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.adapter.DataActivityContentAdapter;
import com.agl.product.adw8_new.adapter.DataActivityGraphAdapter;
import com.agl.product.adw8_new.adapter.DataActivityGroupAdapter;
import com.agl.product.adw8_new.fragment.DataAdsFragment;
import com.agl.product.adw8_new.fragment.DataCampaignFragment;
import com.agl.product.adw8_new.fragment.DataKeywordFragment;
import com.agl.product.adw8_new.model.Ads;
import com.agl.product.adw8_new.model.CampaignData;
import com.agl.product.adw8_new.model.Counts;
import com.agl.product.adw8_new.model.Graph;
import com.agl.product.adw8_new.model.Keywords;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Post;
import com.agl.product.adw8_new.service.data.RequestDataGraphCampaign;
import com.agl.product.adw8_new.service.data.ResponseDataCampaign;
import com.agl.product.adw8_new.service.data.RequestDataCampaign;
import com.agl.product.adw8_new.service.data.ResponseDataGraphCampaign;
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

public class DataActivity extends ActivityBase implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    public String TAG = "DataActivity", fromDate, toDate, fromDateToShow, toDateToShow;
    LinearLayout headerLayout;
    private RecyclerView rvHeaderData, rvGroupData, rvGraph;
    private TabLayout tabLayout;
    private LinearLayout lldefaultSpends, tabsLayout;
    private PopupWindow customDatePopup;
    private View customPopupLayout;
    private ProgressBar progressBar;
    Session session;
    HashMap<String, String> userData;
    ArrayList<Graph> graphsListing;
    private ArrayList<Graph> graphList;
    private ArrayList<Counts> countsList;
    private ArrayList<CampaignData> campaignList;
    private ArrayList<Keywords> keywordList;
    private ArrayList<Ads> adsList;
    DataActivityContentAdapter contentAdapter;
    DataActivityGroupAdapter mAdapter;
    DataActivityGraphAdapter graphAdapter;
    Call<ResponseDataCampaign> campaignCall;
    Call<ResponseDataGraphCampaign> campaignGraphCall;
    private TextView textYesterday, textLastSevenDays, textLastThirtyDays, textCustom, textSelectedDateRange;
    private ConnectionDetector cd;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        session = new Session(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        cd = new ConnectionDetector(this);

        lldefaultSpends = (LinearLayout) findViewById(R.id.lldefaultSpends);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
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

        headerLayout = (LinearLayout) findViewById(R.id.header_layout);
        rvHeaderData = (RecyclerView) findViewById(R.id.rvHeaderData);
        rvHeaderData.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvHeaderData.setLayoutManager(mLayoutManager);

        rvGroupData = (RecyclerView) findViewById(R.id.rvGroupData);
        rvGroupData.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGroupData.setLayoutManager(mLayoutManager1);

        rvGraph = (RecyclerView) findViewById(R.id.rvGraph);
        rvGraph.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGraph.setLayoutManager(mLayoutManager2);

        tabsLayout = (LinearLayout) findViewById(R.id.tabs_layout);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        lldefaultSpends.setOnClickListener(this);
        textYesterday.setOnClickListener(this);
        textLastSevenDays.setOnClickListener(this);
        textLastThirtyDays.setOnClickListener(this);
        textCustom.setOnClickListener(this);
        setupTabLayout();

        userData = session.getUsuarioDetails();

        fromDate = Utils.getSevenDayBeforeDate();
        toDate = Utils.getCurrentDate();
        fromDateToShow = Utils.getDisplaySevenDayBeforeDate();
        toDateToShow = Utils.getDisplayCurrentDate();

        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);

        setList();
        requestDashboardData();
        requestGraphDashboardData();
    }

    private void setList() {
        graphsListing = new ArrayList<Graph>();
        graphList = new ArrayList<Graph>();
        countsList = new ArrayList<Counts>();
        campaignList = new ArrayList<CampaignData>();
        keywordList = new ArrayList<Keywords>();
        adsList = new ArrayList<Ads>();
    }

    private void requestDashboardData() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestDataCampaign requestDataCampaign = new RequestDataCampaign();
        requestDataCampaign.setAccess_token(userData.get(Session.KEY_ACCESS_TOKEN));
        requestDataCampaign.setpId("1");
        requestDataCampaign.setcId(userData.get(Session.KEY_AGENCY_CLIENT_ID));
        requestDataCampaign.setfDate(fromDate);
        requestDataCampaign.settDate(toDate);
        requestDataCampaign.setLimit("5");
        requestDataCampaign.setOrder("DESC");
        requestDataCampaign.setSort("clicks");

        campaignCall = apiAddClientService.getDashboardData(requestDataCampaign);
        campaignCall.enqueue(new Callback<ResponseDataCampaign>() {
            @Override
            public void onResponse(Call<ResponseDataCampaign> call, Response<ResponseDataCampaign> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getError() == 0) {
                            Log.d(TAG, response.body().toString());
                            tabsLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            if (response.body().getCountsList() != null) {
                                if (response.body().getCountsList().size() > 0) {
                                    countsList = response.body().getCountsList();
                                }
                            }
                            setCountListAdapter();

                            if (response.body().getCamapign_data() != null) {
                                if (response.body().getCamapign_data().size() > 0) {
                                    campaignList = response.body().getCamapign_data();
                                }
                            }

                            if (response.body().getKeyword_data() != null) {
                                if (response.body().getKeyword_data().size() > 0) {
                                    keywordList = response.body().getKeyword_data();
                                }
                            }

                            if (response.body().getAds_data() != null) {
                                if (response.body().getAds_data().size() > 0) {
                                    adsList = response.body().getAds_data();
                                }
                            }

                            setCampaignFragmentData();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            AlertDialog builder = new showErrorDialog(DataActivity.this, getResources().getString(R.string.instabilidade_servidor));
                            builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            builder.setCanceledOnTouchOutside(false);
                            builder.setCancelable(false);
                            builder.show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDataCampaign> call, Throwable t) {
                Log.e(TAG, t.toString());
                if (!DataActivity.this.isFinishing()) {
                    AlertDialog builder = new showErrorDialog(DataActivity.this, getResources().getString(R.string.instabilidade_servidor));
                    builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    builder.setCanceledOnTouchOutside(false);
                    builder.setCancelable(false);
                    builder.show();
                }
            }
        });
    }

    private void requestGraphDashboardData() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestDataGraphCampaign requestDataGraphCampaign = new RequestDataGraphCampaign();
        requestDataGraphCampaign.setAccess_token(userData.get(Session.KEY_ACCESS_TOKEN));
        requestDataGraphCampaign.setpId("1");
        requestDataGraphCampaign.setcId(userData.get(Session.KEY_AGENCY_CLIENT_ID));
        requestDataGraphCampaign.setfDate(fromDate);
        requestDataGraphCampaign.settDate(toDate);
        requestDataGraphCampaign.setLimit("5");

        campaignGraphCall = apiAddClientService.getGraphDashboardData(requestDataGraphCampaign);
        campaignGraphCall.enqueue(new Callback<ResponseDataGraphCampaign>() {
            @Override
            public void onResponse(Call<ResponseDataGraphCampaign> call, Response<ResponseDataGraphCampaign> response) {
                if (response.isSuccessful()) {
                    if (response != null) {
                        Log.e(TAG, response.body().getMessage());
                        if (response.body().getError() == 0) {
                            if (response.body().getGraphList() != null) {
                                if (response.body().getGraphList().size() > 0) {
                                    graphList = response.body().getGraphList();
                                    for (int i = 0; i < graphList.size(); i++) {
                                        Graph graphData = graphList.get(i);
                                        if (i == 0) {
                                            graphsListing.add(new Graph(graphData.getKey(), graphData.getGraphViewList(), graphData.getTotal(), true));
                                        } else {
                                            graphsListing.add(new Graph(graphData.getKey(), graphData.getGraphViewList(), graphData.getTotal(), false));
                                        }
                                    }
                                }
                            }

                            setGraphHeaderAdapter();
                            setGraphListAdapter();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            if (!DataActivity.this.isFinishing()) {
                                AlertDialog builder = new showErrorDialog(DataActivity.this, response.body().getMessage());
                                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                builder.setCanceledOnTouchOutside(false);
                                builder.setCancelable(false);
                                builder.show();
                            }

                        }
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    if (!DataActivity.this.isFinishing()) {
                        AlertDialog builder = new showErrorDialog(DataActivity.this, getResources().getString(R.string.instabilidade_servidor));
                        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        builder.setCanceledOnTouchOutside(false);
                        builder.setCancelable(false);
                        builder.show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseDataGraphCampaign> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                progressBar.setVisibility(View.GONE);
                if (!DataActivity.this.isFinishing()) {
                    AlertDialog builder = new showErrorDialog(DataActivity.this, getResources().getString(R.string.instabilidade_servidor));
                    builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    builder.setCanceledOnTouchOutside(false);
                    builder.setCancelable(false);
                    builder.show();
                }

            }
        });
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("CAMPAIGN"), true);
        tabLayout.addTab(tabLayout.newTab().setText("KEYWORDS"));
        tabLayout.addTab(tabLayout.newTab().setText("ADS"));
        tabLayout.addOnTabSelectedListener(this);
        setCurrentTabFragment(0);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tab.select();
        setCurrentTabFragment(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    private void setCurrentTabFragment(int position) {
        switch (position) {
            case 0:
                setCampaignFragmentData();
                break;
            case 1:
                setKeywordFragmentData();
                break;
            case 2:
                setAdsFragmentData();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lldefaultSpends:
                customDatePopup.showAsDropDown(lldefaultSpends, -5, -5);
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
                AlertDialog builder = new ShowDateRangeDialog(DataActivity.this, getResources().getString(R.string.instabilidade_servidor));
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
        textSelectedDateRange.setText(Utils.getDisplayYesterdayDate());
        customDatePopup.dismiss();
        headerLayout.setVisibility(View.GONE);
        rvGraph.setVisibility(View.GONE);
        tabsLayout.setVisibility(View.GONE);
        rvGroupData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        setList();
        requestDashboardData();
        requestGraphDashboardData();
    }

    private void setCustomDay(){
        if (!cd.isConnectedToInternet()) return;
        headerLayout.setVisibility(View.GONE);
        rvGraph.setVisibility(View.GONE);
        tabsLayout.setVisibility(View.GONE);
        rvGroupData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);
        setList();
        requestDashboardData();
        requestGraphDashboardData();
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
        textSelectedDateRange.setText(Utils.getDisplaySevenDayBeforeDate() + " - " + Utils.getDisplayCurrentDate());
        customDatePopup.dismiss();

        headerLayout.setVisibility(View.GONE);
        rvGraph.setVisibility(View.GONE);
        tabsLayout.setVisibility(View.GONE);
        rvGroupData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        setList();
        requestDashboardData();
        requestGraphDashboardData();
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
        textSelectedDateRange.setText(Utils.getDisplayThirtyDayBeforeDate() + " - " + Utils.getDisplayCurrentDate());
        customDatePopup.dismiss();

        headerLayout.setVisibility(View.GONE);
        rvGraph.setVisibility(View.GONE);
        tabsLayout.setVisibility(View.GONE);
        rvGroupData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        setList();
        requestDashboardData();
        requestGraphDashboardData();
    }


    @Override
    protected void onDestroy() {
        if (campaignCall != null) {
            campaignCall.cancel();
        }

        if (campaignGraphCall != null) {
            campaignGraphCall.cancel();
        }
        super.onDestroy();
    }

    private void setGraphHeaderAdapter() {
        if (graphsListing != null) {
            if (graphsListing.size() > 0) {
                headerLayout.setVisibility(View.VISIBLE);
                contentAdapter = new DataActivityContentAdapter(DataActivity.this, graphsListing);
                rvHeaderData.setAdapter(contentAdapter);
                contentAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            } else {
                headerLayout.setVisibility(View.GONE);
            }
        } else {
            headerLayout.setVisibility(View.GONE);
        }
    }

    private void setGraphListAdapter() {
        if (graphList != null) {
            if (graphList.size() > 0) {
                rvGraph.setVisibility(View.VISIBLE);
                graphAdapter = new DataActivityGraphAdapter(DataActivity.this, graphList);
                rvGraph.setAdapter(graphAdapter);
                graphAdapter.notifyDataSetChanged();
            } else {
                rvGraph.setVisibility(View.GONE);
            }
        } else {
            rvGraph.setVisibility(View.GONE);
        }
    }

    private void setCountListAdapter() {
        if (countsList != null) {
            if (countsList.size() > 0) {
                rvGroupData.setVisibility(View.VISIBLE);
                mAdapter = new DataActivityGroupAdapter(DataActivity.this, countsList);
                rvGroupData.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            } else {
                rvGroupData.setVisibility(View.GONE);
            }
        } else {
            rvGroupData.setVisibility(View.GONE);
        }
    }

    private void setCampaignFragmentData() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("campaignList", campaignList);
        DataCampaignFragment campaignFragment = new DataCampaignFragment();
        campaignFragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, campaignFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commitAllowingStateLoss();
    }

    private void setKeywordFragmentData() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("keywordList", keywordList);
        DataKeywordFragment keywordFragment = new DataKeywordFragment();
        keywordFragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, keywordFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commitAllowingStateLoss();
    }

    private void setAdsFragmentData() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("adsList", adsList);
        DataAdsFragment adsFragment = new DataAdsFragment();
        adsFragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, adsFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commitAllowingStateLoss();
    }

    private class showErrorDialog extends AlertDialog {
        protected showErrorDialog(Context context, String message) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogLayout = inflater.inflate(R.layout.custom_alert_layout_single, (ViewGroup) getCurrentFocus());
            setView(dialogLayout);

            final TextView textViewTitle = (TextView) dialogLayout.findViewById(R.id.textview_title);
            textViewTitle.setText(getResources().getString(R.string.app_name));
            final TextView textViewMessage = (TextView) dialogLayout.findViewById(R.id.textview_text);
            textViewMessage.setText(message);
            final TextView textviewPositive = (TextView) dialogLayout.findViewById(R.id.textview_positive);
            textviewPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
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
                    datePickerDialog = new DatePickerDialog(DataActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
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
                    datePickerDialog = new DatePickerDialog(DataActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
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


                    },2017, 05, 15);

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
                    if( fromDay != null && toDay != null && fromDisplay != null && toDisplay!= null  ){
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
