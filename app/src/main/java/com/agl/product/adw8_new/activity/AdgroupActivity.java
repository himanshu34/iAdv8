package com.agl.product.adw8_new.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.custom_view.MyHorizontalScrollView;
import com.agl.product.adw8_new.custom_view.TableMainLayout;

public class AdgroupActivity extends AppCompatActivity {

    private TableLayout tlName;
    private HorizontalScrollView hrone,hrsecond;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adgroup);
        hrone = (HorizontalScrollView) findViewById(R.id.hrone);
        hrsecond = (HorizontalScrollView) findViewById(R.id.hrsecond);
        hrsecond.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
            hrone.scrollTo(hrsecond.getScrollX(),hrsecond.getScrollY());

            }
        });

    }
}
