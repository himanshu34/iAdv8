package com.agl.product.adw8_new.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.fragment.DialogListNew;
import com.agl.product.adw8_new.fragment.ProgressDialogFragment;
import com.agl.product.adw8_new.model.FieldsData;
import com.agl.product.adw8_new.model.LeadsDetail;
import com.agl.product.adw8_new.model.Owner;
import com.agl.product.adw8_new.model.Status;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Get;
import com.agl.product.adw8_new.service.Post;
import com.agl.product.adw8_new.service.data.RequestDataLeadsStatusUpdate;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsDetails;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsOwner;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsStatus;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsStatusUpdate;
import com.agl.product.adw8_new.utils.CustomDialogClickListner;
import com.agl.product.adw8_new.utils.CustomDialogItemClickListner;
import com.agl.product.adw8_new.utils.Session;
import com.agl.product.adw8_new.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadDetailsActivity extends ActivityBase implements View.OnClickListener, CustomDialogItemClickListner, CustomDialogClickListner {

    private static final String TAG = "LeadDetailsPage :: ";
    private static final String TAG_BTN_EDIT = "btnEdit";
    private static final String TAG_BTN_REMINDER = "btnReminder";
    private static final String TAG_BTN_HISTORY = "btnHistory";
    private ProgressBar progressLeadDetail;
    private RelativeLayout rlMainLayout, rlDefaultLayout;
    private LinearLayout parentView, linChild, linFollowLayout;
    TextView textLastFollowValue, textNextFollowValue;
    LeadsDetail leadDetail;
    private ArrayList<FieldsData> fieldsDataList;
    private ArrayList<Owner> ownersList;
    private ArrayList<Status> statusList;
    ArrayList<String> owners;
    ArrayList<String> status;
    Typeface tf;
    ProgressDialogFragment progressDialogFragment;
    private String leadId, LeadName = "NA";
    Session session;
    HashMap<String, String> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lead_details_page);
        session = new Session(this);
        userData = session.getUsuarioDetails();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lead Details");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        tf = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Medium.ttf");
        initData();

        parentView = (LinearLayout) findViewById(R.id.parentView);
        progressLeadDetail = (ProgressBar) findViewById(R.id.progress_lms_home);
        rlDefaultLayout = (RelativeLayout) findViewById(R.id.rl_lead_detail_default_layout);
        rlMainLayout = (RelativeLayout) findViewById(R.id.rl_lead_detail_main_layout);
        linChild = (LinearLayout) findViewById(R.id.lin_child);
        linFollowLayout = (LinearLayout) findViewById(R.id.follow_layout);
        textLastFollowValue = (TextView) findViewById(R.id.txt_last_follow_val);
        textNextFollowValue = (TextView) findViewById(R.id.txt_next_follow_val);
        progressDialogFragment = (ProgressDialogFragment) getSupportFragmentManager().findFragmentByTag(Utils.TAG_PROGRESS_DIALOG);
        if (progressDialogFragment == null) {
            progressDialogFragment = ProgressDialogFragment.newInstance();
        }
        getLeadDetailData();
    }

    private void initData() {
        fieldsDataList = new ArrayList<FieldsData>();
        LeadName = getIntent().getStringExtra(Utils.LEAD_NAME);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            leadId = bundle.getString(Utils.LEAD_ID);
        }
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
                    Log.e(TAG, response.body().getMessage());
                    if (response.isSuccessful()) {
                        if (response.body().getError() == 0) {
                            Log.d(TAG, response.body().toString());
                            if(response.body().getLeadsDetail() != null) {
                                leadDetail = response.body().getLeadsDetail();
                                if(leadDetail.getFieldsDataList() != null) {
                                    if(leadDetail.getFieldsDataList().size() > 0) {
                                        fieldsDataList = leadDetail.getFieldsDataList();
                                    }
                                }

                                setMainLayout();
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

            @Override
            public void onFailure(Call<ResponseDataLeadsDetails>call, Throwable t) {
                Log.e(TAG, t.toString());
                if(! LeadDetailsActivity.this.isFinishing()) {
                    setDefaultLayout();
                }
            }
        });
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

        linChild.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int i=0; i<fieldsDataList.size(); i++) {
            View view = null;
            final int pos = i;
            if(i%2 == 0) {
                view = inflater.inflate(R.layout.leads_item_row_first, linChild, false);
            } else {
                view = inflater.inflate(R.layout.leads_item_row_second, linChild, false);
            }

            TextView textLabel = (TextView) view.findViewById(R.id.txt_label);
            TextView textValue = (TextView) view.findViewById(R.id.txt_val);
            final ImageButton imgEdit = (ImageButton) view.findViewById(R.id.img_edit);
            textLabel.setText(fieldsDataList.get(i).getLable()+":");
            textValue.setText(fieldsDataList.get(i).getValue());
            textValue.setTypeface(tf);

            if(fieldsDataList.get(i).getLable().equalsIgnoreCase("owner")) {
                imgEdit.setVisibility(View.VISIBLE);
            } else if (fieldsDataList.get(i).getLable().equalsIgnoreCase("lead_status")) {
                imgEdit.setVisibility(View.VISIBLE);
            } else if (fieldsDataList.get(i).getLable().equalsIgnoreCase("status")) {
                imgEdit.setVisibility(View.VISIBLE);
            } else {
                imgEdit.setVisibility(View.GONE);
            }

            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fieldsDataList.get(pos).getLable().equalsIgnoreCase("owner")) {
                        Log.i(TAG, "Owner Clicked");
                        progressDialogFragment.show(getSupportFragmentManager(), Utils.TAG_PROGRESS_DIALOG);
                        getLeadOwnerInfo(leadDetail.getOwner_id());
                    } else if (fieldsDataList.get(pos).getLable().equalsIgnoreCase("lead_status")) {
                        Log.i(TAG, "Status Clicked");
                        progressDialogFragment.show(getSupportFragmentManager(), Utils.TAG_PROGRESS_DIALOG);
                        getLeadStatusInfo(leadDetail.getStatus_id());
                    }
                }
            });

            linChild.addView(view);
        }

        if(fieldsDataList.size()%2 == 0) {
            linFollowLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            linFollowLayout.setBackgroundColor(Color.parseColor("#f7f7f7"));
        }
        textLastFollowValue.setText(leadDetail.getLast_followup());
		textNextFollowValue.setText(leadDetail.getNext_followup());
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
        if (owners != null) {
            getLeadOwnerUpdate(owners.get(position), leadId);
        } else {
            if (!Utils.isEmptyString(leadId)) {
                String statusValue = status.get(position);
                getLeadStatusUpdate(statusValue, leadId);
            }
        }
    }

    @Override
    public void onCustomDialogClick(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirm ...");
        alertDialog.setMessage("Are you sure you want to change Owner?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                if (owners != null) {
                    getLeadOwnerUpdate(owners.get(position), leadId);
                }
                else {
                    if (!Utils.isEmptyString(leadId)) {
                        String statusValue = status.get(position);
                        getLeadStatusUpdate(statusValue, leadId);
                    }
                }
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
//        adapterLeadDetail.stopMediaPlayer();
    }

    private class showErrorDialog extends AlertDialog {
        protected showErrorDialog(Context context, String message) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogLayout = inflater.inflate(R.layout.custom_alert_layout_single, (ViewGroup) getCurrentFocus());
            setView(dialogLayout);
            final TextView textViewTitle = (TextView) dialogLayout.findViewById(R.id.textview_title);
            textViewTitle.setText(getResources().getString(R.string.app_name));
            final TextView textViewMessage = (TextView) dialogLayout.findViewById(R.id.textview_text);
            textViewMessage.setText(message);
            final TextView textviewPositive = (TextView) dialogLayout.findViewById(R.id.textview_positive);
            textviewPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    private void getLeadOwnerInfo(String ownerId) {
        Get apiLeadsOwnerService = ApiClient.getClientEarlier().create(Get.class);
        String url = "webforms/get-owners/userEmail/"+userData.get(Session.KEY_EMAIL)+
                "/password/"+userData.get(Session.KEY_PASSWORD)+"/sKeys/1r2a3k4s5h6s7i8n9h10/clientId/"+
                userData.get(Session.KEY_AGENCY_CLIENT_ID);
        Call<ResponseDataLeadsOwner> leadsCall = apiLeadsOwnerService.getLeadsOwnerInfo(url);
        leadsCall.enqueue(new Callback<ResponseDataLeadsOwner>() {
            @Override
            public void onResponse(Call<ResponseDataLeadsOwner>call, Response<ResponseDataLeadsOwner> response) {
                ownersList = new ArrayList<Owner>();
                progressDialogFragment.dismiss();
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getError() == 0) {
                            Log.d(TAG, response.body().toString());

                            owners = new ArrayList<String>();
                            if(response.body().getOwnersList() != null) {
                                if(response.body().getOwnersList().size() > 0) {
                                    ownersList = response.body().getOwnersList();
                                    for(Owner u : ownersList) {
                                        owners.add(u.getOwner());
                                    }
                                    DialogListNew.newInstance(owners, LeadDetailsActivity.this, "Owner List").show(getSupportFragmentManager(), TAG);
                                }
                            }

                        } else {
                            AlertDialog builder = new showErrorDialog(LeadDetailsActivity.this, response.body().getMessage());
                            builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            builder.setCanceledOnTouchOutside(false);
                            builder.setCancelable(false);
                            builder.show();
                        }
                    } else {
                        AlertDialog builder = new showErrorDialog(LeadDetailsActivity.this, getResources().getString(R.string.instabilidade_servidor));
                        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        builder.setCanceledOnTouchOutside(false);
                        builder.setCancelable(false);
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDataLeadsOwner>call, Throwable t) {
                Log.e(TAG, t.toString());
                progressDialogFragment.dismiss();
                AlertDialog builder = new showErrorDialog(LeadDetailsActivity.this, getResources().getString(R.string.instabilidade_servidor));
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                builder.setCanceledOnTouchOutside(false);
                builder.setCancelable(false);
                builder.show();
            }
        });
    }

    private void getLeadStatusInfo(String statusId) {
        Get apiLeadsStatusService = ApiClient.getClientEarlier().create(Get.class);
        String url="webforms/get-status/userEmail/"+userData.get(Session.KEY_EMAIL)+
                "/password/"+userData.get(Session.KEY_PASSWORD)+"/sKeys/1r2a3k4s5h6s7i8n9h10/clientId/"
                +userData.get(Session.KEY_AGENCY_CLIENT_ID);
        Call<ResponseDataLeadsStatus> leadsCall = apiLeadsStatusService.getLeadsStatusInfo(url);
        leadsCall.enqueue(new Callback<ResponseDataLeadsStatus>() {
            @Override
            public void onResponse(Call<ResponseDataLeadsStatus>call, Response<ResponseDataLeadsStatus> response) {
                statusList = new ArrayList<Status>();
                progressDialogFragment.dismiss();
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getError() == 0) {
                            Log.d(TAG, response.body().toString());

                            status = new ArrayList<String>();
                            if(response.body().getStatusList() != null) {
                                if(response.body().getStatusList().size() > 0) {
                                    statusList = response.body().getStatusList();
                                    for(Status u : statusList) {
                                        status.add(u.getStatus_name());
                                    }
                                    DialogListNew.newInstance(status, LeadDetailsActivity.this, "Status List").show(getSupportFragmentManager(), TAG);
                                }
                            }

                        } else {
                            AlertDialog builder = new showErrorDialog(LeadDetailsActivity.this, response.body().getMessage());
                            builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            builder.setCanceledOnTouchOutside(false);
                            builder.setCancelable(false);
                            builder.show();
                        }
                    } else {
                        AlertDialog builder = new showErrorDialog(LeadDetailsActivity.this, getResources().getString(R.string.instabilidade_servidor));
                        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        builder.setCanceledOnTouchOutside(false);
                        builder.setCancelable(false);
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDataLeadsStatus>call, Throwable t) {
                Log.e(TAG, t.toString());
                progressDialogFragment.dismiss();
                AlertDialog builder = new showErrorDialog(LeadDetailsActivity.this, getResources().getString(R.string.instabilidade_servidor));
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                builder.setCanceledOnTouchOutside(false);
                builder.setCancelable(false);
                builder.show();
            }
        });
    }

    private void getLeadStatusUpdate(String statusId, String leadId) {
        RequestDataLeadsStatusUpdate requestDataLeadsStatusUpdate = new RequestDataLeadsStatusUpdate();
        requestDataLeadsStatusUpdate.setUpdateType("status");
        requestDataLeadsStatusUpdate.setStatusId(statusId);
        requestDataLeadsStatusUpdate.setLeadId(leadId);

        Post apiLeadsStatusUpdateService = ApiClient.getClientEarlier().create(Post.class);
        String url = "/webforms/update-lead"+"/userEmail/"+ userData.get(Session.KEY_EMAIL)+
                "/password/"+userData.get(Session.KEY_PASSWORD)+"/sKeys/1r2a3k4s5h6s7i8n9h10";
        Call<ResponseDataLeadsStatusUpdate> call = apiLeadsStatusUpdateService.leadsStatusUpdate(url, requestDataLeadsStatusUpdate);
        call.enqueue(new Callback<ResponseDataLeadsStatusUpdate>() {
            @Override
            public void onResponse(Call<ResponseDataLeadsStatusUpdate>call, Response<ResponseDataLeadsStatusUpdate> response) {
                if(response != null) {
                    if(response.isSuccessful()) {
                        Log.e(TAG, response.body().getMessage());
                        if(response.body().getError() == 0) {
                            refreshPageData();
                        } else {
                            AlertDialog builder = new showErrorDialog(LeadDetailsActivity.this, response.body().getMessage());
                            builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            builder.setCanceledOnTouchOutside(false);
                            builder.setCancelable(false);
                            builder.show();
                        }
                    } else {
                        AlertDialog builder = new showErrorDialog(LeadDetailsActivity.this, response.body().getMessage());
                        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        builder.setCanceledOnTouchOutside(false);
                        builder.setCancelable(false);
                        builder.show();
                    }
                } else {
                    AlertDialog builder = new showErrorDialog(LeadDetailsActivity.this, getResources().getString(R.string.instabilidade_servidor));
                    builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    builder.setCanceledOnTouchOutside(false);
                    builder.setCancelable(false);
                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataLeadsStatusUpdate>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                AlertDialog builder = new showErrorDialog(LeadDetailsActivity.this, getResources().getString(R.string.instabilidade_servidor));
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                builder.setCanceledOnTouchOutside(false);
                builder.setCancelable(false);
                builder.show();
            }
        });
    }

    public void refreshPageData(){
        getLeadDetailData() ;
    }

    private void getLeadOwnerUpdate(String ownerId, String leadId) {
//        userEmail = sharedPreferences.getString(Constants.USER_EMAIL, "");
//        userPassword = sharedPreferences.getString(Constants.USER_PASSWORD, "");
//        String url = UrlEndpoints.URL_ADV8+"/webforms/update-lead"+"/webforms/update-lead"+"/userEmail/"+userEmail+"/password/"+userPassword+"/sKeys/1r2a3k4s5h6s7i8n9h10";
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put(UrlEndpoints.URL_UPDATE_TYPE, "owner");
//            jsonObject.put(UrlEndpoints.URL_OWNER_ID, ownerId);
//            jsonObject.put(UrlEndpoints.URL_LEAD_ID, leadId);
//            Log.e(TAG, "URL_UPDATE_TYPE" + "owner");
//            Log.e(TAG, "URL_OWNER_ID" + ownerId);
//            Log.e(TAG, "URL_LEAD_ID" + leadId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        //String url = getUpdateOwnerUrl(userEmail, userPassword, ownerId, leadId);
//        Log.e(TAG, "Url : " + url);
//        Log.e(TAG, jsonObject.toString());
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>(){
//
//            @Override
//            public void onResponse(JSONObject response) {
//                progressDialogFragment.dismiss();
//                Log.e(TAG, "Response : " + response.toString());
//                String message = Parser.parseOwnerUpdate(response);
//                if (message.equalsIgnoreCase(Constants.MSG_SUCCESS)) {
//                    if (leadDetailsPage != null) {
//                        leadDetailsPage.refreshPageData();
//                    }
//                }
//            }
//
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.e(TAG, "VolleyError : " + volleyError);
//                progressDialogFragment.dismiss();
//                Toast.makeText(context, context.getResources().getString(
//                        R.string.oops_msg), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
