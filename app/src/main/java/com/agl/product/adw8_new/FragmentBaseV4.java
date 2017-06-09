package com.agl.product.adw8_new;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentBaseV4 extends Fragment {

    protected View rootView;
    protected ViewGroup container;
    protected AppCompatActivity mActivity;

    protected void initialize(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, int fragment) {
        rootView = inflater.inflate(fragment, container, false);
        this.container = container;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (AppCompatActivity)activity;
    }
}
