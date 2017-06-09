package com.agl.product.adw8_new.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Post;
import com.agl.product.adw8_new.service.data.campaign_model.CampaignData;
import com.agl.product.adw8_new.service.data.campaign_model.RequestDataCampaign;
import com.agl.product.adw8_new.utils.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampaignActivity extends AppCompatActivity implements View.OnClickListener, Callback<RequestDataCampaign> {

    private TableLayout ll;
    private PopupWindow filterPopup, customDatePopup;
    private View filterLayout, customPopupLayout;
    private LinearLayout llDateLayout;
    Session session;
    HashMap<String, String> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        session = new Session(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        ll = (TableLayout) findViewById(R.id.tableLayout);
        llDateLayout = (LinearLayout) findViewById(R.id.llDateLayout);
        filterLayout = getLayoutInflater().inflate(R.layout.custom_filter_layout, null);
        filterPopup = new PopupWindow(this);
        filterPopup.setWidth(400);
        filterPopup.setHeight(ListPopupWindow.WRAP_CONTENT);
        filterPopup.setOutsideTouchable(true);
        filterPopup.setContentView(filterLayout);
        filterPopup.setBackgroundDrawable(new BitmapDrawable());
        filterPopup.setFocusable(true);


        customPopupLayout = getLayoutInflater().inflate(R.layout.date_range_layout, null);
        customDatePopup = new PopupWindow(this);
        customDatePopup.setWidth(400);
        customDatePopup.setHeight(ListPopupWindow.WRAP_CONTENT);
        customDatePopup.setOutsideTouchable(true);
        customDatePopup.setContentView(customPopupLayout);
        customDatePopup.setBackgroundDrawable(new BitmapDrawable());
        customDatePopup.setFocusable(true);

        llDateLayout.setOnClickListener(this);

//        createTable();
        userData = session.getUsuarioDetails();
        requestCampaign();

    }

    private void requestCampaign() {
        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        JSONObject inputJson = new JSONObject();
        try {
            inputJson.put("access_token", "{25E62EBC-AAC7-0046-0647-E2600DEBCF17}");
            inputJson.put("pId", "1");
            inputJson.put("cId", "279");
            inputJson.put("fDate", "2016-11-06");
            inputJson.put("tDate", "2016-11-06");
            inputJson.put("limit", "2");
            inputJson.put("sortBy", "clicks");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (inputJson).toString());
        Call<RequestDataCampaign> campaignCall = apiAddClientService.getCampaign(body);
        campaignCall.enqueue(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.campaign_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_filter:
                displayFilterLayout();
                break;
        }
        return true;
    }

    private void createTable() {
        for (int i = 0; i < 15; i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row.setLayoutParams(lp);

            if (i == 0) {
                setFirstRow(row, lp);
            } else setOtherRow(row, lp, i);

        }
    }

    private void setOtherRow(TableRow row, TableRow.LayoutParams lp, int i) {
        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.cell_shape);
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setText("Campaign" + i);
        row.addView(textView, lp);

        TextView textView1 = new TextView(this);
        textView1.setBackgroundResource(R.drawable.cell_shape);
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setText("              ");
        row.addView(textView1, lp);

        TextView textView2 = new TextView(this);
        textView2.setBackgroundResource(R.drawable.cell_shape);
        textView2.setText("              ");
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        row.addView(textView2, lp);

        TextView textView3 = new TextView(this);
        textView3.setText("              ");
        textView3.setPadding(20, 20, 20, 20);
        textView3.setLayoutParams(lp);
        textView3.setBackgroundResource(R.drawable.cell_shape);
        row.addView(textView3, lp);


        ll.addView(row, i);
    }

    private void setFirstRow(TableRow row, TableRow.LayoutParams lp) {
        TextView textView = new TextView(this);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp);
        textView.setText("Name");
        textView.setGravity(Gravity.CENTER);
        row.addView(textView, lp);

        TextView textView1 = new TextView(this);
        textView1.setTextColor(getResources().getColor(R.color.black));
        textView1.setPadding(20, 20, 20, 20);
        textView1.setLayoutParams(lp);
        textView1.setText("Impressions");
        textView1.setGravity(Gravity.CENTER);
        row.addView(textView1, lp);

        TextView textView2 = new TextView(this);
        textView2.setTextColor(getResources().getColor(R.color.black));
        textView2.setText("CTR");
        textView2.setGravity(Gravity.CENTER);
        textView2.setPadding(20, 20, 20, 20);
        textView2.setLayoutParams(lp);
        row.addView(textView2, lp);

        TextView textView3 = new TextView(this);
        textView3.setText("Clicks");
        textView3.setPadding(20, 20, 20, 20);
        textView3.setGravity(Gravity.CENTER);
        textView3.setLayoutParams(lp);
        textView3.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView3, lp);

        TextView textView4 = new TextView(this);
        textView4.setText("Budget");
        textView4.setPadding(20, 20, 20, 20);
        textView4.setGravity(Gravity.CENTER);
        textView4.setLayoutParams(lp);
        textView4.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView4, lp);


        TextView textView5 = new TextView(this);
        textView5.setText("Cost");
        textView5.setPadding(20, 20, 20, 20);
        textView5.setGravity(Gravity.CENTER);
        textView5.setLayoutParams(lp);
        textView5.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView5, lp);


        TextView textView6 = new TextView(this);
        textView6.setText("Converted Clicks");
        textView6.setPadding(20, 20, 20, 20);
        textView6.setGravity(Gravity.CENTER);
        textView6.setLayoutParams(lp);
        textView6.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView6, lp);


        TextView textView7 = new TextView(this);
        textView7.setText("CPA");
        textView7.setPadding(20, 20, 20, 20);
        textView7.setGravity(Gravity.CENTER);
        textView7.setLayoutParams(lp);
        textView7.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView7, lp);

        TextView textView8 = new TextView(this);
        textView8.setText("Currency");
        textView8.setPadding(20, 20, 20, 20);
        textView8.setGravity(Gravity.CENTER);
        textView8.setLayoutParams(lp);
        textView8.setTextColor(getResources().getColor(R.color.black));
        row.addView(textView8, lp);


        ll.addView(row, 0);
    }

    public void displayFilterLayout() {
        filterPopup.showAtLocation(findViewById(R.id.menu_filter), Gravity.RIGHT | Gravity.TOP, 20, getActionBarHeight());
    }

    private int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();

        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llDateLayout:
                customDatePopup.showAsDropDown(llDateLayout, 0, 10);
                break;
        }
    }

    @Override
    public void onResponse(Call<RequestDataCampaign> call, Response<RequestDataCampaign> response) {
        if (response.isSuccessful()) {
            RequestDataCampaign campaignData = response.body();
            try {
//                ArrayList<CampaignData> campaignDatas = campaignData.getData();
//                if (campaignDatas.size() > 0) {
//                    createDataTable(campaignDatas);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

        }
    }

    private void createDataTable(ArrayList<CampaignData> campaignDatas) {
        TableRow row1 = new TableRow(this);
        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp1.span = 1;
        row1.setLayoutParams(lp1);
        setFirstRow(row1, lp1);

        for (int i = 0; i < campaignDatas.size(); i++) {
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.span = 1;
            row1.setLayoutParams(lp);
            setOtherRow(row1, lp, i);
        }
    }

    @Override
    public void onFailure(Call<RequestDataCampaign> call, Throwable t) {

    }
}
