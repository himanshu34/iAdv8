package com.agl.product.adw8_new.service.data;


import com.agl.product.adw8_new.model.Keywords;
import com.agl.product.adw8_new.model.Total;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDataKeywords {

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<Keywords> data;

    @SerializedName("total")
    private Total total;

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

    public ArrayList<Keywords> getData() {
        return data;
    }

    public void setData(ArrayList<Keywords> data) {
        this.data = data;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }
}
