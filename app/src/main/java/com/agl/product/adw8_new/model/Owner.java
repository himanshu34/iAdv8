package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("owner_id")
    private String owner_id;

    @SerializedName("owner")
    private String owner;

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
