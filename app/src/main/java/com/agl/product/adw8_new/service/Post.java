package com.agl.product.adw8_new.service;

import com.agl.product.adw8_new.service.data.RequestDataAddCient;
import com.agl.product.adw8_new.service.data.RequestDataAds;
import com.agl.product.adw8_new.service.data.RequestDataCampaignDetails;
import com.agl.product.adw8_new.service.data.RequestDataKeywords;
import com.agl.product.adw8_new.service.data.ResponseDataAds;
import com.agl.product.adw8_new.service.data.ResponseDataCampaign;
import com.agl.product.adw8_new.service.data.RequestDataCampaign;
import com.agl.product.adw8_new.service.data.RequestDataLogin;
import com.agl.product.adw8_new.service.data.ResponseDataAddCient;
import com.agl.product.adw8_new.service.data.ResponseDataCampaignDetails;
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
    Call<ResponseDataCampaignDetails> getCampaignData(@Body RequestDataCampaignDetails dataCampaignDetails);

    @POST("paid/campaign/get_Data_Of_Account")
    Call<ResponseDataCampaign> getDashboardData(@Body RequestDataCampaign dataCampaign);

    @POST("ad/get_Ad_Of_Account")
    Call<ResponseDataAds> getAdsData(@Body RequestDataAds requestDataAds);

    @POST("keyword/get_Paid_Keyword_detail")
    Call<ResponseDataAds> getKeywordsList(@Body RequestDataKeywords requestKeywords);
}