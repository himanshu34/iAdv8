package com.agl.product.adw8_new.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LeadsDetail {

    @SerializedName("form_type")
    private String form_type;

    @SerializedName("visible_fields")
    private String visible_fields;

    @SerializedName("business_unit_name")
    private String business_unit_name;

    @SerializedName("business_unit_id")
    private String business_unit_id;

    @SerializedName("landing_page_name")
    private String landing_page_name;

    @SerializedName("landing_page_url")
    private String landing_page_url;

    @SerializedName("landing_page_id")
    private String landing_page_id;

    @SerializedName("client")
    private String client;

    @SerializedName("lead_id")
    private String lead_id;

    @SerializedName("submitted_on")
    private String submitted_on;

    @SerializedName("fields_data")
    private ArrayList<FieldsData> fieldsDataList;

    @SerializedName("utm_source")
    private String utm_source;

    @SerializedName("utm_medium")
    private String utm_medium;

    @SerializedName("utm_campaignid")
    private String utm_campaignid;

    @SerializedName("utm_keyword")
    private String utm_keyword;

    @SerializedName("lead_type")
    private String lead_type;

    @SerializedName("ctn")
    private String ctn;

    @SerializedName("calling")
    private String calling;

    @SerializedName("forwarding")
    private String forwarding;

    @SerializedName("recording")
    private String recording;

    @SerializedName("call_duration")
    private String call_duration;

    @SerializedName("shared_with")
    private String shared_with;

    @SerializedName("owner")
    private String owner;

    @SerializedName("user_designation")
    private String user_designation;

    @SerializedName("owner_id")
    private String owner_id;

    @SerializedName("lead_status")
    private String lead_status;

    @SerializedName("status_id")
    private String status_id;

    @SerializedName("lead_age")
    private String lead_age;

    @SerializedName("last_followup")
    private String last_followup;

    @SerializedName("next_followup")
    private String next_followup;

    @SerializedName("social_data")
    private SocialData social_data;

    @SerializedName("cu_visible")
    private String cu_visible;

    @SerializedName("cu_editable")
    private String cu_editable;

    @SerializedName("lp_visible")
    private String lp_visible;

    @SerializedName("lp_editable")
    private String lp_editable;

    @SerializedName("status_visible")
    private String status_visible;

    @SerializedName("status_editable")
    private String status_editable;

    @SerializedName("owner_visible")
    private String owner_visible;

    @SerializedName("owner_editable")
    private String owner_editable;

    @SerializedName("shared_with_visible")
    private String shared_with_visible;

    @SerializedName("shared_with_editable")
    private String shared_with_editable;

    public String getForm_type() {
        return form_type;
    }

    public void setForm_type(String form_type) {
        this.form_type = form_type;
    }

    public String getVisible_fields() {
        return visible_fields;
    }

    public void setVisible_fields(String visible_fields) {
        this.visible_fields = visible_fields;
    }

    public String getBusiness_unit_name() {
        return business_unit_name;
    }

    public void setBusiness_unit_name(String business_unit_name) {
        this.business_unit_name = business_unit_name;
    }

    public String getBusiness_unit_id() {
        return business_unit_id;
    }

    public void setBusiness_unit_id(String business_unit_id) {
        this.business_unit_id = business_unit_id;
    }

    public String getLanding_page_name() {
        return landing_page_name;
    }

    public void setLanding_page_name(String landing_page_name) {
        this.landing_page_name = landing_page_name;
    }

    public String getLanding_page_url() {
        return landing_page_url;
    }

    public void setLanding_page_url(String landing_page_url) {
        this.landing_page_url = landing_page_url;
    }

    public String getLanding_page_id() {
        return landing_page_id;
    }

    public void setLanding_page_id(String landing_page_id) {
        this.landing_page_id = landing_page_id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
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

    public String getUtm_source() {
        return utm_source;
    }

    public void setUtm_source(String utm_source) {
        this.utm_source = utm_source;
    }

    public String getUtm_medium() {
        return utm_medium;
    }

    public void setUtm_medium(String utm_medium) {
        this.utm_medium = utm_medium;
    }

    public String getUtm_campaignid() {
        return utm_campaignid;
    }

    public void setUtm_campaignid(String utm_campaignid) {
        this.utm_campaignid = utm_campaignid;
    }

    public String getUtm_keyword() {
        return utm_keyword;
    }

    public void setUtm_keyword(String utm_keyword) {
        this.utm_keyword = utm_keyword;
    }

    public String getLead_type() {
        return lead_type;
    }

    public void setLead_type(String lead_type) {
        this.lead_type = lead_type;
    }

    public String getCtn() {
        return ctn;
    }

    public void setCtn(String ctn) {
        this.ctn = ctn;
    }

    public String getCalling() {
        return calling;
    }

    public void setCalling(String calling) {
        this.calling = calling;
    }

    public String getForwarding() {
        return forwarding;
    }

    public void setForwarding(String forwarding) {
        this.forwarding = forwarding;
    }

    public String getRecording() {
        return recording;
    }

    public void setRecording(String recording) {
        this.recording = recording;
    }

    public String getCall_duration() {
        return call_duration;
    }

    public void setCall_duration(String call_duration) {
        this.call_duration = call_duration;
    }

    public String getShared_with() {
        return shared_with;
    }

    public void setShared_with(String shared_with) {
        this.shared_with = shared_with;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUser_designation() {
        return user_designation;
    }

    public void setUser_designation(String user_designation) {
        this.user_designation = user_designation;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getLead_status() {
        return lead_status;
    }

    public void setLead_status(String lead_status) {
        this.lead_status = lead_status;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getLead_age() {
        return lead_age;
    }

    public void setLead_age(String lead_age) {
        this.lead_age = lead_age;
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

    public SocialData getSocial_data() {
        return social_data;
    }

    public void setSocial_data(SocialData social_data) {
        this.social_data = social_data;
    }

    public String getCu_visible() {
        return cu_visible;
    }

    public void setCu_visible(String cu_visible) {
        this.cu_visible = cu_visible;
    }

    public String getCu_editable() {
        return cu_editable;
    }

    public void setCu_editable(String cu_editable) {
        this.cu_editable = cu_editable;
    }

    public String getLp_visible() {
        return lp_visible;
    }

    public void setLp_visible(String lp_visible) {
        this.lp_visible = lp_visible;
    }

    public String getLp_editable() {
        return lp_editable;
    }

    public void setLp_editable(String lp_editable) {
        this.lp_editable = lp_editable;
    }

    public String getStatus_visible() {
        return status_visible;
    }

    public void setStatus_visible(String status_visible) {
        this.status_visible = status_visible;
    }

    public String getStatus_editable() {
        return status_editable;
    }

    public void setStatus_editable(String status_editable) {
        this.status_editable = status_editable;
    }

    public String getOwner_visible() {
        return owner_visible;
    }

    public void setOwner_visible(String owner_visible) {
        this.owner_visible = owner_visible;
    }

    public String getOwner_editable() {
        return owner_editable;
    }

    public void setOwner_editable(String owner_editable) {
        this.owner_editable = owner_editable;
    }

    public String getShared_with_visible() {
        return shared_with_visible;
    }

    public void setShared_with_visible(String shared_with_visible) {
        this.shared_with_visible = shared_with_visible;
    }

    public String getShared_with_editable() {
        return shared_with_editable;
    }

    public void setShared_with_editable(String shared_with_editable) {
        this.shared_with_editable = shared_with_editable;
    }

    public ArrayList<FieldsData> getFieldsDataList() {
        return fieldsDataList;
    }

    public void setFieldsDataList(ArrayList<FieldsData> fieldsDataList) {
        this.fieldsDataList = fieldsDataList;
    }
}
