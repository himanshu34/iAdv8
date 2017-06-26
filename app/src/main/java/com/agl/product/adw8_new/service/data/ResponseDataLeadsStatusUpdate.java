package com.agl.product.adw8_new.service.data;

import com.google.gson.annotations.SerializedName;

public class ResponseDataLeadsStatusUpdate {

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

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
}
