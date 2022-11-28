
package com.arraykart.b2b.Retrofit.ModelClass.Rupifi;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CustomerGMV {

    @SerializedName("merchantCustomerRefId")
    @Expose
    private String merchantCustomerRefId;
    @SerializedName("startDateWithAnchor")
    @Expose
    private String startDateWithAnchor;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("creditPeriod")
    @Expose
    private Integer creditPeriod;
    @SerializedName("gmvData")
    @Expose
    private List<GmvDatum> gmvData = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CustomerGMV() {
    }

    /**
     * 
     * @param startDateWithAnchor
     * @param phone
     * @param merchantCustomerRefId
     * @param gmvData
     * @param creditPeriod
     */
    public CustomerGMV(String merchantCustomerRefId, String startDateWithAnchor, String phone, Integer creditPeriod, List<GmvDatum> gmvData) {
        super();
        this.merchantCustomerRefId = merchantCustomerRefId;
        this.startDateWithAnchor = startDateWithAnchor;
        this.phone = phone;
        this.creditPeriod = creditPeriod;
        this.gmvData = gmvData;
    }

    public String getMerchantCustomerRefId() {
        return merchantCustomerRefId;
    }

    public void setMerchantCustomerRefId(String merchantCustomerRefId) {
        this.merchantCustomerRefId = merchantCustomerRefId;
    }

    public String getStartDateWithAnchor() {
        return startDateWithAnchor;
    }

    public void setStartDateWithAnchor(String startDateWithAnchor) {
        this.startDateWithAnchor = startDateWithAnchor;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCreditPeriod() {
        return creditPeriod;
    }

    public void setCreditPeriod(Integer creditPeriod) {
        this.creditPeriod = creditPeriod;
    }

    public List<GmvDatum> getGmvData() {
        return gmvData;
    }

    public void setGmvData(List<GmvDatum> gmvData) {
        this.gmvData = gmvData;
    }

}
