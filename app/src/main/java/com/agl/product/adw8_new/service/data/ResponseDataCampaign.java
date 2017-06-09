package com.agl.product.adw8_new.service.data;

import com.agl.product.adw8_new.service.data.campaign_model.CampaignData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDataCampaign {

    @SerializedName("adCount")
    private String adCount;

    @SerializedName("keywordCount")
    private String keywordCount;

    @SerializedName("CampaignCount")
    private String CampaignCount;

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("camapign_data")
    private ArrayList<CampaignData> camapign_data;

    @SerializedName("keyword_data")
    private ArrayList<CampaignData> keyword_data;

    @SerializedName("ads_data")
    private ArrayList<CampaignData> ads_data;

    public String getAdCount() {
        return adCount;
    }

    public void setAdCount(String adCount) {
        this.adCount = adCount;
    }

    public String getKeywordCount() {
        return keywordCount;
    }

    public void setKeywordCount(String keywordCount) {
        this.keywordCount = keywordCount;
    }

    public String getCampaignCount() {
        return CampaignCount;
    }

    public void setCampaignCount(String campaignCount) {
        CampaignCount = campaignCount;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<CampaignData> getCamapign_data() {
        return camapign_data;
    }

    public void setCamapign_data(ArrayList<CampaignData> camapign_data) {
        this.camapign_data = camapign_data;
    }

    public ArrayList<CampaignData> getKeyword_data() {
        return keyword_data;
    }

    public void setKeyword_data(ArrayList<CampaignData> keyword_data) {
        this.keyword_data = keyword_data;
    }

    public ArrayList<CampaignData> getAds_data() {
        return ads_data;
    }

    public void setAds_data(ArrayList<CampaignData> ads_data) {
        this.ads_data = ads_data;
    }
}
