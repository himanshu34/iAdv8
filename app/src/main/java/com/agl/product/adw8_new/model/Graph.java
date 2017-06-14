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

    private boolean is_clicked = false;

    public Graph(String key, ArrayList<GraphView> graphViewList, String total, boolean is_clicked) {
        this.key = key;
        this.graphViewList = graphViewList;
        this.total = total;
        this.is_clicked = is_clicked;
    }

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

    public boolean is_clicked() {
        return is_clicked;
    }

    public void setIs_clicked(boolean is_clicked) {
        this.is_clicked = is_clicked;
    }
}
