package com.agl.product.adw8_new.service;

import com.agl.product.adw8_new.service.data.ResponseDataLeads;
import com.agl.product.adw8_new.service.data.ResponseDataLeadsListing;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Get {

    @GET()
    Call<ResponseDataLeads> getLeadsData(@Url String url);

    @GET()
    Call<ResponseDataLeadsListing> getLeadsLmsData(@Url String url);
}
