package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Leads implements Serializable{

    @SerializedName("main")
    private  ArrayList<MainLeads> main;

    @SerializedName("additional")
    private  ArrayList<MainLeads> additional;

    @SerializedName("status")
    private String status;

    @SerializedName("cnt")
    private String cnt;

    @SerializedName("status_id")
    private String status_id;

    @SerializedName("per")
    private String per;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public ArrayList<MainLeads> getMain() {
        return main;
    }

    public void setMain(ArrayList<MainLeads> main) {
        this.main = main;
    }

    public ArrayList<MainLeads> getAdditional() {
        return additional;
    }

    public void setAdditional(ArrayList<MainLeads> additional) {
        this.additional = additional;
    }
}
