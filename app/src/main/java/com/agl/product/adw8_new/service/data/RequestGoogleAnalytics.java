package com.agl.product.adw8_new.service.data;


import com.google.gson.annotations.SerializedName;

public class RequestGoogleAnalytics  {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("cid")
    private String cid;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
