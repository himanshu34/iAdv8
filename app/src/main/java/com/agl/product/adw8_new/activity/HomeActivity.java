package com.agl.product.adw8_new.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.fragment.FragmentAppHome;
import com.agl.product.adw8_new.fragment.ProgressDialogFragment;
import com.agl.product.adw8_new.utils.CustomDialogItemClickListner;
import com.agl.product.adw8_new.utils.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends ActivityBase implements CustomDialogItemClickListner, View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "HomeActivity :: ";
    Toolbar toolbar;
    FragmentManager fragmentManager;
    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    LinearLayout btnSetting;
    TextView txtUserName, txtUserEmail;
    private ProgressDialogFragment progressDialogFragment;
    Session session;
    HashMap<String, String> usuarioData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new Session(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnSetting = (LinearLayout)findViewById(R.id.ll_setting);
        btnSetting.setOnClickListener(HomeActivity.this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        final ActionBar abar = getSupportActionBar();
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(true);
        abar.setTitle("");
        abar.setIcon(getResources().getDrawable(R.drawable.header_logo_white));
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        usuarioData = session.getUsuarioDetails();
        setHeaderLayout(navigationView.getHeaderView(0));
        navigationView.setItemIconTintList(null);

        fragmentManager = getSupportFragmentManager();
        progressDialogFragment = (ProgressDialogFragment) fragmentManager.findFragmentByTag("tagProgressDialog");
        if(progressDialogFragment == null){
            progressDialogFragment = progressDialogFragment.newInstance();
        }

        Fragment fragment = new FragmentAppHome();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_app_home, fragment);
        fragmentTransaction.commit();
    }

    private void setHeaderLayout(View view) {
        txtUserName = (TextView) view.findViewById(R.id.txt_user_name);
        txtUserName.setText(usuarioData.get(Session.KEY_USERNAME));
        txtUserEmail = (TextView) view.findViewById(R.id.txt_user_email);
        txtUserEmail.setText(usuarioData.get(Session.KEY_EMAIL));
    }

    @Override
    public void onCustomDialogItemClick(int position) {
        Log.e(TAG,"position " + position);
        Fragment fragmentAppHome = getSupportFragmentManager().findFragmentByTag(FragmentAppHome.TAG);
        if (fragmentAppHome != null) {
            ((FragmentAppHome) fragmentAppHome).onCustomDialogItemClick(position);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            fragment = new FragmentAppHome();
            try {
                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    if (fragmentManager != null) {
                        fragmentManager.beginTransaction().replace(R.id.fragment_container_app_home, fragment).commit();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_settings) {
//            startActivity(new Intent(HomeActivity.this, SettingPage.class));
        } else if (id == R.id.nav_rate_app) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share_app) {
            invokeShare();
        } else if (id == R.id.nav_logout) {
            setUserLogout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUserLogout() {
        session.logoutUser();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void invokeShare() {
        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_app_text));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.share_text_subject));
        emailIntent.setType("message/rfc822");

        PackageManager pm = getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");

        Intent openInChooser = Intent.createChooser(emailIntent,"Invite Friend via");
        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if(packageName.contains("android.email")) {
                emailIntent.setPackage(packageName);
            }
            else if(packageName.contains("com.whatsapp") || packageName.contains("com.facebook.katana") || packageName.contains("mms") || packageName.contains("com.google.android.talk")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");

                if(packageName.contains("com.facebook.katana")) {
                    intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_app_text));
                }
                else  if(packageName.contains("com.google.android.talk")) {
                    intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_app_text));
                }
                else if(packageName.contains("com.whatsapp")) {
                    intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_app_text));
                }
                else if(packageName.contains("mms")) {
                    intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_app_text));
                } else if(packageName.contains("android.gm")) {
                    intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_app_text));
                    intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_text_subject));
                    intent.setType("message/rfc822");
                }

                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        startActivity(openInChooser);
    }
}
