package com.agl.product.adw8_new.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
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
    private String fromDate,toDate, fromDateToShow, toDateToShow;
    private ConnectionDetector cd;
    private DatePickerDialog datePickerDialog;
    private CheckBox checkDisplay, checkSearch, checkEnabled,checkDisabled;
    private TextView textClear, textApply;
    private String filterNetwork, filterNetworkValue;
    private String filterEnable, filterEnableValue;
    private ImageView editIcon;
    private TextView textClicksTotal,textImprTotal,textAvgCpcTotal,textCostTotal,textCtrTotal,textConvTotal,textCpaTotal,textConvRateTotal;
    private String rupeeSymbol;

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
        rupeeSymbol = getString(R.string.rupee);

        swipeRefreshLayout = (SwipeRefreshLayoutBottom) findViewById(R.id.swipeRefresh);
        textSelectedDateRange = (TextView) findViewById(R.id.textSelectedDateRange);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        llDataContainer = (LinearLayout) findViewById(R.id.llDataContainer);
        tlValues = (TableLayout) findViewById(R.id.tlValues);
        hrone = (HorizontalScrollView) findViewById(R.id.hrone);
        hrsecond = (HorizontalScrollView) findViewById(R.id.hrsecond);
        hrbottom = (HorizontalScrollView) findViewById(R.id.hrbottom);
        tlName = (TableLayout) findViewById(R.id.tlName);
        editIcon = (ImageView) findViewById(R.id.edit_icon);
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

        textClear = (TextView) filterLayout.findViewById(R.id.textClear);
        textApply = (TextView) filterLayout.findViewById(R.id.textApply);
        checkDisplay = (CheckBox) filterLayout.findViewById(R.id.checkDisplay);
        checkEnabled = (CheckBox) filterLayout.findViewById(R.id.checkEnabled);
        checkSearch = (CheckBox) filterLayout.findViewById(R.id.checkSearch);
        checkDisabled = (CheckBox) filterLayout.findViewById(R.id.checkDisabled);


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



