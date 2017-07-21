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
import android.widget.Toast;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.custom_view.SwipeRefreshLayoutBottom;
import com.agl.product.adw8_new.model.CampaignData;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Post;
import com.agl.product.adw8_new.service.data.RequestDataCampaignDetails;
import com.agl.product.adw8_new.service.data.ResponseDataCampaignDetails;
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

public class CampaignActivity extends ActivityBase implements View.OnClickListener, SwipeRefreshLayoutBottom.OnRefreshListener {

    private PopupWindow filterPopup, customDatePopup;
    private HorizontalScrollView hrone, hrsecond, hrbottom;
    private View filterLayout, customPopupLayout;
    private LinearLayout llDateLayout, llDataContainer;
    Session session;
    HashMap<String, String> userData;
    private int offset = 0, limit = 50;
    private SwipeRefreshLayoutBottom swipeRefreshLayout;
    private int rowCount;
    private TableLayout tlName, tlValues;
    private TextView textYesterday, textLastSevenDays, textLastThirtyDays, textCustom, textSelectedDateRange, textMessage;
    private TextView textCpa, textConv, textCtr, textCost, textAvgCpc, textImpr, textClicks, textBudget;
    private ProgressBar progressBar;
    private String fromDate, toDate, fromDateToShow, toDateToShow, sortBy, sortingOrder;
    private ConnectionDetector cd;
    private DatePickerDialog datePickerDialog;
    ImageView editIcon;
    private CheckBox checkDisplay, checkSearch, checkEnabled,checkDisabled;
    private TextView textClear, textApply;
    private String filterNetwork, filterNetworkValue;
    private String filterEnable, filterEnableValue;
    private TextView textBudgetTotal, textClicksTotal,textImprTotal,textAvgCpcTotal,textCostTotal,textCtrTotal,textConvTotal,textCpaTotal;
    private String rupeeSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        session = new Session(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        cd = new ConnectionDetector(this);
        rupeeSymbol = getString(R.string.rupee);

        swipeRefreshLayout = (SwipeRefreshLayoutBottom) findViewById(R.id.swipeRefresh);
        textSelectedDateRange = (TextView) findViewById(R.id.textSelectedDateRange);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        llDataContainer = (LinearLayout) findViewById(R.id.llDataContainer);
        textMessage = (TextView) findViewById(R.id.textMessage);
        tlValues = (TableLayout) findViewById(R.id.tlValues);
        tlName = (TableLayout) findViewById(R.id.tlName);
        hrone = (HorizontalScrollView) findViewById(R.id.hrone);
        hrsecond = (HorizontalScrollView) findViewById(R.id.hrsecond);
        hrbottom = (HorizontalScrollView) findViewById(R.id.hrbottom);
        editIcon = (ImageView) findViewById(R.id.edit_icon);
        textCpa = (TextView) findViewById(R.id.textCpa);
        textConv = (TextView) findViewById(R.id.textConv);
        textCtr = (TextView) findViewById(R.id.textCtr);
        textCost = (TextView) findViewById(R.id.textCost);
        textAvgCpc = (TextView) findViewById(R.id.textAvgCpc);
        textImpr = (TextView) findViewById(R.id.textImpr);
        textClicks = (TextView) findViewById(R.id.textClicks);
        textBudget = (TextView) findViewById(R.id.textBudget);
        textBudgetTotal = (TextView) findViewById(R.id.textBudgetTotal);
        textClicksTotal = (TextView) findViewById(R.id.textClicksTotal);
        textImprTotal = (TextView) findViewById(R.id.textImprTotal);
        textAvgCpcTotal = (TextView) findViewById(R.id.textAvgCpcTotal);
        textCostTotal = (TextView) findViewById(R.id.textCostTotal);
        textCtrTotal = (TextView) findViewById(R.id.textCtrTotal);
        textConvTotal = (TextView) findViewById(R.id.textConvTotal);
        textCpaTotal = (TextView) findViewById(R.id.textCpaTotal);
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

        llDateLayout.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        textYesterday.setOnClickListener(this);
        textLastSevenDays.setOnClickListener(this);
        textLastThirtyDays.setOnClickListener(this);
        textCustom.setOnClickListener(this);
        textCpa.setOnClickListener(this);
        textConv.setOnClickListener(this);
        textCtr.setOnClickListener(this);
        textCost.setOnClickListener(this);
        textAvgCpc.setOnClickListener(this);
        textImpr.setOnClickListener(this);
        textClicks.setOnClickListener(this);
        textBudget.setOnClickListener(this);
        textClear.setOnClickListener(this);
        textApply.setOnClickListener(this);

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

        sortBy = Utils.CLICKS;
        sortingOrder = Utils.DESC;

        fromDate = Utils.getSevenDayBeforeDate();
        toDate = Utils.getCurrentDate();
        fromDateToShow = Utils.getDisplaySevenDayBeforeDate();
        toDateToShow = Utils.getDisplayCurrentDate();

        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);
        getCampaignDataActual();
    }

    private void getCampaignDataActual() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestDataCampaignDetails requestDataCampaignDetails = new RequestDataCampaignDetails();
        requestDataCampaignDetails.setAccess_token(userData.get(Session.KEY_ACCESS_TOKEN));
        requestDataCampaignDetails.setpId("1");
        requestDataCampaignDetails.setcId(userData.get(Session.KEY_AGENCY_CLIENT_ID));
        requestDataCampaignDetails.setfDate(fromDate);
        requestDataCampaignDetails.settDate(toDate);
        requestDataCampaignDetails.setLimit(limit + "");
        requestDataCampaignDetails.setOrderBy(sortingOrder);
        requestDataCampaignDetails.setSortBy(sortBy);
        requestDataCampaignDetails.setOffset(offset);
        if (filterEnableValue != null && filterEnable != null)
            requestDataCampaignDetails.setCampaign_state(filterEnableValue);
        if (filterNetwork != null && filterNetworkValue != null)
            requestDataCampaignDetails.setAdvertising_channel(filterNetworkValue);
        if (offset == 0) {
            // Show loading on first time
            llDataContainer.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            textMessage.setVisibility(View.GONE);
        }

        Call<ResponseDataCampaignDetails> campaignCall = apiAddClientService.getCampaignData(requestDataCampaignDetails);
        campaignCall.enqueue(new Callback<ResponseDataCampaignDetails>() {
            @Override
            public void onResponse(Call<ResponseDataCampaignDetails> call, Response<ResponseDataCampaignDetails> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    ResponseDataCampaignDetails campaignData = response.body();

                    try {
                        ArrayList<CampaignData> data = campaignData.getData();
                        if (data != null) {
                            if (data.size() > 0) {
                                llDataContainer.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.GONE);
                                setTotalRow(campaignData);
                                createCampaignTable(data);
                                offset = offset + limit;
                            } else {
                                if (offset == 0) {
                                    llDataContainer.setVisibility(View.INVISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    textMessage.setVisibility(View.VISIBLE);
                                    textMessage.setText("No Campaigns Found.");
                                }
                            }
                        } else {
                            if (offset == 0) {
                                llDataContainer.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.VISIBLE);
                                textMessage.setText("Some error occured");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (offset == 0) {
                            llDataContainer.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.GONE);
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Some error occured");
                        }
                    }
                } else {
                    if (offset == 0) {
                        llDataContainer.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        textMessage.setText("Some error occured");
                    } else
                        Toast.makeText(CampaignActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataCampaignDetails> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if (t != null) {
                    Log.d("TAG", t.getMessage());
                }
                if (offset == 0) {
                    llDataContainer.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText("There is some connectivity issue.");
                } else
                    Toast.makeText(CampaignActivity.this, "There is some connectivity issue.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCampaignDataOnRefresh() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestDataCampaignDetails requestDataCampaignDetails = new RequestDataCampaignDetails();
        requestDataCampaignDetails.setAccess_token(userData.get(Session.KEY_ACCESS_TOKEN));
        requestDataCampaignDetails.setpId("1");
        requestDataCampaignDetails.setcId(userData.get(Session.KEY_AGENCY_CLIENT_ID));
        requestDataCampaignDetails.setfDate(fromDate);
        requestDataCampaignDetails.settDate(toDate);
        requestDataCampaignDetails.setLimit(limit + "");
        requestDataCampaignDetails.setOrderBy(sortingOrder);
        requestDataCampaignDetails.setSortBy(sortBy);
        requestDataCampaignDetails.setOffset(offset);
        if (filterEnableValue != null && filterEnable != null)
            requestDataCampaignDetails.setCampaign_state(filterEnableValue);
        if (filterNetwork != null && filterNetworkValue != null)
            requestDataCampaignDetails.setAdvertising_channel(filterNetworkValue);
        if (offset == 0) {
            // Show loading on first time
            llDataContainer.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            textMessage.setVisibility(View.GONE);
        }

        Call<ResponseDataCampaignDetails> campaignCall = apiAddClientService.getCampaignData(requestDataCampaignDetails);
        campaignCall.enqueue(new Callback<ResponseDataCampaignDetails>() {
            @Override
            public void onResponse(Call<ResponseDataCampaignDetails> call, Response<ResponseDataCampaignDetails> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    ResponseDataCampaignDetails campaignData = response.body();
                    try {
                        ArrayList<CampaignData> data = campaignData.getData();
                        if (data != null) {
                            if (data.size() > 0) {
                                llDataContainer.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.GONE);
                                setTotalRow(campaignData);
                                createCampaignTableOnRefresh(data);
                                offset = offset + limit;
                            } else {
                                if (offset == 0) {
                                    llDataContainer.setVisibility(View.INVISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    textMessage.setVisibility(View.VISIBLE);
                                    textMessage.setText("No Campaigns Found.");
                                }
                            }
                        } else {
                            if (offset == 0) {
                                llDataContainer.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);
                                textMessage.setVisibility(View.VISIBLE);
                                textMessage.setText("Some error occured");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (offset == 0) {
                            llDataContainer.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.GONE);
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Some error occured");
                        }
                    }
                } else {
                    if (offset == 0) {
                        llDataContainer.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        textMessage.setText("Some error occured");
                    } else
                        Toast.makeText(CampaignActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataCampaignDetails> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if (t != null) {
                    Log.d("TAG", t.getMessage());
                }
                if (offset == 0) {
                    llDataContainer.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText("There is some connectivity issue.");
                } else
                    Toast.makeText(CampaignActivity.this, "There is some connectivity issue.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTotalRow(ResponseDataCampaignDetails campaignData) {
        textBudgetTotal.setText(rupeeSymbol+" "+campaignData.getTotal().getBudget());
        textClicksTotal.setText(campaignData.getTotal().getClicks());
        textImprTotal.setText(campaignData.getTotal().getImpressions());
        textAvgCpcTotal.setText(rupeeSymbol+" "+campaignData.getTotal().getAvg_cpc());
        textCostTotal.setText(rupeeSymbol+" "+campaignData.getTotal().getCost());
        textCtrTotal.setText(campaignData.getTotal().getCtr());
        textConvTotal.setText(campaignData.getTotal().getConverted_clicks());
        textCpaTotal.setText(rupeeSymbol+" "+campaignData.getTotal().getCpa());
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

            default:
                break;
        }
        return true;
    }

    private void setOtherRow(TableRow row, TableRow.LayoutParams lp, int i, CampaignData campaignData) {
        setCampaignName(i, campaignData);
        View view = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        TextView textView1 = (TextView) view.findViewById(R.id.text_view);
        textView1.setText(rupeeSymbol+" "+campaignData.getBudget());
        row.addView(textView1);

        TextView textView2 = new TextView(this);
        textView2.setBackgroundResource(R.drawable.cell_shape);
        textView2.setPadding(20, 20, 20, 20);
        textView2.setGravity(Gravity.CENTER);
        textView2.setLayoutParams(lp);

        view = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        textView2 = (TextView) view.findViewById(R.id.text_view);
        textView2.setText(campaignData.getClicks());
        row.addView(textView2);

        TextView textView3 = new TextView(this);
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(lp);
        textView3.setGravity(Gravity.CENTER);
        textView3.setBackgroundResource(R.drawable.cell_shape);

        view = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        textView3 = (TextView) view.findViewById(R.id.text_view);
        textView3.setText(campaignData.getImpressions());
        row.addView(textView3);

        TextView textView4 = new TextView(this);
        textView4.setPadding(20, 20, 20, 20);
        textView4.setLayoutParams(lp);
        textView4.setGravity(Gravity.CENTER);
        textView4.setBackgroundResource(R.drawable.cell_shape);

        view = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        textView4 = (TextView) view.findViewById(R.id.text_view);
        textView4.setText(rupeeSymbol+" "+campaignData.getAvg_cpc());
        row.addView(textView4);

        TextView textView5 = new TextView(this);
        textView5.setPadding(20, 20, 20, 20);
        textView5.setLayoutParams(lp);
        textView5.setGravity(Gravity.CENTER);
        textView5.setBackgroundResource(R.drawable.cell_shape);

        view = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        textView5 = (TextView) view.findViewById(R.id.text_view);
        textView5.setText(rupeeSymbol+" "+campaignData.getCost());
        row.addView(textView5);

        TextView textView6 = new TextView(this);
        textView6.setPadding(20, 20, 20, 20);
        textView6.setLayoutParams(lp);
        textView6.setGravity(Gravity.CENTER);
        textView6.setBackgroundResource(R.drawable.cell_shape);

        view = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        textView6 = (TextView) view.findViewById(R.id.text_view);
        textView6.setText(campaignData.getCtr());
        row.addView(textView6);

        TextView textView7 = new TextView(this);
        textView7.setPadding(20, 20, 20, 20);
        textView7.setLayoutParams(lp);
        textView7.setGravity(Gravity.CENTER);
        textView7.setBackgroundResource(R.drawable.cell_shape);

        TextView textView8 = new TextView(this);
        textView8.setPadding(20, 20, 20, 20);
        textView8.setLayoutParams(lp);
        textView8.setGravity(Gravity.CENTER);
        textView8.setBackgroundResource(R.drawable.cell_shape);

        view = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        textView8 = (TextView) view.findViewById(R.id.text_view);
        textView8.setText(campaignData.getConverted_clicks());
        row.addView(textView8);

        TextView textView9 = new TextView(this);
        textView9.setPadding(20, 20, 20, 20);
        textView9.setLayoutParams(lp);
        textView9.setGravity(Gravity.CENTER);
        textView9.setBackgroundResource(R.drawable.cell_shape);

        view = LayoutInflater.from(this).inflate(R.layout.row_textview, row, false);
        textView9 = (TextView) view.findViewById(R.id.text_view);
        textView9.setText(rupeeSymbol+" "+campaignData.getCpa());
        row.addView(textView9);

        tlValues.addView(row, i);
        rowCount++;
    }

    private void setCampaignName(int i, CampaignData data) {
        TableRow row = new TableRow(this);
        View v = LayoutInflater.from(this).inflate(R.layout.campaign_first_row, row, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        TextView tv = (TextView) v.findViewById(R.id.text_view);
        if (data.getAdvertising_channel().equalsIgnoreCase("display")) {
            imageView.setImageResource(R.drawable.ic_campaign_display);
        } else{
            imageView.setImageResource(R.drawable.ic_campaign_search);
        }
        tv.setText(data.getCampaign());
        tlName.addView(v, i);
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
                customDatePopup.showAsDropDown(editIcon, 0, 20);
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
                AlertDialog builder = new ShowDateRangeDialog(CampaignActivity.this, getResources().getString(R.string.instabilidade_servidor));
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                builder.setCanceledOnTouchOutside(false);
                builder.setCancelable(false);
                builder.show();
                break;
            case R.id.textCpa:
                break;
            case R.id.textConv:
                break;
            case R.id.textCtr:
                break;
            case R.id.textCost:
                break;
            case R.id.textAvgCpc:
                break;
            case R.id.textImpr:
                break;
            case R.id.textClicks:
                break;
            case R.id.textBudget:
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
                if( tlValues.getChildCount() > 0 ) tlValues.removeAllViews();
                if( tlName.getChildCount() > 0 ) tlName.removeAllViews();
                getCampaignDataActual();
                filterPopup.dismiss();
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
        if( tlValues.getChildCount() > 0 ) tlValues.removeAllViews();
        if( tlName.getChildCount() > 0 ) tlName.removeAllViews();
        getCampaignDataActual();
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
        if( tlValues.getChildCount() > 0 ) tlValues.removeAllViews();
        if( tlName.getChildCount() > 0 ) tlName.removeAllViews();
        getCampaignDataActual();
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
        if( tlValues.getChildCount() > 0 ) tlValues.removeAllViews();
        if( tlName.getChildCount() > 0 ) tlName.removeAllViews();
        getCampaignDataActual();
    }

    private void setCustomDay() {
        if (!cd.isConnectedToInternet()) return;
        textSelectedDateRange.setText(fromDateToShow + " - " + toDateToShow);
        offset = 0;
        rowCount = 0;
        if( tlValues.getChildCount() > 0 ) tlValues.removeAllViews();
        if( tlName.getChildCount() > 0 ) tlName.removeAllViews();
        getCampaignDataActual();
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
                    datePickerDialog = new DatePickerDialog(CampaignActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
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
                    datePickerDialog = new DatePickerDialog(CampaignActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
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

    private void createCampaignTable(ArrayList<CampaignData> campaignDatas) {
        tlName.removeAllViews();
        tlValues.removeAllViews();
        for (int i = 0; i < campaignDatas.size(); i++) {
            TableRow row1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            row1.setLayoutParams(lp);
            setOtherRow(row1, lp, rowCount, campaignDatas.get(i));
        }
    }

    private void createCampaignTableOnRefresh(ArrayList<CampaignData> campaignDatas) {
        for (int i = 0; i < campaignDatas.size(); i++) {
            TableRow row1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            row1.setLayoutParams(lp);
            setOtherRow(row1, lp, rowCount, campaignDatas.get(i));
        }
    }

    @Override
    public void onRefresh() {
        getCampaignDataOnRefresh();
    }
}