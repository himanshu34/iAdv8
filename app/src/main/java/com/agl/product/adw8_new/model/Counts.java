package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

public class Counts {

    @SerializedName("key")
    private String key;

    @SerializedName("value")
    private String value;

    public Counts(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
