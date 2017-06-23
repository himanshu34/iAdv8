package com.agl.product.adw8_new.service.data;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestInsightData implements Serializable {

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("profile_id")
    private String profile_id;

    @SerializedName("fDate")
    private String fDate;

    @SerializedName("tDate")
    private String tDate;


    @SerializedName("offset")
    private String offset;


    @SerializedName("row_count")
    private String row_count;

    @SerializedName("order")
    private String order;

    @SerializedName("cuId")
    private String cuId;


    @SerializedName("cId")
    private String cId;

    @SerializedName("group_type_id")
    private String group_type_id;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
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

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getRow_count() {
        return row_count;
    }

    public void setRow_count(String row_count) {
        this.row_count = row_count;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCuId() {
        return cuId;
    }

    public void setCuId(String cuId) {
        this.cuId = cuId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getGroup_type_id() {
        return group_type_id;
    }

    public void setGroup_type_id(String group_type_id) {
        this.group_type_id = group_type_id;
    }
}
