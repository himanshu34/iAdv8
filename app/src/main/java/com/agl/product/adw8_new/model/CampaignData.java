package com.agl.product.adw8_new.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CampaignData implements Parcelable {

    @SerializedName("advertising_channel")
    private String advertising_channel;

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

    public CampaignData() {

    }

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

    public String getAvg_cpc() {
        return avg_cpc;
    }

    public void setAvg_cpc(String avg_cpc) {
        this.avg_cpc = avg_cpc;
    }

    public String getCampaign_state() {
        return campaign_state;
    }

    public void setCampaign_state(String campaign_state) {
        this.campaign_state = campaign_state;
    }

    public String getGcid() {
        return gcid;
    }

    public void setGcid(String gcid) {
        this.gcid = gcid;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }

    public String getAvg_position() {
        return avg_position;
    }

    public void setAvg_position(String avg_position) {
        this.avg_position = avg_position;
    }

    public String getAdvertising_channel() {
        return advertising_channel;
    }

    public void setAdvertising_channel(String advertising_channel) {
        this.advertising_channel = advertising_channel;
    }

    public CampaignData(Parcel in) {
        this();
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.advertising_channel = in.readString();
        this.campaign = in.readString();
        this.budget = in.readString();
        this.clicks = in.readString();
        this.impressions = in.readString();
        this.cost = in.readString();
        this.ctr = in.readString();
        this.avg_cpc = in.readString();
        this.avg_position = in.readString();
        this.campaign_id = in.readString();
        this.gcid = in.readString();
        this.campaign_state = in.readString();
        this.converted_clicks = in.readString();
        this.cpa = in.readString();
        this.currency = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(advertising_channel);
        parcel.writeString(campaign);
        parcel.writeString(budget);
        parcel.writeString(clicks);
        parcel.writeString(impressions);
        parcel.writeString(cost);
        parcel.writeString(ctr);
        parcel.writeString(avg_cpc);
        parcel.writeString(avg_position);
        parcel.writeString(campaign_id);
        parcel.writeString(gcid);
        parcel.writeString(campaign_state);
        parcel.writeString(converted_clicks);
        parcel.writeString(cpa);
        parcel.writeString(currency);
    }

    public static final Creator<CampaignData> CREATOR = new Creator<CampaignData>() {
        public CampaignData createFromParcel(Parcel in) {
            return new CampaignData(in);
        }

        public CampaignData[] newArray(int size) {
            return new CampaignData[size];
        }
    };


}
