package com.agl.product.adw8_new.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.adapter.DataActivityGraphAdapter;
import com.agl.product.adw8_new.adapter.DataActivityGroupAdapter;
import com.agl.product.adw8_new.fragment.DataAdsFragment;
import com.agl.product.adw8_new.fragment.DataCampaignFragment;
import com.agl.product.adw8_new.fragment.DataKeywordFragment;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Post;
import com.agl.product.adw8_new.service.data.ResponseDataCampaign;
import com.agl.product.adw8_new.service.data.campaign_model.CampaignData;
import com.agl.product.adw8_new.service.data.campaign_model.RequestDataCampaign;
import com.agl.product.adw8_new.utils.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataActivity extends ActivityBase implements TabLayout.OnTabSelectedListener,
        View.OnClickListener, Callback<ResponseDataCampaign> {

    public String TAG = "DataActivity";
    private RecyclerView rvGroupData,rvGraph;
    private TabLayout tabLayout;
    private DataCampaignFragment dataCampaignFragment;
    private DataKeywordFragment dataKeywordFragment;
    private DataAdsFragment dataAdsFragment;
    private LinearLayout lldefaultSpends;
    private PopupWindow customDatePopup;
    private View customPopupLayout;
    Session session;
    HashMap<String, String> userData;
    private ArrayList<CampaignData> keywordData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        session = new Session(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        lldefaultSpends = (LinearLayout) findViewById(R.id.lldefaultSpends);

        customPopupLayout = getLayoutInflater().inflate(R.layout.date_range_layout, null);
        customDatePopup = new PopupWindow(this);
        customDatePopup.setWidth(400);
        customDatePopup.setHeight(ListPopupWindow.WRAP_CONTENT);
        customDatePopup.setOutsideTouchable(true);
        customDatePopup.setContentView(customPopupLayout);
        customDatePopup.setBackgroundDrawable(new BitmapDrawable());
        customDatePopup.setFocusable(true);

        rvGroupData = (RecyclerView) findViewById(R.id.rvGroupData);
        rvGroupData.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGroupData.setLayoutManager(mLayoutManager);
        rvGroupData.setAdapter(new DataActivityGroupAdapter(this));

        rvGraph = (RecyclerView) findViewById(R.id.rvGraph);
        rvGraph.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGraph.setLayoutManager(mLayoutManager1);
        rvGraph.setAdapter( new DataActivityGraphAdapter());

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        dataCampaignFragment = new DataCampaignFragment();
        dataKeywordFragment = new DataKeywordFragment();
        dataAdsFragment = new DataAdsFragment();

        lldefaultSpends.setOnClickListener(this);
        setupTabLayout();
        userData = session.getUsuarioDetails();
        requestDashboardData();
    }

    private void requestDashboardData() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        RequestDataCampaign requestDataCampaign = new RequestDataCampaign();
        requestDataCampaign.setAccess_token(userData.get(Session.KEY_ACCESS_TOKEN));
        requestDataCampaign.setpId("1");
        requestDataCampaign.setcId(userData.get(Session.KEY_AGENCY_CLIENT_ID));
        requestDataCampaign.setfDate("2016-11-06");
        requestDataCampaign.settDate("2016-11-06");
        requestDataCampaign.setLimit("5");
        requestDataCampaign.setOrder("DESC");
        requestDataCampaign.setSortBy("clicks");

        Call<ResponseDataCampaign> campaignCall = apiAddClientService.getDashboardData(requestDataCampaign);
        campaignCall.enqueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.data_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
        switch (position ){
            case 0:
                replaceFragment(dataCampaignFragment);
                break;
            case 1:
                replaceFragment(dataKeywordFragment);
                if( keywordData != null  ) dataKeywordFragment.setKeywordData(keywordData);
                break;
            case 2:
                replaceFragment(dataAdsFragment);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace( R.id.frame_container, fragment );
        ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lldefaultSpends:
                customDatePopup.showAsDropDown(lldefaultSpends,-5,-5);
                break;
        }
    }

    @Override
    public void onResponse(Call<ResponseDataCampaign> call, Response<ResponseDataCampaign> response) {
        if( response.isSuccessful()) {
            if (response != null) {
                if (response.body().getError() == 0) {
                    Log.d("Pass", response.body().toString());
                    try {
                        ResponseDataCampaign responsCampaign = response.body();
                        ArrayList<CampaignData> campaignData = responsCampaign.getCamapign_data();
                        keywordData = responsCampaign.getKeyword_data();
                        ArrayList<CampaignData> adData =  responsCampaign.getAds_data();
                        dataCampaignFragment.setCampaignData(campaignData);
                        dataKeywordFragment.setKeywordData(keywordData);
                        dataAdsFragment.setAdsData(adData);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertDialog builder = new showErrorDialog(DataActivity.this, response.body().getMessage());
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
        //Alert
        Log.e(TAG, t.toString());
        AlertDialog builder = new showErrorDialog(DataActivity.this, getResources().getString(R.string.instabilidade_servidor));
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        builder.setCanceledOnTouchOutside(false);
        builder.setCancelable(false);
        builder.show();
    }

    private class showErrorDialog extends AlertDialog {
        protected showErrorDialog(Context context, String message) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            final View dialoglayout = inflater.inflate(R.layout.custom_alert_layout_single, (ViewGroup) getCurrentFocus());
            setView(dialoglayout);
            final TextView textviewTitle = (TextView) dialoglayout.findViewById(R.id.textview_title);
            textviewTitle.setText(getResources().getString(R.string.app_name));
            final TextView textviewMessage = (TextView) dialoglayout.findViewById(R.id.textview_text);
            textviewMessage.setText(message);
            final TextView textviewPositive = (TextView) dialoglayout.findViewById(R.id.textview_positive);
            textviewPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }
}
