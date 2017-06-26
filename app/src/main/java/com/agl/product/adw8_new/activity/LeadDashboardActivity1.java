package com.agl.product.adw8_new.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.adapter.LeadDashboardHeaderAdapter;

public class LeadDashboardActivity1 extends AppCompatActivity {

    private RecyclerView rvHeaderData,rvGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_dashboard1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvHeaderData = (RecyclerView) findViewById(R.id.rvHeaderData);
        rvHeaderData.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvHeaderData.setLayoutManager(mLayoutManager);

        rvGraph = (RecyclerView) findViewById(R.id.rvGraph);
        rvGraph.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGraph.setLayoutManager(mLayoutManager2);


        LeadDashboardHeaderAdapter leadDashboardHeaderAdapter = new LeadDashboardHeaderAdapter();
        rvHeaderData.setAdapter(leadDashboardHeaderAdapter);


    }

}
