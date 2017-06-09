package com.agl.product.adw8_new.service.data;

import com.agl.product.adw8_new.model.Ads;
import com.agl.product.adw8_new.model.CampaignData;
import com.agl.product.adw8_new.model.Counts;
import com.agl.product.adw8_new.model.Keywords;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDataCampaign {

    @SerializedName("count")
    private ArrayList<Counts> countsList;

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("camapign_data")
    private ArrayList<CampaignData> camapign_data;

    @SerializedName("keyword_data")
    private ArrayList<Keywords> keyword_data;

    @SerializedName("ads_data")
    private ArrayList<Ads> ads_data;

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

    public ArrayList<Keywords> getKeyword_data() {
        return keyword_data;
    }

    public void setKeyword_data(ArrayList<Keywords> keyword_data) {
        this.keyword_data = keyword_data;
    }

    public ArrayList<Ads> getAds_data() {
        return ads_data;
    }

    public void setAds_data(ArrayList<Ads> ads_data) {
        this.ads_data = ads_data;
    }

    public ArrayList<Counts> getCountsList() {
        return countsList;
    }

    public void setCountsList(ArrayList<Counts> countsList) {
        this.countsList = countsList;
    }
}
