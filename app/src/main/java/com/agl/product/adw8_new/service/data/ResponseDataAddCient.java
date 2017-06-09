package com.agl.product.adw8_new.service.data;

import com.google.gson.annotations.SerializedName;

public class ResponseDataAddCient {

    @SerializedName("cId")
    private String clientId;

    @SerializedName("userPassword")
    private String userPassword;

    @SerializedName("userEmail")
    private String userEmail;

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

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
