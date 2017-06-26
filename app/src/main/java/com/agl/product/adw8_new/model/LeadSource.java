package com.agl.product.adw8_new.model;


import com.google.gson.annotations.SerializedName;

public class LeadSource {

    @SerializedName("utm_source")
    private String utm_source;

    @SerializedName("Proposal Sent")
    private String prosopalSent;


    @SerializedName("Closed Won")
    private String closedWon;

    @SerializedName("Rejected")
    private String rejected;


    @SerializedName("Closed Lost")
    private String closedLost;

    @SerializedName("total")
    private String total;

    public String getUtm_source() {
        return utm_source;
    }

    public void setUtm_source(String utm_source) {
        this.utm_source = utm_source;
    }

    public String getProsopalSent() {
        return prosopalSent;
    }

    public void setProsopalSent(String prosopalSent) {
        this.prosopalSent = prosopalSent;
    }

    public String getClosedWon() {
        return closedWon;
    }

    public void setClosedWon(String closedWon) {
        this.closedWon = closedWon;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }

    public String getClosedLost() {
        return closedLost;
    }

    public void setClosedLost(String closedLost) {
        this.closedLost = closedLost;
    }
}
