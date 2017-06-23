package com.agl.product.adw8_new.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InsightGroupType implements Serializable {

    @SerializedName("group_name")
    private String group_name;

    @SerializedName("id")
    private String id;

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
