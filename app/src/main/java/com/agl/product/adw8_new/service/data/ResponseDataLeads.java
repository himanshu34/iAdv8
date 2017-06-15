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
    private ArrayList<Leads> leadsList;

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

    public ArrayList<Leads> getLeadsList() {
        return leadsList;
    }

    public void setLeadsList(ArrayList<Leads> leadsList) {
        this.leadsList = leadsList;
    }
}
