package com.agl.product.adw8_new.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.agl.product.adw8_new.utils.DateHelper;

public class AdapterSpinnerDateSelector extends ArrayAdapter<String> {

    private static final String TAG = null;
    private int hidingItemIndex;
    private String[] arrDateSelector;
    private Spinner spinnerDateSelector;
    private Context context;
    private FragmentManager fragmentManager;

    public AdapterSpinnerDateSelector(Context context, FragmentManager fragmentManager, int textViewResourceId, String[] objects, int hidingItemIndex, Spinner spinnerDateSelector) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.hidingItemIndex = hidingItemIndex;
        this.arrDateSelector = objects;
        this.spinnerDateSelector = spinnerDateSelector;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (position == hidingItemIndex) {
            TextView tv = new TextView(getContext());
            tv.setVisibility(View.GONE);
            view = tv;
        } else {
            view = super.getDropDownView(position, null, parent);
        }
        return view;
    }

    public String[] getLastWeek() {
        //for Last seven days date
        String strdateLastsevenDays = DateHelper.getLastSevenDaysPrevoius();
        String[] arrDateLastsevenDays = strdateLastsevenDays.split(",");
        String dateLastSevenDays=arrDateLastsevenDays[0];

        //for current date
        String  strCurrentDate = DateHelper.getCurrentDate();
        String[] arrCurrentDate = strCurrentDate.split(",");
        String strCurrentDateValue = arrCurrentDate[0];

        arrDateSelector[arrDateSelector.length - 1] = String.valueOf("Last 7 Days : "+dateLastSevenDays + " - " + strCurrentDateValue);
        spinnerDateSelector.setSelection(arrDateSelector.length - 1);
        return new String[]{DateHelper.getLastSevenDaysUrlFormat(), DateHelper.getCurrentDateUrlFormat()};
    }

    public String[] getLastWeekPreviousDay() {
        //for Last seven days date
        String strdateLastsevenDays = DateHelper.getLastSevenDays();
        String[] arrDateLastsevenDays = strdateLastsevenDays.split(",");
        String dateLastSevenDays=arrDateLastsevenDays[0];

        //for current date
        String  strCurrentDate= DateHelper.yesterday();
        String[] arrCurrentDate=strCurrentDate.split(",");
        String strCurrentDateValue=arrCurrentDate[0];

        arrDateSelector[arrDateSelector.length - 1] = String.valueOf("Last 7 Days : "+dateLastSevenDays + " - " + strCurrentDateValue);
        spinnerDateSelector.setSelection(arrDateSelector.length - 1);
        return new String[]{DateHelper.getLastSevenDaysUrlFormat(), DateHelper.yesterdayUrlFormat()};

    }

    public String[] getLastMonth() {
        // for last month date
        String strLastMonthDate = DateHelper.lastMonth();
        String[] arrLastMonthDate = strLastMonthDate.split(",");
        String strLastMonthDateValue = arrLastMonthDate[0];

        //for current date
        String  strCurrentDate = DateHelper.getCurrentDate();
        String[] arrCurrentDate = strCurrentDate.split(",");
        String strCurrentDateValue = arrCurrentDate[0];

        arrDateSelector[arrDateSelector.length - 1] = String.valueOf("Last 30 Days : "+strLastMonthDateValue + " - " + strCurrentDateValue);
        spinnerDateSelector.setSelection(arrDateSelector.length - 1);
        return new String[]{DateHelper.lastMonthUrlFormat(), DateHelper.getCurrentDateUrlFormat()};
    }

    public String[] getLastMonthPreviousDay() {
        // for last month date
        String strLastMonthDate = DateHelper.lastMonth();
        String[] arrLastMonthDate = strLastMonthDate.split(",");
        String strLastMonthDateValue = arrLastMonthDate[0];

        //for current date
        String  strCurrentDate = DateHelper.yesterday();
        String[] arrCurrentDate = strCurrentDate.split(",");
        String strCurrentDateValue = arrCurrentDate[0];

        arrDateSelector[arrDateSelector.length - 1] = String.valueOf("Last 30 Days : "+strLastMonthDateValue + " - " + strCurrentDateValue);
        spinnerDateSelector.setSelection(arrDateSelector.length - 1);
        // return new String[]{DateHelper.lastMonthUrlFormat(), DateHelper.getCurrentDateUrlFormat()};
        return new String[]{DateHelper.lastMonthUrlFormat(), DateHelper.yesterdayUrlFormat()};
    }

    public String[] getYesterday() {
        //for YesterdayDate
        String yesterday = DateHelper.yesterday();
        String[] arrLastMonthDate = yesterday.split(",");
        String strYesterDay = arrLastMonthDate[0];
        arrDateSelector[arrDateSelector.length - 1] = "Yesterday : "+strYesterDay + " - " + strYesterDay;
        spinnerDateSelector.setSelection(arrDateSelector.length - 1);

        return new String[]{DateHelper.yesterdayUrlFormat(), DateHelper.yesterdayUrlFormat()};
    }
}