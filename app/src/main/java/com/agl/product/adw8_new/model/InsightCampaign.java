package com.agl.product.adw8_new.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InsightCampaign  implements Serializable {

    @SerializedName("campaignName")
    private String campaignName;

    @SerializedName("id")
    private String id;

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
