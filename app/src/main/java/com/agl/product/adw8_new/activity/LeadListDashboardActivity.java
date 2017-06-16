package com.agl.product.adw8_new.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.adapter.AdapterLeads;
import com.agl.product.adw8_new.adapter.AdapterSpinnerDateSelector;
import com.agl.product.adw8_new.custom_view.SwipeRefreshLayoutBottom;
import com.agl.product.adw8_new.database.IAdv8Database;
import com.agl.product.adw8_new.fragment.CustomDateSelectorDialog;
import com.agl.product.adw8_new.fragment.FragmentAppHome;
import com.agl.product.adw8_new.model.LmsLead;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Get;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsListing;
import com.agl.product.adw8_new.utils.OnCustomDateDialogClick;
import com.agl.product.adw8_new.utils.RecyclerTouchListener;
import com.agl.product.adw8_new.utils.Session;
import com.agl.product.adw8_new.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadListDashboardActivity extends ActivityBase implements SwipeRefreshLayoutBottom.OnRefreshListener,
        AdapterView.OnItemSelectedListener, OnCustomDateDialogClick, SearchView.OnQueryTextListener {

    private static final String TAG = "LeadsListPage :: ";
    private ProgressBar progressLeads;
    private RelativeLayout rlMainLayout, rlDefaultLayout;
    private SwipeRefreshLayoutBottom swipeRefreshLayoutBottom;
    private RecyclerView rvLeads;
    private AdapterLeads adapterLeads;
    private Spinner spinnerDateSelector;
    private String arrDateSelector[];
    private AdapterSpinnerDateSelector adapterSpinnerDateSelector;
    private String currentToDate = "";
    private String currentFromDate = "";
    private String dataType = "";
    private CustomDateSelectorDialog customDateSelectorDialog;
    private FragmentManager fragmentManager;
    private int OFFSET_PAGE = 0;
    private boolean ifMoreData = true;
    Intent intent;
    String WEB_FORM_ID=null;
    private String dateType = Utils.DATE_TYPE;
    private String statusId="";
    IAdv8Database database = new IAdv8Database(this,"Iadv8.db",null,1);
    String campaignIds="";
    String landingPageIds="";
    String statusIds="";
    String ownerIds="";
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

        statusId=getIntent().getStringExtra(Utils.ID_TYPE_STATUS);
        currentFromDate = getIntent().getStringExtra(Utils.CURRENT__FROM_DATE);
        currentToDate = getIntent().getStringExtra(Utils.CURRENT_TO_DATE);
        dateType = getIntent().getStringExtra(Utils.DATE_TYPE);
        Resources res = getResources();
        intent=getIntent();
        WEB_FORM_ID=intent.getStringExtra(Utils.WEB_FORM_ID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Lead Overview");
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
        progressLeads = (ProgressBar) findViewById(R.id.progress_leads);
        rlMainLayout = (RelativeLayout) findViewById(R.id.rl_leads_main_layout);
        rlDefaultLayout = (RelativeLayout) findViewById(R.id.rl_leads_default_layout);
        adapterLeads = new AdapterLeads(LeadListDashboardActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LeadListDashboardActivity.this);
        rvLeads = (RecyclerView) findViewById(R.id.rv_leads);
        rvLeads.setAdapter(adapterLeads);
        rvLeads.setLayoutManager(linearLayoutManager);

        arrDateSelector = getResources().getStringArray(R.array.spinner_date_selector);
        spinnerDateSelector = (Spinner) findViewById(R.id.spinner_datepicker);
        adapterSpinnerDateSelector = new AdapterSpinnerDateSelector(LeadListDashboardActivity.this, getSupportFragmentManager(),
                R.layout.spinner_text_item, arrDateSelector, arrDateSelector.length - 1, spinnerDateSelector);
        adapterSpinnerDateSelector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDateSelector.setAdapter(adapterSpinnerDateSelector);
        spinnerDateSelector.setOnItemSelectedListener(LeadListDashboardActivity.this);

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
//                    Intent intentLeadDetails = new Intent(LeadListDashboardActivity.this, LeadDetailsActivity.class);
//                    intentLeadDetails.putExtra(Utils.LEAD_ID, leadId);
//                    startActivity(intentLeadDetails);
                } else {
                    Log.e(TAG, "Lead id is null");
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                // TODO Auto-generated method stub
            }
        }));

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
            String[] arrCurrentDate=currentFromDate.split("-");
            String strMonth=arrCurrentDate[1];
            String strMonthLocal=getMonthShortName(Integer.valueOf(strMonth)-1);
            String strCurrentDateValue=arrCurrentDate[2];
            String[] arrCurrentDate1=currentToDate.split("-");
            String strMonth1=arrCurrentDate1[1];
            // Util.showToast(getApplicationContext(),strMonth1);
            String strMonthLocal1=getMonthShortName(Integer.valueOf(strMonth1)-1);
            String strCurrentDateValue1=arrCurrentDate1[2];
            // arrDateSelector[arrDateSelector.length - 1] = String.valueOf("Custom : ");
            arrDateSelector[arrDateSelector.length - 1] = String.valueOf("Custom : "+strMonthLocal+" - "+strCurrentDateValue + "  -  " + strMonthLocal1+" - "+strCurrentDateValue1);
            spinnerDateSelector.setSelection(arrDateSelector.length - 1);
            getLeadsData(currentToDate, currentFromDate, false,"");
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
        getLeadsData(currentToDate, currentFromDate, false, newText);
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
                                }
                            }

                            setMainLayout();
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
            getMoreLeadsData(currentToDate, currentFromDate, true);
        } else {
            swipeRefreshLayoutBottom.setRefreshing(false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("Id " + id + parent.getItemAtPosition(position));
        System.out.println("pos " + position + " arrDateSelector.length-1" + (arrDateSelector.length - 2));
        switch (position) {
            case 0:
                // adapterSpinnerDateSelector.getYesterday();
                OFFSET_PAGE = 0;
                ifMoreData = true;
                showYesterdayDate();
                break;
            case 1:
                // adapterSpinnerDateSelector.getLastWeek();
                showLastWeekData();
                OFFSET_PAGE = 0;
                ifMoreData = true;
                break;
            case 2:
                // adapterSpinnerDateSelector.getLastMonth();
                showLastMonthData();
                OFFSET_PAGE = 0;
                ifMoreData = true;
                break;
            case 3:
                showCustomDateSelectorDialog();
                OFFSET_PAGE = 0;
                ifMoreData = true;
                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    public void showData() {
        String[] arrDates = adapterSpinnerDateSelector.getLastMonth();
        if (!dataType.equalsIgnoreCase(Utils.TYPE_LAST_MONTH)) {
            dataType = Utils.TYPE_LAST_MONTH;
            getLeadsData(currentToDate, currentFromDate, false, "");
        }
    }

    public void showDataWeek() {
        String[] arrDates = adapterSpinnerDateSelector.getLastWeek();
        if (!dataType.equalsIgnoreCase(Utils.TYPE_LAST_WEEK)) {
            dataType = Utils.TYPE_LAST_WEEK;
            getLeadsData(currentToDate, currentFromDate, false, "");
        }
    }

    public void showDataYesterday() {
        String[] arrDates = adapterSpinnerDateSelector.getYesterday();
        if (!dataType.equalsIgnoreCase(Utils.TYPE_YESTERDAY)) {
            dataType = Utils.TYPE_YESTERDAY;
            getLeadsData(currentToDate, currentFromDate, false, "");
        }
    }

    public void showYesterdayDate() {
        String[] arrDates = adapterSpinnerDateSelector.getYesterday();
        currentFromDate = arrDates[0];
        currentToDate = arrDates[1];
        if (!dataType.equalsIgnoreCase(Utils.TYPE_YESTERDAY)) {
            dataType = Utils.TYPE_YESTERDAY;
            getLeadsData(currentToDate, currentFromDate, false,"");
        }
    }

    public void showLastWeekData() {
        String[] arrDates = adapterSpinnerDateSelector.getLastWeek();
        currentFromDate = arrDates[0];
        currentToDate = arrDates[1];
        if (!dataType.equalsIgnoreCase(Utils.TYPE_LAST_WEEK)) {
            dataType = Utils.TYPE_LAST_WEEK;
            getLeadsData(currentToDate, currentFromDate, false,"");
        }
    }

    public void showLastMonthData() {
        String[] arrDates = adapterSpinnerDateSelector.getLastMonth();
        currentFromDate = arrDates[0];
        currentToDate = arrDates[1];
        if (!dataType.equalsIgnoreCase(Utils.TYPE_LAST_MONTH)) {
            dataType = Utils.TYPE_LAST_MONTH;
            getLeadsData(currentToDate, currentFromDate, false,"");
        }
    }

    @Override
    public void onCustomDatePickerSelected(int[] date, Integer mTAG) {
        int day = date[0];
        int month = date[1];
        int year = date[2];
        customDateSelectorDialog.setFromDate(date, mTAG);
    }

    @Override
    public void onCustomDateSelectorSelected(String toDate, String fromDate, String toCmpDate, String fromCmpDate) {
        currentToDate = toDate;
        currentFromDate = fromDate;
        String[] arrCurrentDate=currentFromDate.split("-");
        String strMonth=arrCurrentDate[1];
        String strMonthLocal=getMonthShortName(Integer.valueOf(strMonth)-1);
        String strCurrentDateValue=arrCurrentDate[2];
        String[] arrCurrentDate1=currentToDate.split("-");
        String strMonth1=arrCurrentDate1[1];
        // Util.showToast(getApplicationContext(),strMonth1);
        String strMonthLocal1=getMonthShortName(Integer.valueOf(strMonth1)-1);
        String strCurrentDateValue1=arrCurrentDate1[2];
        arrDateSelector[arrDateSelector.length - 1] = String.valueOf("Custom : "+strMonthLocal+"  "+strCurrentDateValue + "  -  " + strMonthLocal1+"  "+strCurrentDateValue1);
        spinnerDateSelector.setSelection(arrDateSelector.length - 1);
        getLeadsData(currentToDate, currentFromDate, false,"");
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

    public void showCustomDateSelectorDialog() {
        Log.e(TAG, "Show Custom Dialog date selector");
        customDateSelectorDialog = (CustomDateSelectorDialog) fragmentManager.findFragmentByTag(Utils.TAG_DIALOG_DATE_SELECTOR);
        if (customDateSelectorDialog == null) {
            customDateSelectorDialog = CustomDateSelectorDialog.newInstance(false);
        }
        customDateSelectorDialog.show(fragmentManager, Utils.TAG_DIALOG_DATE_SELECTOR);
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
}