package com.agl.product.adw8_new.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.adapter.DataActivityGraphAdapter;
import com.agl.product.adw8_new.adapter.DataActivityGroupAdapter;
import com.agl.product.adw8_new.fragment.DataAdsFragment;
import com.agl.product.adw8_new.fragment.DataCampaignFragment;
import com.agl.product.adw8_new.fragment.DataKeywordFragment;

public class DataActivity extends ActivityBase implements TabLayout.OnTabSelectedListener {

    private RecyclerView rvGroupData,rvGraph;
    private TabLayout tabLayout;
    private DataCampaignFragment dataCampaignFragment;
    private DataKeywordFragment dataKeywordFragment;
    private DataAdsFragment dataAdsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        rvGroupData = (RecyclerView) findViewById(R.id.rvGroupData);
        rvGroupData.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGroupData.setLayoutManager(mLayoutManager);
        rvGroupData.setAdapter(new DataActivityGroupAdapter());

        rvGraph = (RecyclerView) findViewById(R.id.rvGraph);
        rvGraph.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGraph.setLayoutManager(mLayoutManager1);
        rvGraph.setAdapter( new DataActivityGraphAdapter());

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        dataCampaignFragment = new DataCampaignFragment();
        dataKeywordFragment = new DataKeywordFragment();
        dataAdsFragment = new DataAdsFragment();
        setupTabLayout();
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
}
