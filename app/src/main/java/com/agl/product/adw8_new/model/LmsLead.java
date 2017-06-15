package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LmsLead {

    @SerializedName("business_unit_name")
    private String business_unit_name;

    @SerializedName("lead_type")
    private String lead_type;

    @SerializedName("last_followup")
    private String last_followup;

    @SerializedName("next_followup")
    private String next_followup;

    @SerializedName("owner")
    private String owner;

    @SerializedName("lead_id")
    private String lead_id;

    @SerializedName("submitted_on")
    private String submitted_on;

    @SerializedName("lead_age")
    private String lead_age;

    @SerializedName("lead_status")
    private String lead_status;

    @SerializedName("fields_data")
    private ArrayList<FieldsData> fieldsDataList;

    @SerializedName("IP")
    private String IP;

    public String getBusiness_unit_name() {
        return business_unit_name;
    }

    public void setBusiness_unit_name(String business_unit_name) {
        this.business_unit_name = business_unit_name;
    }

    public String getLead_type() {
        return lead_type;
    }

    public void setLead_type(String lead_type) {
        this.lead_type = lead_type;
    }

    public String getLast_followup() {
        return last_followup;
    }

    public void setLast_followup(String last_followup) {
        this.last_followup = last_followup;
    }

    public String getNext_followup() {
        return next_followup;
    }

    public void setNext_followup(String next_followup) {
        this.next_followup = next_followup;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getSubmitted_on() {
        return submitted_on;
    }

    public void setSubmitted_on(String submitted_on) {
        this.submitted_on = submitted_on;
    }

    public String getLead_age() {
        return lead_age;
    }

    public void setLead_age(String lead_age) {
        this.lead_age = lead_age;
    }

    public String getLead_status() {
        return lead_status;
    }

    public void setLead_status(String lead_status) {
        this.lead_status = lead_status;
    }

    public ArrayList<FieldsData> getFieldsDataList() {
        return fieldsDataList;
    }

    public void setFieldsDataList(ArrayList<FieldsData> fieldsDataList) {
        this.fieldsDataList = fieldsDataList;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
}
