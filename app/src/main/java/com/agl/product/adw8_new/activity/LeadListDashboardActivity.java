package com.agl.product.adw8_new.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.adapter.AdapterLeads;
import com.agl.product.adw8_new.custom_view.SwipeRefreshLayoutBottom;
import com.agl.product.adw8_new.database.IAdv8Database;
import com.agl.product.adw8_new.fragment.FragmentAppHome;
import com.agl.product.adw8_new.model.LmsLead;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Get;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsListing;
import com.agl.product.adw8_new.utils.ConnectionDetector;
import com.agl.product.adw8_new.utils.RecyclerTouchListener;
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

public class LeadListDashboardActivity extends ActivityBase implements SwipeRefreshLayoutBottom.OnRefreshListener,
        SearchView.OnQueryTextListener, View.OnClickListener {

    private static final String TAG = "LeadsListPage :: ";
    private ProgressBar progressLeads;
    private RelativeLayout rlMainLayout, rlDefaultLayout;
    private SwipeRefreshLayoutBottom swipeRefreshLayoutBottom;
    private RecyclerView rvLeads;
    private AdapterLeads adapterLeads;
    private TextView textYesterday, textLastSevenDays, textLastThirtyDays, textCustom, textSelectedDateRange;
    private LinearLayout ll_leadsOverview;
    private ConnectionDetector cd;
    private DatePickerDialog datePickerDialog;
    private PopupWindow customDatePopup;
    private View customPopupLayout;
    private String dataType = "";
    private FragmentManager fragmentManager;
    private int OFFSET_PAGE = 0;
    private boolean ifMoreData = true;
    Intent intent;
    String WEB_FORM_ID=null;
    private String dateType = Utils.DATE_TYPE;
    IAdv8Database database = new IAdv8Database(this,"Iadv8.db",null,1);
    String campaignIds="";
    String landingPageIds="";
    String statusIds="";
    String ownerIds="", fromDate, toDate, fromDateToShow, toDateToShow;
    private String currentSearchString = "";
    Cursor curCampaign,curLanding,curOwners,curStatus;
    SearchView searchView;
    Session session;
    private ArrayList<LmsLead> leadsListing;
    HashMap<String, String> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leads_list_page);
        session = new Session(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        cd = new ConnectionDetector(this);
        statusIds = getIntent().getStringExtra(Utils.ID_TYPE_STATUS);
        fromDate = getIntent().getStringExtra(Utils.CURRENT_FROM_DATE);
        toDate = getIntent().getStringExtra(Utils.CURRENT_TO_DATE);
        fromDateToShow = getIntent().getStringExtra(Utils.CURRENT_FROM_DATE_TO_SHOW);
        toDateToShow = getIntent().getStringExtra(Utils.CURRENT_FROM_DATE_TO_SHOW);
        dateType = getIntent().getStringExtra(Utils.DATE_TYPE);

        Resources res = getResources();
        WEB_FORM_ID = getIntent().getStringExtra(Utils.WEB_FORM_ID);

        fragmentManager = getSupportFragmentManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        userData = session.getUsuarioDetails();
        leadsListing = new ArrayList<LmsLead>();
        swipeRefreshLayoutBottom = (SwipeRefreshLayoutBottom) findViewById(R.id.swipe_leads_list);
        swipeRefreshLayoutBottom.setOnRefreshListener(this);
        ll_leadsOverview = (LinearLayout) findViewById(R.id.ll_leadsOverview);
        ll_leadsOverview.setOnClickListener(this);
        textSelectedDateRange = (TextView) findViewById(R.id.textSelectedDateRange);
        progressLeads = (ProgressBar) findViewById(R.id.progress_leads);
        rlMainLayout = (RelativeLayout) findViewById(R.id.rl_leads_main_layout);
        rlDefaultLayout = (RelativeLayout) findViewById(R.id.rl_leads_default_layout);
        adapterLeads = new AdapterLeads(LeadListDashboardActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LeadListDashboardActivity.this);
        rvLeads = (RecyclerView) findViewById(R.id.rv_leads);
        rvLeads.setAdapter(adapterLeads);
        rvLeads.setLayoutManager(linearLayoutManager);

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
        textYesterday.setOnClickListener(this);
        textLastSevenDays.setOnClickListener(this);
        textLastThirtyDays.setOnClickListener(this);
        textCustom.setOnClickListener(this);

        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration((int) res.getDimension(R.dimen.list_item_top_margin), (int) res.getDimension(R.dimen.list_item_bottom_margin), (int) res.getDimension(R.dimen.list_item_left_margin), (int) res.getDimension(R.dimen.list_item_right_margin));
        rvLeads.addItemDecoration(spacesItemDecoration);
        rvLeads.addOnItemTouchListener(new RecyclerTouchListener(LeadListDashboardActivity.this, rvLeads, new FragmentAppHome.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                String leadId = "";
                if (leadsListing != null) {
                    LmsLead lmsLead = leadsListing.get(position);
                    leadId = lmsLead.getLead_id();
                }

                if (!Utils.isEmptyString(leadId)) {
                    Intent intentLeadDetails = new Intent(LeadListDashboardActivity.this, LeadDetailsActivity.class);
                    intentLeadDetails.putExtra(Utils.LEAD_ID, leadId);
                    startActivity(intentLeadDetails);
                } else {
                    Log.e(TAG, "Lead id is null");
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                // TODO Auto-generated method stub
            }
        }));

        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);

        if (dateType.equalsIgnoreCase(Utils.TYPE_LAST_MONTH)) {
            showData();
        }
        if (dateType.equalsIgnoreCase(Utils.TYPE_YESTERDAY)) {
            showDataYesterday();
        }
        if (dateType.equalsIgnoreCase(Utils.TYPE_LAST_WEEK)) {
            showDataWeek();
        }
        if (dateType.equalsIgnoreCase(Utils.TYPE_CUSTOM_DATE)) {
            textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);

            String[] arrCurrentDate = fromDate.split("-");
            String strMonth = arrCurrentDate[1];
            String strMonthLocal = getMonthShortName(Integer.valueOf(strMonth)-1);
            String strCurrentDateValue = arrCurrentDate[2];
            String[] arrCurrentDate1 = toDate.split("-");
            String strMonth1 = arrCurrentDate1[1];
            String strMonthLocal1 = getMonthShortName(Integer.valueOf(strMonth1)-1);
            String strCurrentDateValue1 = arrCurrentDate1[2];
            getLeadsData(toDate, fromDate, false,"");
        }
    }

    public Cursor getCount() {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cur = db.query(IAdv8Database.FilterTable, null, "FilterChecked=?", new String[]{"true"}, null, null, null);
        return cur;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.seo_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        setupSearchView();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_leadsOverview:
                customDatePopup.showAsDropDown(ll_leadsOverview, -5, -5);
                break;
            case R.id.textYesterday:
                showDataYesterday();
                break;
            case R.id.textLastSevenDays:
                showDataWeek();
                break;
            case R.id.textLastThirtyDays:
                showData();
                break;
            case R.id.textCustom:
                customDatePopup.dismiss();
                AlertDialog builder = new ShowDateRangeDialog(LeadListDashboardActivity.this, getResources().getString(R.string.instabilidade_servidor));
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                builder.setCanceledOnTouchOutside(false);
                builder.setCancelable(false);
                builder.show();
                break;
        }
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("Search");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        getLeadsData(toDate, fromDate, false, newText);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int topMargin;
        private int bottomMargin;
        private int leftMargin;
        private int rightMargin;

        public SpacesItemDecoration(int topMargin, int bottomMargin, int leftMargin, int rightMargin) {
            super();
            this.topMargin = topMargin;
            this.bottomMargin = bottomMargin;
            this.leftMargin = leftMargin;
            this.rightMargin = rightMargin;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = leftMargin;
            outRect.right = rightMargin;
            outRect.bottom = bottomMargin;
            if (parent.getChildPosition(view) == 0)
                outRect.top = topMargin;
        }
    }

    private void setProgressLayout() {
        progressLeads.setVisibility(View.VISIBLE);
        rlDefaultLayout.setVisibility(View.GONE);
        rlMainLayout.setVisibility(View.GONE);
    }

    private void setDefaultLayout() {
        progressLeads.setVisibility(View.GONE);
        rlDefaultLayout.setVisibility(View.VISIBLE);
        rlMainLayout.setVisibility(View.GONE);
    }

    private void setMainLayout() {
        progressLeads.setVisibility(View.GONE);
        rlDefaultLayout.setVisibility(View.GONE);
        rlMainLayout.setVisibility(View.VISIBLE);
    }

    private void getLeadsData(String toDate, String fromDate, final boolean isLazyLoading, String searchKeyword) {
        setProgressLayout();
        campaignIds= getCampaignCount().toString();
        landingPageIds=  getLandingCount().toString();
        ownerIds= getOwnerCount().toString();
        Get apiLeadsDataService = ApiClient.getClientEarlier().create(Get.class);
        String url="webforms/show-data-filter1/userEmail/"+userData.get(Session.KEY_EMAIL)+"/password/"
                +userData.get(Session.KEY_PASSWORD)+"/sKeys/1r2a3k4s5h6s7i8n9h10/clientId/"+userData.get(Session.KEY_AGENCY_CLIENT_ID)
                +"/offset/"+OFFSET_PAGE+"/search/"+searchKeyword+"/campaignIds/" +campaignIds+"/landingPageIds/"+landingPageIds+"/statusIds/"
                +statusIds+"/ownerIds/"+ownerIds+"/fromDate/" +fromDate+"/toDate/"+toDate+"";
        Call<ResponseDataLeadsListing> leadsCall = apiLeadsDataService.getLeadsLmsData(url);
        leadsCall.enqueue(new Callback<ResponseDataLeadsListing>() {
            @Override
            public void onResponse(Call<ResponseDataLeadsListing>call, Response<ResponseDataLeadsListing> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getError() == 0) {
                            Log.d(TAG, response.body().toString());

                            if(response.body().getLmsLeadsList()!= null) {
                                if(response.body().getLmsLeadsList().size() > 0) {
                                    leadsListing = response.body().getLmsLeadsList();
                                    adapterLeads.addData(leadsListing, isLazyLoading);

                                    setMainLayout();
                                } else {
                                    setDefaultLayout();
                                }
                            } else {
                                setDefaultLayout();
                            }
                        } else {
                            setDefaultLayout();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDataLeadsListing>call, Throwable t) {
                Log.e(TAG, t.toString());
                if(! LeadListDashboardActivity.this.isFinishing()) {
                    setDefaultLayout();
                    AlertDialog builder = new showErrorDialog(LeadListDashboardActivity.this, getResources().getString(R.string.instabilidade_servidor));
                    builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    builder.setCanceledOnTouchOutside(false);
                    builder.setCancelable(false);
                    builder.show();
                }
            }
        });
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

    private void getMoreLeadsData(String toDate, String fromDate, final boolean isLazyLoading) {
        campaignIds= getCampaignCount().toString();
        landingPageIds=  getLandingCount().toString();
        ownerIds= getOwnerCount().toString();
        Get apiLeadsDataService = ApiClient.getClientEarlier().create(Get.class);
        String url="webforms/show-data-filter1/userEmail/"+userData.get(Session.KEY_EMAIL)+"/password/"
                +userData.get(Session.KEY_PASSWORD)+"/sKeys/1r2a3k4s5h6s7i8n9h10/clientId/"+userData.get(Session.KEY_AGENCY_CLIENT_ID)
                +"/offset/"+OFFSET_PAGE+"/search/"+currentSearchString+"/campaignIds/"+campaignIds+"/landingPageIds/"+landingPageIds+
                "/statusIds/"+statusIds+"/ownerIds/"+ownerIds+"/fromDate/"+fromDate+"/toDate/"+toDate+"";
        Call<ResponseDataLeadsListing> moreLeadsCall = apiLeadsDataService.getLeadsLmsData(url);
        moreLeadsCall.enqueue(new Callback<ResponseDataLeadsListing>() {
            @Override
            public void onResponse(Call<ResponseDataLeadsListing>call, Response<ResponseDataLeadsListing> response) {
                swipeRefreshLayoutBottom.setRefreshing(false);
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getError() == 0) {
                            Log.d(TAG, response.body().toString());

                            if(response.body().getLmsLeadsList()!= null) {
                                if(response.body().getLmsLeadsList().size() > 0) {
                                    leadsListing.addAll(response.body().getLmsLeadsList());
                                    adapterLeads.addData(leadsListing, isLazyLoading);
                                }
                            } else {
                                ifMoreData = false;
                            }
                        } else {
                            ifMoreData = false;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDataLeadsListing>call, Throwable t) {
                Log.e(TAG, t.toString());
                if(! LeadListDashboardActivity.this.isFinishing()) {
                    swipeRefreshLayoutBottom.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (ifMoreData) {
            OFFSET_PAGE++;
            getMoreLeadsData(toDate, fromDate, true);
        } else {
            swipeRefreshLayoutBottom.setRefreshing(false);
        }
    }

    public void showData() {
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

        if (!dataType.equalsIgnoreCase(Utils.TYPE_LAST_MONTH)) {
            dataType = Utils.TYPE_LAST_MONTH;
            getLeadsData(toDate, fromDate, false, "");
        }
    }

    public void showDataWeek() {
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

        if (!dataType.equalsIgnoreCase(Utils.TYPE_LAST_WEEK)) {
            dataType = Utils.TYPE_LAST_WEEK;
            getLeadsData(toDate, fromDate, false, "");
        }
    }

    public void showDataYesterday() {
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

        if (!dataType.equalsIgnoreCase(Utils.TYPE_YESTERDAY)) {
            dataType = Utils.TYPE_YESTERDAY;
            getLeadsData(toDate, fromDate, false, "");
        }
    }

    //month*************************
    public static String getMonthShortName(int monthNumber) {
        String monthName = "Jan";
        if (monthNumber >= 1 && monthNumber < 12)
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, monthNumber);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
                simpleDateFormat.setCalendar(calendar);
                monthName = simpleDateFormat.format(calendar.getTime());
            } catch (Exception e) {
                if (e != null)
                    e.printStackTrace();
            }
        return monthName;
    }

    public void onResume() {
        super.onResume();
        Cursor cur=getCount();
        if(cur.getCount()>0) {
//            btnFilterCounter.setVisibility(View.VISIBLE);
//            btnFilterCounter.setText(""+cur.getCount());
        } else {
//            btnFilterCounter.setVisibility(View.INVISIBLE);
        }
    }

    public StringBuffer getCampaignCount() {
        SQLiteDatabase db = database.getReadableDatabase();
        curCampaign = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Utils.ID_TYPE_CAMPAIGN_UNITS,"true"}, null, null, null);
        StringBuffer paramCampaignUnitIds = new StringBuffer();
        while (curCampaign.moveToNext()) {
            paramCampaignUnitIds.append(curCampaign.getString(4)+",");
        }
        return  paramCampaignUnitIds;
    }

    public StringBuffer getLandingCount() {
        //Landing********************
        SQLiteDatabase db = database.getReadableDatabase();
        curLanding = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Utils.ID_TYPE_LANDING_PAGES,"true"}, null, null, null);
        StringBuffer paramLanding = new StringBuffer();
        while (curLanding.moveToNext()) {
            paramLanding.append(curLanding.getString(4)+",");
        }
        return paramLanding;
    }

    public StringBuffer getStausCount() {
        SQLiteDatabase db = database.getReadableDatabase();
        curStatus = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Utils.ID_TYPE_STATUS,"true"}, null, null, null);
        StringBuffer paramStaus = new StringBuffer();
        while (curStatus.moveToNext()) {
            paramStaus.append(curStatus.getString(4)+",");
        }
        return paramStaus;
    }

    public  StringBuffer getOwnerCount() {
        SQLiteDatabase db = database.getReadableDatabase();
        curOwners = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Utils.ID_TYPE_OWNERS,"true"}, null, null, null);
        StringBuffer paramOwners = new StringBuffer();
        while (curOwners.moveToNext()) {
            paramOwners.append(curOwners.getString(4)+",");
        }
        return paramOwners;
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
                    datePickerDialog = new DatePickerDialog(LeadListDashboardActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
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
                    datePickerDialog = new DatePickerDialog(LeadListDashboardActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
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

                    textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);
                    String[] arrCurrentDate = fromDate.split("-");
                    String strMonth = arrCurrentDate[1];
                    String strMonthLocal = getMonthShortName(Integer.valueOf(strMonth)-1);
                    String strCurrentDateValue = arrCurrentDate[2];
                    String[] arrCurrentDate1 = toDate.split("-");
                    String strMonth1 = arrCurrentDate1[1];
                    String strMonthLocal1 = getMonthShortName(Integer.valueOf(strMonth1)-1);
                    String strCurrentDateValue1 = arrCurrentDate1[2];
                    getLeadsData(toDate, fromDate, false,"");
                    dismiss();
                }
            });
        }
    }
}