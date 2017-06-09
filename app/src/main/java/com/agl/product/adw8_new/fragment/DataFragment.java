package com.agl.product.adw8_new.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agl.product.adw8_new.R;


public class DataFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private DataCampaignFragment dataCampaignFragment;

    public  DataFragment(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.data_fragment_layout,container , false );
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        setupTabLayout();
        return view ;
    }

    private void setupTabLayout() {
        dataCampaignFragment = new DataCampaignFragment();
        tabLayout.addTab(tabLayout.newTab().setText("CAMPAIGN"),true);
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
                replaceFragment(dataCampaignFragment);
                break;
            case 2:
                replaceFragment(dataCampaignFragment);
                break;
        }
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace( R.id.frame_container, fragment );
        ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );
        ft.commit();
    }

}
