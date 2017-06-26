package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class LeadsGraphData implements Serializable {

    @SerializedName("key")
    private String key;

    @SerializedName("hint")
    private String hint;

    @SerializedName("statusData")
    private ArrayList<LeadsStatus> statusData;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public ArrayList<LeadsStatus> getStatusData() {
        return statusData;
    }

    public void setStatusData(ArrayList<LeadsStatus> statusData) {
        this.statusData = statusData;
    }
}
