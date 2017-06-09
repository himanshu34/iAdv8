package com.agl.product.adw8_new.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Keywords implements Parcelable {

    @SerializedName("adgroup_id")
    private String adgroup_id;

    @SerializedName("keyword_state")
    private String keyword_state;

    @SerializedName("currency")
    private String currency;

    @SerializedName("id")
    private String id;

    @SerializedName("match_type")
    private String match_type;

    @SerializedName("keyword_id")
    private String keyword_id;

    @SerializedName("keyword_name")
    private String keyword_name;

    @SerializedName("clicks")
    private String clicks;

    @SerializedName("impressions")
    private String impressions;

    @SerializedName("converted_clicks")
    private String converted_clicks;

    @SerializedName("cost_per_conversion")
    private String cost_per_conversion;

    @SerializedName("cost")
    private String cost;

    @SerializedName("publisher_account_id")
    private String publisher_account_id;

    @SerializedName("businessunitid")
    private String businessunitid;

    @SerializedName("business_unit_name")
    private String business_unit_name;

    @SerializedName("avg_cpc")
    private String avg_cpc;

    @SerializedName("ctr")
    private String ctr;

    @SerializedName("cpa")
    private String cpa;

    public Keywords() {

    }

    public String getAdgroup_id() {
        return adgroup_id;
    }

    public void setAdgroup_id(String adgroup_id) {
        this.adgroup_id = adgroup_id;
    }

    public String getKeyword_state() {
        return keyword_state;
    }

    public void setKeyword_state(String keyword_state) {
        this.keyword_state = keyword_state;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatch_type() {
        return match_type;
    }

    public void setMatch_type(String match_type) {
        this.match_type = match_type;
    }

    public String getKeyword_id() {
        return keyword_id;
    }

    public void setKeyword_id(String keyword_id) {
        this.keyword_id = keyword_id;
    }

    public String getKeyword_name() {
        return keyword_name;
    }

    public void setKeyword_name(String keyword_name) {
        this.keyword_name = keyword_name;
    }

    public String getClicks() {
        return clicks;
    }

    public void setClicks(String clicks) {
        this.clicks = clicks;
    }

    public String getImpressions() {
        return impressions;
    }

    public void setImpressions(String impressions) {
        this.impressions = impressions;
    }

    public String getConverted_clicks() {
        return converted_clicks;
    }

    public void setConverted_clicks(String converted_clicks) {
        this.converted_clicks = converted_clicks;
    }

    public String getCost_per_conversion() {
        return cost_per_conversion;
    }

    public void setCost_per_conversion(String cost_per_conversion) {
        this.cost_per_conversion = cost_per_conversion;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPublisher_account_id() {
        return publisher_account_id;
    }

    public void setPublisher_account_id(String publisher_account_id) {
        this.publisher_account_id = publisher_account_id;
    }

    public String getBusinessunitid() {
        return businessunitid;
    }

    public void setBusinessunitid(String businessunitid) {
        this.businessunitid = businessunitid;
    }

    public String getBusiness_unit_name() {
        return business_unit_name;
    }

    public void setBusiness_unit_name(String business_unit_name) {
        this.business_unit_name = business_unit_name;
    }

    public String getAvg_cpc() {
        return avg_cpc;
    }

    public void setAvg_cpc(String avg_cpc) {
        this.avg_cpc = avg_cpc;
    }

    public String getCtr() {
        return ctr;
    }

    public void setCtr(String ctr) {
        this.ctr = ctr;
    }

    public String getCpa() {
        return cpa;
    }

    public void setCpa(String cpa) {
        this.cpa = cpa;
    }

    public Keywords(Parcel in) {
        this();
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.adgroup_id = in.readString();
        this.keyword_state = in.readString();
        this.currency = in.readString();
        this.id = in.readString();
        this.match_type = in.readString();
        this.keyword_id = in.readString();
        this.keyword_name = in.readString();
        this.clicks = in.readString();
        this.impressions = in.readString();
        this.converted_clicks = in.readString();
        this.cost_per_conversion = in.readString();
        this.cost = in.readString();
        this.publisher_account_id = in.readString();
        this.businessunitid = in.readString();
        this.business_unit_name = in.readString();
        this.avg_cpc = in.readString();
        this.ctr = in.readString();
        this.cpa = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(adgroup_id);
        parcel.writeString(keyword_state);
        parcel.writeString(currency);
        parcel.writeString(id);
        parcel.writeString(match_type);
        parcel.writeString(keyword_id);
        parcel.writeString(keyword_name);
        parcel.writeString(clicks);
        parcel.writeString(impressions);
        parcel.writeString(converted_clicks);
        parcel.writeString(cost_per_conversion);
        parcel.writeString(cost);
        parcel.writeString(publisher_account_id);
        parcel.writeString(businessunitid);
        parcel.writeString(business_unit_name);
        parcel.writeString(avg_cpc);
        parcel.writeString(ctr);
        parcel.writeString(cpa);
    }

    public static final Creator<Keywords> CREATOR = new Creator<Keywords>() {
        public Keywords createFromParcel(Parcel in) {
            return new Keywords(in);
        }

        public Keywords[] newArray(int size) {
            return new Keywords[size];
        }
    };
}
