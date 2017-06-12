package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Graph {

    @SerializedName("key")
    private String key;

    @SerializedName("data")
    private ArrayList<GraphView> graphViewList;

    @SerializedName("total")
    private String total;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<GraphView> getGraphViewList() {
        return graphViewList;
    }

    public void setGraphViewList(ArrayList<GraphView> graphViewList) {
        this.graphViewList = graphViewList;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
