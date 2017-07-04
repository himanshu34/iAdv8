package com.agl.product.adw8_new.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.adapter.AdapterSettingPage;

public class SettingsActivity extends ActivityBase {

    private ViewPager viewPager;
    private AdapterSettingPage adapterSettingPage;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        viewPager  = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        adapterSettingPage = new AdapterSettingPage(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapterSettingPage);
        tabLayout.setupWithViewPager(viewPager);
    }


}
