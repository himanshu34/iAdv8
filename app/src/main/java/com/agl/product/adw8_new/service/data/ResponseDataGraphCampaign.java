package com.agl.product.adw8_new.service.data;

import com.agl.product.adw8_new.model.Graph;
import com.google.gson.annotations.SerializedName;

public class ResponseDataGraphCampaign {

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Graph graph;

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

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }
}
