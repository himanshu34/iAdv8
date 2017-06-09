package com.agl.product.adw8_new.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ads implements Parcelable {

    @SerializedName("ad")
    private String ad;

    @SerializedName("ad_id")
    private String ad_id;

    @SerializedName("description1")
    private String description1;

    @SerializedName("description2")
    private String description2;

    @SerializedName("clicks")
    private String clicks;

    @SerializedName("impressions")
    private String impressions;

    @SerializedName("converted_clicks")
    private String converted_clicks;

    @SerializedName("cost")
    private String cost;

    @SerializedName("avg_cpc")
    private String avg_cpc;

    @SerializedName("ctr")
    private String ctr;

    @SerializedName("cpa")
    private String cpa;

    public Ads() {

    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
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

    public Ads(Parcel in) {
        this();
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.ad = in.readString();
        this.ad_id = in.readString();
        this.description1 = in.readString();
        this.description2 = in.readString();
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
        parcel.writeString(ad);
        parcel.writeString(ad_id);
        parcel.writeString(description1);
        parcel.writeString(description2);
        parcel.writeString(clicks);
        parcel.writeString(impressions);
        parcel.writeString(converted_clicks);
        parcel.writeString(cost);
        parcel.writeString(avg_cpc);
        parcel.writeString(ctr);
        parcel.writeString(cpa);
    }

    public static final Creator<Ads> CREATOR = new Creator<Ads>() {
        public Ads createFromParcel(Parcel in) {
            return new Ads(in);
        }

        public Ads[] newArray(int size) {
            return new Ads[size];
        }
    };
}
