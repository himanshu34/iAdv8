package com.agl.product.adw8_new.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class InsightData implements Serializable {

    @SerializedName("Compaigns")
    private ArrayList<InsightCampaign> Compaigns;

    @SerializedName("Dimensions")
    private ArrayList<InsightDimension> Dimensions;

    @SerializedName("group_type")
    private ArrayList<InsightGroupType> group_type;

    @SerializedName("total")
    private ArrayList<InsightTotal> total;

    public ArrayList<InsightCampaign> getCompaigns() {
        return Compaigns;
    }

    public void setCompaigns(ArrayList<InsightCampaign> compaigns) {
        Compaigns = compaigns;
    }

    public ArrayList<InsightDimension> getDimensions() {
        return Dimensions;
    }

    public void setDimensions(ArrayList<InsightDimension> dimensions) {
        Dimensions = dimensions;
    }

    public ArrayList<InsightGroupType> getGroup_type() {
        return group_type;
    }

    public void setGroup_type(ArrayList<InsightGroupType> group_type) {
        this.group_type = group_type;
    }

    public ArrayList<InsightTotal> getTotal() {
        return total;
    }

    public void setTotal(ArrayList<InsightTotal> total) {
        this.total = total;
    }
}
