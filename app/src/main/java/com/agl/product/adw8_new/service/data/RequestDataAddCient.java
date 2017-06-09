package com.agl.product.adw8_new.service.data;

import com.google.gson.annotations.SerializedName;

public class RequestDataAddCient {

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("userEmail")
    private String userEmail;

    @SerializedName("name")
    private String name;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
