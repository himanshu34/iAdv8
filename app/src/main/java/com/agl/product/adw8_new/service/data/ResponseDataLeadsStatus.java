package com.agl.product.adw8_new.service.data;

import com.agl.product.adw8_new.model.Status;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDataLeadsStatus {

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<Status> statusList;

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

    public ArrayList<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(ArrayList<Status> statusList) {
        this.statusList = statusList;
    }

}
