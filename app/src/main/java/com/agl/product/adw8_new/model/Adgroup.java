package com.agl.product.adw8_new.model;


import com.google.gson.annotations.SerializedName;

public class Adgroup {

    @SerializedName("adgroup")
    private String adgroup;

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

    @SerializedName("avg_cpc")
    private String avg_cpc;

    @SerializedName("avg_position")
    private String avg_position;

    @SerializedName("conversion_rate")
    private String conversion_rate;



    @SerializedName("cpa")
    private String cpa;

    @SerializedName("currency")
    private String currency;

    public String getAdgroup() {
        return adgroup;
    }

    public void setAdgroup(String adgroup) {
        this.adgroup = adgroup;
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCtr() {
        return ctr;
    }

    public void setCtr(String ctr) {
        this.ctr = ctr;
    }

    public String getConverted_clicks() {
        return converted_clicks;
    }

    public void setConverted_clicks(String converted_clicks) {
        this.converted_clicks = converted_clicks;
    }

    public String getCpa() {
        return cpa;
    }

    public void setCpa(String cpa) {
        this.cpa = cpa;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