//        ,,,,,,,;
        textClicksTotal = (TextView) findViewById(R.id.textClicksTotal);
        textImprTotal = (TextView) findViewById(R.id.textImprTotal);
        textAvgCpcTotal = (TextView) findViewById(R.id.textAvgCpcTotal);
        textCostTotal = (TextView) findViewById(R.id.textCostTotal);
        textCtrTotal = (TextView) findViewById(R.id.textCtrTotal);
        textConvTotal = (TextView) findViewById(R.id.textConvTotal);
        textCpaTotal = (TextView) findViewById(R.id.textCpaTotal);
        textConvRateTotal = (TextView) findViewById(R.id.textConvRateTotal);

        llDateLayout.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        textYesterday.setOnClickListener(this);
        textLastSevenDays.setOnClickListener(this);
        textLastThirtyDays.setOnClickListener(this);
        textCustom.setOnClickListener(this);
        textClear.setOnClickListener(this);
        textApply.setOnClickListener(this);

        cd = new ConnectionDetector(this );


        checkDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEnabled.setChecked(false);
                checkSearch.setChecked(false);
                checkDisabled.setChecked(false);
                if (checkDisplay.isChecked()) {
                    filterNetwork = "advertising_channel";
                    filterNetworkValue = "Display";
                    filterEnable = null;
                    filterEnableValue = null;
                } else {
                    filterNetwork = null;
                    filterNetworkValue = null;
                    filterEnableValue = null ;
                    filterEnable = null;
                }
            }
        });

        checkEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDisplay.setChecked(false);
                checkSearch.setChecked(false);
                checkDisabled.setChecked(false);
                if (checkEnabled.isChecked()) {
                    filterEnable = "campaign_state";
                    filterEnableValue = "enabled";
                    filterNetwork = null;
                    filterNetworkValue = null;
                } else {
                    filterEnable = null;
                    filterEnableValue = null;
                    filterNetwork = null;
                    filterNetworkValue = null;
                }
            }
        });

        checkSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEnabled.setChecked(false);
                checkDisplay.setChecked(false);
                checkDisabled.setChecked(false);
                if (checkSearch.isChecked()) {
                    filterNetwork = "advertising_channel";
                    filterNetworkValue = "Search";
                    filterEnable = null;
                    filterEnableValue = null;
                } else {
                    filterNetwork = null;
                    filterNetworkValue = null;
                    filterEnableValue = null ;
                    filterEnable = null ;
                }
            }
        });

        checkDisabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSearch.setChecked(false);
                checkDisplay.setChecked(false);
                checkEnabled.setChecked(false);

                if( checkDisabled.isChecked() ){
                    filterEnable = "campaign_state";
                    filterEnableValue = "disabled";
                    filterNetwork = null;
                    filterNetworkValue = null;
                } else {
                    filterEnable = null;
                    filterEnableValue = null;
                    filterNetwork = null;
                    filterNetworkValue = null;
                }

            }
        });


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
        fromDateToShow = Utils.getDisplaySevenDayBeforeDate();
        toDateToShow = Utils.getDisplayCurrentDate();
        textSelectedDateRange.setText(fromDateToShow+" - "+toDateToShow);
        getAdsData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llDateLayout:
                customDatePopup.showAsDropDown(editIcon, 0, 20);
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
                customDatePopup.dismiss();
                AlertDialog builder = new ShowDateRangeDialog(AdsActivity.this, getResources().getString(R.string.instabilidade_servidor));
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                builder.setCanceledOnTouchOutside(false);
                builder.setCancelable(false);
                builder.show();
                break;
            case R.id.textClear:
                filterEnable = null;
                filterEnableValue = null;
                filterNetworkValue = null;
                filterNetwork = null;
                checkDisplay.setChecked(false);
                checkEnabled.setChecked(false);
                checkSearch.setChecked(false);
                checkDisabled.setChecked(false);
                break;
            case R.id.textApply:
                if (!cd.isConnectedToInternet()) return;
                offset = 0;
                rowCount = 0;
                getAdsData();
                filterPopup.dismiss();
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
        if (filterEnableValue != null && filterEnable != null)
            requestDataAds.setCampaign_state(filterEnableValue);
        if (filterNetwork != null && filterNetworkValue != null)
            requestDataAds.setAdvertising_channel(filterNetworkValue);

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
                                    setTotalRow(adsData);
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

    private void setTotalRow(ResponseDataAds adsData) {
        textClicksTotal.setText(adsData.getTotal().getClicks());
        textImprTotal.setText(adsData.getTotal().getImpressions());
        textAvgCpcTotal.setText(rupeeSymbol+" "+adsData.getTotal().getAvg_cpc());
        textCostTotal.setText(rupeeSymbol+" "+adsData.getTotal().getCost());
        textCtrTotal.setText(adsData.getTotal().getCtr());
        textConvTotal.setText(adsData.getTotal().getConverted_clicks());
        textCpaTotal.setText(rupeeSymbol+" "+adsData.getTotal().getCpa());
        textConvRateTotal.setText(rupeeSymbol+" "+adsData.getTotal().getCpa());
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
        textView6.setText(rupeeSymbol+" "+adListingData.getAvg_cpc());
        row.addView(textView6);



        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView4 = (TextView) view.findViewById(R.id.text_view);
        textView4.setText(rupeeSymbol+" "+adListingData.getCost());
        row.addView(textView4);



        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView2 = (TextView) view.findViewById(R.id.text_view);
        textView2.setText(adListingData.getCtr());
        row.addView(textView2);


        // conv.

        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView5 = (TextView) view.findViewById(R.id.text_view);
        textView5.setText(adListingData.getConverted_clicks());
        row.addView(textView5);

        // Cost / conv

        view = LayoutInflater.from(this).inflate(R.layout.row_textview,row,false );
        TextView textView8 = (TextView) view.findViewById(R.id.text_view);
        textView8.setText(rupeeSymbol+" "+adListingData.getCpa());
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
      /*  TableRow row = new TableRow(this);
        View v = LayoutInflater.from(this).inflate(R.layout.first_row, row, false);
        TextView tv = (TextView)v.findViewById(R.id.text_view);
        tv.setText(data.getAd());
        tlName.addView(v, i);*/


        TableRow row = new TableRow(this);
        View v = LayoutInflater.from(this).inflate(R.layout.campaign_first_row, row, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        TextView tv = (TextView) v.findViewById(R.id.text_view);
        if (data.getAd_type_custom().equalsIgnoreCase("display")) {
            imageView.setImageResource(R.drawable.ic_campaign_display);
        } else if(data.getAd_type_custom().equalsIgnoreCase("search")){
            imageView.setImageResource(R.drawable.ic_campaign_search);
        }
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
        fromDateToShow = Utils.getDisplayYesterdayDate();
        toDateToShow = Utils.getDisplayYesterdayDate();
        textSelectedDateRange.setText(fromDateToShow);
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
        fromDateToShow = Utils.getDisplaySevenDayBeforeDate();
        toDateToShow = Utils.getDisplayCurrentDate();
        textSelectedDateRange.setText(fromDateToShow+" - "+toDateToShow);
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
        fromDateToShow = Utils.getDisplayThirtyDayBeforeDate();
        toDateToShow = Utils.getDisplayCurrentDate();
        textSelectedDateRange.setText(fromDateToShow+" - "+toDateToShow);
        customDatePopup.dismiss();
        offset = 0;
        rowCount = 0;
        getAdsData();
    }
    private void setCustomDay() {
        if (!cd.isConnectedToInternet()) return;
        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);
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


            textStartDate.setText(fromDateToShow);
            textEndDate.setText(toDateToShow);
            llStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    datePickerDialog = new DatePickerDialog(AdsActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
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
                    datePickerDialog = new DatePickerDialog(AdsActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
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
