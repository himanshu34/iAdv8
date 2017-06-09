package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("id")
    private String id;

    @SerializedName("agency_client_id")
    private String agency_client_id;

    @SerializedName("agency_id")
    private String agency_id;

    @SerializedName("user_name")
    private String user_name;

    @SerializedName("user_email")
    private String user_email;

    @SerializedName("user_role")
    private String user_role;

    @SerializedName("status")
    private String status;

    @SerializedName("user_password")
    private String user_password;

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("hrm_user_id")
    private String hrm_user_id;

    @SerializedName("profile_id")
    private String profile_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgency_client_id() {
        return agency_client_id;
    }

    public void setAgency_client_id(String agency_client_id) {
        this.agency_client_id = agency_client_id;
    }

    public String getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(String agency_id) {
        this.agency_id = agency_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getHrm_user_id() {
        return hrm_user_id;
    }

    public void setHrm_user_id(String hrm_user_id) {
        this.hrm_user_id = hrm_user_id;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }
}
