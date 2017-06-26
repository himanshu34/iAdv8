package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("status_id")
    private String status_id;

    @SerializedName("status_name")
    private String status_name;

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }
}
