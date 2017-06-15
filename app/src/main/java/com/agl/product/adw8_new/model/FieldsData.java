package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

public class FieldsData {

    @SerializedName("lable")
    private String lable;

    @SerializedName("value")
    private String value;

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
