
package com.arraykart.b2b.Retrofit.ModelClass.Rupifi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("merchantCustomerRefId")
    @Expose
    private String merchantCustomerRefId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isEligibleForTxn")
    @Expose
    private Boolean isEligibleForTxn;
    @SerializedName("activationUrl")
    @Expose
    private String activationUrl;
    @SerializedName("currentLimit")
    @Expose
    private Integer currentLimit;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Data() {
    }

    /**
     * 
     * @param merchantCustomerRefId
     * @param isEligibleForTxn
     * @param activationUrl
     * @param status
     * @param currentLimit
     */
    public Data(String merchantCustomerRefId, String status, Boolean isEligibleForTxn, String activationUrl, Integer currentLimit) {
        super();
        this.merchantCustomerRefId = merchantCustomerRefId;
        this.status = status;
        this.isEligibleForTxn = isEligibleForTxn;
        this.activationUrl = activationUrl;
        this.currentLimit = currentLimit;
    }

    public String getMerchantCustomerRefId() {
        return merchantCustomerRefId;
    }

    public void setMerchantCustomerRefId(String merchantCustomerRefId) {
        this.merchantCustomerRefId = merchantCustomerRefId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsEligibleForTxn() {
        return isEligibleForTxn;
    }

    public void setIsEligibleForTxn(Boolean isEligibleForTxn) {
        this.isEligibleForTxn = isEligibleForTxn;
    }

    public String getActivationUrl() {
        return activationUrl;
    }

    public void setActivationUrl(String activationUrl) {
        this.activationUrl = activationUrl;
    }

    public Integer getCurrentLimit() {
        return currentLimit;
    }

    public void setCurrentLimit(Integer currentLimit) {
        this.currentLimit = currentLimit;
    }

}
