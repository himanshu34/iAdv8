package com.agl.product.adw8_new.service.data;


import com.agl.product.adw8_new.model.Adgroup;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDataAdgroup {

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<Adgroup> data;

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

    public ArrayList<Adgroup> getData() {
        return data;
    }

    public void setData(ArrayList<Adgroup> data) {
        this.data = data;
    }
}
