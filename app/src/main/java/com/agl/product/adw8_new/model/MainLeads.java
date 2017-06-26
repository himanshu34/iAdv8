package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class MainLeads implements Serializable {
    @SerializedName("status")
    private String status;

    @SerializedName("status_id")
    private String status_id;

    @SerializedName("cnt")
    private String cnt;

    private boolean isChecked;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
