package com.agl.product.adw8_new.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.model.LeadsDetail;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Get;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsDetails;
import com.agl.product.adw8_new.utils.CustomDialogClickListner;
import com.agl.product.adw8_new.utils.CustomDialogItemClickListner;
import com.agl.product.adw8_new.utils.Session;
import com.agl.product.adw8_new.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadDetailsActivity extends ActivityBase implements View.OnClickListener, CustomDialogItemClickListner, CustomDialogClickListner {

    private static final String TAG = "LeadDetailsPage :: ";
    private static final String TAG_BTN_EDIT = "btnEdit";
    private static final String TAG_BTN_REMINDER = "btnReminder";
    private static final String TAG_BTN_HISTORY = "btnHistory";
    private RecyclerView recycleLmsDetail;
    private Toolbar toolbar;
    private ProgressBar progressLeadDetail;
    private RelativeLayout rlMainLayout, rlDefaultLayout;
//    private AdapterLeadDetail adapterLeadDetail;
    private TextView txtOwner, txtDesignation;
    private String leadId;
    private String ownerId;
    private ImageView imgUser;
    private String imageurl;
    private String twitter;
    private String facebook;
    private String linkdin;
    ImageView imgFacebook,imgTwitter,imgLinked,imgGooglePlus;
    private String statusId="";
    private String LeadName="NA";
    private RelativeLayout rlSocialData;
    Session session;
    HashMap<String, String> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lead_details_page);
        session = new Session(this);
        userData = session.getUsuarioDetails();
    }

    @Override
    public void onStart() {
        super.onStart();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lead Detail");

        progressLeadDetail = (ProgressBar) findViewById(R.id.progress_lms_home);
        rlDefaultLayout = (RelativeLayout) findViewById(R.id.rl_lead_detail_default_layout);
        rlMainLayout = (RelativeLayout) findViewById(R.id.rl_lead_detail_main_layout);

        imgUser = (ImageView) findViewById(R.id.lead_img_default);
        imgFacebook=(ImageView)findViewById(R.id.img_facebook);
        imgTwitter=(ImageView)findViewById(R.id.img_twitter);
        imgLinked=(ImageView)findViewById(R.id.img_linked);
        imgGooglePlus=(ImageView)findViewById(R.id.img_googleplus);
        LeadName=getIntent().getStringExtra(Utils.LEAD_NAME);
        rlSocialData=(RelativeLayout)findViewById(R.id.rl_social_data);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            leadId = bundle.getString(Utils.LEAD_ID);
        }

        txtOwner = (TextView) findViewById(R.id.txt_name_owner);
        txtDesignation = (TextView) findViewById(R.id.txt_designation);

        recycleLmsDetail = (RecyclerView) findViewById(R.id.rv_lmslead_detail);
