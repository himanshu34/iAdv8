package com.agl.product.adw8_new.service.data;

import com.agl.product.adw8_new.model.LmsLead;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDataLeadsListing {

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<LmsLead> lmsLeadsList;

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

    public ArrayList<LmsLead> getLmsLeadsList() {
        return lmsLeadsList;
    }

    public void setLmsLeadsList(ArrayList<LmsLead> lmsLeadsList) {
        this.lmsLeadsList = lmsLeadsList;
    }
}
