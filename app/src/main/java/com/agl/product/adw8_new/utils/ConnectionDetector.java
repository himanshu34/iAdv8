package com.agl.product.adw8_new.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class ConnectionDetector  {

    private Context context;
    private ConnectivityManager cm ;

    public  ConnectionDetector(Context context ){
        this.context = context ;
        cm = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isConnectedToInternet() {
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
