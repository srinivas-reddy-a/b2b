package com.arraykart.b2b.Retrofit.ModelClass.Rupifi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EligibilityApiBody {
    @SerializedName("merchantCustomerRefId")
    @Expose
    private String merchantCustomerRefId;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("updateGMV")
    @Expose
    private String updateGMV;

    @SerializedName("rupifi_access_token")
    @Expose
    private String rupifi_access_token;

    public EligibilityApiBody(String merchantCustomerRefId, String phone, String updateGMV, String rupifi_access_token) {
        this.merchantCustomerRefId = merchantCustomerRefId;
        this.phone = phone;
        this.updateGMV = updateGMV;
        this.rupifi_access_token = rupifi_access_token;
    }

    public String getMerchantCustomerRefId() {
        return merchantCustomerRefId;
    }

    public void setMerchantCustomerRefId(String merchantCustomerRefId) {
        this.merchantCustomerRefId = merchantCustomerRefId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUpdateGMV() {
        return updateGMV;
    }

    public void setUpdateGMV(String updateGMV) {
        this.updateGMV = updateGMV;
    }

    public String getRupifi_access_token() {
        return rupifi_access_token;
    }

    public void setRupifi_access_token(String rupifi_access_token) {
        this.rupifi_access_token = rupifi_access_token;
    }
}
