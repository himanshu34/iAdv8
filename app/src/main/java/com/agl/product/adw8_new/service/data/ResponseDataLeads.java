package com.agl.product.adw8_new.service.data;

import com.agl.product.adw8_new.model.Leads;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDataLeads {

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Leads leads;

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

    public Leads getLeads() {
        return leads;
    }

    public void setLeads(Leads leads) {
        this.leads = leads;
    }
}
