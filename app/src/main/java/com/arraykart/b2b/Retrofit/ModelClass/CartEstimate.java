package com.arraykart.b2b.Retrofit.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartEstimate {
    @SerializedName("estimate_status")
    @Expose
    private String estimateStatus;

    public CartEstimate(String estimateStatus) {
        this.estimateStatus = estimateStatus;
    }

    public String getEstimateStatus() {
        return estimateStatus;
    }

    public void setEstimateStatus(String estimateStatus) {
        this.estimateStatus = estimateStatus;
    }
}

