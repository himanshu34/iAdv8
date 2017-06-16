package com.agl.product.adw8_new.service.data;

import com.agl.product.adw8_new.model.LeadsDetail;
import com.google.gson.annotations.SerializedName;

public class ResponseDataLeadsDetails {

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private LeadsDetail leadsDetail;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LeadsDetail getLeadsDetail() {
        return leadsDetail;
    }

    public void setLeadsDetail(LeadsDetail leadsDetail) {
        this.leadsDetail = leadsDetail;
    }
}
