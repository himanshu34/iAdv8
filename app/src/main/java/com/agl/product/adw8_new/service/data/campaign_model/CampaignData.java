package com.agl.product.adw8_new.service.data.campaign_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class CampaignData implements Serializable {

    @SerializedName("campaign")
    private String campaign;

    @SerializedName("budget")
    private String budget;

    @SerializedName("clicks")
    private String clicks;

    @SerializedName("impressions")
    private String impressions;

    @SerializedName("cost")
    private String cost;

    @SerializedName("ctr")
    private String ctr;

    @SerializedName("avg_cpc")
    private String avg_cpc;

    @SerializedName("avg_position")
    private String avg_position;

    @SerializedName("campaign_id")
    private String campaign_id;

    @SerializedName("gcid")
    private String gcid;

    @SerializedName("campaign_state")
    private String campaign_state;







    @SerializedName("converted_clicks")
    private String converted_clicks;


    @SerializedName("cpa")
    private String cpa;

    @SerializedName("currency")
    private String currency;

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
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
}
