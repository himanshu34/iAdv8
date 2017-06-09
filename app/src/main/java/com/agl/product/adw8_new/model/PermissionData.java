package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

public class PermissionData {

    @SerializedName("PAID")
    private boolean PAID;

    @SerializedName("LMS")
    private boolean LMS;

    @SerializedName("SEO")
    private boolean SEO;

    @SerializedName("BILLING")
    private boolean BILLING;

    @SerializedName("ANALYTICS")
    private boolean ANALYTICS;

    @SerializedName("TASK")
    private boolean TASK;

    public boolean isPAID() {
        return PAID;
    }

    public void setPAID(boolean PAID) {
        this.PAID = PAID;
    }

    public boolean isLMS() {
        return LMS;
    }

    public void setLMS(boolean LMS) {
        this.LMS = LMS;
    }

    public boolean isSEO() {
        return SEO;
    }

    public void setSEO(boolean SEO) {
        this.SEO = SEO;
    }

    public boolean isBILLING() {
        return BILLING;
    }

    public void setBILLING(boolean BILLING) {
        this.BILLING = BILLING;
    }

    public boolean isANALYTICS() {
        return ANALYTICS;
    }

    public void setANALYTICS(boolean ANALYTICS) {
        this.ANALYTICS = ANALYTICS;
    }

    public boolean isTASK() {
        return TASK;
    }

    public void setTASK(boolean TASK) {
        this.TASK = TASK;
    }


}
