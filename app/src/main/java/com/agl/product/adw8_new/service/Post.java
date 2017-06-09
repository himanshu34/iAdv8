package com.agl.product.adw8_new.service;

import com.agl.product.adw8_new.service.data.RequestDataAddCient;
import com.agl.product.adw8_new.service.data.RequestDataLogin;
import com.agl.product.adw8_new.service.data.ResponseDataAddCient;
import com.agl.product.adw8_new.service.data.ResponseDataLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Post {

    @POST("user/login")
    Call<ResponseDataLogin> loginVerification(@Body RequestDataLogin dataLogin);

    @POST("client/add_client")
    Call<ResponseDataAddCient> addClient(@Body RequestDataAddCient dataAddCient);
}