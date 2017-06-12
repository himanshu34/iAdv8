package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

public class GraphView {

    @SerializedName("date")
    private String date;

    @SerializedName("count")
    private String count;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
