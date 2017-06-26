package com.agl.product.adw8_new.service.data;


import com.agl.product.adw8_new.model.LeadsGraphData;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ResponseLeadsGraph {

    @SerializedName("data")
    private ArrayList<LeadsGraphData> data;

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    public ArrayList<LeadsGraphData> getData() {
        return data;
    }

    public void setData(ArrayList<LeadsGraphData> data) {
        this.data = data;
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
}
