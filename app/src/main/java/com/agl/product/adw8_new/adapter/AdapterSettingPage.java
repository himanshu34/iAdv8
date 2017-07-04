package com.agl.product.adw8_new.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.fragment.FragmentSettingAnalytics;
import com.agl.product.adw8_new.fragment.FragmentSettingPage;


public class AdapterSettingPage extends FragmentStatePagerAdapter {

    private Context context;

    public AdapterSettingPage(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = FragmentSettingPage.newInstance(position);
                break;
            case 1:
                fragment = FragmentSettingAnalytics.newInstance(position);
                break;

            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getStringArray(R.array.tabs_setting_page)[position];
    }

}
