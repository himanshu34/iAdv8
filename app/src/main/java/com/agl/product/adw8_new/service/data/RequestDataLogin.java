package com.agl.product.adw8_new.service.data;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;

public class RequestDataLogin {

    @SerializedName("sKeys")
    private String sKeys;

    @SerializedName("userEmail")
    private String userEmail;

    @SerializedName("password")
    private String password;

    public String getsKeys() {
        return sKeys;
    }

    public void setsKeys(String sKeys) {
        this.sKeys = sKeys;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
