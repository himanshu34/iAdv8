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
import com.agl.product.adw8_new.service.Post;
import com.agl.product.adw8_new.service.data.RequestGoogleAnalytics;
import com.agl.product.adw8_new.utils.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.agl.product.adw8_new.utils.Session.PREF_NAME;

//import agl.android.CircularFloatingActionMenu.FloatingActionButton;


public class FragmentSettingAnalytics extends Fragment implements View.OnClickListener{
    private static final String TAG = "FragmentSettingPage: ";
    Activity activity = null;
    private ProgressBar progressTask;
    private RelativeLayout rlDefaultLayout,rlMainLayout;
    private SharedPreferences sharedPreferences;
    TextView txtMsg,txtNoData;
    private static final String TAG_BTN_EDIT = "btnEdit";
    private static final String TAG_BTN_REMINDER = "btnReminder";
    String clientId ="";
    private FloatingActionButton btnFab;
    Session session;
    HashMap<String, String> userData;

    public static Fragment newInstance(int position) {
        Log.e(TAG, "Position " + position);
        FragmentSettingAnalytics fragmentSettingAnalytics = null;
        fragmentSettingAnalytics = new FragmentSettingAnalytics();
        return fragmentSettingAnalytics;
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
            rlDefaultLayout = (RelativeLayout) view
                    .findViewById(R.id.rl_default_layout);
            rlMainLayout = (RelativeLayout) view
                    .findViewById(R.id.rl_main_layout);
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

   /* @Override
    public void onStart() {
        super.onStart();
        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "check");
            }
        });
    }*/

    @Override
    public void onResume() {
        super.onResume();
        checkAccountAddorNot();
    }

    private void checkAccountAddorNot() {
        try {
            setProgressLayout();
            sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            clientId = userData.get(Session.KEY_AGENCY_CLIENT_ID);
            String accessToken = sharedPreferences.getString(Session.KEY_ACCESS_TOKEN, "");
            String url = "http://adwordsv1605.aglkuber.in/adwordsv1609/examples/AdWords/v201609/BasicOperations/CheckRefreshTokenGenerated.php?cId=" + clientId;
            Log.e("param Url-- ", url);
            RequestGoogleAnalytics requestGoogleAnalytics = new RequestGoogleAnalytics();
            requestGoogleAnalytics.setAccessToken(accessToken);
            requestGoogleAnalytics.setCid(clientId);
            Post apiGoogleAnalytics = ApiClient.getClient().create(Post.class);
            Call<ResponseBody> adwordsCall = apiGoogleAnalytics.getGoogleAnalytics(requestGoogleAnalytics,url);
            adwordsCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                     if( response != null ){
                         if(response.isSuccessful()){
                             int error=-1;
                             String message = "NA";
                             JSONObject jsonObject = null;
                             try {
                                 jsonObject = new JSONObject(response.body().string());
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
                             } catch (Exception e) {
                                 e.printStackTrace();
                                 setDefaultLayout();
                             }

                         }else {
                             setDefaultLayout();
//                             txtMsg.setText("Something went wrong,Please try again.");
                         }
                     }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if(t != null ) Log.d("TAG",t.getMessage());
                    setDefaultLayout();
                }
            });

        } catch (Exception e) {
            Log.e(TAG,"Exception " + e);
            setDefaultLayout();
        }

    }

    public void setProgressLayout() {
        progressTask.setVisibility(View.VISIBLE);
        rlDefaultLayout.setVisibility(GONE);
    }

    public void setDefaultLayout() {
        progressTask.setVisibility(GONE);
        rlDefaultLayout.setVisibility(View.VISIBLE);
    }

    public void setMainLayout() {
        progressTask.setVisibility(GONE);
        rlDefaultLayout.setVisibility(GONE);
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
       /* if (v.getTag().equals(TAG_BTN_EDIT)) {
//            startActivity(new Intent(activity, Setting.class));
            openBrowser();
        }*/
        switch (v.getId()){
            case R.id.fab_btn:
                openBrowser();
                break;
        }
    }
    private void openBrowser() {

        String url = "http://gapi.adv8.co/api/auth_success.php?cId="+clientId;
        Log.e(TAG,"browser url " + url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
//        activity.finish();

    }
}
