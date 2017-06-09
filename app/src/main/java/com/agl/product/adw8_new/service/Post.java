package com.agl.product.adw8_new.service;

import com.agl.product.adw8_new.service.data.RequestDataAddCient;
import com.agl.product.adw8_new.service.data.ResponseDataCampaign;
import com.agl.product.adw8_new.service.data.campaign_model.RequestDataCampaign;
import com.agl.product.adw8_new.service.data.RequestDataLogin;
import com.agl.product.adw8_new.service.data.ResponseDataAddCient;
import com.agl.product.adw8_new.service.data.ResponseDataLogin;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Post {

    @POST("user/login")
    Call<ResponseDataLogin> loginVerification(@Body RequestDataLogin dataLogin);

    @POST("client/add_client")
    Call<ResponseDataAddCient> addClient(@Body RequestDataAddCient dataAddCient);

    @POST("paid/campaign/get_Campaigns_Of_Account")
    Call<RequestDataCampaign> getCampaign(@Body RequestBody body);

    @POST("paid/campaign/get_Data_Of_Account")
    Call<ResponseDataCampaign> getDashboardData(@Body RequestDataCampaign dataCampaign);


}