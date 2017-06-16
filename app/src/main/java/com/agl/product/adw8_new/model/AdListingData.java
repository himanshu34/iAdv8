package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class AdListingData implements Serializable {


    @SerializedName("ad")
    private String ad;

    @SerializedName("clicks")
    private String clicks;

    @SerializedName("impressions")
    private String impressions;

    @SerializedName("cost")
    private String cost;

    @SerializedName("ctr")
    private String ctr;


    @SerializedName("converted_clicks")
    private String converted_clicks;

    @SerializedName("cpa")
    private String cpa;

    @SerializedName("avg_cpc")
    private String avg_cpc;

    @SerializedName("avg_position")
    private String avg_position;

    @SerializedName("conversion_rate")
    private String conversion_rate;



    @SerializedName("currency")
    private String currency;

    @SerializedName("ad_type_custom")
    private String ad_type_custom;

    public String getAd_type_custom() {
        return ad_type_custom;
    }

    public void setAd_type_custom(String ad_type_custom) {
        this.ad_type_custom = ad_type_custom;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCpa() {
        return cpa;
    }

    public void setCpa(String cpa) {
        this.cpa = cpa;
    }

    public String getConverted_clicks() {
        return converted_clicks;
    }

    public void setConverted_clicks(String converted_clicks) {
        this.converted_clicks = converted_clicks;
    }

    public String getCtr() {
        return ctr;
    }

    public void setCtr(String ctr) {
        this.ctr = ctr;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getImpressions() {
        return impressions;
    }

    public void setImpressions(String impressions) {
        this.impressions = impressions;
    }

    public String getClicks() {
        return clicks;
    }

    public void setClicks(String clicks) {
        this.clicks = clicks;
    }

    public String getAvg_cpc() {
        return avg_cpc;
    }

    public void setAvg_cpc(String avg_cpc) {
        this.avg_cpc = avg_cpc;
    }

    public String getAvg_position() {
        return avg_position;
    }

    public void setAvg_position(String avg_position) {
        this.avg_position = avg_position;
    }

    public String getConversion_rate() {
        return conversion_rate;
    }

    public void setConversion_rate(String conversion_rate) {
        this.conversion_rate = conversion_rate;
    }
}
