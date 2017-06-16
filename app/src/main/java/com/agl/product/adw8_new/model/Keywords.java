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

    @SerializedName("cost")
    private String cost;

    @SerializedName("conversion_rate")
    private String conversion_rate;

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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
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
        this.conversion_rate = in.readString();
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
        this.cost = in.readString();
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
        parcel.writeString(conversion_rate);
        parcel.writeString(keyword_name);
        parcel.writeString(clicks);
        parcel.writeString(impressions);
        parcel.writeString(converted_clicks);
        parcel.writeString(cost);
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

    public String getConversion_rate() {
        return conversion_rate;
    }

    public void setConversion_rate(String conversion_rate) {
        this.conversion_rate = conversion_rate;
    }
}
