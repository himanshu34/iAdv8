package com.agl.product.adw8_new.service.data;


import com.agl.product.adw8_new.model.InsightData;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseInsightData implements Serializable {

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private InsightData data;

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

    public InsightData getData() {
        return data;
    }

    public void setData(InsightData data) {
        this.data = data;
    }
}