//        adapterLeadDetail = new AdapterLeadDetail(LeadDetailsActivity.this, LeadDetailsActivity.this, getSupportFragmentManager(),leadId);
//        recycleLmsDetail.setAdapter(adapterLeadDetail);
        recycleLmsDetail.setLayoutManager(new LinearLayoutManager(LeadDetailsActivity.this));

        getLeadDetailData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getLeadDetailData() {
        setProgressLayout();
        Get apiLeadsDetailsService = ApiClient.getClientEarlier().create(Get.class);
        String url = "webforms/show-lead/userEmail/"+userData.get(Session.KEY_EMAIL)+"/password/"
                +userData.get(Session.KEY_PASSWORD) +"/sKeys/1r2a3k4s5h6s7i8n9h10/leadId/"+leadId;
        Call<ResponseDataLeadsDetails> leadsDetailCall = apiLeadsDetailsService.getLeadsDetailsData(url);
        leadsDetailCall.enqueue(new Callback<ResponseDataLeadsDetails>() {
            @Override
            public void onResponse(Call<ResponseDataLeadsDetails>call, Response<ResponseDataLeadsDetails> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getError() == 0) {
                            Log.d(TAG, response.body().toString());
                            if(response.body().getLeadsDetail() != null) {
                                LeadsDetail leadDetail = response.body().getLeadsDetail();
                                ownerId = leadDetail.getOwner_id();
                                statusId = leadDetail.getStatus_id();
                                if (leadDetail != null && leadDetail.getFieldsDataList().size() > 0) {
//                                    adapterLeadDetail.addData(leadDetail.getFieldsDataList());
                                    if(leadDetail.getSocial_data() != null) {
                                        twitter=leadDetail.getSocial_data().getTwitter();
                                        facebook=leadDetail.getSocial_data().getFacebook();
                                        linkdin=leadDetail.getSocial_data().getLinkedin();
                                        imageurl=leadDetail.getSocial_data().getUserImage();

                                        Glide.with(LeadDetailsActivity.this).load(imageurl).asBitmap().error(R.drawable.lead_img_default)
                                                .into(imgUser);

                                        if(!twitter.isEmpty()) {
                                            imgTwitter.setBackground(getResources().getDrawable(R.drawable.ic_twitter_active));
                                        } else if(!linkdin.isEmpty()) {
                                            imgLinked.setBackground(getResources().getDrawable(R.drawable.ic_linked_active));
                                        } else if(!facebook.isEmpty()) {
                                            imgFacebook.setBackground(getResources().getDrawable(R.drawable.ic_facebook_active));
                                        }
                                    }
                                    setMainLayout();
                                    if(leadDetail.getLead_type().equalsIgnoreCase("Call")) {
                                        // setData(leadDetail);
                                        rlSocialData.setVisibility(View.INVISIBLE);
                                    } else {
                                        rlSocialData.setVisibility(View.VISIBLE);
                                        setData(leadDetail);
                                    }
                                } else {
                                    setDefaultLayout();
                                }
                            } else {
                                setDefaultLayout();
                            }
                        } else {
                            setDefaultLayout();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDataLeadsDetails>call, Throwable t) {
                Log.e(TAG, t.toString());
                if(! LeadDetailsActivity.this.isFinishing()) {
                    setDefaultLayout();
                }
            }
        });
    }

    private void setData(LeadsDetail leadDetail) {
        if (leadDetail != null) {
            txtOwner.setText(LeadName);
            txtDesignation.setText(leadDetail.getUser_designation());
        }
    }

    private void setProgressLayout() {
        progressLeadDetail.setVisibility(View.VISIBLE);
        rlDefaultLayout.setVisibility(View.GONE);
        rlMainLayout.setVisibility(View.GONE);
    }

    private void setDefaultLayout() {
        progressLeadDetail.setVisibility(View.GONE);
        rlDefaultLayout.setVisibility(View.VISIBLE);
        rlMainLayout.setVisibility(View.GONE);
    }

    private void setMainLayout() {
        progressLeadDetail.setVisibility(View.GONE);
        rlDefaultLayout.setVisibility(View.GONE);
        rlMainLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getTag().equals(TAG_BTN_EDIT)) {
            Snackbar.make(view, "Coming Soon", Snackbar.LENGTH_SHORT).show();
        }
        if (view.getTag().equals(TAG_BTN_REMINDER)) {
//            Intent intentLmsFollowAdd = new Intent(LeadDetailsActivity.this, LmsFollowAdd.class);
//            intentLmsFollowAdd.putExtra(Utils.LEAD_ID, leadId);
//            intentLmsFollowAdd.putExtra(Utils.OWNER_ID, ownerId);
//            startActivity(intentLmsFollowAdd);
        }
        if (view.getTag().equals(TAG_BTN_HISTORY)) {
//            Intent intentFollowUpPage = new Intent(LeadDetailsActivity.this, FollowUpPage.class);
//            intentFollowUpPage.putExtra(Utils.LEAD_ID, leadId);
//            startActivity(intentFollowUpPage);
        }
    }

    @Override
    public void onCustomDialogItemClick(int position) {
//        if (adapterLeadDetail != null) {
//            adapterLeadDetail.onCustomDialogItemClick(position);
//        }
    }

    @Override
    public void onCustomDialogClick(int position) {
//        if (adapterLeadDetail != null) {
//            adapterLeadDetail.onCustomDialogClick(position);
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        adapterLeadDetail.stopMediaPlayer();
    }
}
