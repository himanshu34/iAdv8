package com.agl.product.adw8_new.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InsightDimension implements Serializable{

    @SerializedName("dimension")
    private String dimension;

    @SerializedName("new")
    private String newData;

    @SerializedName("org_src")
    private String org_src;

    @SerializedName("sess")
    private String sess;


    @SerializedName("users")
    private String users;

    @SerializedName("visits")
    private String visits;

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public String getOrg_src() {
        return org_src;
    }

    public void setOrg_src(String org_src) {
        this.org_src = org_src;
    }

    public String getSess() {
        return sess;
    }

    public void setSess(String sess) {
        this.sess = sess;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getVisits() {
        return visits;
    }

    public void setVisits(String visits) {
        this.visits = visits;
    }
}
