package com.agl.product.adw8_new.service.data;


import com.google.gson.annotations.SerializedName;

public class RequestDataAdgroup {

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

    @SerializedName("sortBy")
    private String sortBy;

    @SerializedName("orderBy")
    private String orderBy;

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

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
