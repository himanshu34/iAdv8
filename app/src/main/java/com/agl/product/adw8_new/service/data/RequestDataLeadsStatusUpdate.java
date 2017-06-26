package com.agl.product.adw8_new.service.data;

import com.google.gson.annotations.SerializedName;

public class RequestDataLeadsStatusUpdate {

    @SerializedName("updateType")
    private String updateType;

    @SerializedName("statusId")
    private String statusId;

    @SerializedName("leadId")
    private String leadId;

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }
}
