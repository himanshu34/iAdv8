package com.agl.product.adw8_new.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.adapter.AdapterSpinnerDateSelector;
import com.agl.product.adw8_new.database.IAdv8Database;
import com.agl.product.adw8_new.fragment.CustomDateSelectorDialog;
import com.agl.product.adw8_new.model.Leads;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Get;
import com.agl.product.adw8_new.service.Post;
import com.agl.product.adw8_new.service.data.ResponseDataLeads;
import com.agl.product.adw8_new.utils.ColorGenerator;
import com.agl.product.adw8_new.utils.OnCustomDateDialogClick;
import com.agl.product.adw8_new.utils.Session;
import com.agl.product.adw8_new.utils.TextDrawable;
import com.agl.product.adw8_new.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadDashBoardActivity extends ActivityBase implements OnCustomDateDialogClick {

    public String TAG = "LeadsDashboardActivity";
    Session session;
    HashMap<String, String> userData;
    ListView lvDashboardNew;
    private ProgressBar progressBar;
    private TextView noDataTextView;
    private Spinner spinnerDateSelector;
    private String arrDateSelector[];
    private AdapterSpinnerDateSelector adapterSpinnerDateSelector;
    ArrayList<Leads> leadsArrayList;
    private String currentToDate = "", currentFromDate = "";
    IAdv8Database database = new IAdv8Database(this, "Iadv8.db", null, 1);
    private String dataType = Utils.TYPE_LAST_MONTH;
    String campaignIds="", landingPageIds="", statusIds="", ownerIds="";
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    private TextDrawable.IBuilder mDrawableBuilderRectangle;
    private CustomDateSelectorDialog customDateSelectorDialog;
    private FragmentManager fragmentManager;

//    HorizontalListView hlLeadDashboardNew;
//    MyAppliction globalVariable;
//    private Button buttonTotal,btnFilterCounter;
//    IAdv8Database database = new IAdv8Database(this,"Iadv8.db",null,1);
//    ArrayList<String> aarHor;
//    ArrayList<String> arrHoriFlag;
//    Cursor curCampaign,curLanding,curOwners,curStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leads_dashboard);
        session = new Session(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Leads Dashboard");
        fragmentManager = getSupportFragmentManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        userData = session.getUsuarioDetails();
        initViews();
        initData();
        setListeners();
    }

    private void initViews() {
        lvDashboardNew = (ListView)findViewById(R.id.lv_lead_dashboard_new);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        noDataTextView = (TextView) findViewById(R.id.txt_no_data);
        arrDateSelector = getResources().getStringArray(R.array.spinner_date_selector);
        spinnerDateSelector = (Spinner) findViewById(R.id.spinner_datepicker);

//        Cursor cur1=getCount();
//        if(cur1.getCount()>0) {
//            arrHoriFlag=new ArrayList<String>();
//            aarHor=new ArrayList<String>();
//            while (cur1.moveToNext()) {
//                int id = cur1.getInt(0);
//                String FilterName = cur1.getString(1);
//                aarHor.add(Integer.toString(id));
//                arrHoriFlag.add(FilterName);
//                btnFilterCounter.setVisibility(View.VISIBLE);
//                hlLeadDashboardNew.setVisibility(View.VISIBLE);
//                filterValues adapter = new filterValues(this, arrHoriFlag, aarHor);
//                hlLeadDashboardNew.setAdapter(adapter);
//                btnFilterCounter.setText(""+arrHoriFlag.size());
//                adapter.notifyDataSetChanged();
//            }
//        }
    }

    private void initData() {
        leadsArrayList = new ArrayList<Leads>();
        mDrawableBuilder = TextDrawable.builder(Color.WHITE).beginConfig().withBorder(4).endConfig().round();
        mDrawableBuilderRectangle = TextDrawable.builder(Color.WHITE).beginConfig().withBorder(4).endConfig().roundRect(10);
        adapterSpinnerDateSelector = new AdapterSpinnerDateSelector(LeadDashBoardActivity.this, getSupportFragmentManager(),
                R.layout.spinner_text_item, arrDateSelector, arrDateSelector.length - 1, spinnerDateSelector);
        adapterSpinnerDateSelector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDateSelector.setAdapter(adapterSpinnerDateSelector);
        spinnerDateSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Id " + id + parent.getItemAtPosition(position));
                System.out.println("pos " + position + " arrDateSelector.length-1"
                        + (arrDateSelector.length - 2));
                switch (position) {
                    case 0:
                        showYesterdayDate();
                        break;
                    case 1:
                        showLastWeekData();
                        break;
                    case 2:
                        showLastMonthData();
                        break;
                    case 3:
                        showCustomDateSelectorDialog();
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        campaignIds= getCampaignCount().toString();
        landingPageIds=  getLandingCount().toString();
        ownerIds= getOwnerCount().toString();
        showLastWeekData();
    }

    private void setListeners() {
        lvDashboardNew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Leads leadsData = leadsArrayList.get(position);
                startActivity(new Intent(LeadDashBoardActivity.this, LeadListDashboardActivity.class)
                        .putExtra(Utils.CURRENT__FROM_DATE, currentFromDate)
                        .putExtra(Utils.CURRENT_TO_DATE, currentToDate)
                        .putExtra(Utils.DATE_TYPE, dataType)
                        .putExtra(Utils.ID_TYPE_STATUS, leadsData.getStatus_id()+","));
            }
        });
    }

    public void showYesterdayDate() {
        String[] arrDates = adapterSpinnerDateSelector.getYesterday();
        currentFromDate = arrDates[0];
        currentToDate = arrDates[1];
        if (!dataType.equalsIgnoreCase(Utils.TYPE_YESTERDAY)) {
            dataType = Utils.TYPE_YESTERDAY;
            getLeadsFilterData(currentFromDate, currentToDate, campaignIds, landingPageIds, statusIds, ownerIds);
        }
    }

    public void showLastWeekData() {
        String[] arrDates = adapterSpinnerDateSelector.getLastWeek();
        currentFromDate = arrDates[0];
        currentToDate = arrDates[1];
        if (!dataType.equalsIgnoreCase(Utils.TYPE_LAST_WEEK)) {
            dataType = Utils.TYPE_LAST_WEEK;
            getLeadsFilterData(currentFromDate, currentToDate, campaignIds, landingPageIds, statusIds, ownerIds);
        } else{
            getLeadsFilterData(currentFromDate, currentToDate, campaignIds, landingPageIds, statusIds, ownerIds);
        }
    }

    public void showLastMonthData() {
        String[] arrDates = adapterSpinnerDateSelector.getLastMonth();
        currentFromDate = arrDates[0];
        currentToDate = arrDates[1];
        if (!dataType.equalsIgnoreCase(Utils.TYPE_LAST_MONTH)) {
            dataType = Utils.TYPE_LAST_MONTH;
            getLeadsFilterData(currentFromDate, currentToDate, campaignIds, landingPageIds, statusIds, ownerIds);
        }
    }

    public void showCustomDateSelectorDialog() {
        customDateSelectorDialog = (CustomDateSelectorDialog) fragmentManager.findFragmentByTag(Utils.TAG_DIALOG_DATE_SELECTOR);
        if (customDateSelectorDialog == null) {
            customDateSelectorDialog = CustomDateSelectorDialog.newInstance(false);
        }
        customDateSelectorDialog.show(fragmentManager, Utils.TAG_DIALOG_DATE_SELECTOR);
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
        String[] arrCurrentDate = currentFromDate.split("-");
        String strMonth = arrCurrentDate[1];
        String strMonthLocal = getMonthShortName(Integer.valueOf(strMonth)-1);
        String strCurrentDateValue = arrCurrentDate[2];
        String[] arrCurrentDate1 = currentToDate.split("-");
        String strMonth1 = arrCurrentDate1[1];
        String strMonthLocal1 = getMonthShortName(Integer.valueOf(strMonth1)-1);
        String strCurrentDateValue1 = arrCurrentDate1[2];
        arrDateSelector[arrDateSelector.length - 1] = String.valueOf("Custom : "+strMonthLocal+"  "+strCurrentDateValue + "  -  " + strMonthLocal1+"  "+strCurrentDateValue1);
        spinnerDateSelector.setSelection(arrDateSelector.length - 1);

        if (!dataType.equalsIgnoreCase(Utils.TYPE_CUSTOM_DATE)) {
            dataType = Utils.TYPE_CUSTOM_DATE;
            getLeadsFilterData(currentFromDate, currentToDate,campaignIds,landingPageIds,statusIds,ownerIds);
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

    public StringBuffer getCampaignCount() {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor curCampaign = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Utils.ID_TYPE_CAMPAIGN_UNITS,"true"}, null, null, null);
        StringBuffer paramCampaignUnitIds = new StringBuffer();
        while (curCampaign.moveToNext()) {
            paramCampaignUnitIds.append(curCampaign.getString(4)+",");
        }

        return  paramCampaignUnitIds;
    }

    public StringBuffer getLandingCount() {
        //Landing********************
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor curLanding = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Utils.ID_TYPE_LANDING_PAGES,"true"}, null, null, null);
        StringBuffer paramLanding = new StringBuffer();
        while (curLanding.moveToNext()) {
            paramLanding.append(curLanding.getString(4)+",");
        }

        return paramLanding;
    }

    public StringBuffer getStatusCount() {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor curStatus = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Utils.ID_TYPE_STATUS,"true"}, null, null, null);
        StringBuffer paramStaus = new StringBuffer();
        while (curStatus.moveToNext()) {
            paramStaus.append(curStatus.getString(4)+",");
        }
        return paramStaus;
    }

    public  StringBuffer getOwnerCount() {
        //Owners
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor curOwners = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Utils.ID_TYPE_OWNERS,"true"}, null, null, null);
        StringBuffer paramOwners = new StringBuffer();
        while (curOwners.moveToNext()) {
            paramOwners.append(curOwners.getString(4)+",");
        }
        return paramOwners;
    }

    private void setProgressLayout() {
        progressBar.setVisibility(View.VISIBLE);
        noDataTextView.setVisibility(View.GONE);
        lvDashboardNew.setVisibility(View.GONE);
    }

    private void setDefaultLayout() {
        progressBar.setVisibility(View.GONE);
        noDataTextView.setVisibility(View.VISIBLE);
        lvDashboardNew.setVisibility(View.GONE);
    }

    private void setMainLayout() {
        progressBar.setVisibility(View.GONE);
        noDataTextView.setVisibility(View.GONE);
        lvDashboardNew.setVisibility(View.VISIBLE);

        if(leadsArrayList != null) {
            if(leadsArrayList.size() > 0) {
                lvDashboardNew.setAdapter(new LeadAdapter(LeadDashBoardActivity.this, leadsArrayList));
            }
        }
    }

    private void getLeadsFilterData(String fromDate, String toDate, String campaignIds, String landingPageIds, String statusIds, String ownerIds) {
        setProgressLayout();
        Get apiLeadsService = ApiClient.getClientEarlier().create(Get.class);
        String url="webforms/lead-matrics/userEmail/"+userData.get(Session.KEY_EMAIL)+"/password/"+userData.get(Session.KEY_PASSWORD)
                +"/sKeys/1r2a3k4s5h6s7i8n9h10/clientId/"+userData.get(Session.KEY_AGENCY_CLIENT_ID)+"/groupBy/status/fromDate/"+fromDate+
                "/toDate/"+toDate+"/campaignIds/"+campaignIds+"/landingPageIds/"+landingPageIds+"/statusIds/"+statusIds+"/ownerIds/"
                +ownerIds;
        Call<ResponseDataLeads> leadsCall = apiLeadsService.getLeadsData(url);
        leadsCall.enqueue(new Callback<ResponseDataLeads>() {
            @Override
            public void onResponse(Call<ResponseDataLeads>call, Response<ResponseDataLeads> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getError() == 0) {
                            Log.d(TAG, response.body().toString());

                            if(response.body().getLeadsList()!= null) {
                                if(response.body().getLeadsList().size() > 0) {
                                    leadsArrayList = response.body().getLeadsList();
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
            public void onFailure(Call<ResponseDataLeads>call, Throwable t) {
                Log.e(TAG, t.toString());
                if(! LeadDashBoardActivity.this.isFinishing()) {
                    setDefaultLayout();

                    AlertDialog builder = new showErrorDialog(LeadDashBoardActivity.this, getResources().getString(R.string.instabilidade_servidor));
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

    public class LeadAdapter extends BaseAdapter {
        Context context;
        LayoutInflater infalter;
        ArrayList<Leads> arrayList;

        LeadAdapter(Context context, ArrayList<Leads> arrayList) {
            this.context=context;
            infalter=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.arrayList=arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = infalter.inflate(R.layout.lead_dashboard_item_row, parent, false);
            TextView txtPer = (TextView)view.findViewById(R.id.txt_per);
            ImageView txtCount = (ImageView)view.findViewById(R.id.txt_cnt);
            ImageView imageUpDwn = (ImageView)view.findViewById(R.id.image_up_dwn);
            TextView TextName = (TextView)view.findViewById(R.id.txt_name);
            TextDrawable drawable = mDrawableBuilder.build(String.valueOf(arrayList.get(position).getCnt()), mColorGenerator.getColor(arrayList.get(position)));
            TextName.setText(arrayList.get(position).getStatus());
            txtPer.setText(arrayList.get(position).getPer());
            if(arrayList.get(position).getPer().equalsIgnoreCase("0%")) {
                imageUpDwn.setBackground(context.getResources().getDrawable(R.drawable.red_up));
            } else {
                imageUpDwn.setBackground(context.getResources().getDrawable(R.drawable.green_up));
            }

            txtCount.setImageDrawable(drawable);

            return view;
        }
    }

//    class  filterValues extends  BaseAdapter {
//        Context context;
//        ArrayList<String> arrayName;
//        ArrayList<String> arrayId;
//        LayoutInflater infalter;
//        LeadFilterBean leadFilterBean;
//
//        public filterValues(Context context, ArrayList<String> arrayName, ArrayList<String> arrayId) {
//            this.context = context;
//            this.arrayName = arrayName;
//            this.arrayId = arrayId;
//            infalter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//
//        @Override
//        public int getCount() {
//            return arrayName.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return position;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            View v = infalter.inflate(R.layout.lead_dashboard_horizontal_items_filter, null);
//            TextView textValue = (TextView) v.findViewById(R.id.txtValue);
//            Button btnCancal = (Button) v.findViewById(R.id.btnCancel);
//            textValue.setText(arrayName.get(position).toString());
//            LinearLayout lnDelete = (LinearLayout) v.findViewById(R.id.ln_delete);
//            final SQLiteDatabase db = database.getWritableDatabase();
//            lnDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (arrayName.size() <= 1) {
//                        hlLeadDashboardNew.setVisibility(View.GONE);
//                        btnFilterCounter.setVisibility(View.GONE);
//                        getSupportActionBar().setTitle("Lead Dashboard");
//                        ContentValues cv = new ContentValues();
//                        cv.put(IAdv8Database.FilterChecked, "false");
//                        agl.android.util.Log.i("Tag", "Position::" + position + "value" + arrayId.get(position).toString());
//                        db.update(IAdv8Database.FilterTable, cv, "_id=?", new String[]{arrayId.get(position).toString()});
//                        arrayName.remove(position);
//                        arrayId.remove(position);
//                        notifyDataSetChanged();
//                        campaignIds = getCampaignCount().toString();
//                        landingPageIds = getLandingCount().toString();
//                        ownerIds = getOwnerCount().toString();
//                        agl.android.util.Log.e(TAG, campaignIds);
//                        getCampaignFilterData(currentFromDate, currentToDate, campaignIds, landingPageIds, statusIds, ownerIds);
//                    } else {
//                        agl.android.util.Log.i("Tag", "Position::" + position + "value" + arrayId.get(position).toString());
//                        ContentValues cv = new ContentValues();
//                        cv.put(IAdv8Database.FilterChecked, "false");
//                        db.update(IAdv8Database.FilterTable, cv, "_id=?", new String[]{arrayId.get(position).toString()});
//                        arrayId.remove(position);
//                        arrayName.remove(position);
//                        btnFilterCounter.setVisibility(View.VISIBLE);
//                        btnFilterCounter.setText("" + arrayId.size());
//                        notifyDataSetChanged();
//                        campaignIds = getCampaignCount().toString();
//                        agl.android.util.Log.e(TAG, campaignIds);
//                        landingPageIds = getLandingCount().toString();
//                        ownerIds = getOwnerCount().toString();
//                        getCampaignFilterData(currentFromDate, currentToDate, campaignIds, landingPageIds, statusIds, ownerIds);
//                    }
//                }
//            });
//            return v;
//        }
//    }

    /*public Cursor getCount() {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cur = db.query(IAdv8Database.FilterTable, null, "FilterChecked=?", new String[]{"true"}, null, null, null);
        return cur;
    }

    public void showCheckedIds(Cursor cur) {
        arrHoriFlag=new ArrayList<String>();
        aarHor=new ArrayList<String>();
        while (cur.moveToNext()) {
            int id = cur.getInt(0);
            String FilterName = cur.getString(1);
            aarHor.add(Integer.toString(id));
            arrHoriFlag.add(FilterName);
            btnFilterCounter.setVisibility(View.VISIBLE);
            filterValues adapter = new filterValues(this, arrHoriFlag, aarHor);
            hlLeadDashboardNew.setAdapter(adapter);
            btnFilterCounter.setText(""+arrHoriFlag.size());
            adapter.notifyDataSetChanged();
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

        if (!dataType.equalsIgnoreCase(Constants.TYPE_CUSTOM_DATE)) {
            dataType = Constants.TYPE_CUSTOM_DATE;
            getCampaignFilterData(currentFromDate, currentToDate,campaignIds,landingPageIds,statusIds,ownerIds);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Cursor cur=getCount();
        if(cur.getCount()>0) {
            arrHoriFlag=new ArrayList<String>();
            aarHor=new ArrayList<String>();
            while (cur.moveToNext()) {
                int id = cur.getInt(0);
                String FilterName = cur.getString(1);
                aarHor.add(Integer.toString(id));
                arrHoriFlag.add(FilterName);
                btnFilterCounter.setVisibility(View.VISIBLE);
                hlLeadDashboardNew.setVisibility(View.VISIBLE);
                filterValues adapter = new filterValues(this, arrHoriFlag, aarHor);
                hlLeadDashboardNew.setAdapter(adapter);
                btnFilterCounter.setText(""+arrHoriFlag.size());
                adapter.notifyDataSetChanged();
                campaignIds= getCampaignCount().toString();
                landingPageIds=  getLandingCount().toString();
                ownerIds= getOwnerCount().toString();
                agl.android.util.Log.e(TAG,campaignIds);
                getCampaignFilterData(currentFromDate, currentToDate, campaignIds, landingPageIds, statusIds, ownerIds);

            }
        }
    }

    public StringBuffer getCampaignCount() {
        SQLiteDatabase db = database.getReadableDatabase();
        curCampaign = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Constants.ID_TYPE_CAMPAIGN_UNITS,"true"}, null, null, null);
        StringBuffer paramCampaignUnitIds = new StringBuffer();
        while (curCampaign.moveToNext()) {
            paramCampaignUnitIds.append(curCampaign.getString(4)+",");
        }

        return  paramCampaignUnitIds;
    }

    public StringBuffer getLandingCount() {
        //Landing********************
        SQLiteDatabase db = database.getReadableDatabase();
        curLanding = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Constants.ID_TYPE_LANDING_PAGES,"true"}, null, null, null);
        StringBuffer paramLanding = new StringBuffer();
        while (curLanding.moveToNext()) {
            paramLanding.append(curLanding.getString(4)+",");
        }

        return paramLanding;
    }

    public StringBuffer getStausCount() {
        SQLiteDatabase db = database.getReadableDatabase();
        curStatus = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Constants.ID_TYPE_STATUS,"true"}, null, null, null);
        StringBuffer paramStaus = new StringBuffer();
        while (curStatus.moveToNext()) {
            paramStaus.append(curStatus.getString(4)+",");
        }
        return paramStaus;
    }

    public  StringBuffer getOwnerCount() {
        //Owners
        SQLiteDatabase db = database.getReadableDatabase();
        curOwners = db.query(IAdv8Database.FilterTable, null, "FilterFlag=? AND FilterChecked=?", new String[]{Constants.ID_TYPE_OWNERS,"true"}, null, null, null);
        StringBuffer paramOwners = new StringBuffer();
        while (curOwners.moveToNext()) {
            paramOwners.append(curOwners.getString(4)+",");
        }
        return paramOwners;
    }*/
}
