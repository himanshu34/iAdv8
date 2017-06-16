package com.agl.product.adw8_new.service.data;

import com.agl.product.adw8_new.model.CampaignData;
import com.agl.product.adw8_new.model.Total;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseDataCampaignDetails implements Serializable {

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<CampaignData> data;

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    @SerializedName("total")

    private Total total;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<CampaignData> getData() {
        return data;
    }

    public void setData(ArrayList<CampaignData> data) {
        this.data = data;
    }
}
