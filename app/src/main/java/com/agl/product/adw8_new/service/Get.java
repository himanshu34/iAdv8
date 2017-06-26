package com.agl.product.adw8_new.service;

import com.agl.product.adw8_new.service.data.ResponseDataLeads;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsDetails;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsListing;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsOwner;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsStatus;
import com.agl.product.adw8_new.service.data.ResponseLeadsGraph;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Get {

    @GET()
    Call<ResponseDataLeads> getLeadsData(@Url String url);

    @GET()
    Call<ResponseDataLeadsListing> getLeadsLmsData(@Url String url);

    @GET()
    Call<ResponseDataLeadsDetails> getLeadsDetailsData(@Url String url);

    @GET()
    Call<ResponseDataLeadsOwner> getLeadsOwnerInfo(@Url String url);

    @GET()
    Call<ResponseDataLeadsStatus> getLeadsStatusInfo(@Url String url);

    @GET()
    Call<ResponseLeadsGraph> getLeadGraph(@Url String url);
}
