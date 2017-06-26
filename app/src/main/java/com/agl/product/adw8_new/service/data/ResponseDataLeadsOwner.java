package com.agl.product.adw8_new.service.data;

import com.agl.product.adw8_new.model.Owner;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDataLeadsOwner {

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<Owner> ownersList;

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

    public ArrayList<Owner> getOwnersList() {
        return ownersList;
    }

    public void setOwnersList(ArrayList<Owner> ownersList) {
        this.ownersList = ownersList;
    }
}
