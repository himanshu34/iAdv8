package com.agl.product.adw8_new.service.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestDataCampaign {

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("pId")
    private String pId;

    @SerializedName("cId")
    private String cId;

    @SerializedName("fDate")
    private String fDate;

    @SerializedName("tDate")
    private String tDate;

    @SerializedName("limit")
    private String limit;

    @SerializedName("sort")
    private String sort;

    @SerializedName("order")
    private String order;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getfDate() {
        return fDate;
    }

    public void setfDate(String fDate) {
        this.fDate = fDate;
    }

    public String gettDate() {
        return tDate;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
