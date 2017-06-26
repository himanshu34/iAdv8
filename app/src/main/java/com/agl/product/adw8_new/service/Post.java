package com.agl.product.adw8_new.service;

import com.agl.product.adw8_new.service.data.RequestDataAddCient;
import com.agl.product.adw8_new.service.data.RequestDataAdgroup;
import com.agl.product.adw8_new.service.data.RequestDataAds;
import com.agl.product.adw8_new.service.data.RequestDataCampaignDetails;
import com.agl.product.adw8_new.service.data.RequestDataGraphCampaign;
import com.agl.product.adw8_new.service.data.RequestDataKeywords;
import com.agl.product.adw8_new.service.data.RequestDataLeadsStatusUpdate;
import com.agl.product.adw8_new.service.data.RequestInsightData;
import com.agl.product.adw8_new.service.data.ResponseDataAdgroup;
import com.agl.product.adw8_new.service.data.ResponseDataAds;
import com.agl.product.adw8_new.service.data.ResponseDataCampaign;
import com.agl.product.adw8_new.service.data.RequestDataCampaign;
import com.agl.product.adw8_new.service.data.RequestDataLogin;
import com.agl.product.adw8_new.service.data.ResponseDataAddCient;
import com.agl.product.adw8_new.service.data.ResponseDataCampaignDetails;
import com.agl.product.adw8_new.service.data.ResponseDataGraphCampaign;
import com.agl.product.adw8_new.service.data.ResponseDataKeywords;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsStatus;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsStatusUpdate;
import com.agl.product.adw8_new.service.data.ResponseDataLogin;
import com.agl.product.adw8_new.service.data.ResponseInsightData;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

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

    @POST("paid/campaign/get_Graphs_Data_Of_Account")
    Call<ResponseDataGraphCampaign> getGraphDashboardData(@Body RequestDataGraphCampaign dataGraphCampaign);

    @POST("adgroup/get_AdGroups_Of_Account")
    Call<ResponseDataAdgroup> getAdgroupList(@Body RequestDataAdgroup requestKeywords);

    @POST("keyword/get_Paid_Keyword_detail")
    Call<ResponseDataKeywords> getKeywordsList(@Body RequestDataKeywords requestKeywords);

    @POST()
    Call<ResponseInsightData> getInsightData(@Url String url, @Body RequestInsightData requestInsight);

    @POST()
    Call<ResponseDataLeadsStatusUpdate> leadsStatusUpdate(@Url String url, @Body RequestDataLeadsStatusUpdate requestDataLeadsStatusUpdate);
}