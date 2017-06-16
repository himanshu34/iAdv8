package com.agl.product.adw8_new.utils;

import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static final int VERTICAL_ITEM_SPACE = 20;

    public static final String TYPE_YESTERDAY = "typeYesterday";
    public static final String TYPE_LAST_WEEK = "typeLastWeek";
    public static final String TYPE_LAST_MONTH = "typeLastMonth";
    public static final String TYPE_CUSTOM_DATE = "typeCustomDate";
    public static final String ID_TYPE_CAMPAIGN_UNITS = "campaignIds";
    public static final String ID_TYPE_LANDING_PAGES = "landingPageIds";
    public static final String ID_TYPE_OWNERS = "ownerIds";
    public static final String ID_TYPE_STATUS = "statusIds";
    public static final int BTN_FROM_START = 1;
    public static final int BTN_FROM_END = 2;
    public static final int BTN_TO_START = 3;
    public static final String CURRENT__FROM_DATE="current_from_date";
    public static final String CURRENT_TO_DATE="current_to_date";
    public static final String DATE_TYPE="date_type";
    public static final String TAG_DIALOG_DATE_SELECTOR = "tagDialogDateSelector";
    public static final String WEB_FORM_ID="wId";
    public static final String LEAD_ID = "leadId";
    public static final String TYPE_LEAD_WEB = "Web";
    public static final String TYPE_LEAD_CALL = "Call";

    public static final String CAMPAIGN = "campaign";
    public static final String NAME = "name";
    public static final String AD = "ad";
    public static final String CLICKS = "clicks";
    public static final String BUDGET = "budget";
    public static final String IMPRESSIONS = "impressions";
    public static final String COST = "cost";
    public static final String CTR = "ctr";
    public static final String CONVERTED_CLICKS = "converted_clicks";
    public static final String CPA = "cpa";
    public static final String CONVERSION_RATE = "conversion_rate";
    public static final String KEYWORD_STATE = "keyword_state";

    public static final String DESC = "DESC";
    public static final String ASSC = "ASC";


    public static boolean closeKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            boolean b = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return b;
        }

        return false;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if(info != null) {
                for(int i = 0; i < info.length; i++) {
                    if(info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static String getDisplayYesterdayDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    public static String getYesterdayDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    public static String getDisplaySevenDayBeforeDate(){
        DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DATE, -7);
        return dateFormat.format(cal.getTime());
    }

    public static String getSevenDayBeforeDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DATE, -7);
        return dateFormat.format(cal.getTime());
    }

    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static String getDisplayCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static String getThirtyDayBeforeDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DATE, -30);
        return dateFormat.format(cal.getTime());
    }

    public static String getDisplayThirtyDayBeforeDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DATE, -30);
        return dateFormat.format(cal.getTime());
    }
}