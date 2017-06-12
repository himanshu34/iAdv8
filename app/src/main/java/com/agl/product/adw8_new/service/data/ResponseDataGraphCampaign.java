package com.agl.product.adw8_new.service.data;

import com.agl.product.adw8_new.model.Graph;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDataGraphCampaign {

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<Graph> graphList;

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

    public ArrayList<Graph> getGraphList() {
        return graphList;
    }

    public void setGraphList(ArrayList<Graph> graphList) {
        this.graphList = graphList;
    }
}
