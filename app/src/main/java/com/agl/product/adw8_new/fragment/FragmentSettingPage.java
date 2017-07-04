package com.agl.product.adw8_new.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.activity.SettingsActivity;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Get;
import com.agl.product.adw8_new.service.data.ResponseDataLeads;
import com.agl.product.adw8_new.utils.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.agl.product.adw8_new.utils.Session.PREF_NAME;


public class FragmentSettingPage extends Fragment implements View.OnClickListener {
    private static final String TAG = "FragmentSettingPage: ";
    Activity activity = null;
    private ProgressBar progressTask;
    private RelativeLayout rlDefaultLayout, rlMainLayout;
    TextView txtMsg,txtNoData;
    static int mPositionADV8 = -1;
    static int mPositionANALYTICS = -1;
    private static final String TAG_BTN_EDIT = "btnEdit";
    private static final String TAG_BTN_REMINDER = "btnReminder";
    String clientId = " ";
    private FloatingActionButton btnFab;
    Session session;
    HashMap<String, String> userData;

    public static Fragment newInstance(int position) {
        if (position == 0) {
            mPositionADV8 = position;
        }
        if (position == 1) {
            mPositionANALYTICS = position;
        }

        Log.e(TAG, "Position " + position);
        FragmentSettingPage fragmentSettingPage = null;
        fragmentSettingPage = new FragmentSettingPage();
        return fragmentSettingPage;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SettingsActivity) activity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;

        try {
            view = inflater.inflate(R.layout.fragment_setting_page, container, false);
            session = new Session(getActivity());
            userData = session.getUsuarioDetails();
            txtMsg = (TextView) view.findViewById(R.id.txt_msg);
            txtNoData = (TextView) view.findViewById(R.id.txt_no_data);
            rlMainLayout = (RelativeLayout) view
                    .findViewById(R.id.rl_main_layout);
            rlDefaultLayout = (RelativeLayout) view
                    .findViewById(R.id.rl_default_layout);
            progressTask = (ProgressBar) view
                    .findViewById(R.id.progress_geo_details);
            btnFab = (FloatingActionButton) view.findViewById(R.id.fab_btn);
            btnFab.setOnClickListener(this);
//            checkAccountAddorNot();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkAccountAddorNot();
    }

    private void checkAccountAddorNot() {
        try {
            Log.e(TAG, "GOT mPositionADV8 " + mPositionADV8);
            Log.e(TAG, "GOT mPositionANALYTICS " + mPositionANALYTICS);
            Log.e(TAG, "checkAccountAddorNot");
            setProgressLayout();
            clientId = userData.get(Session.KEY_AGENCY_CLIENT_ID);
            String url = "http://adwordsv1605.aglkuber.in/adwordsv1609/examples/AdWords/v201609/BasicOperations/CheckRefreshTokenGenerated.php?cId=" + clientId;
            Get googleAdwordService = ApiClient.getClientEarlier().create(Get.class);
            Call<ResponseBody> adwordsCall = googleAdwordService.getGoogleAdwords(url);
            adwordsCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressTask.setVisibility(View.GONE);
                    if (response != null) {
                        if (response.isSuccessful()) {
                            try {
                                int error = -1;
                                String message = "NA";
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                if (jsonObject.has("error"))
                                    error = jsonObject.getInt("error");
                                if (jsonObject.has("message")){
                                    message = jsonObject.getString("message");
                                }

                                if (error == 0) {
                                    setMainLayout() ;
//                                        btnFab.setVisibility(View.GONE);
                                } else {
                                        txtNoData.setText(message);
                                    setDefaultLayout();
//                                        btnFab.setVisibility(View.VISIBLE);
                                }

                                txtMsg.setText(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            setDefaultLayout();
//                            txtMsg.setText("Something went wrong,Please try again.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t != null) {
                        Log.d("TAG", t.getMessage());
                    }
                    setDefaultLayout();
//                    txtMsg.setText("Could not connect , Try again.");
                }
            });
            Log.e("param Url ", url);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            setDefaultLayout();
        }

    }

    public void setProgressLayout() {
        progressTask.setVisibility(View.VISIBLE);
        rlDefaultLayout.setVisibility(View.GONE);
        rlMainLayout.setVisibility(View.GONE);
    }

    public void setDefaultLayout() {
        progressTask.setVisibility(View.GONE);
        rlDefaultLayout.setVisibility(View.VISIBLE);
        rlMainLayout.setVisibility(View.GONE);
    }

    public void setMainLayout() {
        progressTask.setVisibility(View.GONE);
        rlDefaultLayout.setVisibility(View.GONE);
        rlMainLayout.setVisibility(View.VISIBLE);
    }

   /* private void buildFAB() {
        // define the icon for the main floating action button
        ImageView iconActionButton = new ImageView(activity);
        iconActionButton.setImageResource(R.drawable.ic_action_new);

        // set the appropriate background for the main floating action button
        // along with its icon
        FloatingActionButton actionButton = new FloatingActionButton.Builder(
                activity).setContentView(iconActionButton)
                .setBackgroundDrawable(R.drawable.selector_button_red).build();
        actionButton.setTag(TAG_BTN_EDIT);

        // set the background for all the sub buttons
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(activity);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.selector_sub_button_gray));

        // build the sub buttons


        // add the sub buttons to the main floating action button
        // add the sub buttons to the main floating action button
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(activity)
                .attachTo(actionButton).build();
        actionButton.setOnClickListener(this);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_btn:
                Log.e(TAG, "fab_btn clccik");
                openBrowser();
                break;
        }
        /*if (v.getTag().equals(TAG_BTN_EDIT)) {
            startActivity(new Intent(activity, Setting.class));
            openBrowser();
        }*/
    }

    private void openBrowser() {
        String url = "http://adwordsv1605.aglkuber.in/adwordsv1609/examples/AdWords/Auth/GetRefreshTokenWithoutIniFile.php?state=" + clientId;
        Log.e(TAG, "browser url " + url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        /*i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_FROM_BACKGROUND);*/
        startActivity(i);
//        activity.finish();

    }
}
